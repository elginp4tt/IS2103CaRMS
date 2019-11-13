/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = true, length = 16)
    private String name;
    @Column(nullable = false, unique = true, length = 16)
    private String username;
    @Column(nullable = false, length = 16)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeAccessRightEnum accessRight;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OutletEntity outlet;
    
    @OneToMany(mappedBy = "transitDriver")
    private ArrayList<DispatchEntity> dispatches;

    public EmployeeEntity() {
        this.dispatches = new ArrayList<>();
    }

    public EmployeeEntity(String name, String username, String password, EmployeeAccessRightEnum accessRight, OutletEntity outlet) {
        this();
        this.name = name;
        this.username = username;
        this.password = password;
        this.accessRight = accessRight;
        this.outlet = outlet;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + employeeId + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    @XmlTransient
    public void setOutlet(OutletEntity outlet) {
        this.outlet = outlet;
    }

    /**
     * @return the accessRight
     */
    public EmployeeAccessRightEnum getAccessRight() {
        return accessRight;
    }

    /**
     * @param accessRight the accessRight to set
     */
    public void setAccessRight(EmployeeAccessRightEnum accessRight) {
        this.accessRight = accessRight;
    }

    /**
     * @return the dispatches
     */
    public ArrayList<DispatchEntity> getDispatches() {
        return dispatches;
    }

    /**
     * @param dispatches the dispatches to set
     */
    public void setDispatches(ArrayList<DispatchEntity> dispatches) {
        this.dispatches = dispatches;
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
    
}
