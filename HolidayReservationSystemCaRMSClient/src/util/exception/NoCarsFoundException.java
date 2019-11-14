/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Elgin Patt
 */
public class NoCarsFoundException extends Exception{

    public NoCarsFoundException() {
    }

    public NoCarsFoundException(String message) {
        super(message);
    }
    
}
