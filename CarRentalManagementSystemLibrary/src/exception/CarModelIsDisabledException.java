/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;


public class CarModelIsDisabledException extends Exception{

    public CarModelIsDisabledException() {
    }

    public CarModelIsDisabledException(String message) {
        super(message);
    }
    
}
