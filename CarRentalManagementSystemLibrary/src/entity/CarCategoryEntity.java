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
import javax.persistence.OneToOne;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class CarCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carCategoryId;
    private String carCategory;
    
    @OneToOne
    private RentalRateEntity rentalRate;

    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCategoryId fields are not set
        if (!(object instanceof CarCategoryEntity)) {
            return false;
        }
        CarCategoryEntity other = (CarCategoryEntity) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CategoryEntity[ id=" + carCategoryId + " ]";
    }

    /**
     * @return the carCategory
     */
    public String getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }
    
}
