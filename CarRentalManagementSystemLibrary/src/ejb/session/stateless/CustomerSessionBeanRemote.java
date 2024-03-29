/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exception.CustomerNotFoundException;
import exception.InvalidLoginException;


public interface CustomerSessionBeanRemote {
    public long createCustomerEntity(CustomerEntity newCustomerEntity);

    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException;
    
    public void updateCustomerEntity(CustomerEntity customerEntity);

    public void deleteCustomerEntity(long customerId);

    public CustomerEntity doLogin(String email, String password) throws InvalidLoginException;
    
    public CustomerEntity retrieveCustomerEntityByUsername(String username) throws CustomerNotFoundException;
}
