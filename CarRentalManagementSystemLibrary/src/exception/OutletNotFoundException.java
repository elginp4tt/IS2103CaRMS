/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Elgin Patt
 */
public class OutletNotFoundException extends Exception{

    public OutletNotFoundException() {
    }

    public OutletNotFoundException(String message) {
        super(message);
    }    
}
