/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemcarmsclient;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.NoCarsFoundException;
import ws.client.CarCategoryEntity;
import ws.client.CarCategoryNotFoundException_Exception;
import ws.client.CarModelEntity;
import ws.client.CarModelNotFoundException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.NoCarModelsException_Exception;
import ws.client.NoCarsException_Exception;
import ws.client.NoRentalRatesFoundException_Exception;
import ws.client.OutletEntity;
import ws.client.OutletNotFoundException_Exception;
import ws.client.PartnerEntity;
import ws.client.RentalRateEntity;
import ws.client.RentalRateNotFoundException_Exception;
import ws.client.ReservationEntity;
import ws.client.ReservationNotFoundException_Exception;

/**
 *
 * @author Elgin Patt
 */
public class HolidayReservationSystemCaRMSClientApp {

    public HolidayReservationSystemCaRMSClientApp() {
    }

    boolean loggedIn = false;
    PartnerEntity partnerEntity;
    CustomerEntity customerEntity;

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Holiday Reservation System Car Rental Client");
        while (!loggedIn) {
            System.out.println("Please log in using our partner username and password");
            System.out.println("Please enter partner username");
            String username = sc.next();
            System.out.println("Please enter partner password");
            String password = sc.next();

            try {
                partnerEntity = doLogin(username, password);
                loggedIn = true;
            } catch (InvalidLoginException_Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (loggedIn) {
            mainMenu();
        }
    }

    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        System.out.println("1: Search and Then Reserve Car");
        System.out.println("2: Cancel Reservation");
        System.out.println("3: View Reservation Details");
        System.out.println("4: View All My Reservations");
        System.out.println("5: Partner Logout");
        option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                try {
                    doSearchCar();
                } catch (NoRentalRatesFoundException_Exception | NoCarsFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                break;

            case 2:
                doCancelReservation();
                break;
            case 3:
                System.out.println("Enter Your Reservation Id: ");
                long reservationId = sc.nextLong();

                try {
                    ReservationEntity reservationEntity = retrieveReservationEntityByReservationId(reservationId);
                    CarCategoryEntity carCategoryEntity = retrieveCarCategoryByReservationId(reservationEntity.getReservationId());
                    CarModelEntity carModelEntity = retrieveCarModelByReservationId(reservationEntity.getReservationId());

                    System.out.println("Reservation Start Date: " + reservationEntity.getStartDate().toString());
                    System.out.println("Reservation End Date: " + reservationEntity.getEndDate().toString());
                    System.out.println("Car Category: " + carCategoryEntity.getCarCategory());
                    System.out.println("Car Model: " + carModelEntity.getModel());
                    System.out.println("Car Make: " + carModelEntity.getMake());
                    System.out.println("Car Pick up location: " + reservationEntity.getPickupOutlet().getName());
                    System.out.println("Car Return location: " + reservationEntity.getReturnOutlet().getName());
                    System.out.println("Cancelled : " + reservationEntity.isCancelled());

                } catch (ReservationNotFoundException_Exception | NoCarsException_Exception | NoCarModelsException_Exception | NullPointerException ex) {
                    System.out.println(ex.getMessage());
                }
                break;

            case 4:
                System.out.println("Viewing all past reservations");
                List<ReservationEntity> reservations = retrieveReservationsByPartnerId(partnerEntity.getPartnerId());

                for (ReservationEntity reservation : reservations) {
                    try {
                    CarCategoryEntity carCategoryEntity = retrieveCarCategoryByReservationId(reservation.getReservationId());
                    CarModelEntity carModelEntity = retrieveCarModelByReservationId(reservation.getReservationId());
                    
                    System.out.println("Reservation ID: " + reservation.getReservationId() + " Start Date: " + reservation.getStartDate() + " End Date: " + reservation.getEndDate());
                    if (carModelEntity == null) {
                        System.out.println("Car Category: " + carCategoryEntity.getCarCategory());
                    } else {
                        System.out.println("Car Category: " + carCategoryEntity.getCarCategory() + "| Car Model: " + carModelEntity.getMake() + " " + carModelEntity.getModel());
                    }
                    System.out.println("Pickup Outlet: " + reservation.getPickupOutlet().getName() + "| Return Outlet: " + reservation.getReturnOutlet().getName() + "| Cancelled: " + reservation.isCancelled());
                    } catch (NoCarsException_Exception | NoCarModelsException_Exception ex){
                    
                    }
                }
                break;

            case 5:
                loggedIn = false;
                System.out.println("Logged out of the system");
                break;
        }
    }

    private void doSearchCar() throws NoRentalRatesFoundException_Exception, NoCarsFoundException {
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
                incPickupOutlet = retrieveOutletEntityByName(pickupName);
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
            } catch (OutletNotFoundException_Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again, no outlet with that name is found");
            }
        }

        while (true) {
            System.out.println("Enter return outlet name");
            String returnName = sc.nextLine();
            try {
                incReturnOutlet = retrieveOutletEntityByName(returnName);
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
            } catch (OutletNotFoundException_Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Please try again, no outlet with that name is found");
            }
        }
        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        //System.out.println(startCalendar.getTime());
        XMLGregorianCalendar xmlStartDate;

        GregorianCalendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        //System.out.println(endCalendar.getTime());
        XMLGregorianCalendar xmlEndDate;
        
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(startCalendar);
            System.out.println("Year: " + xmlStartDate.getYear() + " Month: " + xmlStartDate.getMonth() + " Day: " + xmlStartDate.getDay() + " Hour: " + xmlStartDate.getHour());
            
            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(endCalendar);
            System.out.println("Year: " + xmlEndDate.getYear() + " Month: " + xmlEndDate.getMonth() + " Day: " + xmlEndDate.getDay() + " Hour: " + xmlEndDate.getHour());
            List<CarCategoryEntity> availableCarCategories = retrieveCarCategoriesWithConditions(xmlStartDate, xmlEndDate, incPickupOutlet, incReturnOutlet);

            HashMap<CarCategoryEntity, Double> carCategoryPrice = new HashMap<CarCategoryEntity, Double>();

            for (CarCategoryEntity carCategoryEntity : availableCarCategories) {
                try {
                    List<RentalRateEntity> rentalRates = calculateTotalRentalRate(carCategoryEntity, xmlStartDate, xmlEndDate);
                    double carRentalRate = 0;
                    for (RentalRateEntity rentalRate : rentalRates) {
                        carRentalRate = carRentalRate + rentalRate.getDailyRate();
                    }
                    carCategoryPrice.put(carCategoryEntity, carRentalRate);
                    System.out.println("ID: " + carCategoryEntity.getCarCategoryId() + " Category: " + carCategoryEntity.getCarCategory() + " Total Rental Price: " + carRentalRate);
                } catch (NoRentalRatesFoundException_Exception e) {
                    //System.out.println(e.getMessage());
                    //System.out.println("No rental rates found for " + entry.getKey().getCarCategory());
                }
            }

            if (!availableCarCategories.isEmpty()) {
                while (true) {
                    System.out.println("Select ID of car category to view available models");
                    selectedCategory = sc.nextInt();
                    try {
                        carCategory = retrieveCarCategoryEntityByCarCategoryId(selectedCategory);
                        break;
                    } catch (CarCategoryNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("Selected ID was not valid, please re-enter your selection");
                    }
                }
            } else {
                throw new NoCarsFoundException("No cars found for specified timeperiod");
            }

            System.out.println("Press 1 to select a specific model and any other number to skip");
            int option = sc.nextInt();

            if (option == 1) {
                List<CarModelEntity> availableCarModels = retrieveCarModelsWithConditions(xmlStartDate, xmlEndDate, incPickupOutlet, incReturnOutlet, carCategory);

                for (CarModelEntity carModelEntity : availableCarModels) {
                    System.out.println("ID: " + carModelEntity.getCarModelId() + " Model: " + carModelEntity.getMake() + " " + carModelEntity.getModel());
                }

                System.out.println("Enter ID of car model to reserve, or enter -1 to only select car category");
                selectedModel = sc.nextInt();
                while (selectedModel != -1) {
                    try {
                        if (selectedModel != -1) {
                            carModel = retrieveCarModelEntityByCarModelId(selectedModel);
                            break;
                        }
                    } catch (CarModelNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("Please try again");
                    }
                }
            }

            while (true) {
                System.out.println("Press 1 to book for an existing customer, and 2 for a new customer");

                int response = sc.nextInt();

                if (response == 1) {
                    System.out.println("Please enter customer email");
                    String email = sc.next();
                    sc.nextLine();
                    try {
                        customerEntity = retrieveCustomerEntityByEmail(email);
                        break;
                    } catch (CustomerNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    sc.nextLine();
                    System.out.print("Enter their username: ");
                    String username = sc.nextLine().trim();
                    System.out.print("Enter their password: ");
                    String password = sc.nextLine().trim();
                    System.out.print("Enter their email: ");
                    String email = sc.nextLine().trim();
                    System.out.print("Enter their phone Nnumber: ");
                    String phoneNum = sc.nextLine().trim();
                    System.out.print("Enter their passport number: ");
                    String passportNum = sc.nextLine().trim();
                    customerEntity = new CustomerEntity();
                    customerEntity.setUsername(username);
                    customerEntity.setPassword(password);
                    customerEntity.setEmail(email);
                    customerEntity.setMobilePhoneNumber(phoneNum);
                    customerEntity.setPassportNumber(passportNum);
                    createCustomerEntity(customerEntity);

                    partnerEntity.getCustomers().add(customerEntity);
                }
            }

            System.out.println("To continue to reserve the car you selected, please press 1");
            System.out.println("Press 2 to go back to the main menu");
            int reserve = 0;
            if (reserve != 2) {
                reserve = sc.nextInt();

                if (reserve == 1) {
                    List<RentalRateEntity> rentalRates = calculateTotalRentalRate(carCategory, xmlStartDate, xmlEndDate);
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

                    long reservationId = createReservationEntity(paid, creditCardNumber, cvv, xmlStartDate, xmlEndDate, customerEntity, incPickupOutlet, incReturnOutlet, price, partnerEntity, carCategory, carModel);

                    ReservationEntity reservation;
                    try {
                        reservation = retrieveReservationEntityByReservationId(reservationId);
                        if (!carCategory.getReservations().contains(reservation)) {
                            carCategory.getReservations().add(reservation);
                        }

                        if (carModel != null && !carModel.getReservations().contains(reservation)) {
                            carModel.getReservations().add(reservation);
                        }

                        if (!customerEntity.getReservations().contains(reservation)) {
                            customerEntity.getReservations().add(reservation);
                        }
                    } catch (ReservationNotFoundException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    for (RentalRateEntity rentalRate : rentalRates) {
                        setRentalRateAsUsed(rentalRate.getRentalRateId());
                    }

                    System.out.println("Reservation with ID: " + reservationId + " has been successfully created");
                }
            }
        } catch (DatatypeConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doCancelReservation() {
        Date currentDate = new Date();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please key in the reservation ID you would like to cancel");
        long reservationId = sc.nextLong();
        double cost = 0;

        try {
            ReservationEntity reservationEntity = retrieveReservationEntityByReservationId(reservationId);
            cost = reservationEntity.getPrice();
            boolean paid = reservationEntity.isPaid();

            Calendar reservationC = new GregorianCalendar();

            reservationC.setTime(reservationEntity.getStartDate().toGregorianCalendar().getTime());
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
                setReservationToCancelledByReservationId(reservationEntity.getReservationId());
            System.out.println("Reservation with Id: " + reservationEntity.getReservationId() + " has been cancelled");
        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static PartnerEntity doLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.doLogin(arg0, arg1);
    }

    private static java.util.List<ws.client.CarCategoryEntity> retrieveCarCategoriesWithConditions(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, ws.client.OutletEntity arg2, ws.client.OutletEntity arg3) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarCategoriesWithConditions(arg0, arg1, arg2, arg3);
    }

    private static CarCategoryEntity retrieveCarCategoryEntityByCarCategoryId(long arg0) throws CarCategoryNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarCategoryEntityByCarCategoryId(arg0);
    }

    private static CarModelEntity retrieveCarModelEntityByCarModelId(long arg0) throws CarModelNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarModelEntityByCarModelId(arg0);
    }

    private static java.util.List<ws.client.CarModelEntity> retrieveCarModelsWithConditions(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, ws.client.OutletEntity arg2, ws.client.OutletEntity arg3, ws.client.CarCategoryEntity arg4) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarModelsWithConditions(arg0, arg1, arg2, arg3, arg4);
    }

    private static CustomerEntity retrieveCustomerEntityByCustomerId(java.lang.Long arg0) throws CustomerNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCustomerEntityByCustomerId(arg0);
    }

    private static CustomerEntity retrieveCustomerEntityByEmail(java.lang.String arg0) throws CustomerNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCustomerEntityByEmail(arg0);
    }

    private static RentalRateEntity retrieveRentalRateEntityByRentalRateId(long arg0) throws RentalRateNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveRentalRateEntityByRentalRateId(arg0);
    }

    private static ReservationEntity retrieveReservationEntityByReservationId(java.lang.Long arg0) throws ReservationNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveReservationEntityByReservationId(arg0);
    }

    private static java.util.List<ws.client.ReservationEntity> retrieveReservationsByPartnerId(java.lang.Long arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveReservationsByPartnerId(arg0);
    }

    private static void updateRentalRateEntity(ws.client.RentalRateEntity arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.updateRentalRateEntity(arg0);
    }

    private static void updateReservationEntity(ws.client.ReservationEntity arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.updateReservationEntity(arg0);
    }

    private static OutletEntity retrieveOutletEntityByName(java.lang.String arg0) throws OutletNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveOutletEntityByName(arg0);
    }

    private static long createCustomerEntity(ws.client.CustomerEntity arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.createCustomerEntity(arg0);
    }

    private static void setRentalRateAsUsed(long arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.setRentalRateAsUsed(arg0);
    }

    private static long createReservationEntity(boolean arg0, java.lang.String arg1, java.lang.String arg2, javax.xml.datatype.XMLGregorianCalendar arg3, javax.xml.datatype.XMLGregorianCalendar arg4, ws.client.CustomerEntity arg5, ws.client.OutletEntity arg6, ws.client.OutletEntity arg7, double arg8, ws.client.PartnerEntity arg9, ws.client.CarCategoryEntity arg10, ws.client.CarModelEntity arg11) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.createReservationEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    }

    private static CarCategoryEntity retrieveCarCategoryByReservationId(long arg0) throws NoCarsException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarCategoryByReservationId(arg0);
    }

    private static CarModelEntity retrieveCarModelByReservationId(long arg0) throws NoCarModelsException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCarModelByReservationId(arg0);
    }

    private static java.util.List<ws.client.RentalRateEntity> calculateTotalRentalRate(ws.client.CarCategoryEntity arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2) throws NoRentalRatesFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.calculateTotalRentalRate(arg0, arg1, arg2);
    }

    private static void setReservationToCancelledByReservationId(long arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.setReservationToCancelledByReservationId(arg0);
    }

}
