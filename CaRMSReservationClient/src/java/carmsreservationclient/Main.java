/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Elgin Patt
 */
public class Main {
    
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    public static void main(String[] args) {
    
    
}
}
