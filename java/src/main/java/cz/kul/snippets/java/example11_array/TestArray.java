/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.example11_array;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kulhalad
 */
public class TestArray {

    @Test
    public void maxArrLength() {
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
    
    @Test
    public void javaPassTheRerenceToTheArrayNotTheArray() {
        // Java pass all params by value. And array is not an exception. It pass reference to the array
        // by value so the function works with the same array
        int[] arr = {1, 2, 3};
        func(arr);
        Assert.assertEquals(10, arr[0]);;
    }    
    
    private void func(int[] arr) {
        arr[0] = 10;
    }
    
    @Test
    public void arrayEqualsReturnsTrueOnlyForIdenticalObjects() {
        int[] arr1 = new int[] {1, 2, 3};
        int[] arr2 = new int[] {1, 2, 3};
        Assert.assertTrue(arr1.equals(arr1));
        Assert.assertFalse(arr1.equals(arr2));
        Assert.assertFalse(arr1.hashCode() == arr2.hashCode());
    }
    
    
    
}
