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
import exception.NoCarsException;
import exception.NoRentalRatesFoundException;
import exception.OutletNotFoundException;
import exception.ReservationNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        HashMap<CarCategoryEntity, Integer> hashmap = reservationSessionBean.retrieveCarCategoriesWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet);
        List<CarCategoryEntity> cars = new ArrayList<CarCategoryEntity>();
        for (CarCategoryEntity carCategoryEntity : hashmap.keySet()){
            cars.add(carCategoryEntity);
        }
        return cars;
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
        HashMap<CarModelEntity, Integer> hashmap = reservationSessionBean.retrieveCarModelsWithConditions(startDate, endDate, incPickupOutlet, incReturnOutlet, carCategory);
        List<CarModelEntity> cars = new ArrayList<CarModelEntity>();
        for (CarModelEntity carModelEntity : hashmap.keySet()){
            cars.add(carModelEntity);
        }
        return cars;
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
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

