/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import exception.OutletNotFoundException;
import java.util.List;

public interface OutletSessionBeanRemote {

    public void updateOutletEntity(OutletEntity outletEntity);
    
    public long createOutletEntity(OutletEntity outletEntity);
    
    public OutletEntity retrieveOutletEntityByOutletId(long outletId) throws OutletNotFoundException;
    
    public OutletEntity retrieveOutletEntityByOutletName(String name) throws OutletNotFoundException;
    
    public List<OutletEntity> retrieveAllOutletEntities();
}
