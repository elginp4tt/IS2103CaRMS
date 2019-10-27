/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(long employeeId) throws EmployeeNotFoundException{
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        if (employeeEntity != null){
        return employeeEntity;
        } else {
        throw new EmployeeNotFoundException("Employee not found");
        }
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeEntityByUsername(String username) throws EmployeeNotFoundException{
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (EmployeeEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new EmployeeNotFoundException("Employee not found");
        }
    }
    
    public EmployeeEntity login(String username, String password) throws InvalidLoginException{
        try{
        EmployeeEntity employeeEntity = retrieveEmployeeEntityByUsername(username);
            if (employeeEntity.getPassword().equals(password)){
                return employeeEntity;
            } else {
                throw new InvalidLoginException("Login credentials are invalid");
            }
        } catch (EmployeeNotFoundException e){
            throw new InvalidLoginException("Login credentials are invalid");
        }
    }
    
}
