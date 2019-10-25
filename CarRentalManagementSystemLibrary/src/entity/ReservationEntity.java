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
import javax.persistence.ManyToOne;
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
    private String customerName;
    @Column(nullable = false)
    private boolean isPaid;
    @Column(nullable = false)
    private String creditCardNumber;
    @Column(nullable = false)
    private String cvv;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    
    @OneToOne
    private OutletEntity pickupOutlet;
    
    @OneToOne
    private CarEntity car;
    
    @OneToOne
    private CustomerEntity customer;
    
    @OneToOne
    private DispatchEntity dispatch;
    
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private RentalRateEntity rentalRate;

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
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the isPaid
     */
    public boolean isIsPaid() {
        return isPaid;
    }

    /**
     * @param isPaid the isPaid to set
     */
    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
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
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the pickupOutlet
     */
    public OutletEntity getPickupOutlet() {
        return pickupOutlet;
    }

    /**
     * @param pickupOutlet the pickupOutlet to set
     */
    public void setPickupOutlet(OutletEntity pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
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
     * @return the rentalRate
     */
    public RentalRateEntity getRentalRate() {
        return rentalRate;
    }

    /**
     * @param rentalRate the rentalRate to set
     */
    public void setRentalRate(RentalRateEntity rentalRate) {
        this.rentalRate = rentalRate;
    }
    
}
