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
public class NoCarsException extends Exception{

    public NoCarsException() {
    }

    public NoCarsException(String message) {
        super(message);
    }
    
}
