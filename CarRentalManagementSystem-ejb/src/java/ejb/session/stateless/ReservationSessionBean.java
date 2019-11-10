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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
    private OutletSessionBeanLocal outletSessionBeanLocal;
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
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
    public void cancelReservation(Date currentDate) {
        Scanner sc = new Scanner(System.in);
        long reservationId = sc.nextLong();
        double cost = 0;

        try {
            ReservationEntity reservationEntity = retrieveReservationEntityByReservationId(reservationId);
            List<RentalRateEntity> rentalRates = reservationEntity.getRentalRates();
            for (RentalRateEntity rentalRate : rentalRates) {
                cost = cost + rentalRate.getDailyRate();
            }
            boolean paid = reservationEntity.isPaid();

            Calendar reservationC = new GregorianCalendar();
            reservationC.setTime(reservationEntity.getStartDate());
            reservationC.add(Calendar.DAY_OF_MONTH, -14);

            Date rental14 = reservationC.getTime();

            reservationC.add(Calendar.DAY_OF_MONTH, 7);

            Date rental7 = reservationC.getTime();

            reservationC.add(Calendar.DAY_OF_MONTH, 4);

            Date rental3 = reservationC.getTime();

            if (currentDate.after(rental3)) {
                System.out.println("You will be charged for 70% of your reservation");
                if (paid) {
                    System.out.println(cost * 0.3 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.7 + " will be deducted from your card");
                }
            } else if (currentDate.after(rental7)) {
                System.out.println("You will be charged for 50% of your reservation");
                if (paid) {
                    System.out.println(cost * 0.5 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.5 + " will be deducted from your card");
                }
            } else if (currentDate.after(rental14)) {
                System.out.println("You will be charged for 20% of your reservation");
                if (paid) {
                    System.out.println(cost * 0.8 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.2 + " will be deducted from your card");
                }
            } else {
                System.out.println("You will not be charged for your reservation");
            }
            reservationEntity.setCancelled(true);
            updateReservationEntity(reservationEntity);
            System.out.println("Reservation with Id: " + reservationEntity.getReservationId() + " has been cancelled");
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
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
                throw new ReservationNoModelNoCategoryException("The reservation has no model or category, please remake it");
            }

        } catch (NoCarModelsException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }

    @Override
    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithCarQuantity() {
        List<CarCategoryEntity> carCategories = carSessionBeanLocal.retrieveAllCarCategoryEntities();
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
    public HashMap<CarCategoryEntity, Integer> retrieveCarCategoriesWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet) {
        HashMap<CarCategoryEntity, Integer> availableCars = retrieveCarCategoriesWithCarQuantity();
        Date startDateMinusTwoHours;
        Date endDatePlusTwoHours;
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.HOUR_OF_DAY, -2);
        startDateMinusTwoHours = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.HOUR_OF_DAY, 2);
        endDatePlusTwoHours = c.getTime();

        for (Map.Entry<CarCategoryEntity, Integer> entry : availableCars.entrySet()) {
            CarCategoryEntity carCategoryEntity = entry.getKey();

            for (ReservationEntity reservation : carCategoryEntity.getReservations()) {
                if (!reservation.isCancelled()) {
                    OutletEntity returnOutlet = reservation.getReturnOutlet();
                    OutletEntity pickupOutlet = reservation.getPickupOutlet();
                    //Case 1: Incoming reservation starts before but ends during existing reservation
                    if (startDate.before(reservation.getStartDate()) && endDate.after(reservation.getStartDate()) && endDate.before(reservation.getEndDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                    //Case 2: Incoming reservation starts during and ends during existing reservation
                    if (startDate.after(reservation.getStartDate()) && endDate.before(reservation.getEndDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                    //Case 3: Incoming reservation starts before but ends after existing reservation
                    if (startDate.before(reservation.getStartDate()) && endDate.after(reservation.getEndDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                    //Case 4: Incoming reservation starts during but ends during exising reservation
                    if (startDate.after(reservation.getStartDate()) && startDate.before(reservation.getEndDate()) && endDate.after(reservation.getEndDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                    //Case 5: Incoming reservation starts after existing reservation but different outlet and less than 2 hours apart
                    if (!returnOutlet.equals(incPickupOutlet) && startDateMinusTwoHours.before(reservation.getEndDate()) && startDateMinusTwoHours.after(reservation.getStartDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                    //Case 6: Incoming reservation starts before existing reservation but different outlet and less than 2 hours apart
                    if (!pickupOutlet.equals(incReturnOutlet) && endDatePlusTwoHours.after(reservation.getStartDate()) && endDatePlusTwoHours.before(reservation.getEndDate())) {
                        availableCars.replace(carCategoryEntity, availableCars.get(carCategoryEntity) - 1);
                        break;
                    }
                }
            }
        }
        return availableCars;
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
    public HashMap<CarModelEntity, Integer> retrieveCarModelsWithConditions(Date startDate, Date endDate, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, CarCategoryEntity carCategory) {
        HashMap<CarModelEntity, Integer> availableCars = retrieveCarModelsForCarCategoryWithCarQuantity(carCategory);
        Date startDateMinusTwoHours;
        Date endDatePlusTwoHours;
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.HOUR_OF_DAY, -2);
        startDateMinusTwoHours = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.HOUR_OF_DAY, 2);
        endDatePlusTwoHours = c.getTime();

        for (Map.Entry<CarModelEntity, Integer> entry : availableCars.entrySet()) {
            CarModelEntity carModelEntity = entry.getKey();

            for (ReservationEntity reservation : carModelEntity.getReservations()) {
                if (!reservation.isCancelled()) {
                    OutletEntity returnOutlet = reservation.getReturnOutlet();
                    OutletEntity pickupOutlet = reservation.getPickupOutlet();
                    //Case 1: Incoming reservation starts before but ends during existing reservation
                    if (startDate.before(reservation.getStartDate()) && endDate.after(reservation.getStartDate()) && endDate.before(reservation.getEndDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                    //Case 2: Incoming reservation starts during and ends during existing reservation
                    if (startDate.after(reservation.getStartDate()) && endDate.before(reservation.getEndDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                    //Case 3: Incoming reservation starts before but ends after existing reservation
                    if (startDate.before(reservation.getStartDate()) && endDate.after(reservation.getEndDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                    //Case 4: Incoming reservation starts during but ends during exising reservation
                    if (startDate.after(reservation.getStartDate()) && startDate.before(reservation.getEndDate()) && endDate.after(reservation.getEndDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                    //Case 5: Incoming reservation starts after existing reservation but different outlet and less than 2 hours apart
                    if (!returnOutlet.equals(incPickupOutlet) && startDateMinusTwoHours.before(reservation.getEndDate()) && startDateMinusTwoHours.after(reservation.getStartDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                    //Case 6: Incoming reservation starts before existing reservation but different outlet and less than 2 hours apart
                    if (!pickupOutlet.equals(incReturnOutlet) && endDatePlusTwoHours.after(reservation.getStartDate()) && endDatePlusTwoHours.before(reservation.getEndDate())) {
                        availableCars.replace(carModelEntity, availableCars.get(carModelEntity) - 1);
                        break;
                    }
                }
            }
        }
        return availableCars;
    }

    @Override
    public void searchForAvailableCars(PartnerEntity partnerEntity, CustomerEntity customerEntity) throws NoCarsException, NoRentalRatesFoundException {
        Scanner sc = new Scanner(System.in);
        OutletEntity incPickupOutlet;
        OutletEntity incReturnOutlet;
        CarCategoryEntity carCategory = null;
        CarModelEntity carModel = null;
        int selectedCategory;
        int selectedModel = -2;

        System.out.println("Enter year of starting date (YYYY)");
        int startYear = sc.nextInt() - 1900;
        System.out.println("Enter month of starting date (MM) (1-12)");
        int startMonth = sc.nextInt() - 1;
        System.out.println("Enter day of starting date (DD) (1-31)");
        int startDay = sc.nextInt();
        System.out.println("Enter hour of starting date (HH) (0-23)");
        int startHour = sc.nextInt();
        System.out.println("Enter minutes of starting date (HH) (0-59)");
        int startMinutes = sc.nextInt();
        Date startDate = new Date(startYear, startMonth, startDay, startHour, startMinutes);

        System.out.println("Enter year of ending date (YYYY)");
        int endYear = sc.nextInt() - 1900;
        System.out.println("Enter month of ending date (MM) (1-12)");
        int endMonth = sc.nextInt() - 1;
        System.out.println("Enter day of ending date (DD) (1-31)");
        int endDay = sc.nextInt();
        System.out.println("Enter hour of ending date (HH) (0-23)");
        int endHour = sc.nextInt();
        System.out.println("Enter minutes of ending date (HH) (0-59)");
        int endMinutes = sc.nextInt();
        Date endDate = new Date(endYear, endMonth, endDay, endHour, endMinutes);

        while (true) {
            System.out.println("Enter pickup outlet name");
            String pickupName = sc.next();
            try {
                incPickupOutlet = outletSessionBeanLocal.retrieveOutletEntityByName(pickupName);
                break;
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again");
            }
        }

        while (true) {
            System.out.println("Enter return outlet name");
            String returnName = sc.next();
            try {
                incReturnOutlet = outletSessionBeanLocal.retrieveOutletEntityByName(returnName);
                break;
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again");
            }
        }
        HashMap<CarCategoryEntity, Integer> availableCarCategories = retrieveCarCategoriesWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet);
        HashMap<CarCategoryEntity, Double> carCategoryPrice = new HashMap<CarCategoryEntity, Double>();

        for (Map.Entry<CarCategoryEntity, Integer> entry : availableCarCategories.entrySet()) {
            try {
                List<RentalRateEntity> rentalRates = calculateTotalRentalRate(entry.getKey(), startDate, endDate);
                double carRentalRate = 0;
                for (RentalRateEntity rentalRate : rentalRates) {
                    carRentalRate = carRentalRate + rentalRate.getDailyRate();
                }
                carCategoryPrice.put(entry.getKey(), carRentalRate);
                System.out.println("ID: " + entry.getKey().getCarCategoryId() + "Category: " + entry.getKey().getCarCategory() + ", Available Cars: " + entry.getValue() + " Total Rental Price: " + carRentalRate);
            } catch (NoRentalRatesFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("No rental rates found for " + entry.getKey().getCarCategory());
            }
        }

        if (!availableCarCategories.isEmpty()) {
            while (true) {
                System.out.println("Select ID of car category to reserve");
                selectedCategory = sc.nextInt();
                try {
                    carCategory = carSessionBeanLocal.retrieveCarCategoryEntityByCarCategoryId(selectedCategory);
                    break;
                } catch (CarCategoryNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Selected ID was not valid, please re-enter your selection");
                }
            }
        } else {
            throw new NoCarsException("No cars are available at this time for your selected timeframe");
        }

        System.out.println("Press 1 to select a specific model and any other number to skip");
        int option = sc.nextInt();

        if (option == 1) {
            HashMap<CarModelEntity, Integer> availableCarModels = retrieveCarModelsWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet, carCategory);

            for (Map.Entry<CarModelEntity, Integer> entry : availableCarModels.entrySet()) {
                System.out.println("ID: " + entry.getKey().getCarModelId() + "Model: " + entry.getKey().getMake() + " " + entry.getKey().getModel() + ", Available Cars: " + entry.getValue());
            }

            System.out.println("Enter ID of car model to reserve, or enter -1 to only select car category");
            selectedModel = sc.nextInt();
            while (selectedModel != -1) {
                try {
                    if (selectedModel != -1) {
                        carModel = carSessionBeanLocal.retrieveCarModelEntityByCarModelId(selectedModel);
                        break;
                    }
                } catch (CarModelNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Please try again");
                }
            }
        }

        if (customerEntity != null) {
            System.out.println("To continue to reserve the car you selected, please press 1");
            System.out.println("Press 2 to go back to the main menu");
            int reserve = 0;
            while (reserve != 2) {
                reserve = sc.nextInt();

                if (reserve == 1) {
                    reserveAvailableCar(carCategory, carModel, startDate, endDate, customerEntity, incPickupOutlet, incReturnOutlet, partnerEntity);
                }
            }
        }

    }

    @Override
    public void reserveAvailableCar(CarCategoryEntity carCategory, CarModelEntity carModel, Date startDate, Date endDate, CustomerEntity customerEntity, OutletEntity incPickupOutlet, OutletEntity incReturnOutlet, PartnerEntity partnerEntity) throws NoRentalRatesFoundException {
        List<RentalRateEntity> rentalRates = calculateTotalRentalRate(carCategory, startDate, endDate);
        Scanner sc = new Scanner(System.in);
        System.out.println("You can choose to pay upfront, or at time of pickup at the outlet");
        System.out.println("Press 1 to pay upfront, or any other number to pay at the outlet");
        int paymentChoice = sc.nextInt();
        System.out.println("Please key in your credit card number");
        String creditCardNumber = sc.next();
        System.out.println("Please key in your credit card cvv");
        String cvv = sc.next();
        boolean paid = false;
        if (paymentChoice == 1) {
            System.out.println("You will now be redirected to a payment portal (WIP)");
            System.out.println("Thank you for your payment");
            paid = true;
        } else {
            System.out.println("Please rememeber to pay at the outlet");
        }
        System.out.println("Please note that these are our company rules for cancellation");
        System.out.println("Less than 14 days but at least 7 days before pickup – 20% penalty");
        System.out.println("Less than 7 days but at least 3 days before pickup – 50% penalty");
        System.out.println("Less than 3 days before pickup – 70% penalty");
        System.out.println("Please hold on as your reservation is being created and confirmed");

        ReservationEntity reservation = new ReservationEntity(paid, creditCardNumber, cvv, startDate, endDate, customerEntity, incPickupOutlet, incReturnOutlet, rentalRates);
        reservation.setCarCategory(carCategory);
        if (carModel != null) {
            reservation.setCarModel(carModel);
        }
        if (partnerEntity != null) {
            reservation.setPartner(partnerEntity);
        }
        long reservationId = createReservationEntity(reservation);

        for (RentalRateEntity rentalRate : rentalRates) {
            rentalRate.setUsed(true);
            rentalRateSessionBeanLocal.updateRentalRateEntity(rentalRate);
        }

        carCategory.getReservations().add(reservation);
        carSessionBeanLocal.updateCarCategoryEntity(carCategory);

        if (carModel != null) {
            carModel.getReservations().add(reservation);
            carSessionBeanLocal.updateCarModelEntity(carModel);
        }

        customerEntity.getReservations().add(reservation);
        customerSessionBeanLocal.updateCustomerEntity(customerEntity);

        if (partnerEntity != null) {
            partnerEntity.getReservations().add(reservation);
            partnerSessionBeanLocal.updatePartnerEntity(partnerEntity);
        }

        System.out.println("Reservation with ID: " + reservationId + " has been successfully created");
    }

    public List<RentalRateEntity> calculateTotalRentalRate(CarCategoryEntity carCategory, Date startDate, Date endDate) throws NoRentalRatesFoundException {
        System.out.println("Now calculating rental rate for specified period and car category");

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(new Date(startDate.getYear(), startDate.getMonth(), startDate.getDay()));
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(new Date(endDate.getYear(), endDate.getMonth(), endDate.getDay()));
        ArrayList<RentalRateEntity> rentalRates = new ArrayList<RentalRateEntity>();

        //Get rental rates for every day
        while (startCalendar.before(endCalendar)) {
            Date curDate = startCalendar.getTime();
            double cheapestRateOfDay = -1;
            RentalRateEntity chosenRentalRate = null;
            boolean isFound = false;
            for (RentalRateEntity rentalRate : carCategory.getRentalRates()) {
                Date rentalRateStartDate = rentalRate.getValidityPeriodStart();
                Date rentalRateEndDate = rentalRate.getValidityPeriodEnd();
                if (rentalRateStartDate.equals(curDate) || rentalRateStartDate.before(curDate) && rentalRateEndDate.after(curDate)) {
                    if (rentalRate.isDisabled() == false && (cheapestRateOfDay == -1 || rentalRate.getDailyRate() < cheapestRateOfDay)) {
                        cheapestRateOfDay = rentalRate.getDailyRate();
                        chosenRentalRate = rentalRate;
                        isFound = true;
                    }
                }
            }
            if (isFound = true) {
                rentalRates.add(chosenRentalRate);
            } else {
                throw new NoRentalRatesFoundException("Rental Rates not found for current date and category");
            }
            isFound = false;
            startCalendar.add(Calendar.DATE, 1);
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

                updateReservationEntity(reservationEntity);
                carSessionBeanLocal.updateCarEntity(carEntity);
                break;
            } else if (carEntity.getCurrentOutlet() == null && carEntity.getStatus().equals(CarStatusEnum.INOUTLET)) {
                throw new NullCurrentOutletException("Car " + carEntity.getCarId() + " has a null current outlet but is INOUTLET");
            }
        }

        if (reservationEntity.getCar() == null) {
            List<CarEntity> backupCars = getBackupCarsForReservation(reservationEntity, reservationEntity.getPickupOutlet());
            for (CarEntity carEntity : backupCars) {
                if (carEntity.getCurrentReservation() != null) {
                    reservationEntity.setCar(carEntity);
                }
                carEntity.setCurrentReservation(reservationEntity);
                carEntity.setReturnOutlet(reservationEntity.getCarReturn().getOutlet());
                DispatchEntity dispatchEntity = new DispatchEntity(reservationEntity, carEntity, carEntity.getCurrentOutlet(), reservationEntity.getPickupOutlet());

                dispatchSessionBeanLocal.createDispatchEntity(dispatchEntity);
                reservationEntity.setDispatch(dispatchEntity);

                updateReservationEntity(reservationEntity);
                carSessionBeanLocal.updateCarEntity(carEntity);
                dispatchSessionBeanLocal.updateDispatchEntity(dispatchEntity);

                break;
            }
        }

        if (reservationEntity.getCar() == null) {
            throw new NoCarsException("No car that satisfies the conditions have been found for reservation " + reservationEntity.getReservationId());
        }

    }
}
