/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import exception.OutletNotFoundException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(OutletSessionBeanLocal.class)
@Remote(OutletSessionBeanRemote.class)
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;


    @Override
    public void updateOutletEntity(OutletEntity outletEntity){
        em.merge(outletEntity);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public long createOutletEntity(OutletEntity outletEntity){
        em.persist(outletEntity);
        em.flush();
        
        return outletEntity.getOutletId();
    }
    
    @Override
    public OutletEntity retrieveOutletEntityByOutletId(long outletId) throws OutletNotFoundException{
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);
        if (outletEntity != null){
            return outletEntity;
        } else {
            throw new OutletNotFoundException("Outlet not found");
        }
    }
    
    @Override
    public OutletEntity retrieveOutletEntityByOutletName(String name) throws OutletNotFoundException{
        Query query = em.createQuery("SELECT o FROM OutletEntity o WHERE o.name = :inName");
        query.setParameter("inName", name);
        
        try{
            return (OutletEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new OutletNotFoundException("Outlet not found");
        }
    }
}
