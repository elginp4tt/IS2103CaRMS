/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import exception.CarNotFoundException;


public interface CarSessionBeanRemote {
    public void deleteCarEntity(String licensePlate);

    public CarEntity retrieveCarEntityByLicensePlate(String licensePlate) throws CarNotFoundException;
   
}
