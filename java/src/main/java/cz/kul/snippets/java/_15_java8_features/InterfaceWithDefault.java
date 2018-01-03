/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._15_java8_features;

/**
 *
 * @author kulhalad
 */
public interface InterfaceWithDefault {
    
    default String defaultMethod() {
        return "Am default method";
    }
    
}
