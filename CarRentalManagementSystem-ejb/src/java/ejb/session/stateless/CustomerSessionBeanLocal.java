/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exception.CustomerNotFoundException;


public interface CustomerSessionBeanLocal {

    public long createCustomerEntity(CustomerEntity newCustomerEntity);

    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId);

    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException;

    public void updateCustomerEntity(CustomerEntity customerEntity);

    public void deleteCustomerEntity(long customerId);
    
}
