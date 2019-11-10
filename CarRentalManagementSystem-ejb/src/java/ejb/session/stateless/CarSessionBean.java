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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    public long createCarCategoryEntity(CarCategoryEntity carCategoryEntity) {
        em.persist(carCategoryEntity);
        em.flush();
        
        return carCategoryEntity.getCarCategoryId();
    }
    
    @Override
    public long createCarModelEntity(CarModelEntity carModelEntity) {
        em.persist(carModelEntity);
        em.flush();
        
        return carModelEntity.getCarModelId();
    }
    
    @Override
    public long createCarEntity(CarEntity carEntity) {
        em.persist(carEntity);
        em.flush();
        
        return carEntity.getCarId();
    }
    
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
    public CarCategoryEntity retrieveCarCategoryEntityByCarCategory(String carCategory)throws CarCategoryNotFoundException {
        Query query = em.createQuery("SELECT c FROM CarCategoryEntity c WHERE c.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategory);
        
        try {
            return (CarCategoryEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new CarCategoryNotFoundException("Car category not found");
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
    public List<CarEntity> retrieveAvailableCarsByCarModelIdNotDisabled(long carModelId){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel = :inCarModel AND c.disabled = false");
        query.setParameter("inCarModel", carModelId);
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
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
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
    public List<CarEntity> retrieveAvailableCarsByCarCategoryId(long carCategoryId){
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        List<CarModelEntity> carModels = query.getResultList();
        List<CarEntity> cars = new ArrayList<CarEntity>();
            for (CarModelEntity carModelEntity : carModels){
                List<CarEntity> curCars = retrieveAvailableCarsByCarModelIdNotDisabled(carModelEntity.getCarModelId());
                cars.addAll(curCars);
            }
            return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdInOutlet(long carCategoryId, long outletId) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
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
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
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
        } catch (NoResultException | NonUniqueResultException e) {
            throw new CarNotFoundException("Car not found");
        }
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(long carModelId, long outletId, Date date){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel.carModelId = :inCarModel AND c.status = :inStatus AND c.returnOutlet.outletId = :inReturnOutlet AND c.returnTime <= :inReturnTime");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.ONRENTAL);
        query.setParameter("returnOutlet", outletId);
        query.setParameter("returnTime", date, TemporalType.TIMESTAMP);
        
        List<CarEntity> cars = query.getResultList();
        
        return cars;
    }

    @Override
    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(long carModelId, long outletId, Date date){
        Query query = em.createQuery("SELECT c FROM CarEntity c WHERE c.carModel.carModelId = :inCarModel AND c.status = :inStatus AND c.returnOutlet.outletId <> :inReturnOutlet AND c.returnTime <= :inReturnTime");
        query.setParameter("inCarModel", carModelId);
        query.setParameter("inStatus", CarStatusEnum.ONRENTAL);
        query.setParameter("returnOutlet", outletId);
        query.setParameter("returnTime", date, TemporalType.TIMESTAMP);
        
        List<CarEntity> cars = query.getResultList();
        
        return cars;
    }
    
    @Override
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTime(long carCategoryId, long outletId, Date date) throws NoCarModelsException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
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
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.carCategory.carCategoryId = :inCarCategory");
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
    public List<CarModelEntity> retrieveAllCarModelsByCategoryThenMakeThenModel(){
        Query query = em.createQuery("SELECT c FROM CarModelEntity c ORDER BY c.carCategory, c.make, c.model");
        
        return query.getResultList();
    }
    
    @Override
    public CarModelEntity retrieveCarModelEntityByMakeAndModel(String make, String model) throws CarModelNotFoundException{
        Query query = em.createQuery("SELECT c FROM CarModelEntity c WHERE c.make = :inMake AND c.model = :inModel");
        query.setParameter("inMake", make);
        query.setParameter("inModel", model);
        
        try{
            return (CarModelEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e){
            throw new CarModelNotFoundException("Car model entity not found");
        }
    }
    
    @Override
    public List<CarEntity> retrieveCarsByCategoryThenMakeThenModelThenLicensePlate() {
        Query query = em.createQuery("SELECT c FROM CarEntity c JOIN c.carModel cm JOIN cm.carCategory cc ORDER BY cc.carCategory, cm.make, cm.model, c.licensePlate");
        return query.getResultList();
    }
    
    @Override
    public void deleteCarEntity(String licensePlate){
        try {
            CarEntity carEntity = retrieveCarEntityByLicensePlate(licensePlate);
            if (carEntity.isUsed()){
                carEntity.setDisabled(true);
                updateCarEntity(carEntity);
                System.out.println("*****Car has been updated to disabled*****");
            } else {
                em.remove(carEntity);
                System.out.println("*****Car has been deleted*****");
            }
        } catch (CarNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void deleteCarModelEntity(String make, String model) throws CarModelNotFoundException{
        CarModelEntity carModelEntity = retrieveCarModelEntityByMakeAndModel(make, model);
            if (carModelEntity.getCars().isEmpty()){
                em.remove(carModelEntity);
                System.out.println("*****Car Model has been deleted*****");
            } else {
                carModelEntity.setDisabled(true);
                updateCarModelEntity(carModelEntity);
                System.out.println("*****Car Model has updated to disabled*****");
            }
    }

    @Override
    public List<CarCategoryEntity> retrieveAllCarCategoryEntities(){
        Query query = em.createQuery("SELECT cc FROM CarCategoryEntity cc");
        return query.getResultList();
    }
    
    @Override
    public List<CarModelEntity> retrieveAllCarModelEntitiesByCarCategory(long carCategoryId) {
        Query query = em.createQuery("SELECT cm FROM CarModelEntity cm WHERE cm.carCategory.carCategoryId = :inCarCategory");
        query.setParameter("inCarCategory", carCategoryId);
        
        return query.getResultList();
    }
    
    @Override
    public void updateCarEntity(CarEntity carEntity){
        em.merge(carEntity);
    }
    
    @Override
    public void updateCarModelEntity(CarModelEntity carModelEntity){
        em.merge(carModelEntity);
    }
    
    @Override
    public void updateCarCategoryEntity(CarCategoryEntity carCategoryEntity){
        em.merge(carCategoryEntity);
    }

}
