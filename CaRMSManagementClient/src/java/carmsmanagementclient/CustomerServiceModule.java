/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.ReservationEntity;
import exception.CustomerNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Elgin Patt
 */
public class CustomerServiceModule {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private EmployeeEntity employeeEntity;

    public CustomerServiceModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeEntity employeeEntity) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeEntity = employeeEntity;
    }
    
    public void recordPickupCar(){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Please key in the email of the customer");
        String email = sc.next();
        try{
            CustomerEntity customerEntity = customerSessionBeanRemote.retrieveCustomerEntityByEmail(email);
            
            ArrayList<ReservationEntity> reservations = customerEntity.getReservations();
        } catch (CustomerNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    
    
    
}
