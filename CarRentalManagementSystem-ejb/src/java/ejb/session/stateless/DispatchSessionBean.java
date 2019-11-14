/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DispatchEntity;
import entity.OutletEntity;
import exception.DispatchNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(DispatchSessionBeanLocal.class)
@Remote(DispatchSessionBeanRemote.class)
public class DispatchSessionBean implements DispatchSessionBeanRemote, DispatchSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    
    
    @Override
    public long createDispatchEntity(DispatchEntity dispatchEntity) {
        em.persist(dispatchEntity);
        em.flush();
        
        return dispatchEntity.getDispatchId();
    }
    
    @Override
    public void updateDispatchEntity(DispatchEntity dispatchEntity) {
        em.merge(dispatchEntity);
    }
    
    @Override
    public DispatchEntity retrieveDispatchEntityByDispatchId(long dispatchId) throws DispatchNotFoundException{
        DispatchEntity dispatchEntity = em.find(DispatchEntity.class, dispatchId);
        
        if (dispatchEntity != null){
            return dispatchEntity;
        } else {
            throw new DispatchNotFoundException("Dispatch not found");
        }
    }
    
    @Override
    public List<DispatchEntity> retrieveDispatchesByDate(Date date) {
        Query query = em.createQuery("SELECT d FROM DispatchEntity d JOIN ReservationEntity r WHERE d.reservation = r AND r.startDate = :inDate");
        query.setParameter("indate", date, TemporalType.DATE);
        
        List<DispatchEntity> dispatches = query.getResultList();
        
        return dispatches;
    }
    
    @Override
    public List<DispatchEntity> retrieveDispatchesByDateToOutlet(Date date, OutletEntity outletEntity) {
        Query query = em.createQuery("SELECT d FROM DispatchEntity d JOIN ReservationEntity r WHERE d.reservation = r AND r.startDate = :inDate AND d.endOutlet = :inOutlet");
        query.setParameter("inDate", date, TemporalType.DATE);
        query.setParameter("inoutlet", outletEntity.getOutletId());
        
        List<DispatchEntity> dispatches = query.getResultList();
        
        return dispatches;
    }
    
    @Override
    public List<DispatchEntity> retrieveDispatchesByDateFromOutlet(Date date, OutletEntity outletEntity) {
        Query query = em.createQuery("SELECT d FROM DispatchEntity d WHERE d.reservation.startDate = :inDate AND d.currentOutlet.outletId = :inOutlet");
        query.setParameter("inDate", date, TemporalType.DATE);
        query.setParameter("inOutlet", outletEntity.getOutletId());
        
        List<DispatchEntity> dispatches = query.getResultList();
        
        return dispatches;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
