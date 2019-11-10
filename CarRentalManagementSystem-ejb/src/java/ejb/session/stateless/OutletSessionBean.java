/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.DispatchEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import exception.CarNotFoundException;
import exception.EmployeeNotFoundException;
import exception.OutletNotFoundException;
import exception.OutletUpdateException;
import javax.ejb.EJB;
import exception.OutletNotFoundException;
import java.util.List;
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

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createOutletEntity(OutletEntity newOutletEntity) {
        em.persist(newOutletEntity);
        em.flush();

        return newOutletEntity.getOutletId();
    }

    @Override
    public OutletEntity retrieveOutletEntityById(long outletId) throws OutletNotFoundException {
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);

        if (outletEntity != null) {
            return outletEntity;
        } else {
            throw new OutletNotFoundException("Outlet not found");
        }
    }

    @Override
    public OutletEntity retrieveOutletEntityByName(String outletName) throws OutletNotFoundException {
        Query query = em.createQuery("SELECT o FROM OutletEntity o WHERE o.name = :inOutletName");
        query.setParameter("inOutletName", outletName);

        try {
            return (OutletEntity) query.getSingleResult();
        } catch (Exception e) {
            throw new OutletNotFoundException("Outlet not found");
        }
    }

//    @Override
//    public void updateOutletEntity(OutletEntity outletEntity) throws OutletNotFoundException, OutletUpdateException {
//        if (outletEntity != null && outletEntity.getOutletId() != null) {
//            OutletEntity outletEntityToUpdate = retrieveOutletEntityById(outletEntity.getOutletId());
//            if (outletEntityToUpdate.getName().equals(outletEntity.getName())) {
//                outletEntityToUpdate.setAddress(outletEntity.getAddress());
//                outletEntityToUpdate.setName(outletEntity.getName());
//                outletEntityToUpdate.setOpeningHours(outletEntity.getOpeningHours());
//                em.merge(outletEntityToUpdate);
//            } else {
//                throw new OutletUpdateException("Name of outlet to be updated does not match the existing record");
//            }
//        } else {
//            throw new OutletNotFoundException("Outlet ID not provided for Outler to be updated");
//        }
//    }
    
    @Override
    public void updateOutletEntity(OutletEntity outletEntity){
        em.merge(outletEntity);
    }

    @Override
    public void deleteOutletEntity(long outletId) throws OutletUpdateException {
        try {
            OutletEntity outletEntity = retrieveOutletEntityById(outletId);
            em.remove(outletEntity);
        } catch (OutletNotFoundException e) {
            System.out.println(e.getMessage());
            throw new OutletUpdateException("Outlet ID provided is not found and not delete");
        }
    }

    @Override
    public OutletEntity addCarToOutletEntity(String outletName, CarEntity car) throws OutletUpdateException {

        try {
            OutletEntity outletEntityToUpdate = retrieveOutletEntityByName(outletName);
            CarEntity carToUpdate = carSessionBeanLocal.retrieveCarEntityByCarId(car.getCarId());

            carToUpdate.setCurrentOutlet(outletEntityToUpdate);
            outletEntityToUpdate.getCars().add(carToUpdate);

            em.flush();

            return outletEntityToUpdate;
        } catch (OutletNotFoundException | CarNotFoundException ex) {
            throw new OutletUpdateException("Car is not added to the Outlet");
        }
    }

    @Override
    public OutletEntity addEmployeeToOutletEntity(String outletName, long EmployeeId) throws OutletUpdateException {
        try {
            EmployeeEntity employeeEntityToUpdate = employeeSessionBeanLocal.retrieveEmployeeEntityByEmployeeId(EmployeeId);
            OutletEntity outletEntityToUpdate = retrieveOutletEntityByName(outletName);

            employeeEntityToUpdate.setOutlet(outletEntityToUpdate);
            outletEntityToUpdate.getEmployees().add(employeeEntityToUpdate);

            em.flush();
            
            return new OutletEntity();
        } catch (EmployeeNotFoundException | OutletNotFoundException ex) {
            throw new OutletUpdateException("Employee is not added to the Outlet");
        }
    }

    @Override
    public OutletEntity addDispatchToOutletEntity (DispatchEntity dispatchRecord) throws OutletUpdateException{
        try {
            OutletEntity fromOutletEntityToUpdate = retrieveOutletEntityByName(dispatchRecord.getCurrentOutlet().getName());
            OutletEntity toOutletEntityToUpdate = retrieveOutletEntityByName(dispatchRecord.getEndOutlet().getName());
            
            fromOutletEntityToUpdate.getDispatches().add(dispatchRecord);
            toOutletEntityToUpdate.getDispatches().add(dispatchRecord);
            
            em.persist(dispatchRecord);
            em.flush();
           
            return fromOutletEntityToUpdate;
        } catch (OutletNotFoundException ex){
            throw new OutletUpdateException ("Dispatch record is not added to the current and end outlet");
        }
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
    
    @Override
    public List<OutletEntity> retrieveAllOutletEntities(){
        Query query = em.createQuery("SELECT o FROM OutletEntity o");
        
        return query.getResultList();
    }
}
