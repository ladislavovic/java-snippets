/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._02_entitystate;

/**
 *
 * @author kulhalad
 */
public interface MyService {

    void stateTransient();
    
    void statePersistent();
    
    void stateDetached();

    public void stateRemoved();
    
}
