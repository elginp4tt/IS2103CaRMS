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
public class NoCarModelsException extends Exception{

    public NoCarModelsException() {
    }

    public NoCarModelsException(String message) {
        super(message);
    }
    
}
