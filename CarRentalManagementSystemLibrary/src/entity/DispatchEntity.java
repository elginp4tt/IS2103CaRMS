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
public class DispatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dispatchId;

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
    
}
