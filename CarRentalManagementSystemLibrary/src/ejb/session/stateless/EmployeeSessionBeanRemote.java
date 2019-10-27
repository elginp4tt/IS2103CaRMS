/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginException;

public interface EmployeeSessionBeanRemote {
    public EmployeeEntity retrieveEmployeeEntityByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(long employeeId) throws EmployeeNotFoundException;
    
    public EmployeeEntity login(String username, String password) throws InvalidLoginException;
    
}
