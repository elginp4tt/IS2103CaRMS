/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.DispatchSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.ReservationEntity;
import exception.NoCarsException;
import exception.NoReservationsException;
import exception.NullCurrentOutletException;
import exception.ReservationNoModelNoCategoryException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Elgin Patt
 */
public class SalesManagementModule {
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private DispatchSessionBeanRemote dispatchSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private EmployeeEntity employeeEntity;
    private OutletEntity outletEntity;
    private Date currentDate;

    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, DispatchSessionBeanRemote dispatchSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, EmployeeEntity employeeEntity, Date currentDate) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.dispatchSessionBeanRemote = dispatchSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.employeeEntity = employeeEntity;
        this.outletEntity = employeeEntity.getOutlet();
        this.currentDate = currentDate;
    }
    
    public void menu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (option != 5){
        System.out.println("*****Select subsystem to access*****");
        System.out.println("1 : Rental Rate Subsystem");
        System.out.println("2 : Car Model Subsystem");
        System.out.println("3 : Car Subsystem");
        System.out.println("4 : Transit Driver Subsystem");
        System.out.println("5 : Exit");
        
        option = sc.nextInt();
        
        switch (option){
            case 1:
                rentalRateMenu();
                break;
            case 2:
                carModelMenu();
                break;
            case 3:
                carMenu();
                break;
            case 4:
                transitDriverMenu();
                break;
            }
        }
    }
    
    public void rentalRateMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (option != 6){
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create Rental Rate");
            System.out.println("2 : View All Rental Rates");
            System.out.println("3 : View Rental Rate Details");
            System.out.println("4 : Update Rental Rate");
            System.out.println("5 : Delete Rental Rate");
            System.out.println("6 : Exit");
            
            option = sc.nextInt();
            
            switch(option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    }
    
    public void allocateCarsToCurrentDayReservations() throws NoReservationsException{
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservationsByDate(currentDate);
        
        if (!reservations.isEmpty()){
            for (ReservationEntity reservationEntity : reservations){
                if (reservationEntity.getCar() != null){
                    try {
                    reservationSessionBeanRemote.autoAllocateCarToReservation(reservationEntity, outletEntity);
                    } catch (ReservationNoModelNoCategoryException | NullCurrentOutletException | NoCarsException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        } else {
            throw new NoReservationsException("No reservations found for the day");
        }
    }
    
    public void carModelMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (option != 5){
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create New Model");
            System.out.println("2 : View All Models");
            System.out.println("3 : Update Model");
            System.out.println("4 : Delete Model");
            System.out.println("5 : Exit");
            
            option = sc.nextInt();
            
            switch(option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
    }
    
    public void carMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (option != 6){
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create New Car");
            System.out.println("2 : View All Cars");
            System.out.println("3 : View Car Details");
            System.out.println("4 : Update Car");
            System.out.println("5 : Delete Car");
            System.out.println("6 : Exit");
            
            option = sc.nextInt();
            
            switch(option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    }
    
    public void transitDriverMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (option != 4){
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("2 : Assign Transit Driver");
            System.out.println("3 : Update Transit as Completed");
            System.out.println("4 : Exit");
            
            option = sc.nextInt();
            
            switch(option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }
    
}
