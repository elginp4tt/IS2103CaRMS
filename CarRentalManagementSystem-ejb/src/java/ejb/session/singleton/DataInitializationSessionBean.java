/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author Elgin Patt
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct(){
        try {
        
        } catch (Exception e) {
            initializeData();
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private void initializeData() {
        
    }
}
