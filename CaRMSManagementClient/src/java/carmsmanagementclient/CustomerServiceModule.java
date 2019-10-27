/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarEntity;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.ReservationEntity;
import exception.CustomerNotFoundException;
import exception.ReservationNotFoundException;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author Elgin Patt
 */
public class CustomerServiceModule {
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private EmployeeEntity employeeEntity;
    private OutletEntity outletEntity;
    private final CustomerSessionBeanRemote customerSessionBeanRemote;

    public CustomerServiceModule(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, EmployeeEntity employeeEntity) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.employeeEntity = employeeEntity;
        this.outletEntity = employeeEntity.getOutlet();
    }
    
    public void recordPickupCar(){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Please key in the ID of the reservation");
        long reservationId = sc.nextLong();
        int option = 0;
        
        try{
            System.out.println("Please key in the email of the customer");
            String email = sc.next();
            CustomerEntity customerEntity = customerSessionBeanRemote.retrieveCustomerEntityByEmail(email);
            
            ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationEntityByReservationId(reservationId);
            if (!reservationEntity.isPaid()){
                System.out.println("Payment has not been made");
                System.out.println("Please recieve payment before allowing for car collection");
                System.out.println("Once payment has been made, please enter 1 to continue");
                System.out.println("To cancel this operation, press 2");
                    switch(option){
                        case 1: 
                            reservationEntity.setPaid(true);
                            reservationSessionBeanRemote.updateReservationEntity(reservationEntity);
                            break;
                            
                        case 2:
                            System.out.println("Pickup operation has been terminated");
                            break;
                    }
                }
            
            if (option == 1){
                CarEntity carEntity = reservationEntity.getCar();
                carEntity.setLocation(customerEntity.getUsername());
                carEntity.setStatus(CarStatusEnum.ONRENTAL);
                carSessionBeanRemote.updateCarEntity(carEntity);
                System.out.println("Pickup operation has been completed, car location and status have been updated");
                }
            
            } catch (ReservationNotFoundException | CustomerNotFoundException e){
                System.out.println(e.getMessage());
        }
    }
}
