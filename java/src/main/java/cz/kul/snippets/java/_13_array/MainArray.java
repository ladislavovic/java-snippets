/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._13_array;

import org.junit.Assert;

/**
 *
 * @author kulhalad
 */
public class MainArray {
    
    public static void main(String[] args) {
        maxArrLength();
    }

    private static void maxArrLength() {
        // The array in Java has integer as index so max theoretical size
        // is Integer.MAX_VALUE.
        //
        // But in practise it is less. Some values are used for different
        // purposes. The real max limit depends on particular VM and 
        // particular platform. My current platform has limit smaller than
        // 1 000 000 000 (java 1.8 64b, windows 7 64b)
        try {
            int[] arr = new int[Integer.MAX_VALUE];
            Assert.fail("An exception should be throwen");
        } catch (OutOfMemoryError err) {
        }
        
        int[] arr = new int[Integer.MAX_VALUE - 1500000000];
        Assert.assertEquals(Integer.MAX_VALUE - 1500000000, arr.length);
    }
    
}
