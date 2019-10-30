/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarPickupEntity;
import entity.CarReturnEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(CarPickupReturnSessionBeanLocal.class)
@Remote(CarPickupReturnSessionBeanRemote.class)
public class CarPickupReturnSessionBean implements CarPickupReturnSessionBeanRemote, CarPickupReturnSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createCarPickupEntity(CarPickupEntity carPickupEntity){
        em.persist(carPickupEntity);
        em.flush();
        
        return carPickupEntity.getCarPickupId();
    }
    
    @Override
    public long createCarReturnEntity(CarReturnEntity carReturnEntity){
        em.persist(carReturnEntity);
        em.flush();
        
        return carReturnEntity.getCarReturnId();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
