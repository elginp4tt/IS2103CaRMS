/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.CustomerEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import exception.CustomerNotFoundException;
import exception.InvalidLoginException;
import exception.ReservationNotFoundException;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Elgin Patt
 */
@WebService(serviceName = "CarRentalManagementWebService")
@Stateless()
public class CarRentalManagementWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    
    

    
    @WebMethod
    public PartnerEntity doLogin(@WebParam String username, @WebParam String password) throws InvalidLoginException {
        return partnerSessionBean.doLogin(username, password);
    }
    
    @WebMethod
    public ReservationEntity retrieveReservationEntityByReservationId(@WebParam Long reservationId) throws ReservationNotFoundException {
        return reservationSessionBean.retrieveReservationEntityByReservationId(reservationId);
    }
    
}
