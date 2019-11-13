/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import exception.RentalRateNotFoundException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(RentalRateSessionBeanLocal.class)
@Remote(RentalRateSessionBeanRemote.class)
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createRentalRateEntity(RentalRateEntity rentalRateEntity){
        em.persist(rentalRateEntity);
        em.flush();
        
        return rentalRateEntity.getRentalRateId();
    }
    
    @Override
    public void updateRentalRateEntity(RentalRateEntity rentalRateEntity){
        em.merge(rentalRateEntity);
    }
    
    @Override
    public void deleteRentalRateEntity(long rentalRateId) throws RentalRateNotFoundException{
            RentalRateEntity rentalRateEntity = retrieveRentalRateEntityByRentalRateId(rentalRateId);
            if (rentalRateEntity.isUsed()){
                rentalRateEntity.setDisabled(true);
                updateRentalRateEntity(rentalRateEntity);
                System.out.println("*****Rental Rate has been updated to disabled*****");
            } else {
                em.remove(rentalRateEntity);
                System.out.println("*****Rental Rate has been deleted*****");
            }
    }
    
    @Override
    public RentalRateEntity retrieveRentalRateEntityByRentalRateId(long rentalRateId) throws RentalRateNotFoundException {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);
        if (rentalRateEntity != null){
            return rentalRateEntity;
        } else {
            throw new RentalRateNotFoundException("Rental rate is not found");
        }
    }
    
    @Override
    public List<RentalRateEntity> retrieveAllRentalRatesByCarCategoryThenDate(){
        Query query = em.createQuery("SELECT r FROM RentalRateEntity r ORDER BY r.carCategory, r.startDate");
        
        return query.getResultList();
    }
}
