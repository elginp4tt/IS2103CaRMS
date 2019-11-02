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
import entity.CarCategoryEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.RentalRateEntity;
import entity.ReservationEntity;
import exception.CarCategoryNotFoundException;
import exception.NoCarsException;
import exception.NoReservationsException;
import exception.NullCurrentOutletException;
import exception.RentalRateNotFoundException;
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
                    doViewAllRentalRates();
                    break;
                case 3:
                    doViewRentalRateDetails();
                    break;
                case 4:
                    doUpdateRentalRate();
                    break;
                case 5:
                    doDeleteRentalRate();
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
        System.out.println("*****Enter 1 to use today's date, else use DDMMYYYY*****");
        int dateOption = sc.nextInt();
        
        Date date = new Date();
        if (dateOption != 1){
            int day = dateOption/1000000;
            int month = dateOption/10000;
            month = month%100;
            int year = dateOption%10000;
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
    
    public void doViewAllRentalRates() {
        System.out.println("*****View all rental rates*****");
        
        List<RentalRateEntity> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRatesByCarCategoryThenDate();
        for (RentalRateEntity rentalRate : rentalRates){
            System.out.println(rentalRate);
        }
    }
    
    public void doViewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****View Rental Rate Details*****");
        System.out.println("*****Key in the ID of the rental rate you want to view*****");
        long rentalRateId = sc.nextLong();
        
        try {
            RentalRateEntity rentalRateEntity = rentalRateSessionBeanRemote.retrieveRentalRateEntityByRentalRateId(rentalRateId);
            System.out.println(rentalRateEntity);
        } catch (RentalRateNotFoundException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public void doUpdateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Update Rental Rate*****");
        System.out.println("*****Key in the ID of the rental rate you want to update*****");
        long rentalRateId = sc.nextLong();
        int option = 0;
        
        try {
            RentalRateEntity rentalRateEntity = rentalRateSessionBeanRemote.retrieveRentalRateEntityByRentalRateId(rentalRateId);
            while (option != 6){
                System.out.println("Select field of rental rate to edit");
                System.out.println("1: Name");
                System.out.println("2: Car Category");
                System.out.println("3: Daily Rate");
                System.out.println("4: Start Date");
                System.out.println("5: Validity Period");
                System.out.println("6: Exit and Update");
                option = sc.nextInt();
                
                switch (option){
                    case 1:
                        System.out.println("Please key in the new name (single word)");
                        String name = sc.next();
                        rentalRateEntity.setName(name);
                        break;
                    case 2:
                        System.out.println("Please key in the new car category");
                        String carCategory = sc.nextLine();
                        try {
                            CarCategoryEntity carCategoryEntity = carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(carCategory);
                            rentalRateEntity.setCarCategory(carCategoryEntity);
                        } catch (CarCategoryNotFoundException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Please key in the new daily rate");
                        double dailyRate = sc.nextDouble();
                        rentalRateEntity.setDailyRate(dailyRate);
                        break;
                    case 4:
                        System.out.println("Please key in the new start date in DDMMYYYY");
                        int numericalDate = sc.nextInt();
                        int day = numericalDate/1000000;
                        int month = numericalDate/10000;
                        month = month%100;
                        int year = numericalDate%10000;
                        Date date = new Date(year, month, day);
                        rentalRateEntity.setValidityPeriodStart(date);
                        break;
                    case 5:
                        System.out.println("Please key in the new validity period");
                        System.out.println("The period should be the number of valid days, including the starting day");
                        int validityPeriod = sc.nextInt();
                        rentalRateEntity.setValidityPeriod(validityPeriod);
                        break;
                    case 6:
                        rentalRateSessionBeanRemote.updateRentalRateEntity(rentalRateEntity);
                        break;
                }
            }
        } catch (RentalRateNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Delete Rental Rate*****");
        System.out.println("*****Key in the ID of the rental rate you want to update*****");
        long rentalRateId = sc.nextLong();
        
        try {
            RentalRateEntity rentalRateEntity = rentalRateSessionBeanRemote.retrieveRentalRateEntityByRentalRateId(rentalRateId);
            if (rentalRateEntity.isUsed()){
                rentalRateEntity.setDisabled(true);
                rentalRateSessionBeanRemote.updateRentalRateEntity(rentalRateEntity);
            } else {
                rentalRateSessionBeanRemote.deleteRentalRateEntity(rentalRateEntity);
            }
        } catch (RentalRateNotFoundException e){
            System.out.println(e.getMessage());
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
