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
public class CarReturnEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carReturnId;

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
    
}
