package cz.kul.snippets.java.example07_bigdecimal;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestBigDecimal {

    @Test
    public void testUnscalledValueAndScale() {
        // BigDecimal has a unscaledValue and scale
        //   unscaledValue - just a number. It is BigInteger.
        //   scale - number of numbers after decimal point
        // The value of BigDecimal is unscaledValue * 10^(-scale)
        BigDecimal num = new BigDecimal(new BigInteger("512"), 2);
        assertEquals(new BigDecimal("5.12"), num);
        assertEquals(new BigInteger("512"), num.unscaledValue());
        assertEquals(2, num.scale());
    }

    @Test
    @Ignore
    public void testNegativeScale() {
        // Scale can be negative. Than it "enlarge unscalled value" by adding zeros on the end.
        BigDecimal num = new BigDecimal(new BigInteger("512"), -2);
        assertEquals(new BigDecimal("51200"), num);
    }

    @Test
    public void testEquals() {
        // equal method returns true only if both scale and unscaled value are equal.
        // Even though the final value of the number is identical but scale and unscalled value differs
        // equals return false.
        BigDecimal num1 = new BigDecimal(new BigInteger("100"), 0);
        BigDecimal num2 = new BigDecimal(new BigInteger("1"), -2);
        assertNotEquals(num1, num2);
        assertEquals(num1.intValue(), num2.intValue());
    }

    @Test
    public void testCompareTo() {
        // compareTo compares value of the numbers, the scale and unscaledValue can be different.
        BigDecimal num1 = new BigDecimal(new BigInteger("100"), 0);
        BigDecimal num2 = new BigDecimal(new BigInteger("1"), -2);
        assertEquals(0, num1.compareTo(num2));
    }

    @Test
    public void testPrecision() {
        // Precision is the number of digits in the unscaled value
        BigDecimal b = new BigDecimal(new BigInteger("51210"), 2);
        assertEquals(5, b.precision());
    }

    @Test
    public void testRound() {
        // round() method round an unscaledValue according to math context.
        // You can NOT use if for tasks like "round it to two decimals". You
        // must use setScale() for that.
        BigDecimal num = new BigDecimal(new BigInteger("1239000"), 3);
        MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal(new BigInteger("124"), -1), num.round(mc));
    }

    @Test
    public void testDividing() {
        // For dividing you usually need to have MathContext. It specify
        // max precision of unscalledValue and rounding mode.
        BigDecimal num1 = new BigDecimal(new BigInteger("10"), 0);
        BigDecimal num2 = new BigDecimal(new BigInteger("3"), 0);
        MathContext mc = new MathContext(5, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal(new BigInteger("33333"), 4), num1.divide(num2, mc));
    }

    public static void main(String[] args) {
        // setScale() is very useful method, which can be used for tasks like
        // "round it to two decimals".
        // It retuns numerical equal value but with specified scale.
        {
            System.out.println("setScale(): ");
            BigDecimal a = new BigDecimal(new BigInteger("100"), 0);
            BigDecimal b = a.setScale(2, RoundingMode.HALF_UP);
            System.out.println("a: " + a);
            System.out.println("b: " + b);
        }

        // stripTrailingZeros() method returns all zeros from the end of the unscalled value
        // (not only after decimal point!!)
        {
            System.out.println("stripTrailingZeros(): ");
            BigDecimal a = new BigDecimal("1000.00");
            a = a.stripTrailingZeros();
            System.out.println("a: " + a + " unscalled value: " + a.unscaledValue() + " scale: " + a.scale());
        }

        // When to user BD:
        // Saying that "BigDecimal is an exact way of representing numbers" is misleading. 
        // 1/3 and 1/7 can't be expressed exactly in a base 10 number system (BigDecimal) 
        // or in base 2 number system (float or double). 1/3 could be exactly expressed 
        // in base 3, base 6, base 9, base 12, etc. and 1/7 could be expressed exactly 
        // in base 7, base 14, base 21, etc. 
        // BigDecimal advantages are that it is arbitrary precision and that humans are 
        // used to the rounding errors you getAppender in base 10.
    }

}
