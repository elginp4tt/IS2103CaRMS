/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(nullable = false)
    private boolean paid;
    @Column(nullable = false)
    private String creditCardNumber;
    @Column(nullable = false)
    private String cvv;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;
    
    @OneToMany
    @JoinColumn
    private List<RentalRateEntity> rentalRates = new ArrayList<RentalRateEntity>();
    
    @OneToOne(optional = true)
    @JoinColumn
    private CarCategoryEntity carCategory;
    
    @OneToOne(optional = true)
    @JoinColumn
    private CarModelEntity carModel;
    
    @OneToOne(optional = false)
    @JoinColumn
    private CarEntity car;
    
    @ManyToOne(optional = false)
    @JoinColumn
    private CustomerEntity customer;
    
    @ManyToOne(optional = true)
    @JoinColumn
    private PartnerEntity partner;
    
    @OneToOne(optional = false, mappedBy = "reservation")
    private DispatchEntity dispatch;
    
    @OneToOne(optional = false)
    @JoinColumn
    private OutletEntity outlet;
    
    @OneToOne(optional = false)
    @JoinColumn
    private CarPickupEntity carPickup;
    
    @OneToOne(optional = false)
    @JoinColumn
    private CarReturnEntity carReturn;

    public ReservationEntity() {
    }

    public ReservationEntity(boolean paid, String creditCardNumber, String cvv, Date startDate, Date endDate, CarEntity car, CustomerEntity customer, OutletEntity outlet) {
        this();
        this.paid = paid;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.startDate = startDate;
        this.endDate = endDate;
        this.car = car;
        this.customer = customer;
        this.outlet = outlet;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }

    /**
     * @return the isPaid
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
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
     * @return the customer
     */
    public CustomerEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    /**
     * @return the dispatch
     */
    public DispatchEntity getDispatch() {
        return dispatch;
    }

    /**
     * @param dispatch the dispatch to set
     */
    public void setDispatch(DispatchEntity dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @return the carPickup
     */
    public CarPickupEntity getCarPickup() {
        return carPickup;
    }

    /**
     * @param carPickup the carPickup to set
     */
    public void setCarPickup(CarPickupEntity carPickup) {
        this.carPickup = carPickup;
    }

    /**
     * @return the carReturn
     */
    public CarReturnEntity getCarReturn() {
        return carReturn;
    }

    /**
     * @param carReturn the carReturn to set
     */
    public void setCarReturn(CarReturnEntity carReturn) {
        this.carReturn = carReturn;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the carCategory
     */
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
     * @return the carModel
     */
    public CarModelEntity getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModelEntity carModel) {
        this.carModel = carModel;
    }

    /**
     * @return the partner
     */
    public PartnerEntity getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
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

    /**
     * @return the rentalRates
     */
    public List<RentalRateEntity> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRateEntity> rentalRates) {
        this.rentalRates = rentalRates;
    }
    
}
