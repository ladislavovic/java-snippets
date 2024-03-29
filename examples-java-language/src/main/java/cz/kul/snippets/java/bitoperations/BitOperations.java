/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.bitoperations;

import org.junit.Assert;
import org.junit.Test;


/**
 *
 * @author kulhalad
 */
public class BitOperations {

    @Test
    public void testLeftShiftOperator() {
        // Left Shift Operator <<
        int a = parseInt("00000000 00000000 00000000 00001100");
        int b = a << 2;
        // Bits on right are filled with 0, bits on right are lost
        Assert.assertEquals("00000000 00000000 00000000 00110000", toStr(b));
    }

    @Test
    public void testRightShiftOperator() {
        // Right Shift Operator >>
        int a = parseInt("11111111 11111111 11111111 11110100");
        Assert.assertEquals(-12, a);
        int b = a >> 2;
        // Bits on right are lost, bits on left are filled with "sign bit"
        // (the bit with is the most on the left)
        // So shift right newer change sign of the number
        Assert.assertEquals("11111111 11111111 11111111 11111101", toStr(b));
        Assert.assertEquals(-3, b);

    }

    @Test
    public void testUnsignedRightShiftOperator() {
        // Unsigned Right shift Operator >>>
        int a = parseInt("11111111 11111111 11111111 11110100");
        Assert.assertEquals(-12, a);
        int b = a >>> 2;
        // Bits on right are lost, bits on left are filled with 0
        // It cause the change of negative numbers on positive
        Assert.assertEquals("00111111 11111111 11111111 11111101", toStr(b));
        Assert.assertEquals(1073741821, b);

    }

    // TODO I do not understand this test
    @Test
    public void test_DONotUnderstand() {
        // Range of shift operators for int
        // You can shift only in interval from 0 - 31 inclusive.
        // It means only five lowest bits of right operand is used.
        // ex: left << ( right & 0x1f)
        int a = 10;
        int right1 = 34;
        Assert.assertEquals(2, right1 & 0x1f);
        Assert.assertEquals(a << 2, a << right1);
        int right2 = -3;
        Assert.assertEquals(29, right2 & 0x1f);
        Assert.assertEquals(a << 29, a << right2);

    }

    // TODO I do not understand this test
    @Test
    public void test_IDoNotUnderstand2() {
        // Range of shift operators for long
        // You can shift only in interval from 0 - 63 inclusive.
        // It means only six lowest bits of right operand is used.
        // ex: left << ( right & 0x3f)
        long a = 10;
        long right = 65;
        Assert.assertEquals(1, right & 0x3f);
        Assert.assertEquals(a << 1, a << right);
    }

    private int parseInt(String str) {
        str = str.replaceAll(" ", "");
        return Integer.parseUnsignedInt(str, 2);
    }
    
    private String toStr(int i) {
        StringBuilder result = new StringBuilder(Integer.toBinaryString(i));
        int missing = 32 - result.length();
        for (int j = 0; j < missing; j++) {
            result.insert(0, "0");
        }
        for (int j = 8; j < result.length(); j += 9) {
            result.insert(j, " ");
        }
        return result.toString();
    }

}
