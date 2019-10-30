/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;


public class CarCategoryNotFoundException extends Exception{

    public CarCategoryNotFoundException() {
    }

    public CarCategoryNotFoundException(String message) {
        super(message);
    }
    
}
