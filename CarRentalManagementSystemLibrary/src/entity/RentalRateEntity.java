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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class RentalRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double dailyRate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date validityPeriodStart;
    @Column(nullable = false)
    private int validityPeriod;
    
    @ManyToOne
    private CarCategoryEntity carCategory;

    public RentalRateEntity() {
    }

    public RentalRateEntity(String name, double dailyRate, Date validityPeriodStart, int validityPeriod, CarCategoryEntity carCategory) {
        this();
        this.name = name;
        this.dailyRate = dailyRate;
        this.validityPeriodStart = validityPeriodStart;
        this.validityPeriod = validityPeriod;
        this.carCategory = carCategory;
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRateEntity)) {
            return false;
        }
        RentalRateEntity other = (RentalRateEntity) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateEntity[ id=" + rentalRateId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dailyRate
     */
    public double getDailyRate() {
        return dailyRate;
    }

    /**
     * @param dailyRate the dailyRate to set
     */
    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    /**
     * @return the car
     */
    public CarCategoryEntity getCarCategory() {
        return carCategory;
    }

    /**
     * @param car the car to set
     */
    public void setCar(CarCategoryEntity carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * @return the validityPeriodStart
     */
    public Date getValidityPeriodStart() {
        return validityPeriodStart;
    }

    /**
     * @param validityPeriodStart the validityPeriodStart to set
     */
    public void setValidityPeriodStart(Date validityPeriodStart) {
        this.validityPeriodStart = validityPeriodStart;
    }

    /**
     * @return the validityPeriod
     */
    public int getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }


}
