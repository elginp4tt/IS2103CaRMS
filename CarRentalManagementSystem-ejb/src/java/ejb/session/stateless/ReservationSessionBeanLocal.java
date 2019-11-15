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
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.NoCarModelsException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ReservationSessionBeanLocal {

    public ReservationEntity retrieveReservationEntityByReservationId(Long reservationId) throws ReservationNotFoundException;

    public void updateReservationEntity(ReservationEntity reservationEntity);

    public List<ReservationEntity> retrieveReservationsByDate(Date date);

    public List<CarEntity> getCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException;

    public void autoAllocateCarToReservation(ReservationEntity reservationEntity, Date currentDate) throws ReservationNoModelNoCategoryException, NullCurrentOutletException, NoCarsException;

    public List<CarEntity> getBackupCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity, Date currentDate) throws ReservationNoModelNoCategoryException;

    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithCarQuantity();

    public List<CarCategoryEntity> retrieveCarCategoriesWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet);

    public HashMap<CarModelEntity, Integer> retrieveCarModelsForCarCategoryWithCarQuantity(CarCategoryEntity carCategory);

    public List<CarModelEntity> retrieveCarModelsWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, CarCategoryEntity carCategory);

    public long createReservationEntity(ReservationEntity reservationEntity);

    public List<ReservationEntity> retrieveReservationsByCustomerId(Long customerId);

    public List<ReservationEntity> retrieveReservationsByPartnerId(Long partnerId);

    public List<RentalRateEntity> calculateTotalRentalRate(CarCategoryEntity carCategory, Date startDate, Date endDate) throws NoRentalRatesFoundException;

    public List<ReservationEntity> retrieveReservationsBetweenDates(Date startDate, Date endDate);

    public long createReservationEntity(boolean paid, String creditCardNumber, String cvv, Date startDate, Date endDate, CustomerEntity customer, OutletEntity pickupOutlet, OutletEntity returnOutlet, double price, PartnerEntity partner, CarCategoryEntity carCategory, CarModelEntity carModel);

    public CarModelEntity retrieveCarModelByReservationId(long reservationId) throws NoCarModelsException;

    public CarCategoryEntity retrieveCarCategoryByReservationId(long reservationId) throws NoCarsException;

    public void setReservationToCancelledByReservationId(long reservationId);
    
}
