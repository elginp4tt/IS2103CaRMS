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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class CarPickupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carPickupId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date pickupDateTime; //can use for both date and time
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity outlet;

    public CarPickupEntity() {
    }

    public CarPickupEntity(Date pickupDateTime, OutletEntity outlet) {
        this();
        this.pickupDateTime = pickupDateTime;
        this.outlet = outlet;
    }

    public Long getCarPickupId() {
        return carPickupId;
    }

    public void setCarPickupId(Long carPickupId) {
        this.carPickupId = carPickupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carPickupId != null ? carPickupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carPickupid fields are not set
        if (!(object instanceof CarPickupEntity)) {
            return false;
        }
        CarPickupEntity other = (CarPickupEntity) object;
        if ((this.carPickupId == null && other.carPickupId != null) || (this.carPickupId != null && !this.carPickupId.equals(other.carPickupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarPickupEntity[ id=" + carPickupId + " ]";
    }

    /**
     * @return the pickupDateTime
     */
    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    /**
     * @param pickupDateTime the pickupDateTime to set
     */
    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    /**
     * @return the outlet
     */
    public OutletEntity getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(OutletEntity outlet) {
        this.outlet = outlet;
    }
    
}
