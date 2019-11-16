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
public class CustomerAlreadyExistsException extends Exception{

    public CustomerAlreadyExistsException() {
    }

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
    
}
