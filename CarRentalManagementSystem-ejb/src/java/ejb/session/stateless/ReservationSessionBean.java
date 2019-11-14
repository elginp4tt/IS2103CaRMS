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
import exception.CarCategoryNotFoundException;
import exception.CarModelNotFoundException;
import exception.NoCarModelsException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.NullCurrentOutletException;
import exception.OutletNotFoundException;
import exception.ReservationNoModelNoCategoryException;
import exception.ReservationNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.ejb.EJB;
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

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private DispatchSessionBeanLocal dispatchSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @Override
    public long createReservationEntity(ReservationEntity reservationEntity) {
        em.persist(reservationEntity);
        em.flush();

        return reservationEntity.getReservationId();
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
    public List<ReservationEntity> retrieveReservationsByDate(Date date) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.startDate = :inDate");
        query.setParameter("inDate", date, TemporalType.DATE);
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
    public List<CarEntity> getBackupCarsForReservation(ReservationEntity reservationEntity, OutletEntity outletEntity) throws ReservationNoModelNoCategoryException {
        CarCategoryEntity carCategory = reservationEntity.getCarCategory();
        CarModelEntity carModel = reservationEntity.getCarModel();

        Date date = reservationEntity.getStartDate();
        Date currentDate = new Date();
        Date twoHoursBefore = new Date(date.getYear(), date.getMonth(), date.getHours() - 2);

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
                    //Case 1: Incoming reservation starts before but ends during existing reservation
                    if ((startDate.before(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && endDate.after(reservation.getStartDate()) && (endDate.before(reservation.getEndDate()) || endDate.equals(reservation.getEndDate()))) {
                        counter--;
                        System.out.println("Case 1");
                    }
                    //Case 2: Incoming reservation starts during and ends during existing reservation
                    else if ((startDate.after(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && (endDate.before(reservation.getEndDate()) || endDate.equals(reservation.getEndDate()))) {
                        counter--;
                        System.out.println("Case 2");
                    }
                    //Case 3: Incoming reservation starts before but ends after existing reservation
                    else if ((startDate.before(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && endDate.after(reservation.getStartDate())) {
                        counter--;
                        System.out.println("Case 3");
                    }
                    //Case 4: Incoming reservation starts during but ends during exising reservation
                    else if ((startDate.after(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && startDate.before(reservation.getEndDate())) {
                        counter--;
                        System.out.println("Case 4");
                    }
                    //Case 5: Incoming reservation starts after existing reservation but different outlet and less than 2 hours apart
                    else if (!returnOutlet.equals(incPickupOutlet) && startDateMinusTwoHours.before(reservation.getEndDate()) && startDateMinusTwoHours.after(reservation.getStartDate())) {
                        counter--;
                        System.out.println("Case 5");
                    }
                    //Case 6: Incoming reservation starts before existing reservation but different outlet and less than 2 hours apart
                    else if (!pickupOutlet.equals(incReturnOutlet) && endDatePlusTwoHours.after(reservation.getStartDate()) && endDatePlusTwoHours.before(reservation.getEndDate())) {
                        counter--;
                        System.out.println("Case 6");
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
            allCars.put(carModelEntity, carList.size());
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
        List<CarModelEntity> availableCategories = new ArrayList<CarModelEntity>();
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
            counter = 0;
            for (ReservationEntity reservation : carModelEntity.getReservations()) {
                if (!reservation.isCancelled()) {
                    OutletEntity returnOutlet = reservation.getReturnOutlet();
                    OutletEntity pickupOutlet = reservation.getPickupOutlet();
                    //Case 1: Incoming reservation starts before but ends during existing reservation
                    if ((startDate.before(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && endDate.after(reservation.getStartDate()) && (endDate.before(reservation.getEndDate()) || endDate.equals(reservation.getEndDate()))) {
                        counter--;
                        System.out.println("Case 1");
                    }
                    //Case 2: Incoming reservation starts during and ends during existing reservation
                    else if ((startDate.after(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && (endDate.before(reservation.getEndDate()) || endDate.equals(reservation.getEndDate()))) {
                        counter--;
                        System.out.println("Case 2");
                    }
                    //Case 3: Incoming reservation starts before but ends after existing reservation
                    else if ((startDate.before(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && endDate.after(reservation.getStartDate())) {
                        counter--;
                        System.out.println("Case 3");
                    }
                    //Case 4: Incoming reservation starts during but ends during exising reservation
                    else if ((startDate.after(reservation.getStartDate()) || startDate.equals(reservation.getStartDate())) && startDate.before(reservation.getEndDate())) {
                        counter--;
                        System.out.println("Case 4");
                    }
                    //Case 5: Incoming reservation starts after existing reservation but different outlet and less than 2 hours apart
                    else if (!returnOutlet.equals(incPickupOutlet) && startDateMinusTwoHours.before(reservation.getEndDate()) && startDateMinusTwoHours.after(reservation.getStartDate())) {
                        counter--;
                        System.out.println("Case 5");
                    }
                    //Case 6: Incoming reservation starts before existing reservation but different outlet and less than 2 hours apart
                    else if (!pickupOutlet.equals(incReturnOutlet) && endDatePlusTwoHours.after(reservation.getStartDate()) && endDatePlusTwoHours.before(reservation.getEndDate())) {
                        counter--;
                        System.out.println("Case 6");
                    }
                }
            }
            if (counter > 0) {
                availableCategories.add(entry.getKey());
            }
            System.out.println("Available Count of " + carModelEntity.getMake() + " " + carModelEntity.getModel() + " is " + counter);
        }

        return availableCategories;
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

        if ((startDate.getHours() * 60 + startDate.getMinutes()) <= (endDate.getHours() * 60 + endDate.getMinutes())) {
            double cheapestRateOfDay = -1337;
            RentalRateEntity chosenRentalRate = null;
            boolean isFound = false;
            for (RentalRateEntity rentalRate : carCategoryRates) {
                Date rentalRateStartDate = rentalRate.getStartDate();
                Date rentalRateEndDate = rentalRate.getEndDate();
                if (rentalRateStartDate == null && rentalRateStartDate == null || (rentalRateStartDate.before(endDate) || rentalRateStartDate.equals(endDate)) && (rentalRateEndDate.after(endDate)) || rentalRateEndDate.equals(endDate)) {
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
        }

        if (!rentalRates.isEmpty()) {
            return rentalRates;
        } else {
            throw new NoRentalRatesFoundException("Rental Rates not found for current date and category");
        }
    }

    @Override
    public void autoAllocateCarToReservation(ReservationEntity reservationEntity) throws ReservationNoModelNoCategoryException, NullCurrentOutletException, NoCarsException {
        List<CarEntity> cars = getCarsForReservation(reservationEntity, reservationEntity.getPickupOutlet());
        for (CarEntity carEntity : cars) {
            if (carEntity.getCurrentReservation() == null) {
                reservationEntity.setCar(carEntity);
                carEntity.setCurrentReservation(reservationEntity);
                carEntity.setReturnOutlet(reservationEntity.getCarReturn().getOutlet());
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
            List<CarEntity> backupCars = getBackupCarsForReservation(reservationEntity, reservationEntity.getPickupOutlet());
            for (CarEntity carEntity : backupCars) {
                if (carEntity.getCurrentReservation() != null) {
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
}
