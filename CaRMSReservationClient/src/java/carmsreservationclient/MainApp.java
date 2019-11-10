/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.CustomerEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.InvalidLoginException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.ReservationNotFoundException;
import java.util.Date;
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
    private ReservationSessionBeanRemote reservationSessionBean;
    private CustomerEntity customer;

    private CustomerEntity customerEntity;
    private boolean isLoggedin = false;

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBean) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBean = reservationSessionBean;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Welcome to Merlion Car Rental Reservation Client*****");

        int option = 0;
        while (true) {
            System.out.println("1: Register As Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Customer Logout");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    registerCustomer();
                    break;
                case 2: {
                    try {
                        customerLogin();
                        mainMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Login failed, please try again");
                    }
                }
                break;
                case 3:
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
            System.out.println("1: Search and Then Reserve Car");
            System.out.println("2: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Customer Logout");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    doSearchForCar();
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
            if (option == 5){
                break;
            }
        }
    }
    
    private void doSearchForCar() {
        try {
            reservationSessionBean.searchForAvailableCars(null, customerEntity);
        } catch (NoCarsException | NoRentalRatesFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void doCancelReservation() {
        Date currentDate = new Date();
        reservationSessionBean.cancelReservation(currentDate);
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
            ReservationEntity reservationEntity = reservationSessionBean.retrieveReservationEntityByReservationId(reservationId);
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
        List<ReservationEntity> reservations = reservationSessionBean.retrieveReservationsByCustomerId(customerEntity.getCustomerId());
        
        for (ReservationEntity reservation : reservations){
            System.out.println("Reservation ID: " + reservation.getReservationId() + " Start Date: " + reservation.getStartDate() + " End Date: " + reservation.getEndDate());
            System.out.println("    Car Category: " + reservation.getCarCategory().getCarCategory() + " Pickup Outlet: " + reservation.getPickupOutlet().getName() + " Return Outlet: " + reservation.getReturnOutlet().getName());
        }
    }

}
