/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exception.CustomerNotFoundException;
import exception.InvalidLoginException;
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
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createCustomerEntity(CustomerEntity newCustomerEntity) {
        em.persist(newCustomerEntity);
        em.flush();

        return newCustomerEntity.getCustomerId();
    }

    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (Exception e) {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        em.merge(customerEntity);
    }

    @Override
    public void deleteCustomerEntity(long customerId) {
        try {
            CustomerEntity customerEntity = retrieveCustomerEntityByCustomerId(customerId);
            em.remove(customerEntity);
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CustomerEntity doLogin(String email, String password) throws InvalidLoginException {
        try {
            CustomerEntity cust = retrieveCustomerEntityByEmail(email);
            if (cust.getPassword().equals(password)) {
                return cust;
            } else {
                throw new InvalidLoginException("Email or Password provided is incorrectly");
            }

        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginException("Email or Password provided is incorrectly");
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
