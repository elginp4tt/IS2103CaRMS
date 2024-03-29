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
import javax.xml.bind.annotation.XmlTransient;

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
    private boolean cancelled = false;
    @Column(nullable = false, length = 32)
    private String creditCardNumber;
    @Column(nullable = false, length = 3)
    private String cvv;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;
    
    @Column(nullable = true)
    private double price;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategoryEntity carCategory;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private CarModelEntity carModel;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = true)
    private CarEntity car;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private PartnerEntity partner;
    
    @OneToOne(optional = false, mappedBy = "reservation")
    private DispatchEntity dispatch;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity pickupOutlet;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity returnOutlet;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = true)
    private CarPickupEntity carPickup;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = true)
    private CarReturnEntity carReturn;

    public ReservationEntity() {
    }

    public ReservationEntity(boolean paid, String creditCardNumber, String cvv, Date startDate, Date endDate, CustomerEntity customer, OutletEntity pickupOutlet, OutletEntity returnOutlet, double price) {
        this();
        this.paid = paid;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.pickupOutlet = pickupOutlet;
        this.returnOutlet = returnOutlet;
        this.price = price;
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
    @XmlTransient
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
    @XmlTransient
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
     * @return the carModel
     */
    @XmlTransient
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
    @XmlTransient
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
     * @return the returnOutlet
     */
    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled the cancelled to set
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
}
