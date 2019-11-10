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
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.List;
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
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    
    
    @WebMethod
    public PartnerEntity doLogin(@WebParam String username, @WebParam String password) throws InvalidLoginException {
        return partnerSessionBean.doLogin(username, password);
    }
    
    @WebMethod
    public ReservationEntity retrieveReservationEntityByReservationId(@WebParam Long reservationId) throws ReservationNotFoundException {
        return reservationSessionBean.retrieveReservationEntityByReservationId(reservationId);
    }
    
    @WebMethod
    public void cancelReservation(@WebParam Date currentDate){
        reservationSessionBean.cancelReservation(currentDate);
    }
    
    @WebMethod
    public void searchForAvailableCars(@WebParam PartnerEntity partnerEntity, @WebParam CustomerEntity customerEntity) throws NoCarsException, NoRentalRatesFoundException{
        reservationSessionBean.searchForAvailableCars(partnerEntity, customerEntity);
    }
    
    @WebMethod
    public List<ReservationEntity> retrieveReservationsByPartnerId(@WebParam Long partnerId){
        return reservationSessionBean.retrieveReservationsByPartnerId(partnerId);
    }
    
    @WebMethod
    public CustomerEntity retrieveCustomerEntityByCustomerId(@WebParam Long customerId) throws CustomerNotFoundException{
        return customerSessionBean.retrieveCustomerEntityByCustomerId(customerId);
    }
    
    @WebMethod
    public CustomerEntity retrieveCustomerEntityByEmail(@WebParam String email) throws CustomerNotFoundException{
        return customerSessionBean.retrieveCustomerEntityByEmail(email);
    }
}
