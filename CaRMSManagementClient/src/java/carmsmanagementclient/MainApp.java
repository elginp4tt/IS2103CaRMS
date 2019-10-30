/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarPickupReturnSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DispatchSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.EmployeeEntity;
import exception.InvalidLoginException;
import java.util.Scanner;


public class MainApp {
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private DispatchSessionBeanRemote dispatchSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote;
    
    private EmployeeEntity employeeEntity;
    
    private CustomerServiceModule customerServiceModule;
    private SalesManagementModule salesManagementModule;
    
    

    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, DispatchSessionBeanRemote dispatchSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.dispatchSessionBeanRemote = dispatchSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carPickupReturnSessionBeanRemote = carPickupReturnSessionBeanRemote; 
    }

    public void runApp(){
        Scanner sc = new Scanner(System.in);
        int option = 0;
        
        while (true) {
            System.out.println("Please log in to continue");
            
        try{
            doLogin();
            System.out.println("Login successful");
            }
        catch (InvalidLoginException e) {
            System.out.println("Login failed, please try again");
            System.out.println(e.getMessage());
            }
        
        this.customerServiceModule = new CustomerServiceModule(customerSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, carPickupReturnSessionBeanRemote, outletSessionBeanRemote, employeeEntity);
        this.salesManagementModule = new SalesManagementModule(rentalRateSessionBeanRemote, carSessionBeanRemote, dispatchSessionBeanRemote, employeeEntity);
        
        mainMenu();
        
        }
        
    }
    
    public void doLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*****Please key in your Username to begin*****");
        username = sc.next();
        System.out.println("*****Please key in your password*****");
        password = sc.next();
        
        employeeEntity = employeeSessionBeanRemote.login(username, password);
    }
    
    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;      
        
        while(option != 3){
            System.out.println("*****Select module to access*****");
            System.out.println("1 : Sales Management Module");
            System.out.println("2 : Customer Service Module");
            System.out.println("3 : Exit");
            option = sc.nextInt();
            
            switch (option){
                case 1:
                    salesManagementModule.menu();
                    break;
                case 2:
                    customerServiceModule.menu();
                    break;
            }
        }
          
    }
    
}
