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
import javax.ejb.EJB;

/**
 *
 * @author Elgin Patt
 */
public class Main {

    @EJB
    private static CarPickupReturnSessionBeanRemote carPickupReturnSessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    @EJB
    private static DispatchSessionBeanRemote dispatchSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(rentalRateSessionBean, partnerSessionBean, outletSessionBean, employeeSessionBean, dispatchSessionBean, carSessionBean, customerSessionBean, reservationSessionBean, carPickupReturnSessionBean);
        mainApp.runApp();
    }
    
}
