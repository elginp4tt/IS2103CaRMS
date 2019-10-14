/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class CarPickupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carPickupid;

    public Long getCarPickupid() {
        return carPickupid;
    }

    public void setCarPickupid(Long carPickupid) {
        this.carPickupid = carPickupid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carPickupid != null ? carPickupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carPickupid fields are not set
        if (!(object instanceof CarPickupEntity)) {
            return false;
        }
        CarPickupEntity other = (CarPickupEntity) object;
        if ((this.carPickupid == null && other.carPickupid != null) || (this.carPickupid != null && !this.carPickupid.equals(other.carPickupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarPickupEntity[ id=" + carPickupid + " ]";
    }
    
}
