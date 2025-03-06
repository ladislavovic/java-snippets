package cz.kul.snippets.java.bigdecimal;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestBigDecimal
{

    @Test
    public void testUnscalledValueAndScale()
    {
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
    public void testNegativeScale()
    {
        // Scale can be negative. Than it "enlarge unscalled value" by adding zeros on the end.
        BigDecimal num = new BigDecimal(new BigInteger("512"), -2);
        assertEquals(new BigDecimal("51200"), num);
    }

    @Test
    public void testEquals()
    {
        // equal method returns true only if both scale and unscaled value are equal.
        // Even though the final value of the number is identical but scale and unscalled value differs
        // equals return false.
        BigDecimal num1 = new BigDecimal(new BigInteger("100"), 0);
        BigDecimal num2 = new BigDecimal(new BigInteger("1"), -2);
        assertNotEquals(num1, num2);
        assertEquals(num1.intValue(), num2.intValue());
    }

    @Test
    public void testCompareTo()
    {
        // compareTo compares value of the numbers, the scale and unscaledValue can be different.
        BigDecimal num1 = new BigDecimal(new BigInteger("100"), 0);
        BigDecimal num2 = new BigDecimal(new BigInteger("1"), -2);
        assertEquals(0, num1.compareTo(num2));
    }

    @Test
    public void testPrecision()
    {
        // Precision is the number of digits in the unscaled value
        BigDecimal b = new BigDecimal(new BigInteger("51210"), 2);
        assertEquals(5, b.precision());
    }

    @Test
    public void testRound()
    {
        // round() method round an unscaledValue according to math context.
        // You can NOT use if for tasks like "round it to two decimals". You
        // must use setScale() for that.
        //
        // round() also change scale to keep the value (almost) the same
        BigDecimal num = new BigDecimal(new BigInteger("1239000"), 3); // 1239.000

        MathContext mc = new MathContext(3, RoundingMode.HALF_UP);

        assertEquals(
            new BigDecimal(new BigInteger("124"), -1), // 1240
            num.round(mc) // 1240
        );
    }

    @Test
    public void testDividing()
    {
        // For dividing, you usually need to specify target scale and rounding mode

        // The first option how to specify it is by the divided number - the result has the same
        // scale as the divided number:
        assertEquals(
            new BigDecimal("3.33"), // the divided number has scale 2 so the result has also the scale 2
            (new BigDecimal("10.00")).divide(new BigDecimal("3"), RoundingMode.HALF_UP)
        );

        // The second option is to specify the scale explicitly as a method argument
        assertEquals(
            new BigDecimal("3.33333"), // scale argument is 5, so the result has 5 decimal digits
            (new BigDecimal("10.00")).divide(new BigDecimal("3"), 5, RoundingMode.FLOOR)
        );

        // Be careful with MathContext. Because it does not specify scale,
        // but precision - number of digits in unscalled value
        assertEquals(
            // the precision is 1 so the unscalled value has only 1 place.
            // So 1 is not number of decimal places !!!
            new BigDecimal(new BigInteger("2"), -1),
            (new BigDecimal("44")).divide(new BigDecimal("2"), new MathContext(1, RoundingMode.HALF_UP))
        );
        assertEquals(
            // the precision is max number of digits, the scaled value can be shorter
            new BigDecimal(new BigInteger("22"), 0),
            (new BigDecimal("44")).divide(new BigDecimal("2"), new MathContext(8, RoundingMode.HALF_UP))
        );
        assertEquals(
            // the precision is 1 so the unscalled value has only 1 place.
            // So 1 is not number of decimal places !!!
            new BigDecimal(new BigInteger("33333333"), 7),
            (new BigDecimal("10")).divide(new BigDecimal("3"), new MathContext(8, RoundingMode.HALF_UP))
        );

        // There is also a method where you do not specify it: divide(BigDecimal divisor).
        // This method expects the dividing will not need any rounding, so in practise it is not used often.
        // If the rounding occurs the exception is thrown.
        assertEquals(
            new BigDecimal("5.00"),
            (new BigDecimal("10.00")).divide(new BigDecimal("2"))
        );
    }

    @Test
    public void testSetScale()
    {
        // setScale() is very useful method, which can be used for tasks like
        // "round it to two decimals".
        //
        // It returns a BigDecimal with the given scale.
        // If you reduce the scale it must divide the unscalled value, so you can lost precision. So you have to provice RoudingMode.
        BigDecimal a = new BigDecimal(new BigInteger("10035"), 2); // 100.35

        assertEquals(new BigDecimal("100"), a.setScale(0, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("100.4"), a.setScale(1, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("100.35"), a.setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("100.350"), a.setScale(3, RoundingMode.HALF_UP));

        // This is not used often but you can set also a negative scale
        assertEquals(new BigDecimal(new BigInteger("10"), -1), a.setScale(-1, RoundingMode.HALF_UP));
    }

    @Test
    public void testStripTrailingZeros()
    {
        // stripTrailingZeros() method returns all zeros from the end of the unscalled value
        // (not only after decimal point!!)
        System.out.println("stripTrailingZeros(): ");
        BigDecimal a = new BigDecimal("1000.00");
        a = a.stripTrailingZeros();
        System.out.println("a: " + a + " unscalled value: " + a.unscaledValue() + " scale: " + a.scale());
    }

    public static void main(String[] args)
    {
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
