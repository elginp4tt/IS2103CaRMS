/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
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
    private String name;
    private String address;
    private Hashtable<String, Hashtable<Date, Date>> openingHours;
    
    @OneToMany
    private List<CarEntity> cars;
    
    @OneToMany
    private List<EmployeeEntity> employees;

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
    public Hashtable getOpeningHours() {
        return openingHours;
    }

    /**
     * @param openingHours the openingHours to set
     */
    public void setOpeningHours(Hashtable openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * @return the cars
     */
    public List<CarEntity> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    /**
     * @return the employees
     */
    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
    }
    
}
