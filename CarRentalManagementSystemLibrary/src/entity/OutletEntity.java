/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private HashMap<String, HashMap<Date, Date>> openingHours;
    
    @OneToMany(mappedBy = "currentOutlet")
    private ArrayList<CarEntity> cars;
    
    @OneToMany(mappedBy = "outlet")
    private ArrayList<EmployeeEntity> employees;
    
    @OneToMany(mappedBy = "currentOutlet")
    private ArrayList<DispatchEntity> dispatches;

    public OutletEntity() {
        this.cars = new ArrayList<CarEntity>();
        this.employees = new ArrayList<EmployeeEntity>();
        this.dispatches = new ArrayList<DispatchEntity>();
        this.openingHours = new HashMap<String, HashMap<Date, Date>>();
    }

    public OutletEntity(String name, String address) {
        this();
        this.name = name;
        this.address = address;
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
     * @return the openingHours
     */
    public HashMap getOpeningHours() {
        return openingHours;
    }

    /**
     * @param openingHours the openingHours to set
     */
    public void setOpeningHours(HashMap openingHours) {
        this.openingHours = openingHours;
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
    
}
