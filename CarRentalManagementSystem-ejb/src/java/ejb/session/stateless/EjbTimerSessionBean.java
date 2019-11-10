/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import exception.NoCarsException;
import exception.NoReservationsException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Elgin Patt
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @Schedule(dayOfMonth = "*", hour = "0", info = "allocateCarsToCurrentDayReservationsAndGenerateDispatch")
    public void allocateCarsToCurrentDayReservationsAndGenerateDispatch() throws NoReservationsException{
        Date currentDate = new Date();
        List<ReservationEntity> reservations = reservationSessionBean.retrieveReservationsByDate(currentDate);
        
        if (!reservations.isEmpty()){
            for (ReservationEntity reservationEntity : reservations){
                if (reservationEntity.getCar() != null && !reservationEntity.isCancelled()){
                    try {
                        reservationSessionBean.autoAllocateCarToReservation(reservationEntity);
                    } catch (ReservationNoModelNoCategoryException | NullCurrentOutletException | NoCarsException e){
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
