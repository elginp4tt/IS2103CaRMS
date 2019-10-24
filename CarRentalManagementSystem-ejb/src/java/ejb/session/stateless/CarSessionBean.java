/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import exception.CarNotFoundException;
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
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    public CarEntity retrieveCarEntityByCarId(Long carId){
        CarEntity carEntity = em.find(CarEntity.class, carId);
        
        return carEntity;
    }
    
    @Override
    public CarEntity retrieveCarEntityByLicensePlate(String licensePlate)throws CarNotFoundException{
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.licensePlate = :inLicensePlate");
        query.setParameter("inLicensePlate", licensePlate);
        try{
            return (CarEntity)query.getSingleResult();
        } catch (Exception e) {
            throw new CarNotFoundException("Car not found");
        }
    }
    
    @Override
    public void deleteCarEntity(String licensePlate){
        try {
        CarEntity carEntity = retrieveCarEntityByLicensePlate(licensePlate);
        em.remove(carEntity);
        } catch (CarNotFoundException e){
        System.out.println(e.getMessage());
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
