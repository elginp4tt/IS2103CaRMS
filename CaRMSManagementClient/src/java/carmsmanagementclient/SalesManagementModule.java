/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.DispatchSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarCategoryEntity;
import entity.CarEntity;
import entity.CarModelEntity;
import entity.DispatchEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.RentalRateEntity;
import exception.CarCategoryNotFoundException;
import exception.CarModelIsDisabledException;
import exception.CarModelNotFoundException;
import exception.CarNotFoundException;
import exception.DispatchNotFoundException;
import exception.EmployeeNotFoundException;
import exception.OutletNotFoundException;
import exception.RentalRateNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author Elgin Patt
 */
public class SalesManagementModule {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private DispatchSessionBeanRemote dispatchSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private EmployeeEntity employeeEntity;
    private OutletEntity outletEntity;
    private Date currentDate;

    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, DispatchSessionBeanRemote dispatchSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, EmployeeEntity employeeEntity, Date currentDate) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.dispatchSessionBeanRemote = dispatchSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.employeeEntity = employeeEntity;
        this.outletEntity = employeeEntity.getOutlet();
        this.currentDate = currentDate;
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 5) {
            System.out.println("*****Select subsystem to access*****");
            System.out.println("1 : Rental Rate Subsystem");
            System.out.println("2 : Car Model Subsystem");
            System.out.println("3 : Car Subsystem");
            System.out.println("4 : Transit Driver Subsystem");
            System.out.println("5 : Exit");

            option = sc.nextInt();

            switch (option) {
                case 1:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                        rentalRateMenu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
                case 2:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.OPERATIONSMANAGER)) {
                        carModelMenu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
                case 3:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.OPERATIONSMANAGER)) {
                        carMenu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
                case 4:
                    if (employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employeeEntity.getAccessRight().equals(EmployeeAccessRightEnum.OPERATIONSMANAGER)) {
                        transitDriverMenu();
                    } else {
                        System.out.println("You do not have sufficient privileges");
                    }
                    break;
            }
        }
    }

    public void rentalRateMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 6) {
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create Rental Rate");
            System.out.println("2 : View All Rental Rates");
            System.out.println("3 : View Rental Rate Details");
            System.out.println("4 : Update Rental Rate");
            System.out.println("5 : Delete Rental Rate");
            System.out.println("6 : Exit");

            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
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

    public void doCreateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Create a new rental rate*****");
        System.out.println("*****Enter the name for the new rental rate*****");
        String name = sc.nextLine();

        System.out.println("*****Enter the name of the car category for the new rental rate*****");
        String carCategory = sc.nextLine();

        System.out.println("*****Enter the daily rate for the rental rate (in dollars)*****");
        double dailyRate = sc.nextDouble();

        System.out.println("*****Enter the start date for the new rental rate*****");
        System.out.println("*****Enter 1 to set an always valid rental rate*****");
        System.out.println("*****Enter 2 to set rental rate start date*****");
        int validity = sc.nextInt();
        Date startDate = new Date();
        Date endDate = new Date();

        if (validity == 1) {
            startDate = null;
            endDate = null;

        } else if (validity == 2) {
            System.out.println("*****Enter 1 to use today's date, else key in date in the format DDMMYYYY*****");
            int dateDay = sc.nextInt();

            if (dateDay != 1) {
                System.out.println("*****Enter rental rate starting hour (0-23)*****");
                int hour = sc.nextInt();
                System.out.println("*****Enter rental rate starting minute (0-59)*****");
                int minute = sc.nextInt();

                int day = dateDay / 1000000;
                int month = dateDay / 10000;
                month = month % 100;
                int year = (dateDay % 10000) - 1900;
                startDate = new Date(year, month, day, hour, minute);
            }

            System.out.println("*****Enter 1 to use today's date, else key in date in the format DDMMYYYY*****");
            dateDay = sc.nextInt();

            if (dateDay != 1) {
                System.out.println("*****Enter rental rate starting hour (0-23)*****");
                int hour = sc.nextInt();
                System.out.println("*****Enter rental rate starting minute (0-59)*****");
                int minute = sc.nextInt();

                int day = dateDay / 1000000;
                int month = dateDay / 10000;
                month = month % 100;
                int year = (dateDay % 10000) - 1900;
                endDate = new Date(year, month, day, hour, minute);
            }
        }

        try {
            rentalRateSessionBeanRemote.createRentalRateEntity(new RentalRateEntity(name, carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(carCategory), dailyRate, startDate, endDate));
        } catch (CarCategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("*****New rental rate has been successfully created");
    }

    public void doViewAllRentalRates() {
        System.out.println("*****View all rental rates*****");

        List<RentalRateEntity> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRatesByCarCategoryThenDate();
        for (RentalRateEntity rentalRate : rentalRates) {
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
        } catch (RentalRateNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void doUpdateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Update Rental Rate*****");
        System.out.println("*****Key in the ID of the rental rate you want to update*****");
        long rentalRateId = sc.nextLong();
        int option = 0;
        CarCategoryEntity carCategoryEntity = null;

        try {
            RentalRateEntity rentalRateEntity = rentalRateSessionBeanRemote.retrieveRentalRateEntityByRentalRateId(rentalRateId);
            while (option != 6) {
                System.out.println("Select field of rental rate to edit");
                System.out.println("1: Name");
                System.out.println("2: Car Category");
                System.out.println("3: Daily Rate");
                System.out.println("4: Start Date");
                System.out.println("5: End Date");
                System.out.println("6: Exit and Update");
                option = sc.nextInt();
                sc.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Please key in the new name (single word)");
                        String name = sc.next();
                        rentalRateEntity.setName(name);
                        break;
                    case 2:
                        System.out.println("Please key in the new car category");
                        String carCategory = sc.nextLine();
                        try {
                            carCategoryEntity = carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(carCategory);
                            if (rentalRateEntity.getCarCategory().getRentalRates().contains(rentalRateEntity)) {
                                rentalRateEntity.getCarCategory().getRentalRates().remove(rentalRateEntity);
                            }
                            rentalRateEntity.setCarCategory(carCategoryEntity);
                            carCategoryEntity.getRentalRates().add(rentalRateEntity);
                        } catch (CarCategoryNotFoundException e) {
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
                        int day = numericalDate / 1000000;
                        int month = numericalDate / 10000;
                        month = month % 100;
                        int year = numericalDate % 10000;

                        System.out.println("*****Enter rental rate starting hour (0-23)*****");
                        int hour = sc.nextInt();
                        System.out.println("*****Enter rental rate starting minute (0-59)*****");
                        int minute = sc.nextInt();

                        Date date = new Date(year, month, day, hour, minute);
                        rentalRateEntity.setStartDate(date);
                        break;
                    case 5:
                        System.out.println("Please key in the new end date in DDMMYYYY");
                        int numericalDateEnd = sc.nextInt();
                        int dayEnd = numericalDateEnd / 1000000;
                        int monthEnd = numericalDateEnd / 10000;
                        monthEnd = monthEnd % 100;
                        int yearEnd = (numericalDateEnd % 10000) - 1900;

                        System.out.println("*****Enter rental rate starting hour (0-23)*****");
                        int hourEnd = sc.nextInt();
                        System.out.println("*****Enter rental rate starting minute (0-59)*****");
                        int minuteEnd = sc.nextInt();

                        Date endDate = new Date(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd);
                        rentalRateEntity.setStartDate(endDate);
                        break;
                    case 6:
                        rentalRateSessionBeanRemote.updateRentalRateEntity(rentalRateEntity);
                        if (carCategoryEntity != null) {
                            carSessionBeanRemote.updateCarCategoryEntity(carCategoryEntity);
                        }
                        break;
                }
            }
        } catch (RentalRateNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doDeleteRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Delete Rental Rate*****");
        System.out.println("*****Key in the ID of the rental rate you want to update*****");
        long rentalRateId = sc.nextLong();
        try {
            rentalRateSessionBeanRemote.deleteRentalRateEntity(rentalRateId);
        } catch (RentalRateNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void carModelMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 5) {
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create New Model");
            System.out.println("2 : View All Models");
            System.out.println("3 : Update Model");
            System.out.println("4 : Delete Model");
            System.out.println("5 : Exit");

            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    doCreateNewModel();
                    break;
                case 2:
                    doViewAllModels();
                    break;
                case 3:
                    doUpdateModel();
                    break;
                case 4:
                    doDeleteModel();
                    break;
            }
        }
    }

    public void doCreateNewModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Create new car model*****");
        System.out.println("*****Key in the make of the new car model*****");
        String make = sc.next();
        System.out.println("*****Key in the model of the new car model*****");
        String model = sc.next();
        sc.nextLine();
        System.out.println("*****Key in an existing car category to add the new car model to*****");
        String category = sc.nextLine();
        try {
            CarCategoryEntity carCategoryEntity = carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(category);
            long carModelId = carSessionBeanRemote.createCarModelEntity(new CarModelEntity(make, model, carCategoryEntity));
            System.out.println("Car Model Entity with ID: " + carModelId + " has been created");
        } catch (CarCategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void doViewAllModels() {
        System.out.println("*****View all car models*****");
        List<CarModelEntity> carModels = carSessionBeanRemote.retrieveAllCarModelsByCategoryThenMakeThenModel();

        for (CarModelEntity carModelEntity : carModels) {
            System.out.println(carModelEntity);
        }
    }

    public void doUpdateModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Update Model*****");
        System.out.println("*****Enter the make of the car model*****");
        String make = sc.next();
        System.out.println("*****Enter the model of the car model*****");
        String model = sc.next();
        try {
            CarModelEntity carModelEntity = carSessionBeanRemote.retrieveCarModelEntityByMakeAndModel(make, model);
            CarCategoryEntity carCategoryEntity = null;
            int option = 0;

            while (option != 4) {
                System.out.println("Select field of car model to edit");
                System.out.println("1: Make");
                System.out.println("2: Model");
                System.out.println("3: Category");
                System.out.println("4: Exit and Update");
                option = sc.nextInt();
                sc.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Please key in the new make");
                        String newMake = sc.nextLine();
                        carModelEntity.setMake(newMake);
                        break;
                    case 2:
                        System.out.println("Please key in the new model");
                        String newModel = sc.nextLine();
                        carModelEntity.setModel(newModel);
                        break;
                    case 3:
                        System.out.println("Please key in an existing car category for the car model");
                        String carCategoryString = sc.nextLine();
                        try {
                            CarCategoryEntity carCategory = carSessionBeanRemote.retrieveCarCategoryEntityByCarCategory(carCategoryString);
                            if (carModelEntity.getCarCategory().getCarModels().contains(carModelEntity)) {
                                carModelEntity.getCarCategory().getCarModels().remove(carModelEntity);
                            }
                            carModelEntity.setCarCategory(carCategory);
                            carModelEntity.getCarCategory().getCarModels().add(carModelEntity);
                        } catch (CarCategoryNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        carSessionBeanRemote.updateCarModelEntity(carModelEntity);
                        if (carCategoryEntity != null) {
                            carSessionBeanRemote.updateCarCategoryEntity(carCategoryEntity);
                        }
                        break;
                }
            }
        } catch (CarModelNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doDeleteModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Delete Model*****");
        System.out.println("*****Enter the make of the car model*****");
        String make = sc.next();
        System.out.println("*****Enter the model of the car model*****");
        String model = sc.next();

        try {
            carSessionBeanRemote.deleteCarModelEntity(make, model);
        } catch (CarModelNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void carMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 6) {
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : Create New Car");
            System.out.println("2 : View All Cars");
            System.out.println("3 : View Car Details");
            System.out.println("4 : Update Car");
            System.out.println("5 : Delete Car");
            System.out.println("6 : Exit");

            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    try {
                        doCreateNewCar();
                    } catch (CarModelIsDisabledException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    doViewAllCars();
                    break;
                case 3:
                    doViewCarDetails();
                    break;
                case 4:
                    doUpdateCar();
                    break;
                case 5:
                    doDeleteCar();
                    break;
            }
        }
    }

    public void doCreateNewCar() throws CarModelIsDisabledException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Create a new car*****");
        System.out.println("*****Enter the license plate of the new car*****");
        String licensePlate = sc.next();

        System.out.println("*****Enter the colour of the new car*****");
        String colour = sc.next();

        System.out.println("*****Enter a pre-existing make for the new car*****");
        String make = sc.next();
        System.out.println("*****Enter a pre-existing model for the new car*****");
        String model = sc.next();
        try {
            CarModelEntity carModel = carSessionBeanRemote.retrieveCarModelEntityByMakeAndModel(make, model);
            if (!carModel.isDisabled()) {
                CarEntity carEntity = new CarEntity(licensePlate, carModel, outletEntity);
                carEntity.setColour(colour);
                carSessionBeanRemote.createCarEntity(carEntity);
                outletEntity.getCars().add(carEntity);
            } else {
                throw new CarModelIsDisabledException("Car model is disabled");
            }
        } catch (CarModelNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doViewAllCars() {
        System.out.println("*****View all cars*****");
        List<CarEntity> cars = carSessionBeanRemote.retrieveCarsByCategoryThenMakeThenModelThenLicensePlate();

        for (CarEntity car : cars) {
            System.out.println(car);
        }
    }

    public void doViewCarDetails() {
        System.out.println("*****View Car Details*****");
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Enter the license plate of the car*****");
        String licensePlate = sc.next();
        try {
            CarEntity carEntity = carSessionBeanRemote.retrieveCarEntityByLicensePlate(licensePlate);
            System.out.println(carEntity);
        } catch (CarNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doUpdateCar() {
        System.out.println("*****Update a car*****");
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Enter the license plate of the car*****");
        String licensePlate = sc.next();
        OutletEntity updateOutlet = null;
        CarModelEntity carModel = null;
        try {
            CarEntity carEntity = carSessionBeanRemote.retrieveCarEntityByLicensePlate(licensePlate);
            int option = 0;

            while (option != 8) {
                System.out.println("Select field of car to edit");
                System.out.println("1: License Plate");
                System.out.println("2: Colour");
                System.out.println("3: Status");
                System.out.println("4: Location");
                System.out.println("5: Car Model");
                System.out.println("6: Current Outlet");
                System.out.println("7: Return Outlet");
                System.out.println("8: Exit and Update");
                option = sc.nextInt();
                sc.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Please key in the new license plate");
                        String newLicensePlate = sc.next();
                        carEntity.setLicensePlate(newLicensePlate);
                        break;
                    case 2:
                        System.out.println("Please key in the new colour");
                        String colour = sc.next();
                        carEntity.setColour(colour);
                        break;
                    case 3:
                        System.out.println("Please select a new status");
                        System.out.println("Key in 1 for Available");
                        System.out.println("Key in 2 for On Rental");
                        System.out.println("Key in 3 for On Repair");
                        int status = sc.nextInt();
                        switch (status) {
                            case 1:
                                carEntity.setStatus(CarStatusEnum.AVAILABLE);
                                break;
                            case 2:
                                carEntity.setStatus(CarStatusEnum.ONRENTAL);
                                break;
                            case 3:
                                carEntity.setStatus(CarStatusEnum.REPAIR);
                                break;
                        }
                        break;

                    case 4:
                        System.out.println("Please key in the new location");
                        System.out.println("If the car is with a customer, please key in 1");
                        System.out.println("If the car is with an outlet, please key in 2");
                        int locationOption = sc.nextInt();
                        String location = null;
                        if (locationOption == 1) {
                            System.out.println("Please key in the customer Id");
                            location = sc.next();
                            if (carEntity.getCurrentOutlet().getCars().contains(carEntity)) {
                                carEntity.getCurrentOutlet().getCars().remove(carEntity);
                            }
                            carEntity.setCurrentOutlet(null);
                        } else if (locationOption == 2) {
                            System.out.println("Please key in the outlet name");
                            location = sc.next();
                            try {
                                updateOutlet = outletSessionBeanRemote.retrieveOutletEntityByOutletName(location);
                                carEntity.setCurrentOutlet(updateOutlet);
                                if (!updateOutlet.getCars().contains(carEntity)) {
                                    updateOutlet.getCars().add(carEntity);
                                }
                            } catch (OutletNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        if (location != null) {
                            carEntity.setLocation(location);
                        }
                        outletSessionBeanRemote.updateOutletEntity(updateOutlet);
                        break;
                    case 5:
                        System.out.println("Please key in the an existing car model");
                        System.out.println("Please key in the car make");
                        String make = sc.next();
                        System.out.println("Please key in the model");
                        String model = sc.next();
                        try {
                            carModel = carSessionBeanRemote.retrieveCarModelEntityByMakeAndModel(make, model);
                            if (carEntity.getCarModel().getCars().contains(carEntity)) {
                                carEntity.getCarModel().getCars().remove(carEntity);
                            }
                            carEntity.setCarModel(carModel);
                            if (!carEntity.getCarModel().getCars().contains(carEntity)) {
                                carEntity.getCarModel().getCars().add(carEntity);
                            }
                        } catch (CarModelNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        System.out.println("Please key in the outlet name");
                        String name = sc.next();
                        try {
                            updateOutlet = outletSessionBeanRemote.retrieveOutletEntityByOutletName(name);
                            if (carEntity.getCurrentOutlet().getCars().contains(carEntity)) {
                                carEntity.getCurrentOutlet().getCars().remove(carEntity);
                            }
                            carEntity.setCurrentOutlet(updateOutlet);
                            updateOutlet.getCars().size();
                            if (!updateOutlet.getCars().contains(carEntity)) {
                                updateOutlet.getCars().add(carEntity);
                            }
                        } catch (OutletNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        outletSessionBeanRemote.updateOutletEntity(updateOutlet);
                        break;
                    case 7:
                        System.out.println("Please key in the return outlet name");
                        String returnName = sc.next();
                        try {
                            OutletEntity returnOutlet = outletSessionBeanRemote.retrieveOutletEntityByOutletName(returnName);
                            carEntity.setReturnOutlet(returnOutlet);
                        } catch (OutletNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 8:
                        carSessionBeanRemote.updateCarEntity(carEntity);
                        if (updateOutlet != null) {
                            outletSessionBeanRemote.updateOutletEntity(updateOutlet);
                        }
                        if (carModel != null) {
                            carSessionBeanRemote.updateCarModelEntity(carModel);
                        }
                        break;
                }
            }
        } catch (CarNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doDeleteCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Delete a car*****");
        System.out.println("*****Please key in the license plate of the car*****");
        String licensePlate = sc.next();

        carSessionBeanRemote.deleteCarEntity(licensePlate);
    }

    public void transitDriverMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (option != 4) {
            System.out.println("*****Select scenario to access*****");
            System.out.println("1 : View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("2 : Assign Transit Driver");
            System.out.println("3 : Update Transit as Completed");
            System.out.println("4 : Exit");

            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    doViewTransitDriverRecordsForCurrentDayReservations();
                    break;
                case 2:
                    doAssignTransitDriver();
                    break;
                case 3:
                    doUpdateTransitAsCompleted();
                    break;
            }
        }
    }

    public void doViewTransitDriverRecordsForCurrentDayReservations() {
        System.out.println("*****View Transit Driver Records For Current Day Reservations*****");
        List<DispatchEntity> dispatches = dispatchSessionBeanRemote.retrieveDispatchesByDateFromOutlet(currentDate, outletEntity);

        for (DispatchEntity dispatchEntity : dispatches) {
            System.out.println(dispatchEntity);
        }

    }

    public void doAssignTransitDriver() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Assign Transit Driver*****");
        System.out.println("*****Please key in the dispatch id*****");
        long dispatchId = sc.nextLong();

        try {
            DispatchEntity dispatchEntity = dispatchSessionBeanRemote.retrieveDispatchEntityByDispatchId(dispatchId);
            System.out.println("*****Please key in the employee id for dispatch*****");
            long employeeId = sc.nextLong();
            EmployeeEntity dispatchEmployee = employeeSessionBeanRemote.retrieveEmployeeEntityByEmployeeId(employeeId);
            if (dispatchEntity.getTransitDriver() != null && dispatchEntity.getTransitDriver().getDispatches().contains(dispatchEntity)) {
                dispatchEntity.getTransitDriver().getDispatches().remove(dispatchEntity);
            }
            dispatchEntity.setTransitDriver(dispatchEmployee);
            if (!employeeEntity.getDispatches().contains(dispatchEntity)) {
                employeeEntity.getDispatches().add(dispatchEntity);
            }
            System.out.println("Employee with ID: " + dispatchEmployee.getEmployeeId() + " has been assigned to Dispatch with ID: " + dispatchEntity.getDispatchId());
        } catch (DispatchNotFoundException | EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void doUpdateTransitAsCompleted() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*****Update Transit as Completed*****");
        System.out.println("*****Please key in the dispatch id*****");
        long dispatchId = sc.nextLong();

        try {
            DispatchEntity dispatchEntity = dispatchSessionBeanRemote.retrieveDispatchEntityByDispatchId(dispatchId);
            dispatchEntity.setIsComplete(true);
            dispatchEntity.getCar().setCurrentOutlet(outletEntity);
            dispatchEntity.getCar().setLocation(outletEntity.getName());
            if (!outletEntity.getCars().contains(dispatchEntity.getCar())) {
                outletEntity.getCars().add(dispatchEntity.getCar());
                outletSessionBeanRemote.updateOutletEntity(outletEntity);
            }
            dispatchSessionBeanRemote.updateDispatchEntity(dispatchEntity);
            carSessionBeanRemote.updateCarEntity(dispatchEntity.getCar());

        } catch (DispatchNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("*****Transit is updated as completed, car is now recorded as within this outlet*****");
    }
}
