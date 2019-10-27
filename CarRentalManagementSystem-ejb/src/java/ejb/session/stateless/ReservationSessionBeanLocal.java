/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import exception.ReservationNotFoundException;

public interface ReservationSessionBeanLocal {

    public ReservationEntity retrieveReservationEntityByReservationId(Long reservationId) throws ReservationNotFoundException;

    public void updateReservationEntity(ReservationEntity reservationEntity);
    
}
