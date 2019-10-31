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
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarCategoryNotFoundException;
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
                    doCreateRentalRate();
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
    
    public void doCreateRentalRate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Create a new rental rate*****");
        System.out.println("*****Enter the name for the new rental rate*****");
        String name = sc.next();
        System.out.println("*****Enter the name of the car category for the new rental rate*****");
        String carCategory = sc.next();
        System.out.println("*****Enter the daily rate for the rental rate (in dollars)*****");
        double dailyRate = sc.nextDouble();
        System.out.println("*****Enter the start date for the new rental rate*****");
        System.out.println("*****Enter 1 to use today's date, else use DD MM YY*****");
        int dateOption = sc.nextInt();
        Date date = new Date();
        if (dateOption != 1){
            int day = dateOption/10000;
            int month = dateOption/100;
            month = month%100;
            int year = dateOption%100;
            date = new Date(year, month, day);
        }
        System.out.println("*****Enter the number of days this rental rate is valid for*****");
        System.out.println("*****i.e Enter 1 if the rate is only valid for the start day*****");
        int validityPeriod = sc.nextInt();
        try {
            rentalRateSessionBeanRemote.createRentalRateEntity(new RentalRateEntity(name, dailyRate, date, validityPeriod, carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(carCategory)));
        } catch (CarCategoryNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        System.out.println("*****New rental rate has been successfully created");
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
    
    public void allocateCarsToCurrentDayReservationsAndGenerateDispatch() throws NoReservationsException{
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
        
}
