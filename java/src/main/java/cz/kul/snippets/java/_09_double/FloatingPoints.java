/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._09_double;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.junit.Assert;

/**
 * Floating points representation is described in IEEE 754. It is good
 * to know how it works.
 * 
 * It is called "floating", because binary point "float" in mantisa.
 *
 * @author kulhalad
 */
public class FloatingPoints {
    
    public static void main(String args[]) {
        basics();
        exactRepresentation();   
    }
    
    private static void exactRepresentation() {
        /* 
        1. Ne v�echny racion�ln� ��sla lze vyj�d�it kone�n�m desetinn�m rozovjem.
        2. Ne v�echny ��sla vyj�d�en� kone�n�m desetinn�m rozvojem lze vyj�d�it
           kone�n�m bin�rn�m rozvojem.
        3. V�echny ��sla vyj�d�ena kone�n�m bin�rn�m rozvojem lze vyj�d�it kone�n�m
           desetinn�m rozvojem.
        4. V�echny cel� ��sla maj� ve floating point p�esnou reprezentaci
        
        Podrobn�j�� vysv�tlen�:
        1. M�jme zlomek a/b. Pokud existuje n takov�, �e (a/b)*10^n je cel� ��slo,
        pak jde ��slo vyj�d�it kone�n�m desetinn�m rozvojem.
        P��klad1: pro 11/500 existuje ��sl 3: (11/500)*10^3 = 11000/500 = 22.
        P��klad2: pro 1/3 takov� n nen�. M��eme n�sobit jak dlouho chceme, ale
        trojky ve jmenovateli se nezbav�me.
        
        2. Prvno�initel� 10 jsou 2 a 5. Ve jmenovateli se zbav�me jenom takov�ch
        ��sel, kter� maj� taky prvno�initele 2 a 5. P��klady:
        1/2 - prvo�initele: 2 => kone�n� des rozvoj (0.5)
        1/3 - prvo�initele: 3 => NEkone�n� des rozvoj
        1/4 - prvo�initele: 2, 2 => kone�n� des rozvoj (0.25)
        1/5 - prvo�initele: 5 => kone�n� des rozvoj (0.2)
        1/6 - prvo�initele: 2, 3 => NEkone�n� des rozvoj
        
        3. Toto m��eme zobecnit na v�echny ��seln� z�klady: kone�n�m rozvojem lze
        vyj�d�it pouze ��sla, jejich� prvo�initele jmenovatele jsou podmno�ina
        prvo�initel� ��seln� b�ze.
        Tak�e nap��klad 1/3 lze vyj�d�it kone�n�m rozovjem v soustav�ch o z�kladu
        3, 6, 9, 12, ...
        
        4. Prvo�initel ��sla 2 je 2. Bin�rn� ��sla lze tedy vyj�d�it kone�n�m
        rozvojem, jen pokud je jmenovatel mocnina 2.
        Desetinn� ��sla m��ou m�t ve jmenovateli 2 a 5, bin�rn� ��sla s kone�n�m
        rozvojem jsou tedy podmno�inou desetinn�ch ��sel s kone�n�m rozvojem.
        */
        
        Assert.assertTrue("0.1 has factors 2 and 5", new BigDecimal(0.1).compareTo(new BigDecimal("0.1")) != 0);
        Assert.assertTrue("0.2 has factor 5", new BigDecimal(0.2).compareTo(new BigDecimal("0.2")) != 0);
        Assert.assertTrue("0.5 has factor 2", new BigDecimal(0.5).compareTo(new BigDecimal("0.5")) == 0);
        Assert.assertTrue("5 has factor 1", new BigDecimal(5.0).compareTo(new BigDecimal("5.0")) == 0);
    }
    
    private static void basics() {
        // 32b floating point number is according to IEEE 754 represented this way:
        //
        // |S|__Exp___|__________Man__________|
        //
        // S   - Sign Big (1 bit)
        // Exp - Exponent (8 bits)
        // Man - Mantisa  (23 bits)
        //
        // The value of the floating point number is:
        //   value = (+ or -) 1.(mantisa) * 2^(exponent)        
        //
        // This example convert 4.6 to floating point representation
        float f = 4.6f;
        // 1. First we getAppender sign bit. It is 0 if the number is >= 0, 1 for
        //    negative numbers
        Assert.assertEquals("0", getSignPart(f));
        // 2. As second we convert number into normalised form - number
        //    must be in the form 1.xxx. We convert it this way:
        //      4.6 / 2    = 2.3 
        //      2.3 / 2    = 1.15 OK, this is normalised form
        //    4.6 == 1.15 * 2^2
        //    So now we have normalised number and also exponent - 2.
        //
        // 3. Now we create exponent. We have 8 bits for it, that is 256 values.
        //    The values 0-127 are for negative values, 128-255 for positive.
        //    So our exponend is 127 + 2 = 129
        Assert.assertEquals("10000001", getExponentPart(f));
        // 4. Now we create mantisa. We convert 1.15 into decimal form. The 1
        //    at the beginning is not converted, because it is always in
        //    normalised form. So we convert 0.15 only.
        //    It is 00100110011001100110011
        Assert.assertEquals("00100110011001100110011", getMantisaPart(f));        
    }
    
    private static String getSignPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 31, 32);
    }
    
    private static String getExponentPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 23, 31);
    } 
    
    private static String getMantisaPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 0, 23);
    }
    
    private static String toBinary(int num, int from, int to) {
        StringBuilder result = new StringBuilder();
        int mask = 1;
        for (int i = from; i < to; i++) {
            int tmp = num >>> i;
            int bit = tmp & mask;
            result.insert(0, bit);
        }
        return result.toString();        
    }
    
    private static String prettyPrint(float f) {
        return "|" + getSignPart(f) + "|" + getExponentPart(f) + "|" + getMantisaPart(f) + "|";
    }
    
}
