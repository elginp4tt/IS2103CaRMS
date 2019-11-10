/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategoryEntity;
import entity.CarEntity;
import entity.CarModelEntity;
import entity.CustomerEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ReservationSessionBeanRemote {

    public ReservationEntity retrieveReservationEntityByReservationId(Long reservationId) throws ReservationNotFoundException;

    public void updateReservationEntity(ReservationEntity reservationEntity);

    public List<ReservationEntity> retrieveReservationsByDate(Date date);

    public List<CarEntity> getCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException;

    public void autoAllocateCarToReservation(ReservationEntity reservationEntity) throws ReservationNoModelNoCategoryException, NullCurrentOutletException, NoCarsException;

    public List<CarEntity> getBackupCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException;

    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithCarQuantity();

    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet);

    public HashMap<CarModelEntity, Integer> retrieveCarModelsForCarCategoryWithCarQuantity(CarCategoryEntity carCategory);

    public HashMap<CarModelEntity, Integer> retrieveCarModelsWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, CarCategoryEntity carCategory);

    public long createReservationEntity(ReservationEntity reservationEntity);

    public void reserveAvailableCar(CarCategoryEntity carCategory, CarModelEntity carModel, Date startDate, Date endDate, CustomerEntity customerEntity, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, PartnerEntity partnerEntity) throws NoRentalRatesFoundException;

    public void searchForAvailableCars(PartnerEntity partnerEntity, CustomerEntity customerEntity) throws NoCarsException, NoRentalRatesFoundException;
    
    public List<ReservationEntity> retrieveReservationsByCustomerId(Long customerId);
    
    public void cancelReservation(Date currentDate);
}
