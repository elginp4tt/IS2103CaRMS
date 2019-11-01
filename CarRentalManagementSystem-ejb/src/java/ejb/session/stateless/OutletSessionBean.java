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

    public long createOutletEntity(OutletEntity newOutletEntity) {
        em.persist(newOutletEntity);
        em.flush();

        return newOutletEntity.getOutletId();
    }

    public OutletEntity retrieveOutletEntityById(long outletId) throws OutletNotFoundException {
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);

        if (outletEntity != null) {
            return outletEntity;
        } else {
            throw new OutletNotFoundException("Outlet not found");
        }
    }

    public OutletEntity retrieveOutletEntityByName(String outletName) throws OutletNotFoundException {
        Query query = em.createQuery("SELECT o FROM OutletEntity o WHERE o.name = :inOutletName");
        query.setParameter("inOutletName", outletName);

        try {
            return (OutletEntity) query.getSingleResult();
        } catch (Exception e) {
            throw new OutletNotFoundException("Outlet not found");
        }
    }

    public void updateOutletEntity(OutletEntity outletEntity) {
        em.merge(outletEntity);
    }

    public void deleteOutletEntity(long outletId) {
        try {
            OutletEntity outletEntity = retrieveOutletEntityById(outletId);
            em.remove(outletEntity);
        }
        catch (Exception e) {
            System.out.println("");
        }
    }
}
