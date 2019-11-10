/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InvalidLoginException;
import exception.PartnerNotFoundException;

public interface PartnerSessionBeanRemote {
    
    public long createPartnerEntity(PartnerEntity partnerEntity);
    
    public PartnerEntity doLogin(String username, String password) throws InvalidLoginException;
    
    public PartnerEntity retrievePartnerEntityByUsername(String username) throws PartnerNotFoundException;
    
    public void updatePartnerEntity(PartnerEntity partnerEntity);
}
