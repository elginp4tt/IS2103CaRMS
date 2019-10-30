/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategoryEntity;
import entity.CarEntity;
import entity.CarModelEntity;
import exception.CarCategoryNotFoundException;
import exception.CarModelNotFoundException;
import exception.CarNotFoundException;
import exception.NoCarModelsException;
import exception.NoCarsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.enumeration.CarStatusEnum;

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
    
    
    @Override
    public CarEntity retrieveCarEntityByCarId(long carId)throws CarNotFoundException{
        CarEntity carEntity = em.find(CarEntity.class, carId);
        if (carEntity != null){
            return carEntity;
        } else {
            throw new CarNotFoundException("Car not found");
        }
    }
    
    @Override
    public CarModelEntity retrieveCarModelEntityByCarModelId(long carModelId)throws CarModelNotFoundException{
        CarModelEntity carModelEntity = em.find(CarModelEntity.class, carModelId);
        if (carModelEntity != null){
            return carModelEntity;
        } else {
            throw new CarModelNotFoundException("Car model not found");
        }
    }
    
    @Override
    public CarCategoryEntity retrieveCarCategoryEntityByCarCategoryId(long carCategoryId)throws CarCategoryNotFoundException{
        CarCategoryEntity carCategoryEntity = em.find(CarCategoryEntity.class, carCategoryId);
        if (carCategoryEntity != null){
            return carCategoryEntity;
        } else {
            throw new CarCategoryNotFoundException("Car model not found");
        }
    }
    
    @Override
    public List<CarEntity> retrieveCarsByCarModelId(long carModelId){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel");
        query.setParameter("inCarModel", carModelId);
        List<CarEntity> cars = query.getResultList();
        
            return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelId(long carModelId){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.status = :inStatus");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.INOUTLET);
        List<CarEntity> cars = query.getResultList();
        
            return cars;
    }    
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelIdInOutlet(long carModelId, long outletId){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.status = :inStatus AND c.currentOutlet = :inOutlet");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.INOUTLET);
        query.setParameter("inOutlet", outletId);
        List<CarEntity> cars = query.getResultList();
        
            return cars;
    }   
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelIdNotInOutlet(long carModelId, long outletId){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.status = :inStatus AND c.currentOutlet <> :inOutlet");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.INOUTLET);
        query.setParameter("inOutlet", outletId);
        List<CarEntity> cars = query.getResultList();
        
        return cars;
    }   
    
    @Override
    public List<CarEntity> retrieveCarsByCarCategoryId(long carCategoryId) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveCarsByCarModelId(carModelEntity.getCarModelId());
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
        
            return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryId(long carCategoryId) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelId(carModelEntity.getCarModelId());
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
            return cars;

    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdInOutlet(long carCategoryId, long outletId) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelIdInOutlet(carModelEntity.getCarModelId(), outletId);
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
        
            return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdNotInOutlet(long carCategoryId, long outletId) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelIdNotInOutlet(carModelEntity.getCarModelId(), outletId);
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
        
            return cars;
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
    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(long carModelId, long outletId, Date date){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.status = :inStatus AND c.returnOutlet = :inReturnOutlet AND c.returnTime <= :inReturnTime");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.ONRENTAL);
        query.setParameter("returnOutlet", outletId);
        query.setParameter("returnTime", date, TemporalType.TIMESTAMP);
        
        List<CarEntity> cars = query.getResultList();
        
        return cars;
    }

    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(long carModelId, long outletId, Date date){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.status = :inStatus AND c.returnOutlet <> :inReturnOutlet AND c.returnTime <= :inReturnTime");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.ONRENTAL);
        query.setParameter("returnOutlet", outletId);
        query.setParameter("returnTime", date, TemporalType.TIMESTAMP);
        
        List<CarEntity> cars = query.getResultList();
        
        return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTime(long carCategoryId, long outletId, Date date) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(carModelEntity.getCarModelId(), outletId, date);
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
            return cars;

    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTimeOtherOutlet(long carCategoryId, long outletId, Date date) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
        if (!carModels.isEmpty()){
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(carModelEntity.getCarModelId(), outletId, date);
                cars.addAll(curCars);
            }
        } else {
            throw new NoCarModelsException("No car models found for the specified car category");
        }
            return cars;

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
    
    @Override
    public void updateCarEntity(CarEntity carEntity){
        em.merge(carEntity);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
