/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exception.InvalidLoginException;
import exception.PartnerNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Elgin Patt
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public long createPartnerEntity(PartnerEntity partnerEntity){
        em.persist(partnerEntity);
        em.flush();
        
        return partnerEntity.getPartnerId();    
    }
    
    @Override
    public PartnerEntity doLogin(String username, String password) throws InvalidLoginException {
        try {
            PartnerEntity partner = retrievePartnerEntityByUsername(username);
            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginException("Login details provided are incorrect");
            }

        } catch (PartnerNotFoundException e) {
            throw new InvalidLoginException("Login details provided are incorrect");
        }
    }

    @Override
    public PartnerEntity retrievePartnerEntityByUsername(String username) throws PartnerNotFoundException{
        Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            return (PartnerEntity) query.getSingleResult();
        } catch (Exception e) {
            throw new PartnerNotFoundException("Customer not found");
        }
    }
    
    @Override
    public void updatePartnerEntity(PartnerEntity partnerEntity){
        em.merge(partnerEntity);
    }
}
