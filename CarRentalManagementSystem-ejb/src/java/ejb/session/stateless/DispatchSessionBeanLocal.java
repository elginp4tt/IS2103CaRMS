/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DispatchEntity;
import entity.OutletEntity;
import java.util.Date;
import java.util.List;


public interface DispatchSessionBeanLocal {

    public long createDispatchEntity(DispatchEntity dispatchEntity);

    public void updateDispatchEntity(DispatchEntity dispatchEntity);

    public List<DispatchEntity> retrieveDispatchesByDate(Date date);
    
    public List<DispatchEntity> retrieveDispatchesByDateToOutlet(Date date, OutletEntity outletEntity);
    
}
