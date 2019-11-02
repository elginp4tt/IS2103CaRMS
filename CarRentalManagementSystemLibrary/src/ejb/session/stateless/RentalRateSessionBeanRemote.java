/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import exception.RentalRateNotFoundException;
import java.util.List;

public interface RentalRateSessionBeanRemote {
    
    public long createRentalRateEntity(RentalRateEntity rentalRateEntity);
    
    public List<RentalRateEntity> retrieveAllRentalRatesByCarCategoryThenDate();
    
    public RentalRateEntity retrieveRentalRateEntityByRentalRateId(long rentalRateId) throws RentalRateNotFoundException;
    
    public void updateRentalRateEntity(RentalRateEntity rentalRateEntity);
    
    public void deleteRentalRateEntity(long rentalRateId) throws RentalRateNotFoundException;
}
