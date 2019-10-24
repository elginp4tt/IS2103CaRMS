/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exception.CustomerNotFoundException;
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
    public long createCustomerEntity(CustomerEntity newCustomerEntity){
        em.persist(newCustomerEntity);
        em.flush();
        
        return newCustomerEntity.getCustomerId();
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId){
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
    
        return customerEntity;
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException{
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        try{
            return (CustomerEntity)query.getSingleResult();
        } catch (Exception e){
            throw new CustomerNotFoundException("Customer not found");
        }
    }
    
    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity){
        em.merge(customerEntity);
    }
    
    @Override
    public void deleteCustomerEntity(long customerId){
        CustomerEntity customerEntity = retrieveCustomerEntityByCustomerId(customerId);
        em.remove(customerEntity);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
