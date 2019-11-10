/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.DispatchEntity;
import entity.OutletEntity;
import exception.OutletNotFoundException;

import exception.OutletUpdateException;
import java.util.List;

public interface OutletSessionBeanLocal {

    public long createOutletEntity(OutletEntity newOutletEntity);

    public OutletEntity retrieveOutletEntityById(long outletId) throws OutletNotFoundException;

    public OutletEntity retrieveOutletEntityByName(String outletName) throws OutletNotFoundException;

//    public void updateOutletEntity(OutletEntity outletEntity) throws OutletNotFoundException, OutletUpdateException;

    public void updateOutletEntity(OutletEntity outletEntity);
    
    public void deleteOutletEntity(long outletId) throws OutletUpdateException;

//    public OutletEntity addCarToOutletEntity(String outletName, CarEntity car) throws OutletUpdateException;
//
//    public OutletEntity addEmployeeToOutletEntity(String outletName, long EmployeeId) throws OutletUpdateException;
//
//    public OutletEntity addDispatchToOutletEntity(DispatchEntity dispatchRecord) throws OutletUpdateException;
        
}
