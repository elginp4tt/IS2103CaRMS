/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.CarCategoryEntity;
import entity.CarEntity;
import entity.CarModelEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.RentalRateEntity;
import exception.EmployeeNotFoundException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author Elgin Patt
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBean.retrieveEmployeeEntityByUsername("Employee A1");
        } catch (EmployeeNotFoundException e) {
            initializeData();
        }
    }

    private void initializeData() {
        //Outlet A
        OutletEntity outletA = new OutletEntity("Outlet A", null, null);
        outletSessionBean.createOutletEntity(outletA);

        EmployeeEntity employee = new EmployeeEntity("Employee A1", "Employee A1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletA);
        employeeSessionBean.createEmployeeEntity(employee);
        outletA.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee A2", "Employee A2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletA);
        employeeSessionBean.createEmployeeEntity(employee);
        outletA.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee A3", "Employee A3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletA);
        employeeSessionBean.createEmployeeEntity(employee);
        outletA.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee A4", "Employee A4", "password", EmployeeAccessRightEnum.EMPLOYEE, outletA);
        employeeSessionBean.createEmployeeEntity(employee);
        outletA.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee A5", "Employee A5", "password", EmployeeAccessRightEnum.EMPLOYEE, outletA);
        employeeSessionBean.createEmployeeEntity(employee);
        outletA.getEmployees().add(employee);

        //Outlet B
        OutletEntity outletB = new OutletEntity("Outlet B", null, null);
        outletSessionBean.createOutletEntity(outletB);

        employee = new EmployeeEntity("Employee B1", "Employee B1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletB);
        employeeSessionBean.createEmployeeEntity(employee);
        outletB.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee B2", "Employee B2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletB);
        employeeSessionBean.createEmployeeEntity(employee);
        outletB.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee B3", "Employee B3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletB);
        employeeSessionBean.createEmployeeEntity(employee);
        outletB.getEmployees().add(employee);

        //Outlet C
        OutletEntity outletC = new OutletEntity("Outlet C", "1000", "2200");
        outletSessionBean.createOutletEntity(outletC);

        employee = new EmployeeEntity("Employee C1", "Employee C1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletC);
        employeeSessionBean.createEmployeeEntity(employee);
        outletC.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee C2", "Employee C2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletC);
        employeeSessionBean.createEmployeeEntity(employee);
        outletC.getEmployees().add(employee);

        employee = new EmployeeEntity("Employee C3", "Employee C3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletC);
        employeeSessionBean.createEmployeeEntity(employee);
        outletC.getEmployees().add(employee);

        //Standard Sedan
        CarCategoryEntity standardSedan = new CarCategoryEntity("Standard Sedan");
        carSessionBean.createCarCategoryEntity(standardSedan);

        CarModelEntity toyotaCorolla = new CarModelEntity("Toyota", "Corolla", standardSedan);
        carSessionBean.createCarModelEntity(toyotaCorolla);
        standardSedan.getCarModels().add(toyotaCorolla);

        CarEntity carEntity = new CarEntity("SS00A1TC", toyotaCorolla, outletA);
        carSessionBean.createCarEntity(carEntity);
        toyotaCorolla.getCars().add(carEntity);
        outletA.getCars().add(carEntity);

        carEntity = new CarEntity("SS00A2TC", toyotaCorolla, outletA);
        carSessionBean.createCarEntity(carEntity);
        toyotaCorolla.getCars().add(carEntity);
        outletA.getCars().add(carEntity);

        carEntity = new CarEntity("SS00A3TC", toyotaCorolla, outletA);
        carSessionBean.createCarEntity(carEntity);
        toyotaCorolla.getCars().add(carEntity);
        outletA.getCars().add(carEntity);

        CarModelEntity hondaCivic = new CarModelEntity("Honda", "Civic", standardSedan);
        carSessionBean.createCarModelEntity(hondaCivic);
        standardSedan.getCarModels().add(hondaCivic);

        carEntity = new CarEntity("SS00B1HC", hondaCivic, outletB);
        carSessionBean.createCarEntity(carEntity);
        hondaCivic.getCars().add(carEntity);
        outletB.getCars().add(carEntity);

        carEntity = new CarEntity("SS00B2HC", hondaCivic, outletB);
        carSessionBean.createCarEntity(carEntity);
        hondaCivic.getCars().add(carEntity);
        outletB.getCars().add(carEntity);

        carEntity = new CarEntity("SS00B3HC", hondaCivic, outletB);
        carSessionBean.createCarEntity(carEntity);
        hondaCivic.getCars().add(carEntity);
        outletB.getCars().add(carEntity);

        CarModelEntity nissanSunny = new CarModelEntity("Nissan", "Sunny", standardSedan);
        carSessionBean.createCarModelEntity(nissanSunny);
        standardSedan.getCarModels().add(nissanSunny);

        carEntity = new CarEntity("SS00C1NS", nissanSunny, outletC);
        carSessionBean.createCarEntity(carEntity);
        nissanSunny.getCars().add(carEntity);
        outletC.getCars().add(carEntity);

        carEntity = new CarEntity("SS00C2NS", nissanSunny, outletC);
        carSessionBean.createCarEntity(carEntity);
        nissanSunny.getCars().add(carEntity);
        outletC.getCars().add(carEntity);

        carEntity = new CarEntity("SS00C3NS", nissanSunny, outletC);
        carSessionBean.createCarEntity(carEntity);
        nissanSunny.getCars().add(carEntity);
        outletC.getCars().add(carEntity);

        //Family Sedan
        CarCategoryEntity familySedan = new CarCategoryEntity("Family Sedan");
        carSessionBean.createCarCategoryEntity(familySedan);

        //Luxury Sedan
        CarCategoryEntity luxurySedan = new CarCategoryEntity("Luxury Sedan");
        carSessionBean.createCarCategoryEntity(luxurySedan);

        CarModelEntity mercedesEClass = new CarModelEntity("Mercedes", "E Class", luxurySedan);
        carSessionBean.createCarModelEntity(mercedesEClass);
        luxurySedan.getCarModels().add(mercedesEClass);

        carEntity = new CarEntity("LS00A4ME", mercedesEClass, outletA);
        carSessionBean.createCarEntity(carEntity);
        mercedesEClass.getCars().add(carEntity);
        outletA.getCars().add(carEntity);

        CarModelEntity bmw5Series = new CarModelEntity("BMW", "5 Series", luxurySedan);
        carSessionBean.createCarModelEntity(bmw5Series);
        luxurySedan.getCarModels().add(bmw5Series);

        carEntity = new CarEntity("LS00B4B5", bmw5Series, outletB);
        carSessionBean.createCarEntity(carEntity);
        bmw5Series.getCars().add(carEntity);
        outletB.getCars().add(carEntity);

        CarModelEntity audiA6 = new CarModelEntity("Audi", "A6", luxurySedan);
        carSessionBean.createCarModelEntity(audiA6);
        luxurySedan.getCarModels().add(audiA6);

        carEntity = new CarEntity("LS00C4A6", audiA6, outletC);
        carSessionBean.createCarEntity(carEntity);
        audiA6.getCars().add(carEntity);
        outletC.getCars().add(carEntity);

        //SUV and Minivan
        carSessionBean.createCarCategoryEntity(new CarCategoryEntity("SUV and Minivan"));

        //RentalRate
        RentalRateEntity rentalRateEntity = new RentalRateEntity("Default", standardSedan, 100, null, null);
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        standardSedan.getRentalRates().add(rentalRateEntity);

        rentalRateEntity = new RentalRateEntity("Weekend Promo", standardSedan, 80, new Date(119, 11, 6, 12, 0), new Date(119, 11, 8, 0, 0));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        standardSedan.getRentalRates().add(rentalRateEntity);

        rentalRateEntity = new RentalRateEntity("Default", familySedan, 200, null, null);
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        familySedan.getRentalRates().add(rentalRateEntity);
        
        rentalRateEntity = new RentalRateEntity("Monday", luxurySedan, 310, new Date(119, 11, 2, 0, 0), new Date(119, 11, 2, 23, 59));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        luxurySedan.getRentalRates().add(rentalRateEntity);
        
        rentalRateEntity = new RentalRateEntity("Tuesday", luxurySedan, 320, new Date(119, 11, 3, 0, 0), new Date(119, 11, 3, 23, 59));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        luxurySedan.getRentalRates().add(rentalRateEntity);
        
        rentalRateEntity = new RentalRateEntity("Wednesday", luxurySedan, 330, new Date(119, 11, 4, 0, 0), new Date(119, 11, 4, 23, 59));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        luxurySedan.getRentalRates().add(rentalRateEntity);

        rentalRateEntity = new RentalRateEntity("Weekday Promo", luxurySedan, 250, new Date(119, 11 , 4, 12, 0), new Date(119, 11, 5, 12, 00));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
        luxurySedan.getRentalRates().add(rentalRateEntity);
        
        partnerSessionBean.createPartnerEntity(new PartnerEntity("Holiday.com", "password", "Holiday.com"));

    }
}
