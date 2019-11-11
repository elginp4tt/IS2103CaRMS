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
import entity.CarCategoryEntity;
import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import exception.EmployeeNotFoundException;
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
    public void postConstruct(){
        try {
            employeeSessionBean.retrieveEmployeeEntityByUsername("manager");
        } catch (EmployeeNotFoundException e) {
            initializeData();
        }
    }

    private void initializeData() {
        OutletEntity outletEntity = new OutletEntity("Outlet0", "0 Outlet Road #00-00","0800","2300");
        outletSessionBean.createOutletEntity(outletEntity);
        employeeSessionBean.createEmployeeEntity(new EmployeeEntity("admin", "password", EmployeeAccessRightEnum.ADMINISTRATOR, outletEntity));
        partnerSessionBean.createPartnerEntity(new PartnerEntity("partner", "password", "partner"));
        carSessionBean.createCarCategoryEntity(new CarCategoryEntity("0"));
    }
}
