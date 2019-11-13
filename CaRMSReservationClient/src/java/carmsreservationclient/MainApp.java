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
import entity.CarCategoryEntity;
import entity.CarModelEntity;
import entity.CustomerEntity;
import entity.OutletEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarCategoryNotFoundException;
import exception.CarModelNotFoundException;
import exception.InvalidLoginException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private CustomerEntity customerEntity = null;
    private boolean isLoggedin = false;

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
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
                    registerCustomer();
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

    private void registerCustomer() {
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

        long custId = customerSessionBeanRemote.createCustomerEntity(currCust);
        System.out.println("Customer has been created with id: " + custId);
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

            switch (option) {
                case 1:
                    try {
                        doSearchForCar();
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
                break;
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again, no outlet with that name is found");
            }
        }
        HashMap<CarCategoryEntity, Integer> availableCarCategories = reservationSessionBeanRemote.retrieveCarCategoriesWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet);
        HashMap<CarCategoryEntity, Double> carCategoryPrice = new HashMap<CarCategoryEntity, Double>();

        for (Map.Entry<CarCategoryEntity, Integer> entry : availableCarCategories.entrySet()) {
            try {
                List<RentalRateEntity> rentalRates = reservationSessionBeanRemote.calculateTotalRentalRate(entry.getKey(), startDate, endDate);
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

        System.out.println("Press 1 to select a specific model and any other number to skip");
        int option = sc.nextInt();

        if (option == 1) {
            HashMap<CarModelEntity, Integer> availableCarModels = reservationSessionBeanRemote.retrieveCarModelsWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet, carCategory);

            for (Map.Entry<CarModelEntity, Integer> entry : availableCarModels.entrySet()) {
                System.out.println("ID: " + entry.getKey().getCarModelId() + "Model: " + entry.getKey().getMake() + " " + entry.getKey().getModel() + ", Available Cars: " + entry.getValue());
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
            while (reserve != 2) {
                reserve = sc.nextInt();

                if (reserve == 1) {
                    reservationSessionBeanRemote.reserveAvailableCar(carCategory, carModel, startDate, endDate, customerEntity, incPickupOutlet, incReturnOutlet, null);
                }
            }
        }
    }

    private void doCancelReservation() {
        Date currentDate = new Date();
        reservationSessionBeanRemote.cancelReservation(currentDate);
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
            List<RentalRateEntity> rentalRates = reservationEntity.getRentalRates();
            System.out.println("Reservation Start Date: " + reservationEntity.getStartDate().toString());
            System.out.println("Reservation End Date: " + reservationEntity.getEndDate().toString());
            for (int i = 1; i < rentalRates.size(); i++) {
                System.out.println("Rental Rate #" + i + ": " + rentalRates.get(i).getDailyRate());
            }
            System.out.println("Car Category: " + reservationEntity.getCarCategory().getCarCategory());
            System.out.println("Car Model: " + reservationEntity.getCarModel().getModel());
            System.out.println("Car Make: " + reservationEntity.getCarModel().getMake());
            System.out.println("Car Pick up location: " + reservationEntity.getCarPickup().getOutlet().getAddress());
            System.out.println("Car Return location: " + reservationEntity.getCarReturn().getOutlet().getAddress());

        } catch (ReservationNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void doViewAllMyReservations() {
        System.out.print("Viewing all past reservations");
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservationsByCustomerId(customerEntity.getCustomerId());

        for (ReservationEntity reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId() + " Start Date: " + reservation.getStartDate() + " End Date: " + reservation.getEndDate());
            System.out.println("Car Category: " + reservation.getCarCategory().getCarCategory() + " Pickup Outlet: " + reservation.getPickupOutlet().getName() + " Return Outlet: " + reservation.getReturnOutlet().getName());
        }
    }

}
