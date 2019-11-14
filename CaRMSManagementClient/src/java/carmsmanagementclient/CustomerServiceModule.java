/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarPickupReturnSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarEntity;
import entity.CarPickupEntity;
import entity.CarReturnEntity;
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

    private CarSessionBeanRemote carSessionBeanRemote;
    private EmployeeEntity employeeEntity;
    private OutletEntity outletEntity;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;

    public CustomerServiceModule(CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, CarPickupReturnSessionBeanRemote carPickupReturnSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, EmployeeEntity employeeEntity) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carPickupReturnSessionBeanRemote = carPickupReturnSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeEntity = employeeEntity;
        this.outletEntity = employeeEntity.getOutlet();
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 3) {
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Customer Pickup Car");
            System.out.println("2 : Customer Return Car");
            System.out.println("3 : Exit");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    doPickupCar();
                    break;
                case 2:
                    doReturnCar();
                    break;
            }
        }
    }

    public void doPickupCar() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        try {
            System.out.println("Please key in the email of the customer");
            String email = sc.next();
            CustomerEntity customerEntity = customerSessionBeanRemote.retrieveCustomerEntityByEmail(email);

            System.out.println("Please key in the ID of the reservation");
            long reservationId = sc.nextLong();
            ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationEntityByReservationId(reservationId);

            if (!reservationEntity.isPaid()) {
                System.out.println("Payment has not been made");
                System.out.println("Please recieve payment before allowing for car collection");
                System.out.println("Once payment has been made, please enter 1 to continue");
                System.out.println("To cancel this operation, press 2");
                option = sc.nextInt();
                switch (option) {
                    case 1:
                        reservationEntity.setPaid(true);
                        reservationSessionBeanRemote.updateReservationEntity(reservationEntity);
                        break;

                    case 2:
                        System.out.println("Pickup operation has been terminated");
                        break;
                }
            }

            if (option == 1) {
                CarEntity carEntity = reservationEntity.getCar();
                carEntity.setLocation(customerEntity.getUsername());
                carEntity.setStatus(CarStatusEnum.ONRENTAL);
                carEntity.setCurrentOutlet(null);
                carSessionBeanRemote.updateCarEntity(carEntity);

                if (outletEntity.getCars().contains(carEntity)) {
                    outletEntity.getCars().remove(carEntity);
                }
                outletSessionBeanRemote.updateOutletEntity(outletEntity);

                CarPickupEntity carPickupEntity = new CarPickupEntity(reservationEntity.getStartDate(), outletEntity);
                carPickupReturnSessionBeanRemote.createCarPickupEntity(carPickupEntity);

                reservationEntity.setCarPickup(carPickupEntity);
                reservationSessionBeanRemote.updateReservationEntity(reservationEntity);

                System.out.println("Pickup operation has been completed, car location and status have been updated");
                System.out.println("CarPickup entry with id " + carPickupEntity.getCarPickupId() + " has been created");

            }

        } catch (ReservationNotFoundException | CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doReturnCar() {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Please key in the ID of the reservation");
            long reservationId = sc.nextLong();
            ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationEntityByReservationId(reservationId);

            CarEntity carEntity = reservationEntity.getCar();
            carEntity.setLocation(outletEntity.getName());
            carEntity.setStatus(CarStatusEnum.AVAILABLE);
            carEntity.setCurrentReservation(null);
            carSessionBeanRemote.updateCarEntity(carEntity);

            if (!outletEntity.getCars().contains(carEntity)) {
                outletEntity.getCars().add(carEntity);
                outletSessionBeanRemote.updateOutletEntity(outletEntity);
            }

            CarReturnEntity carReturnEntity = new CarReturnEntity(reservationEntity.getEndDate(), outletEntity);
            carPickupReturnSessionBeanRemote.createCarReturnEntity(carReturnEntity);

            reservationEntity.setCarReturn(carReturnEntity);
            reservationSessionBeanRemote.updateReservationEntity(reservationEntity);

            System.out.println("Return operation has been completed, car location and status have been updated");
            System.out.println("CarReturn entry with id " + carReturnEntity.getCarReturnId() + " has been created");

        } catch (ReservationNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
