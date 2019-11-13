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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class CarModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carModelId;
    @Column(nullable = false, length = 32)
    private String make; //Toyota
    @Column(nullable = false, length = 32)
    private String model; //Corolla Altis
    @Column(nullable = false)
    private boolean disabled = false;
    
    @OneToMany(mappedBy = "carModel")
    private ArrayList<CarEntity> cars;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategoryEntity carCategory;
    
    @OneToMany(mappedBy = "carModel")
    private ArrayList<ReservationEntity> reservations;

    public CarModelEntity() {
        this.cars = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public CarModelEntity(String make, String model, CarCategoryEntity carCategory) {
        this();
        this.make = make;
        this.model = model;
        this.carCategory = carCategory;
    }
    
    /**
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carModelId != null ? carModelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carModelId fields are not set
        if (!(object instanceof CarModelEntity)) {
            return false;
        }
        CarModelEntity other = (CarModelEntity) object;
        if ((this.carModelId == null && other.carModelId != null) || (this.carModelId != null && !this.carModelId.equals(other.carModelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ("ID: "+ carModelId + " " + make + " " + model + " in category: " + carCategory);
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
     * @return the carCategory
     */
    @XmlTransient
    public CarCategoryEntity getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(CarCategoryEntity carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * @return the reservations
     */
    public ArrayList<ReservationEntity> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(ArrayList<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
}
