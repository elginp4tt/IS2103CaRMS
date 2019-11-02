/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class DispatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dispatchId;
    @Column(nullable = false)
    private boolean isComplete = false;
    
    @OneToOne(optional = false)
    @Column(nullable = false)
    private ReservationEntity reservation;
    
    @OneToOne(optional = false)
    @Column(nullable = false)
    private CarEntity car;
    
    @OneToOne(optional = false)
    @Column(nullable = false)
    private EmployeeEntity transitDriver;
    
    @OneToOne(optional = false)
    @Column(nullable = false)
    private OutletEntity currentOutlet;
    
    @OneToOne(optional = false)
    @Column(nullable = false)
    private OutletEntity endOutlet;

    public DispatchEntity() {
    }

    public DispatchEntity(ReservationEntity reservation, CarEntity car, OutletEntity currentOutlet, OutletEntity endOutlet) {
        this();
        this.reservation = reservation;
        this.car = car;
        this.currentOutlet = currentOutlet;
        this.endOutlet = endOutlet;
    }

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dispatchId != null ? dispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dispatchId fields are not set
        if (!(object instanceof DispatchEntity)) {
            return false;
        }
        DispatchEntity other = (DispatchEntity) object;
        if ((this.dispatchId == null && other.dispatchId != null) || (this.dispatchId != null && !this.dispatchId.equals(other.dispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DispatchEntity[ id=" + dispatchId + " ]";
    }

    /**
     * @return the isComplete
     */
    public boolean isIsComplete() {
        return isComplete;
    }

    /**
     * @param isComplete the isComplete to set
     */
    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    /**
     * @return the car
     */
    public CarEntity getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(CarEntity car) {
        this.car = car;
    }

    /**
     * @return the transitDriver
     */
    public EmployeeEntity getTransitDriver() {
        return transitDriver;
    }

    /**
     * @param transitDriver the transitDriver to set
     */
    public void setTransitDriver(EmployeeEntity transitDriver) {
        this.transitDriver = transitDriver;
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
     * @return the endOutlet
     */
    public OutletEntity getEndOutlet() {
        return endOutlet;
    }

    /**
     * @param endOutlet the endOutlet to set
     */
    public void setEndOutlet(OutletEntity endOutlet) {
        this.endOutlet = endOutlet;
    }

    /**
     * @return the reservation
     */
    public ReservationEntity getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }
    
}
