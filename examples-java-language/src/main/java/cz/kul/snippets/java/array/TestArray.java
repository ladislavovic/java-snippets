/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.array;

import org.junit.Assert;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        // particular platform. My current platform has limit
        // Integer.MAX_VALUE - 2 (including). If you want bigger arra an Error
        // is thrown:
        // 
        // java.lang.OutOfMemoryError: Requested array size exceeds VM limit
        //
        // Another important issue is a memmory needed for the big array. For
        // example int[] array needs 4B for each item so the array which has
        // 1^9 items takes 4GB of memmory. If you have long[] array it takes
        // twice as much.
        
        printMaxMemmory();
        printUsedMemmory();
        long usedMemmoryBefore = getUsedMemmoryInMB();
        
        // int array
        //
        // You need -Xmx20G to make it working.
        // It takes cca 13 GB for the array and 8B for the item
         int arrLength = Integer.MAX_VALUE - 2;
         int[] arr = new int[arrLength];
        
        // long array
        //
        // You need -Xmx20G to make it working.
        // It takes cca 13 GB for the array and 8B for the item
        // int arrLength = 1_700_000_000;
        // long[] arr = new long[arrLength];
     
        // Object array
        //
        // Here the array takes cca 8 GB of memmory, that is 4 B for the item.
        // Do not understand why, it should be 8B per item, because 64b virtual
        // machine should have 8B reference
        // int arrLength = Integer.MAX_VALUE - 2;
        // Object[] arr = new Object[arrLength];
        
        long usedMemmoryAfter = getUsedMemmoryInMB();
        long memmoryForArray = usedMemmoryAfter - usedMemmoryBefore;
        System.out.println("Memmory used for the array: " + memmoryForArray + " MB");
        System.out.println("Memmory for one array item: " + ((memmoryForArray * 1024 * 1024) / arrLength) + " B");
    }

    @Test
    public void arrayEqualsReturnsTrueOnlyForIdenticalObjects() {
        int[] arr1 = new int[] {1, 2, 3};
        int[] arr2 = new int[] {1, 2, 3};
        assertTrue(arr1.equals(arr1));
        Assert.assertFalse(arr1.equals(arr2));
        Assert.assertFalse(arr1.hashCode() == arr2.hashCode());
    }

    @Test
    public void arrayIsAnObject() {
        String[] strArr = new String[5];

        // Class name is a little bit weird
        assertEquals("[Ljava.lang.String;", strArr.getClass().getName());
        assertTrue(strArr instanceof Object);
        assertTrue(strArr instanceof String[]);

        // Object is a superclass
        Assert.assertSame(Object.class, strArr.getClass().getSuperclass());

        // It is the same for primitive arrays
        int[] intArr = new int[5];
        assertEquals("[I", intArr.getClass().getName());
        assertTrue(intArr instanceof Object);
        Assert.assertSame(Object.class, intArr.getClass().getSuperclass());
    }

    @Test
    public void arraycopy() {
        // it allows copy data from one array to another quicky. It is implemented natively.
        int[] arr1 = new int[] {1, 2, 3};
        int[] arr2 = new int[] {4, 5};
        System.arraycopy(arr1, 0, arr2, 0, 2);
        assertEquals(1, arr2[0]);
        assertEquals(2, arr2[1]);

        // it also works if source and dest array are identical. It first copy data to temporary array.
        System.arraycopy(arr1, 0, arr1, 1, 2);
        assertEquals(1, arr1[0]);
        assertEquals(1, arr1[1]);
        assertEquals(2, arr1[2]);
    }

    @Test
    public void arrayRetyping() {

    }

    private long getUsedMemmoryInMB() {
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        return bean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
    }
    
    private long getMaxMemmoryInMB() {
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        return bean.getHeapMemoryUsage().getMax() / (1024 * 1024);
    }
    
    private void printMaxMemmory() {
        System.out.println(String.format("Max memmory: %s MB", getMaxMemmoryInMB()));
    }
    
    private void printUsedMemmory() {
        System.out.println(String.format("Used memmory: %s MB", getUsedMemmoryInMB()));
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
    
}
