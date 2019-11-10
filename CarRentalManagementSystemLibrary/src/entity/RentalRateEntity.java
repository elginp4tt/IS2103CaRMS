    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlTransient;

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
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false)
    private double dailyRate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date validityPeriodStart;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date validityPeriodEnd;
    @Column(nullable = false)
    private int validityPeriod;
    @Column(nullable = false)
    private boolean disabled = false;
    @Column(nullable = false)
    private boolean used = false;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
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
        
        generateValidityPeriodEnd(validityPeriod);
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
        return ("rentalRateId: " + rentalRateId + " " + name + " " + carCategory + " " + dailyRate + " " + validityPeriodStart + " " + validityPeriod);
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
    @XmlTransient
    public CarCategoryEntity getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(CarCategoryEntity carCategory) {
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
        generateValidityPeriodEnd(validityPeriod);
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
     * @return the validityPeriodEnd
     */
    public Date getValidityPeriodEnd() {
        return validityPeriodEnd;
    }

    /**
     * @param validityPeriodEnd the validityPeriodEnd to set
     */
    public void setValidityPeriodEnd(Date validityPeriodEnd) {
        this.validityPeriodEnd = validityPeriodEnd;
    }
    
    public void generateValidityPeriodEnd(int validityPeriod){
        Calendar c = Calendar.getInstance();
        c.setTime(validityPeriodStart);
        c.add(Calendar.DATE, validityPeriod - 1);
        this.validityPeriodEnd = c.getTime();
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
