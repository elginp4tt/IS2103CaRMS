/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarCategoryEntity;
import entity.CarEntity;
import entity.CarModelEntity;
import entity.DispatchEntity;
import entity.OutletEntity;
import entity.ReservationEntity;
import exception.NoCarModelsException;
import exception.NoCarsException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import exception.ReservationNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    private CarSessionBeanLocal carSessionBeanLocal;
    private DispatchSessionBeanLocal dispatchSessionBeanLocal;

    @Override
    public ReservationEntity retrieveReservationEntityByReservationId(Long reservationId) throws ReservationNotFoundException{
        ReservationEntity reservationEntity = em.find(ReservationEntity.class, reservationId);
        if (reservationEntity != null){
            return reservationEntity;
        } else {
            throw new ReservationNotFoundException("Reservation not found");
        }
    }
    
    @Override
    public void updateReservationEntity(ReservationEntity reservationEntity){
        em.merge(reservationEntity);
    }
    
    @Override
    public List<ReservationEntity> retrieveReservationsByDate(Date date){
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.startDate = :inDate");
        query.setParameter("inDate", date, TemporalType.DATE);
        return query.getResultList();
    }
    
    @Override
    public List<CarEntity> getCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException{
        CarCategoryEntity carCategory = reservationEntity.getCarCategory();
        CarModelEntity carModel = reservationEntity.getCarModel();
        
        List<CarEntity> cars = new ArrayList<CarEntity>();
        
        try {
            if (carModel != null){
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarModelIdInOutlet(carModel.getCarModelId(), outletEntity.getOutletId());
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else if (carModel == null && carCategory != null){
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdInOutlet(carCategory.getCarCategoryId(), outletEntity.getOutletId());
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTime(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else {
                throw new ReservationNoModelNoCategoryException("The reservation has no model or category, please remake it");
            }
            
        } catch (NoCarModelsException e){
            System.out.println(e.getMessage());
        }
        
        return cars;
    }
    
    @Override
    public List<CarEntity> getBackupCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException{
        CarCategoryEntity carCategory = reservationEntity.getCarCategory();
        CarModelEntity carModel = reservationEntity.getCarModel();
        
        Date date = reservationEntity.getStartDate();
        Date currentDate = new Date();
        Date twoHoursBefore = new Date(date.getYear(), date.getMonth(), date.getHours() - 2);
        
        List<CarEntity> cars = new ArrayList<CarEntity>();
        
        try {
            if (carModel != null){
                if (twoHoursBefore.before(currentDate)){
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarModelIdNotInOutlet(carModel.getCarModelId(), outletEntity.getOutletId());
                    }
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(carModel.getCarModelId(), outletEntity.getOutletId(), twoHoursBefore));
            } else if (carModel == null && carCategory != null){
                if (twoHoursBefore.before(currentDate)){
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdNotInOutlet(carCategory.getCarCategoryId(), outletEntity.getOutletId());
                    }
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTimeOtherOutlet(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else {
                throw new ReservationNoModelNoCategoryException("The reservation has no model or category, please remake it");
            }
            
        } catch (NoCarModelsException e){
            System.out.println(e.getMessage());
        }
        
        return cars;
    }
    
    @Override
    public void autoAllocateCarToReservation(ReservationEntity reservationEntity) throws ReservationNoModelNoCategoryException, NullCurrentOutletException, NoCarsException {
        List<CarEntity> cars = getCarsForReservation(reservationEntity, reservationEntity.getOutlet());
        for (CarEntity carEntity : cars){
            if (carEntity.getCurrentReservation() == null){
                    reservationEntity.setCar(carEntity);
                    carEntity.setCurrentReservation(reservationEntity);
                    carEntity.setReturnOutlet(reservationEntity.getCarReturn().getOutlet());
                    
                    updateReservationEntity(reservationEntity);
                    carSessionBeanLocal.updateCarEntity(carEntity);
                    break;
            } else if (carEntity.getCurrentOutlet() == null && carEntity.getStatus().equals(CarStatusEnum.INOUTLET)){
                throw new NullCurrentOutletException("Car " + carEntity.getCarId() + " has a null current outlet but is INOUTLET");
            }
        }
        
        if (reservationEntity.getCar() == null){
            List<CarEntity> backupCars = getBackupCarsForReservation(reservationEntity, reservationEntity.getOutlet());
            for (CarEntity carEntity : backupCars){
                if (carEntity.getCurrentReservation() != null)
                    reservationEntity.setCar(carEntity);
                    carEntity.setCurrentReservation(reservationEntity);
                    carEntity.setReturnOutlet(reservationEntity.getCarReturn().getOutlet());
                    DispatchEntity dispatchEntity = new DispatchEntity(reservationEntity, carEntity, carEntity.getCurrentOutlet(), reservationEntity.getOutlet());
                    
                    dispatchSessionBeanLocal.createDispatchEntity(dispatchEntity);
                    reservationEntity.setDispatch(dispatchEntity);
                    
                    updateReservationEntity(reservationEntity);
                    carSessionBeanLocal.updateCarEntity(carEntity);
                    dispatchSessionBeanLocal.updateDispatchEntity(dispatchEntity);
                    
                    break;
            }
        }
        
        if (reservationEntity.getCar() == null){
            throw new NoCarsException("No car that satisfies the conditions have been found for reservation " + reservationEntity.getReservationId());
        }
        
    }
}
