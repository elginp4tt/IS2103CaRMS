/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class OutletEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = true, unique = true, length = 216)
    private String address;
    @Column(nullable = true)
    private String openingTime;
    @Column(nullable = true)
    private String closingTime;
    
    @OneToMany(mappedBy = "currentOutlet", fetch = FetchType.EAGER)
    private ArrayList<CarEntity> cars;
    
    @OneToMany(mappedBy = "outlet")
    private ArrayList<EmployeeEntity> employees;
    
    @OneToMany(mappedBy = "currentOutlet")
    private ArrayList<DispatchEntity> dispatches;

    public OutletEntity() {
        this.cars = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.dispatches = new ArrayList<>();
    }
    
    public OutletEntity(String name, String openingTime, String closingTime) {
        this();
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public OutletEntity(String name, String address, String openingTime, String closingTime) {
        this();
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OutletEntity[ id=" + outletId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the cars
     */
    public ArrayList<CarEntity> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(ArrayList<CarEntity> cars) {
        this.cars = cars;
    }

    /**
     * @return the employees
     */
    public ArrayList<EmployeeEntity> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(ArrayList<EmployeeEntity> employees) {
        this.employees = employees;
    }

    /**
     * @return the dispatches
     */
    public ArrayList<DispatchEntity> getDispatches() {
        return dispatches;
    }

    /**
     * @param dispatches the dispatches to set
     */
    public void setDispatches(ArrayList<DispatchEntity> dispatches) {
        this.dispatches = dispatches;
    }
    
    public void addCar (CarEntity car){
         cars.add(car);
    }

    /**
     * @return the openingTime
     */
    public String getOpeningTime() {
        return openingTime;
    }

    /**
     * @param openingTime the openingTime to set
     */
    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    /**
     * @return the closingTime
     */
    public String getClosingTime() {
        return closingTime;
    }

    /**
     * @param closingTime the closingTime to set
     */
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

}
