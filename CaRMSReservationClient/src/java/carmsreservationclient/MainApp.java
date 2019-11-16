/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategoryEntity;
import entity.CarModelEntity;
import entity.CustomerEntity;
import entity.OutletEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarCategoryNotFoundException;
import exception.CarModelNotFoundException;
import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elgin Patt
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    private CustomerEntity customerEntity = null;
    private boolean isLoggedin = false;

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Welcome to Merlion Car Rental Reservation Client*****");

        int option = 0;
        while (true) {
            System.out.println("1: Register As Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Car");
            System.out.println("4: Customer Logout");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    try {
                        registerCustomer();
                    } catch (CustomerAlreadyExistsException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;

                case 2:
                    try {
                        customerLogin();
                        mainMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Login failed, please try again");
                    }
                    break;
                case 3:
                    try {
                        doSearchForCar();
                    } catch (NoRentalRatesFoundException | NoCarsException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 4:
                    doLogout();
                    break;
            }

        }
    }

    private void registerCustomer() throws CustomerAlreadyExistsException {
        Scanner sc = new Scanner(System.in);
        String username;
        String password;
        String email;
        String phoneNum;
        String passportNum;

        System.out.print("Enter Your Username: ");
        username = sc.nextLine().trim();
        System.out.print("Enter Your Password: ");
        password = sc.nextLine().trim();
        System.out.print("Enter Your Email: ");
        email = sc.nextLine().trim();
        System.out.print("Enter Your Phone Number: ");
        phoneNum = sc.nextLine().trim();
        System.out.print("Enter Your Passport Number: ");
        passportNum = sc.nextLine().trim();
        CustomerEntity currCust = new CustomerEntity(username, password, email);
        currCust.setPassportNumber(passportNum);
        currCust.setMobilePhoneNumber(phoneNum);

        try {
            customerSessionBeanRemote.retrieveCustomerEntityByUsername(username);
            customerSessionBeanRemote.retrieveCustomerEntityByEmail(email);
            throw new CustomerAlreadyExistsException("Customer with your username/email already exists");
        } catch (CustomerNotFoundException ex) {
            long custId = customerSessionBeanRemote.createCustomerEntity(currCust);
            System.out.println("Customer has been created with id: " + custId);
        }
    }

    private void customerLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Your Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter Your Password: ");
        String password = sc.nextLine().trim();

        try {
            CustomerEntity cust = customerSessionBeanRemote.doLogin(email, password);
            this.customerEntity = cust;
            isLoggedin = true;
        } catch (InvalidLoginException ex) {
            throw ex;
        }
    }

    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (isLoggedin) {
            System.out.println("1: Search and Reserve Car");
            System.out.println("2: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Customer Logout");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    try {
                        doSearchForCar();
                        break;
                    } catch (NoRentalRatesFoundException | NoCarsException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 2:
                    doCancelReservation();
                    break;
                case 3:
                    doViewReservationDetails();
                    break;
                case 4:
                    doViewAllMyReservations();
                    break;
                case 5:
                    doLogout();
                    break;
            }
            if (option == 5) {
                break;
            }
        }
    }

    private void doSearchForCar() throws NoRentalRatesFoundException, NoCarsException {
        Scanner sc = new Scanner(System.in);
        OutletEntity incPickupOutlet;
        OutletEntity incReturnOutlet;
        CarCategoryEntity carCategory = null;
        CarModelEntity carModel = null;
        int selectedCategory;
        int selectedModel = -2;

        Date startDate;
        Date endDate;

        while (true) {
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
            startDate = new Date(startYear, startMonth, startDay, startHour, startMinutes);

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
            endDate = new Date(endYear, endMonth, endDay, endHour, endMinutes);

            if (endDate.after(startDate)) {
                break;
            } else {
                System.out.println("Please key in a valid dates such that the end is after the start date");
            }
        }

        while (true) {
            sc.nextLine();
            System.out.println("Enter pickup outlet name");
            String pickupName = sc.nextLine();
            try {
                incPickupOutlet = outletSessionBeanRemote.retrieveOutletEntityByName(pickupName);
                int openingTime = -1;
                int closingTime = -1;
                if (incPickupOutlet.getOpeningTime() != null) {
                    openingTime = Integer.valueOf(incPickupOutlet.getOpeningTime());
                }
                if (incPickupOutlet.getClosingTime() != null) {
                    closingTime = Integer.valueOf(incPickupOutlet.getClosingTime());
                }
                int startTime = startDate.getHours() * 100 + startDate.getMinutes();

                if (openingTime != -1 && closingTime != -1 && startTime >= openingTime && startTime <= closingTime || (incPickupOutlet.getOpeningTime() == null && incPickupOutlet.getClosingTime() == null)) {
                    break;
                } else {
                    System.out.println("The selected outlet is closed during your selected opening hours");
                }
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again, no outlet with that name is found");
            }
        }

        while (true) {
            System.out.println("Enter return outlet name");
            String returnName = sc.nextLine();
            try {
                incReturnOutlet = outletSessionBeanRemote.retrieveOutletEntityByName(returnName);
                int openingTime = -1;
                int closingTime = -1;
                if (incReturnOutlet.getOpeningTime() != null) {
                    openingTime = Integer.valueOf(incReturnOutlet.getOpeningTime());
                }
                if (incReturnOutlet.getClosingTime() != null) {
                    closingTime = Integer.valueOf(incReturnOutlet.getClosingTime());
                }
                int startTime = startDate.getHours() * 100 + startDate.getMinutes();

                if (openingTime != -1 && closingTime != -1 && startTime >= openingTime && startTime <= closingTime || (incReturnOutlet.getOpeningTime() == null && incReturnOutlet.getClosingTime() == null)) {
                    break;
                } else {
                    System.out.println("The selected outlet is closed during your selected opening hours");
                }
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again, no outlet with that name is found");
            }
        }
        List<CarCategoryEntity> availableCarCategories = reservationSessionBeanRemote.retrieveCarCategoriesWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet);
        HashMap<CarCategoryEntity, Double> carCategoryPrice = new HashMap<CarCategoryEntity, Double>();

        for (CarCategoryEntity carCategoryEntity : availableCarCategories) {
            try {
                List<RentalRateEntity> rentalRates = reservationSessionBeanRemote.calculateTotalRentalRate(carCategoryEntity, startDate, endDate);
                double carRentalRate = 0;
                for (RentalRateEntity rentalRate : rentalRates) {
                    carRentalRate = carRentalRate + rentalRate.getDailyRate();
                }
                carCategoryPrice.put(carCategoryEntity, carRentalRate);
                System.out.println("ID: " + carCategoryEntity.getCarCategoryId() + " Category: " + carCategoryEntity.getCarCategory() + " Total Rental Price: " + carRentalRate);
            } catch (NoRentalRatesFoundException e) {
                //System.out.println(e.getMessage());
                //System.out.println("No rental rates found for " + entry.getKey().getCarCategory());
            }
        }

        if (!availableCarCategories.isEmpty()) {
            while (true) {
                System.out.println("Select ID of car category to view available models");
                selectedCategory = sc.nextInt();
                try {
                    carCategory = carSessionBeanRemote.retrieveCarCategoryEntityByCarCategoryId(selectedCategory);
                    break;
                } catch (CarCategoryNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Selected ID was not valid, please re-enter your selection");
                }
            }
        } else {
            throw new NoCarsException("No cars are available at this time for your selected timeframe");
        }

        System.out.println("Press 1 to view available models for the selected car category and any other number to skip");
        int option = sc.nextInt();

        if (option == 1) {
            List<CarModelEntity> availableCarModels = reservationSessionBeanRemote.retrieveCarModelsWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet, carCategory);

            for (CarModelEntity carModelEntity : availableCarModels) {
                System.out.println("ID: " + carModelEntity.getCarModelId() + " Model: " + carModelEntity.getMake() + " " + carModelEntity.getModel());
            }

            System.out.println("Enter ID of car model to reserve, or enter -1 to only select car category");
            selectedModel = sc.nextInt();
            while (selectedModel != -1) {
                try {
                    if (selectedModel != -1) {
                        carModel = carSessionBeanRemote.retrieveCarModelEntityByCarModelId(selectedModel);
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
            if (reserve != 2) {
                reserve = sc.nextInt();

                if (reserve == 1) {
                    List<RentalRateEntity> rentalRates = reservationSessionBeanRemote.calculateTotalRentalRate(carCategory, startDate, endDate);
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
                        System.out.println("Please remember to pay at the outlet");
                    }
                    System.out.println("Please note that these are our company rules for cancellation");
                    System.out.println("Less than 14 days but at least 7 days before pickup – 20% penalty");
                    System.out.println("Less than 7 days but at least 3 days before pickup – 50% penalty");
                    System.out.println("Less than 3 days before pickup – 70% penalty");
                    System.out.println("Please hold on as your reservation is being created and confirmed");

                    double price = 0;
                    for (RentalRateEntity rentalRateEntity : rentalRates) {
                        price = price + rentalRateEntity.getDailyRate();
                    }

                    ReservationEntity reservation = new ReservationEntity(paid, creditCardNumber, cvv, startDate, endDate, customerEntity, incPickupOutlet, incReturnOutlet, price);
                    reservation.setCarCategory(carCategory);
                    if (carModel != null) {
                        reservation.setCarModel(carModel);
                    }
                    long reservationId = reservationSessionBeanRemote.createReservationEntity(reservation);

                    for (RentalRateEntity rentalRate : rentalRates) {
                        rentalRate.setUsed(true);
                        rentalRateSessionBeanRemote.updateRentalRateEntity(rentalRate);
                    }

                    if (!carCategory.getReservations().contains(reservation)) {
                        carCategory.getReservations().add(reservation);
//                        carSessionBeanRemote.updateCarCategoryEntity(carCategory);
                    }

                    if (carModel != null && !carModel.getReservations().contains(reservation)) {
                        carModel.getReservations().add(reservation);
//                        carSessionBeanRemote.updateCarModelEntity(carModel);
                    }

                    if (!customerEntity.getReservations().contains(reservation)) {
                        customerEntity.getReservations().add(reservation);
//                        customerSessionBeanRemote.updateCustomerEntity(customerEntity);
                    }
                    
                    System.out.println("Reservation with ID: " + reservationId + " has been successfully created");
                }
            }
        }
    }

    private void doCancelReservation() {
        Date currentDate = new Date();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please key in the reservation ID you would like to cancel");
        long reservationId = sc.nextLong();

        try {
            ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationEntityByReservationId(reservationId);
            double cost = reservationEntity.getPrice();
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
            reservationSessionBeanRemote.updateReservationEntity(reservationEntity);
            System.out.println("Reservation with Id: " + reservationEntity.getReservationId() + " has been cancelled");
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doLogout() {
        Scanner sc = new Scanner(System.in);
        if (isLoggedin == false) {
            System.out.println("You are not logged in");
            return;
        }

        String option = "";
        System.out.println("*****Please confirm you would like to logout(Y to confirm)");
        option = sc.next();

        if (option.equals("Y")) {
            customerEntity = null;
            isLoggedin = true;
        }
    }

    private void doViewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Your Reservation Id: ");
        long reservationId = sc.nextLong();

        try {
            ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationEntityByReservationId(reservationId);
            System.out.println("Reservation Start Date: " + reservationEntity.getStartDate().toString());
            System.out.println("Reservation End Date: " + reservationEntity.getEndDate().toString());
            System.out.println("Car Category: " + reservationEntity.getCarCategory().getCarCategory());
            System.out.println("Car Model: " + reservationEntity.getCarModel().getModel());
            System.out.println("Car Make: " + reservationEntity.getCarModel().getMake());
            System.out.println("Car Pick up location: " + reservationEntity.getPickupOutlet().getName());
            System.out.println("Car Return location: " + reservationEntity.getReturnOutlet().getName());
            System.out.println("Cancelled : " + reservationEntity.isCancelled());
            
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doViewAllMyReservations() {
        System.out.println("Viewing all past reservations");
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservationsByCustomerId(customerEntity.getCustomerId());

        for (ReservationEntity reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId() + " Start Date: " + reservation.getStartDate() + " End Date: " + reservation.getEndDate());
            if (reservation.getCarModel() == null) {
                System.out.println("Car Category: " + reservation.getCarCategory().getCarCategory());
            } else {
                System.out.println("Car Category: " + reservation.getCarCategory().getCarCategory() + " Car Model: " + reservation.getCarModel().getMake() + " " + reservation.getCarModel().getModel());
            }
            System.out.println("Pickup Outlet: " + reservation.getPickupOutlet().getName() + " Return Outlet: " + reservation.getReturnOutlet().getName() + "| Cancelled: " + reservation.isCancelled());
        }
    }

}
