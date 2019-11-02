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
import java.util.Date;
import java.util.List;

public interface CarSessionBeanLocal {

    public CarEntity retrieveCarEntityByLicensePlate(String licensePlate) throws CarNotFoundException;

    public void deleteCarEntity(String licensePlate);
    
    public void updateCarEntity(CarEntity carEntity);

    public List<CarEntity> retrieveCarsByCarModelId(long carModelId);

    public List<CarEntity> retrieveCarsByCarCategoryId(long carCategoryId) throws NoCarModelsException;

    public CarCategoryEntity retrieveCarCategoryEntityByCarCategoryId(long carCategoryId) throws CarCategoryNotFoundException;

    public CarModelEntity retrieveCarModelEntityByCarModelId(long carModelId) throws CarModelNotFoundException;

    public CarEntity retrieveCarEntityByCarId(long carId) throws CarNotFoundException;

    public List<CarEntity> retrieveAvailableCarsByCarModelId(long carModelId);
    
    public List<CarEntity> retrieveAvailableCarsByCarCategoryId(long carCategoryId) throws NoCarModelsException;
    
    public List<CarEntity> retrieveAvailableCarsByCarModelIdInOutlet(long carModelId, long outletId);
    
    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdInOutlet(long carCategoryId, long outletId) throws NoCarModelsException;

    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTime(long carCategoryId, long outletId, Date date) throws NoCarModelsException;

    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTime(long carModelId, long outletId, Date date);

    public List<CarEntity> retrieveAvailableCarsByCarModelIdNotInOutlet(long carModelId, long outletId);

    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdNotInOutlet(long carCategoryId, long outletId) throws NoCarModelsException;

    public List<CarEntity> retrieveAvailableCarsByCarCategoryIdWithCustomerButReturnedOnTimeOtherOutlet(long carCategoryId, long outletId, Date date) throws NoCarModelsException;

    public List<CarEntity> retrieveAvailableCarsByCarModelIdWithCustomerButReturnedOnTimeOtherOutlet(long carModelId, long outletId, Date date);

    public CarCategoryEntity retrieveCarCategoryEntityByCarCategory(String carCategory) throws CarCategoryNotFoundException;

    public long createCarCategoryEntity(CarCategoryEntity carCategoryEntity);

    public long createCarModelEntity(CarModelEntity carModelEntity);

    public List<CarModelEntity> retrieveAllCarModelsByCategoryThenMakeThenModel();

    public CarModelEntity retrieveCarModelEntityByMakeAndModel(String make, String model) throws CarModelNotFoundException;

    public void updateCarModelEntity(CarModelEntity carModelEntity);

    public void updateCarCategoryEntity(CarCategoryEntity carCategoryEntity);

    public void deleteCarModelEntity(String make, String model) throws CarModelNotFoundException;
}