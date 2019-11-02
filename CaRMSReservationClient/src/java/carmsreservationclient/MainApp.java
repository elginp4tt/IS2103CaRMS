/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.SearchSessionBeanRemote;
import entity.CustomerEntity;

/**
 *
 * @author Elgin Patt
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBean;
    private SearchSessionBeanRemote searchSessionBeanRemote;

    private CustomerEntity currentCustomerEntity;

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBean, SearchSessionBeanRemote searchSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBean = reservationSessionBean;
        this.searchSessionBeanRemote = searchSessionBeanRemote;
    }

    public void run() {

    }

}
