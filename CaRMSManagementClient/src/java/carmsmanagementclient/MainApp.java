/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarPickupReturnSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DispatchSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.EmployeeEntity;
import entity.ReservationEntity;
import exception.InvalidLoginException;
import exception.NoCarsException;
import exception.NoReservationsException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;

public class MainApp {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private DispatchSessionBeanRemote dispatchSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote;

    private EmployeeEntity employeeEntity;

    private CustomerServiceModule customerServiceModule;
    private SalesManagementModule salesManagementModule;

    private boolean loggedIn = false;
    private Date currentDate;

    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, DispatchSessionBeanRemote dispatchSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.dispatchSessionBeanRemote = dispatchSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carPickupReturnSessionBeanRemote = carPickupReturnSessionBeanRemote;
    }

    public void runApp() {

        while (true) {
            if (!loggedIn) {
                System.out.println("Please log in to continue");

                try {
                    doLogin();
                    System.out.println("Login successful");
                } catch (InvalidLoginException e) {
                    System.out.println("Login failed, please try again");
                    System.out.println(e.getMessage());
                }
            }
            if (loggedIn) {
                setCurrentDate();

                this.customerServiceModule = new CustomerServiceModule(customerSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, carPickupReturnSessionBeanRemote, outletSessionBeanRemote, employeeEntity);
                this.salesManagementModule = new SalesManagementModule(rentalRateSessionBeanRemote, carSessionBeanRemote, dispatchSessionBeanRemote, reservationSessionBeanRemote, outletSessionBeanRemote, employeeSessionBeanRemote, employeeEntity, currentDate);

                mainMenu();
            }
        }

    }

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (loggedIn) {
            System.out.println("*****Select module to access*****");
            System.out.println("1 : Sales Management Module");
            System.out.println("2 : Customer Service Module");
            System.out.println("3 : Logout");
            System.out.println("4 : Allocate Cars To Current Day Reservations");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.OPERATIONSMANAGER)) {
                        salesManagementModule.menu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
                case 2:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE)) {
                        customerServiceModule.menu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
                case 3:
                    doLogout();
                    break;
                case 4: {
                    try {
                        allocateCarsToCurrentDayReservationsAndGenerateDispatch();
                    } catch (NoReservationsException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                break;

            }
        }
    }

    public void doLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*****Please key in your Username to begin*****");
        username = sc.nextLine();
        System.out.println("*****Please key in your password*****");
        password = sc.next();

        employeeEntity = employeeSessionBeanRemote.login(username, password);
        loggedIn = true;
    }

    public void doLogout() {
        Scanner sc = new Scanner(System.in);
        String option = "";

        System.out.println("*****Please confirm you would like to logout (Y to confirm)");
        option = sc.next();

        if (loggedIn == false) {
            System.out.println("You are not logged in");
        } else if (option.equals("Y") && loggedIn == true) {
            employeeEntity = null;
            loggedIn = false;
        }
    }

    private void setCurrentDate() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        System.out.println("Please specify the current date");
        System.out.println("1: Manual Entry");
        System.out.println("2: Automatically retrieve according to system clock");
        option = sc.nextInt();
        switch (option) {
            case 1:
                System.out.println("Please enter current year");
                int year = sc.nextInt();
                System.out.println("Please enter current month");
                int month = sc.nextInt();
                System.out.println("Please enter current day (numerical)");
                int day = sc.nextInt();
                currentDate = new Date(year, month, day);
                break;
            case 2:
                currentDate = new Date();
                break;
        }
    }

    public void allocateCarsToCurrentDayReservationsAndGenerateDispatch() throws NoReservationsException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please key in the date to generate reservations");
        System.out.println("Key in the year (YYYY)");
        int year = sc.nextInt() - 1900;
        System.out.println("Key in the month (1-12)");
        int month = sc.nextInt();
        System.out.println("Key in the day (1-31)");
        int day = sc.nextInt();

        Date date = new Date(year, month, day);
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservationsByDate(date);

        if (!reservations.isEmpty()) {
            for (ReservationEntity reservationEntity : reservations) {
                if (reservationEntity.getCar() != null && !reservationEntity.isCancelled()) {
                    try {
                        reservationSessionBeanRemote.autoAllocateCarToReservation(reservationEntity);
                    } catch (ReservationNoModelNoCategoryException | NullCurrentOutletException | NoCarsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } else {
            //if this happens, this might be ebcause retrieveReservationsByDate may not be working properly
            throw new NoReservationsException("No reservations found for the day");
        }
    }

}
