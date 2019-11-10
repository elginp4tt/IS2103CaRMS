/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategoryEntity;
import entity.CarModelEntity;
import entity.CustomerEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import exception.CarModelNotFoundException;
import exception.InvalidLoginException;
import exception.InvalidReservationException;
import exception.PartnerNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public long createPartnerEntity(PartnerEntity partnerEntity){
        em.persist(partnerEntity);
        em.flush();
        
        return partnerEntity.getPartnerId();    
    }
    
    @Override
    public PartnerEntity doLogin(String username, String password) throws InvalidLoginException {
        try {
            PartnerEntity partner = retrievePartnerEntityByUsername(username);
            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginException("Email or Password provided is incorrectly");
            }

        } catch (PartnerNotFoundException e) {
            throw new InvalidLoginException("Email or Password provided is incorrectly");
        }
    }

    @Override
    public PartnerEntity retrievePartnerEntityByUsername(String username) throws PartnerNotFoundException{
        Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            return (PartnerEntity) query.getSingleResult();
        } catch (Exception e) {
            throw new PartnerNotFoundException("Customer not found");
        }
    }
    
    
    public void reserveCar(String make, String model, CustomerEntity customerEntity, ReservationEntity reservationEntity) throws InvalidReservationException{
        try {
            Scanner sc = new Scanner(System.in);
            
            CarModelEntity carModelEntity = carSessionBean.retrieveCarModelEntityByMakeAndModel(make, model);
            System.out.println("Key in customer credit card number");
            String creditCardNumber = sc.next();
            System.out.println("Key in customer credit card cvv");
            String creditCardCvv = sc.next();
            
            customerEntity.setCreditCardNumber(creditCardNumber);
            customerEntity.setCreditCardCvv(creditCardCvv);
            customerSessionBean.updateCustomerEntity(customerEntity);
            
            carModelEntity.getReservations().add(reservationEntity);
            
        } catch (CarModelNotFoundException e) {
            throw new InvalidReservationException("Car Model is not found, reservation is invalid");
        }
    }
    
    @Override
    public void updatePartnerEntity(PartnerEntity partnerEntity){
        em.merge(partnerEntity);
    }
}
