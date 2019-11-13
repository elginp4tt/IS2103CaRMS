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
import javax.ejb.EJB;

/**
 *
 * @author Elgin Patt
 */
public class Main {

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote);
        mainApp.run();
    }
}
