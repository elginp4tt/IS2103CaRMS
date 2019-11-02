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
public class CarReturnEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carReturnId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date returnDateTime; //can use for both date and time
    
    @OneToOne(optional = true)
    @JoinColumn
    private OutletEntity outlet;

    public CarReturnEntity() {
    }

    public CarReturnEntity(Date returnDateTime, OutletEntity outlet) {
        this();
        this.returnDateTime = returnDateTime;
        this.outlet = outlet;
    }

    public Long getCarReturnId() {
        return carReturnId;
    }

    public void setCarReturnId(Long carReturnId) {
        this.carReturnId = carReturnId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carReturnId != null ? carReturnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carReturnId fields are not set
        if (!(object instanceof CarReturnEntity)) {
            return false;
        }
        CarReturnEntity other = (CarReturnEntity) object;
        if ((this.carReturnId == null && other.carReturnId != null) || (this.carReturnId != null && !this.carReturnId.equals(other.carReturnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarReturnEntity[ id=" + carReturnId + " ]";
    }

    /**
     * @return the returnDateTime
     */
    public Date getReturnDateTime() {
        return returnDateTime;
    }

    /**
     * @param returnDateTime the returnDateTime to set
     */
    public void setReturnDateTime(Date returnDateTime) {
        this.returnDateTime = returnDateTime;
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
