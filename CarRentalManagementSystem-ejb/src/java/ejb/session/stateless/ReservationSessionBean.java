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
import entity.DispatchEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarModelNotFoundException;
import exception.NoCarModelsException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import exception.ReservationNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private DispatchSessionBeanLocal dispatchSessionBeanLocal;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @Override
    public long createReservationEntity(ReservationEntity reservationEntity) {
        em.persist(reservationEntity);
        em.flush();

        return reservationEntity.getReservationId();
    }

    @Override
    public CarCategoryEntity retrieveCarCategoryByReservationId(long reservationId) throws NoCarsException {
        Query query = em.createQuery("SELECT r.carCategory FROM ReservationEntity r WHERE r.reservationId = :inReservation");
        query.setParameter("inReservation", reservationId);

        try {
            return (CarCategoryEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NoCarsException("Car Category is Not Found");
        }
    }

    @Override
    public CarModelEntity retrieveCarModelByReservationId(long reservationId) throws NoCarModelsException {
        Query query = em.createQuery("SELECT r.carModel FROM ReservationEntity r WHERE r.reservationId = :inReservation");
        query.setParameter("inReservation", reservationId);

        try {
            return (CarModelEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NoCarModelsException("Car Model is Not Found");
        }
    }

    @Override
    public long createReservationEntity(boolean paid, String creditCardNumber, String cvv, Date startDate, Date endDate, CustomerEntity customer, OutletEntity pickupOutlet, OutletEntity returnOutlet, double price, PartnerEntity partner, CarCategoryEntity carCategory, long carModelId) throws CarModelNotFoundException {

        ReservationEntity reservationEntity = new ReservationEntity(paid, creditCardNumber, cvv, startDate, endDate, customer, pickupOutlet, returnOutlet, price);
        reservationEntity.setPartner(partner);
        reservationEntity.setCarCategory(carCategory);

        CarModelEntity carModel = carSessionBeanLocal.retrieveCarModelEntityByCarModelId(carModelId);

        if (carModel != null) {
            reservationEntity.setCarModel(carModel);
        }

        long reservationId = createReservationEntity(reservationEntity);

        if (!carCategory.getReservations().contains(reservationEntity)) {
            carCategory.getReservations().add(reservationEntity);
        }

        if (carModel != null && !carModel.getReservations().contains(reservationEntity)) {
            carModel.getReservations().add(reservationEntity);
        }

        if (!customer.getReservations().contains(reservationEntity)) {
            customer.getReservations().add(reservationEntity);
        }

        return reservationId;
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.customer.customerId = :inCustomer");
        query.setParameter("inCustomer", customerId);

        return query.getResultList();
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByPartnerId(Long partnerId) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.partner.partnerId = :inPartner");
        query.setParameter("inPartner", partnerId);

        return query.getResultList();
    }

    @Override
    public ReservationEntity retrieveReservationEntityByReservationId(Long reservationId) throws ReservationNotFoundException {
        ReservationEntity reservationEntity = em.find(ReservationEntity.class, reservationId);
        if (reservationEntity != null) {
            return reservationEntity;
        } else {
            throw new ReservationNotFoundException("Reservation not found");
        }
    }

    @Override
    public void updateReservationEntity(ReservationEntity reservationEntity) {
        em.merge(reservationEntity);
    }

    @Override
    public void setReservationToCancelledByReservationId(long reservationId) {

        try {
            ReservationEntity reservationEntity;
            reservationEntity = retrieveReservationEntityByReservationId(reservationId);
            reservationEntity.setCancelled(true);
            updateReservationEntity(reservationEntity);

        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByDate(Date date) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.startDate = :inDate");
        query.setParameter("inDate", date, TemporalType.DATE);
        return query.getResultList();
    }

    @Override
    public List<ReservationEntity> retrieveReservationsBetweenDates(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.startDate >= :inStartDate AND r.startDate <= :inEndDate");
        query.setParameter("inStartDate", startDate, TemporalType.TIMESTAMP);
        query.setParameter("inEndDate", endDate, TemporalType.TIMESTAMP);
        return query.getResultList();
    }

    @Override
    public List<CarEntity> getCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException {
        CarCategoryEntity carCategory = reservationEntity.getCarCategory();
        CarModelEntity carModel = reservationEntity.getCarModel();

        List<CarEntity> cars = new ArrayList<CarEntity>();

        try {
            if (carModel != null) {
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarModelIdInOutlet(carModel.getCarModelId(), outletEntity.getOutletId());
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else if (carModel == null && carCategory != null) {
                cars = carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdInOutlet(carCategory.getCarCategoryId(), outletEntity.getOutletId());
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTime(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else {
                //this should never happen
                throw new ReservationNoModelNoCategoryException("The reservation has no model or category, please remake it");
            }

        } catch (NoCarModelsException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }

    @Override
    public List<CarEntity> getBackupCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity, Date currentDate) throws ReservationNoModelNoCategoryException {
        CarCategoryEntity carCategory = reservationEntity.getCarCategory();
        CarModelEntity carModel = reservationEntity.getCarModel();

        Date date = reservationEntity.getStartDate();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, -2);

        Date twoHoursBefore = c.getTime();

        List<CarEntity> cars = new ArrayList<CarEntity>();

        try {
            if (carModel != null) {
                if (twoHoursBefore.before(currentDate)) {
                    cars = carSessionBeanLocal.retrieveAvailableCarsByCarModelIdNotInOutlet(carModel.getCarModelId(), outletEntity.getOutletId());
                }
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(carModel.getCarModelId(), outletEntity.getOutletId(), twoHoursBefore));
            } else if (carModel == null && carCategory != null) {
                if (twoHoursBefore.before(currentDate)) {
                    cars = carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdNotInOutlet(carCategory.getCarCategoryId(), outletEntity.getOutletId());
                }
                cars.addAll(carSessionBeanLocal.retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTimeOtherOutlet(carModel.getCarModelId(), outletEntity.getOutletId(), reservationEntity.getStartDate()));
            } else {
                //this should never happen
                throw new ReservationNoModelNoCategoryException("The reservation has no model or category, please remake it");
            }

        } catch (NoCarModelsException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }

    @Override
    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithCarQuantity() {
        List<CarCategoryEntity> carCategories = this.carSessionBeanLocal.retrieveAllCarCategoryEntities();
        HashMap<CarCategoryEntity, Integer> allCars = new HashMap<CarCategoryEntity, Integer>();
        HashMap<CarCategoryEntity, Integer> availableCars = new HashMap<CarCategoryEntity, Integer>();
        for (CarCategoryEntity carCategoryEntity : carCategories) {
            List<CarEntity> carList = carSessionBeanLocal.retrieveAvailableCarsByCarCategoryId(carCategoryEntity.getCarCategoryId());
            allCars.put(carCategoryEntity, carList.size());
        }
        //Now we have a list of all cars of each category
        for (Map.Entry<CarCategoryEntity, Integer> entry : allCars.entrySet()) {
            if (entry.getValue() != 0) {
                availableCars.put(entry.getKey(), entry.getValue());
            }
        }
        return availableCars;
    }

    @Override
    public List<CarCategoryEntity> retrieveCarCategoriesWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet) {
        HashMap<CarCategoryEntity, Integer> availableCars = retrieveCarCategoriesWithCarQuantity();
        List<CarCategoryEntity> availableCategories = new ArrayList<CarCategoryEntity>();
        Date startDateMinusTwoHours;
        Date endDatePlusTwoHours;
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.HOUR_OF_DAY, -2);
        startDateMinusTwoHours = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.HOUR_OF_DAY, 2);
        endDatePlusTwoHours = c.getTime();

        int counter;

        for (Map.Entry<CarCategoryEntity, Integer> entry : availableCars.entrySet()) {
            CarCategoryEntity carCategoryEntity = entry.getKey();
            counter = entry.getValue();
            for (ReservationEntity reservation : carCategoryEntity.getReservations()) {
                if (!reservation.isCancelled()) {
                    OutletEntity returnOutlet = reservation.getReturnOutlet();
                    OutletEntity pickupOutlet = reservation.getPickupOutlet();

                    Date rs = reservation.getStartDate();
                    Date re = reservation.getEndDate();

                    if (startDate.before(re) && endDate.after(rs)) {
                        counter--;
//                        //Case 1: Incoming reservation startDate <= RS and endDate <= RE
//                        if ((startDate.before(rs) || startDate.equals(rs)) && (endDate.before(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 2: Incoming reservation startDate => RS and endDate <= RE
//                        } else if ((startDate.after(rs) || startDate.equals(rs)) && (endDate.before(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 3: Incoming reservation startDate => RS and endDate => RE
//                        } else if ((startDate.after(rs) || startDate.equals(rs)) && (endDate.after(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 4: Incoming reservation startDate <= RS and endDate => RE
//                        } else if ((startDate.before(rs) || startDate.equals(rs)) && (endDate.after(re) || endDate.equals(re))) {
//                            counter--;
//                        }
                        //Case 5: Incoming reservation is before RS & RE, returnOutlet != RpickupOutlet
                    } else if (incReturnOutlet != pickupOutlet && (endDatePlusTwoHours.after(rs) || endDatePlusTwoHours.equals(rs))) {
                        counter--;
                        //Case 6: Incoming reservation is after RS & RE, pickupOutlet != RreturnOutlet
                    } else if (incPickupOutlet != returnOutlet && (startDateMinusTwoHours.before(re) || startDateMinusTwoHours.equals(re))) {
                        counter--;
                    }
                }
            }
            if (counter > 0) {
                availableCategories.add(entry.getKey());
            }
            System.out.println("Available Count of " + carCategoryEntity.getCarCategory() + " is " + counter);
        }

        return availableCategories;
    }

    @Override
    public HashMap<CarModelEntity, Integer> retrieveCarModelsForCarCategoryWithCarQuantity(CarCategoryEntity carCategory) {
        List<CarModelEntity> carModels = carSessionBeanLocal.retrieveAllCarModelEntitiesByCarCategory(carCategory.getCarCategoryId());
        HashMap<CarModelEntity, Integer> allCars = new HashMap<CarModelEntity, Integer>();
        HashMap<CarModelEntity, Integer> availableCars = new HashMap<CarModelEntity, Integer>();
        for (CarModelEntity carModelEntity : carModels) {
            List<CarEntity> carList = carSessionBeanLocal.retrieveAvailableCarsByCarModelId(carModelEntity.getCarModelId());
            if (!carModelEntity.isDisabled()) {
                allCars.put(carModelEntity, carList.size());
            }
        }
        //Now we have a list of all cars of each category
        for (Map.Entry<CarModelEntity, Integer> entry : allCars.entrySet()) {
            if (entry.getValue() != 0) {
                availableCars.put(entry.getKey(), entry.getValue());
            }
        }
        return availableCars;
    }

    @Override
    public List<CarModelEntity> retrieveCarModelsWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, CarCategoryEntity carCategory) {
        HashMap<CarModelEntity, Integer> availableCars = retrieveCarModelsForCarCategoryWithCarQuantity(carCategory);
        List<CarModelEntity> availableModels = new ArrayList<CarModelEntity>();
        Date startDateMinusTwoHours;
        Date endDatePlusTwoHours;
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.HOUR_OF_DAY, -2);
        startDateMinusTwoHours = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.HOUR_OF_DAY, 2);
        endDatePlusTwoHours = c.getTime();

        int counter;

        for (Map.Entry<CarModelEntity, Integer> entry : availableCars.entrySet()) {
            CarModelEntity carModelEntity = entry.getKey();
            counter = entry.getValue();
            for (ReservationEntity reservation : carModelEntity.getReservations()) {
                if (!reservation.isCancelled()) {
                    OutletEntity returnOutlet = reservation.getReturnOutlet();
                    OutletEntity pickupOutlet = reservation.getPickupOutlet();

                    Date rs = reservation.getStartDate();
                    Date re = reservation.getEndDate();

                    if (startDate.before(re) && endDate.after(rs)) {
                        counter--;
//                        //Case 1: Incoming reservation startDate <= RS and endDate <= RE
//                        if ((startDate.before(rs) || startDate.equals(rs)) && (endDate.before(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 2: Incoming reservation startDate => RS and endDate <= RE
//                        } else if ((startDate.after(rs) || startDate.equals(rs)) && (endDate.before(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 3: Incoming reservation startDate => RS and endDate => RE
//                        } else if ((startDate.after(rs) || startDate.equals(rs)) && (endDate.after(re) || endDate.equals(re))) {
//                            counter--;
//                            //Case 4: Incoming reservation startDate <= RS and endDate => RE
//                        } else if ((startDate.before(rs) || startDate.equals(rs)) && (endDate.after(re) || endDate.equals(re))) {
//                            counter--;
//                        }
                        //Case 5: Incoming reservation is before RS & RE, returnOutlet != RpickupOutlet
                    } else if (incReturnOutlet != pickupOutlet && (endDatePlusTwoHours.after(rs) || endDatePlusTwoHours.equals(rs))) {
                        counter--;
                        //Case 6: Incoming reservation is after RS & RE, pickupOutlet != RreturnOutlet
                    } else if (incPickupOutlet != returnOutlet && (startDateMinusTwoHours.before(re) || startDateMinusTwoHours.equals(re))) {
                        counter--;
                    }
                }
            }
            if (counter > 0) {
                availableModels.add(entry.getKey());
            }
            System.out.println("Available Count of " + carModelEntity.getCarCategory() + " is " + counter);
        }

        return availableModels;
    }

    @Override
    public List<RentalRateEntity> calculateTotalRentalRate(CarCategoryEntity carCategory, Date startDate, Date endDate) throws NoRentalRatesFoundException {
        System.out.println("Now calculating rental rate for specified period and car category");

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        ArrayList<RentalRateEntity> rentalRates = new ArrayList<RentalRateEntity>();
        List<RentalRateEntity> carCategoryRates = rentalRateSessionBeanLocal.retrieveAllRentalRatesByCarCategoryId(carCategory.getCarCategoryId());

        //Get rental rates for every day
        while (startCalendar.before(endCalendar)) {
            Date curDate = startCalendar.getTime();
            double cheapestRateOfDay = -1337;
            RentalRateEntity chosenRentalRate = null;
            boolean isFound = false;
            for (RentalRateEntity rentalRate : carCategoryRates) {
                Date rentalRateStartDate = rentalRate.getStartDate();
                Date rentalRateEndDate = rentalRate.getEndDate();
                if (rentalRateStartDate == null && rentalRateStartDate == null || (rentalRateStartDate.before(curDate) || rentalRateStartDate.equals(curDate)) && (rentalRateEndDate.after(curDate)) || rentalRateEndDate.equals(curDate)) {
                    if (rentalRate.isDisabled() == false && (cheapestRateOfDay == -1337 || rentalRate.getDailyRate() < cheapestRateOfDay)) {
                        cheapestRateOfDay = rentalRate.getDailyRate();
                        chosenRentalRate = rentalRate;
                        isFound = true;
                    }
                }
            }
            if (isFound) {
                rentalRates.add(chosenRentalRate);
            } else {
                throw new NoRentalRatesFoundException("Rental Rates not found for current date and category");
            }
            isFound = false;
            startCalendar.add(Calendar.DATE, 1);
        }

//        if ((startDate.getHours() * 60 + startDate.getMinutes()) <= (endDate.getHours() * 60 + endDate.getMinutes())) {
//            double cheapestRateOfDay = -1337;
//            RentalRateEntity chosenRentalRate = null;
//            boolean isFound = false;
//            for (RentalRateEntity rentalRate : carCategoryRates) {
//                Date rentalRateStartDate = rentalRate.getStartDate();
//                Date rentalRateEndDate = rentalRate.getEndDate();
//                if (rentalRateStartDate == null && rentalRateStartDate == null || (rentalRateStartDate.before(endDate) || rentalRateStartDate.equals(endDate)) && (rentalRateEndDate.after(endDate)) || rentalRateEndDate.equals(endDate)) {
//                    if (rentalRate.isDisabled() == false && (cheapestRateOfDay == -1337 || rentalRate.getDailyRate() < cheapestRateOfDay)) {
//                        cheapestRateOfDay = rentalRate.getDailyRate();
//                        chosenRentalRate = rentalRate;
//                        isFound = true;
//                    }
//                }
//            }
//            if (isFound) {
//                rentalRates.add(chosenRentalRate);
//            } else {
//                throw new NoRentalRatesFoundException("Rental Rates not found for current date and category");
//            }
//        }
        if (!rentalRates.isEmpty()) {
            return rentalRates;
        } else {
            throw new NoRentalRatesFoundException("Rental Rates not found for current date and category");
        }
    }

    @Override
    public void autoAllocateCarToReservation(ReservationEntity reservationEntity, Date currentDate) throws ReservationNoModelNoCategoryException, NullCurrentOutletException, NoCarsException {
        List<CarEntity> cars = getCarsForReservation(reservationEntity, reservationEntity.getPickupOutlet());
        for (CarEntity carEntity : cars) {
            if (carEntity.getCurrentReservation() == null) {
                reservationEntity.setCar(carEntity);
                carEntity.setCurrentReservation(reservationEntity);
                carEntity.setReturnOutlet(reservationEntity.getReturnOutlet());
                carEntity.setUsed(true);

                updateReservationEntity(reservationEntity);
                carSessionBeanLocal.updateCarEntity(carEntity);
                break;
            } else if (carEntity.getCurrentOutlet() == null && carEntity.getStatus().equals(CarStatusEnum.AVAILABLE)) {
                //this should never happen
                throw new NullCurrentOutletException("Car " + carEntity.getCarId() + " has a null current outlet but is Available");
            }
        }

        if (reservationEntity.getCar() == null) {
            List<CarEntity> backupCars = getBackupCarsForReservation(reservationEntity, reservationEntity.getPickupOutlet(), currentDate);
            for (CarEntity carEntity : backupCars) {
                if (carEntity.getCurrentReservation() == null) {
                    reservationEntity.setCar(carEntity);
                    carEntity.setCurrentReservation(reservationEntity);
                    carEntity.setReturnOutlet(reservationEntity.getCarReturn().getOutlet());
                    carEntity.setUsed(true);

                    DispatchEntity dispatchEntity = new DispatchEntity(reservationEntity, carEntity, carEntity.getCurrentOutlet(), reservationEntity.getPickupOutlet());

                    dispatchSessionBeanLocal.createDispatchEntity(dispatchEntity);
                    reservationEntity.setDispatch(dispatchEntity);

                    updateReservationEntity(reservationEntity);
                    carSessionBeanLocal.updateCarEntity(carEntity);
                    dispatchSessionBeanLocal.updateDispatchEntity(dispatchEntity);
                    break;
                }
            }
        }

        if (reservationEntity.getCar() == null) {
            // this should not happen under normal conditions
            throw new NoCarsException("No car that satisfies the conditions have been found for reservation " + reservationEntity.getReservationId());
        }

    }

    @Override
    public void evictCache() {
        Cache cache = em.getEntityManagerFactory().getCache();
        cache.evictAll();
    }
}
