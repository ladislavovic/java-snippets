package cz.kul.snippets.java._08_bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalTester {

    public static void main(String[] args) {
        
        // BigDecimal has a unscaledValue and scale
        //   unscaledValue - just a number. It is BigInteger.
        //   scale - number of numbers after decimal point
        // The value of BigDecimal is unscaledValue * 10^(-scale)
        {
            System.out.println("Structure:");
            BigDecimal a = new BigDecimal(new BigInteger("512"), 2);
            System.out.println("a: " + a);
            System.out.println();
        }
        
        // Scale can be negative. Than it adds zeros on the end.
        {
            System.out.println("Negative scale:");
            BigDecimal b = new BigDecimal(new BigInteger("512"), -2);
            System.out.println("b: " + b);
            System.out.println(b.unscaledValue());
            System.out.println();
        }
        
        // equal method returns true only if both scale and unscaled value are equal.
        // Even though the value of the number is identical.
        //
        // compareTo compares value of the numbers, the scale and unscaledValue can be different.
        {
            System.out.println("Equal and compareTo:");
            BigDecimal a = new BigDecimal(new BigInteger("100"), 0);
            BigDecimal b = new BigDecimal(new BigInteger("1"), -2);
            System.out.println("a: " + a);
            System.out.println("b: " + b);
            System.out.println("a value: " + a.intValueExact());
            System.out.println("b value: " + b.intValueExact());
            System.out.println("equal: " + a.equals(b));
            System.out.println("compare to: " + a.compareTo(b));
            System.out.println();
        }
        
        // Precision is the number of digits in the unscaled value
        {
            System.out.println("Precision: ");
            BigDecimal a = new BigDecimal("100");
            System.out.println(a + " precision: " + a.precision());
            BigDecimal b = new BigDecimal("512.10");
            System.out.println(b + " precision: " + b.precision());
            System.out.println();
        }
        
        // round() method round an unscaledValue according to math context.
        // You can NOT use if for tasks like "round it to two decimals". You 
        // must use setScale() for that.
        {
            System.out.println("Rounding: ");
            BigDecimal a = new BigDecimal(new BigInteger("12345"), 3);
            MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
            System.out.println("rounded a: " + a.round(mc));
            System.out.println();
        }        
        
        // For dividing you usually need to have MathContext. It specify
        // max precision of unscalledValue and rounding mode.
        {
            System.out.println("Dividing: ");
            BigDecimal a = new BigDecimal(new BigInteger("12345"), 0);
            BigDecimal b = new BigDecimal(new BigInteger("1"), 0);
            MathContext mc = new MathContext(50, RoundingMode.HALF_UP);
            BigDecimal c = a.divide(b);
            System.out.println("c: " + c + " unscaledValue: " + c.unscaledValue() + " scale: " + c.scale());
            System.out.println();
        }
        
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
        // used to the rounding errors you get in base 10.
    }
        
}
