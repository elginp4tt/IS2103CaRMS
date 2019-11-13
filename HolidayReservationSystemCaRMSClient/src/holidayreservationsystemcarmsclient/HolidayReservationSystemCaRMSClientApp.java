/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemcarmsclient;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.CarCategoryEntity;
import ws.client.CarModelEntity;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.NoCarsException;
import ws.client.NoCarsException_Exception;
import ws.client.NoRentalRatesFoundException;
import ws.client.NoRentalRatesFoundException_Exception;
import ws.client.OutletEntity;
import ws.client.PartnerEntity;
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
                System.out.println("Please enter customer email");
                String email = sc.next();
                customerEntity = retrieveCustomerEntityByEmail(email);
            } catch (InvalidLoginException_Exception | CustomerNotFoundException_Exception e) {
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

        switch (option) {
            case 1:
            {
            }
                break;

            case 2:
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                DatatypeFactory datatypeFactory;
            try {
                datatypeFactory = DatatypeFactory.newInstance();
                XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                cancelReservation(now);
            } catch (DatatypeConfigurationException ex) {
                System.out.println(ex.getMessage());
            }
                break;
            case 3:
                System.out.println("Please key in the reservation id to get");
                long reservationId = sc.nextLong();
            {
                try {
                    retrieveReservationEntityByReservationId(reservationId);
                } catch (ReservationNotFoundException_Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
                break;

            case 4:
                retrieveReservationsByPartnerId(partnerEntity.getPartnerId());
                break;
            
            case 5:
                loggedIn = false;
                System.out.println("Logged out of the system");
                break;
        }
    }

    private static PartnerEntity doLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.doLogin(arg0, arg1);
    }

    private static void cancelReservation(javax.xml.datatype.XMLGregorianCalendar arg0) {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.cancelReservation(arg0);
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

    private static void searchForAvailableCars(ws.client.PartnerEntity arg0, ws.client.CustomerEntity arg1) throws NoCarsException_Exception, NoRentalRatesFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        port.searchForAvailableCars(arg0, arg1);
    }

    private static CustomerEntity retrieveCustomerEntityByEmail(java.lang.String arg0) throws CustomerNotFoundException_Exception {
        ws.client.CarRentalManagementWebService_Service service = new ws.client.CarRentalManagementWebService_Service();
        ws.client.CarRentalManagementWebService port = service.getCarRentalManagementWebServicePort();
        return port.retrieveCustomerEntityByEmail(arg0);
    }
    
}
