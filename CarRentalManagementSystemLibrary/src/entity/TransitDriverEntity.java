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
public class TransitDriverEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverId;

    public Long getTransitDriverId() {
        return transitDriverId;
    }

    public void setTransitDriverId(Long transitDriverId) {
        this.transitDriverId = transitDriverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverId != null ? transitDriverId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverId fields are not set
        if (!(object instanceof TransitDriverEntity)) {
            return false;
        }
        TransitDriverEntity other = (TransitDriverEntity) object;
        if ((this.transitDriverId == null && other.transitDriverId != null) || (this.transitDriverId != null && !this.transitDriverId.equals(other.transitDriverId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverEntity[ id=" + transitDriverId + " ]";
    }
    
}
