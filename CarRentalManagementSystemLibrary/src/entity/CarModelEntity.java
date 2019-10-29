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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    @Column(nullable = false)
    private String make;
    @Column(nullable = false)
    private String model;
    
    @OneToMany
    private ArrayList<CarEntity> cars;
    
    @ManyToOne(optional = false)
    private CarCategoryEntity carCategory;
    
    @OneToMany
    private ArrayList<ReservationEntity> reservations;

    public CarModelEntity() {
        this.cars = new ArrayList<CarEntity>();
        this.reservations = new ArrayList<ReservationEntity>();
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
        return "entity.ModelEntity[ id=" + carModelId + " ]";
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
    
}
