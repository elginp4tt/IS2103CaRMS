/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.CarCategoryEntity;
import entity.CarModelEntity;
import entity.CustomerEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarCategoryNotFoundException;
import exception.CarModelNotFoundException;
import exception.CustomerNotFoundException;
import exception.InvalidLoginException;
import exception.NoCarModelsException;
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
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
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    
    @WebMethod
    public long createCustomerEntity(@WebParam CustomerEntity customerEntity){
        return customerSessionBean.createCustomerEntity(customerEntity);
    }
    
    @WebMethod
    public PartnerEntity doLogin(@WebParam String username, @WebParam String password) throws InvalidLoginException {
        return partnerSessionBean.doLogin(username, password);
    }
    
    @WebMethod
    public ReservationEntity retrieveReservationEntityByReservationId(@WebParam Long reservationId) throws ReservationNotFoundException {
        return reservationSessionBean.retrieveReservationEntityByReservationId(reservationId);
    }
    
    @WebMethod
    public OutletEntity retrieveOutletEntityByName(@WebParam String outletName) throws OutletNotFoundException {
        return outletSessionBean.retrieveOutletEntityByName(outletName);
    }
    
    @WebMethod
    public List<CarCategoryEntity> retrieveCarCategoriesWithConditions(@WebParam Date startDate, @WebParam Date endDate, @WebParam OutletEntity incPickupOutlet, @WebParam OutletEntity incReturnOutlet){
        return reservationSessionBean.retrieveCarCategoriesWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet);
    }
    
    @WebMethod
    public List<RentalRateEntity> calculateTotalRentalRate(@WebParam CarCategoryEntity carCategory, @WebParam Date startDate, @WebParam Date endDate) throws NoRentalRatesFoundException{
        return reservationSessionBean.calculateTotalRentalRate(carCategory, startDate, endDate);
    }
    
    @WebMethod
    public CarCategoryEntity retrieveCarCategoryEntityByCarCategoryId(@WebParam long carCategoryId)throws CarCategoryNotFoundException{
        return carSessionBean.retrieveCarCategoryEntityByCarCategoryId(carCategoryId);
    }
    
    @WebMethod
    public List<CarModelEntity> retrieveCarModelsWithConditions(@WebParam Date startDate, @WebParam Date endDate, @WebParam OutletEntity incPickupOutlet, @WebParam OutletEntity incReturnOutlet, @WebParam CarCategoryEntity carCategory){
        return reservationSessionBean.retrieveCarModelsWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet, carCategory);
    }
    
    @WebMethod
    public CarModelEntity retrieveCarModelEntityByCarModelId(@WebParam long carModelId)throws CarModelNotFoundException{
        return carSessionBean.retrieveCarModelEntityByCarModelId(carModelId);
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
    
    @WebMethod
    public RentalRateEntity retrieveRentalRateEntityByRentalRateId(@WebParam long rentalRateId) throws RentalRateNotFoundException{
        return rentalRateSessionBean.retrieveRentalRateEntityByRentalRateId(rentalRateId);
    }
    
    @WebMethod
    public void updateReservationEntity(@WebParam ReservationEntity reservationEntity){
        reservationSessionBean.updateReservationEntity(reservationEntity);
    }
    
    @WebMethod
    public void updateRentalRateEntity(@WebParam RentalRateEntity rentalRateEntity){
        rentalRateSessionBean.updateRentalRateEntity(rentalRateEntity);
    }
    
    @WebMethod
    public void setRentalRateAsUsed(@WebParam long rentalRateId){
        rentalRateSessionBean.setRentalRateAsUsed(rentalRateId);
    }
    
    @WebMethod
    public long createReservationEntity(@WebParam boolean paid, @WebParam String creditCardNumber, @WebParam String cvv, @WebParam Date startDate, @WebParam Date endDate, @WebParam CustomerEntity customer, @WebParam OutletEntity pickupOutlet, @WebParam OutletEntity returnOutlet, @WebParam double price, @WebParam PartnerEntity partner, @WebParam CarCategoryEntity carCategory, @WebParam CarModelEntity carModel){
        return reservationSessionBean.createReservationEntity(paid, creditCardNumber, cvv, startDate, endDate, customer, pickupOutlet, returnOutlet, price, partner, carCategory, carModel);
    }
    
    @WebMethod
    public CarModelEntity retrieveCarModelByReservationId(long reservationId) throws NoCarModelsException{
        return reservationSessionBean.retrieveCarModelByReservationId(reservationId);
    };

    @WebMethod
    public CarCategoryEntity retrieveCarCategoryByReservationId(long reservationId) throws NoCarsException{
        return reservationSessionBean.retrieveCarCategoryByReservationId(reservationId);
    };
}

