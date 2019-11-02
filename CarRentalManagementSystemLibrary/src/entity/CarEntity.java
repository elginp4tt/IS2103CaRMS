/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class CarEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false)
    private String licensePlate;
    @Column(nullable = false)
    private String colour;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatusEnum status = CarStatusEnum.INOUTLET;
    @Column(nullable = false)
    private String location;
    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date returnTime;
    @Column(nullable = false)
    private boolean disabled = false;
    @Column(nullable = false)
    private boolean used = false;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarModelEntity carModel;
    
    @OneToOne(optional = true)
    private ReservationEntity currentReservation;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private OutletEntity currentOutlet;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private OutletEntity returnOutlet;
    

    public CarEntity() {
    }

    public CarEntity(String licensePlate, String colour, CarModelEntity carModel, OutletEntity currentOutlet) {
        this();
        this.licensePlate = licensePlate;
        this.colour = colour;
        this.carModel = carModel;
        this.currentOutlet = currentOutlet;
        this.location = currentOutlet.getName();
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCarId() != null ? getCarId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof CarEntity)) {
            return false;
        }
        CarEntity other = (CarEntity) object;
        if ((this.getCarId() == null && other.getCarId() != null) || (this.getCarId() != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ("Car Id: " + carId + " " + licensePlate + " " + colour + " " + status + " " + location + "/n" + carModel + " " + currentOutlet);
    }

    /**
     * @return the licensePlate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * @param licensePlate the licensePlate to set
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * @param colour the colour to set
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * @return the status
     */
    public CarStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CarStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the carModel
     */
    public CarModelEntity getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModelEntity carModel) {
        this.carModel = carModel;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the currentReservation
     */
    public ReservationEntity getCurrentReservation() {
        return currentReservation;
    }

    /**
     * @param currentReservation the currentReservation to set
     */
    public void setCurrentReservation(ReservationEntity currentReservation) {
        this.currentReservation = currentReservation;
    }

    /**
     * @return the currentOutlet
     */
    public OutletEntity getCurrentOutlet() {
        return currentOutlet;
    }

    /**
     * @param currentOutlet the currentOutlet to set
     */
    public void setCurrentOutlet(OutletEntity currentOutlet) {
        this.currentOutlet = currentOutlet;
    }

    /**
     * @return the returnOutlet
     */
    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the returnTime
     */
    public Date getReturnTime() {
        return returnTime;
    }

    /**
     * @param returnTime the returnTime to set
     */
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
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

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

}
