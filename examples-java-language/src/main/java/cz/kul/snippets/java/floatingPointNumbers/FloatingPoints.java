/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.floatingPointNumbers;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Floating points representation is described in IEEE 754. It is good
 * to know how it works.
 * 
 * It is called "floating", because binary point "float" in mantisa.
 *
 * @author kulhalad
 */
public class FloatingPoints {
    
    @Test
    public void basics() {
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

    @Test
    public void exactRepresentation() {
        /* 
        1. Ne všechny racionání čísla lze vyjádřit konečným desetinným rozovjem.
        2. Ne všechny čísla vyjádřené konečným desetinným rozvojem lze vyjádřit
           konečným binárním rozvojem.
        3. Všechny čísla vyjádřena konečným binárním rozvojem lze vyjádřit konečným
           desetinným rozvojem.
        4. Všechny celé čísla mají ve floating point přesnou reprezentaci
        
        Podrobnější vysvětlení předchozích bodu:
        1. Mějme zlomek a/b. Pokud existuje n takové, že (a/b)*10^n je celé číslo,
        pak jde číslo vyjádřit konečným desetinným rozvojem.
        Příklad1: pro 11/500 existuje čísl 3: (11/500)*10^3 = 11000/500 = 22.
        Příklad2: pro 1/3 takové n není. Mužeme násobit jak dlouho chceme, ale
        trojky ve jmenovateli se nezbavíme.
        
        2. Prvnočinitelé 10 jsou 2 a 5. Ve jmenovateli se zbavíme jenom takových
        čísel, která mají taky prvnočinitele 2 a 5. Příklady:
        1/2 - prvočinitele: 2 => konečný des rozvoj (0.5)
        1/3 - prvočinitele: 3 => NEkonečný des rozvoj
        1/4 - prvočinitele: 2, 2 => konečný des rozvoj (0.25)
        1/5 - prvočinitele: 5 => konečný des rozvoj (0.2)
        1/6 - prvočinitele: 2, 3 => NEkonečný des rozvoj
        
        3. Toto mužeme zobecnit na všechny číselné základy: konečným rozvojem lze
        vyjádřit pouze čísla, jejichž prvočinitele jmenovatele jsou podmnožina
        prvočinitelu číselné báze.
        Takže například 1/3 lze vyjádřit konečným rozovjem v soustavách o základu
        3, 6, 9, 12, ...
        
        4. Prvočinitel čísla 2 je 2. Binární čísla lze tedy vyjádřit konečným
        rozvojem, jen pokud je jmenovatel mocnina 2.
        Desetinná čísla mužou mít ve jmenovateli 2 a 5, binární čísla s konečným
        rozvojem jsou tedy podmnožinou desetinných čísel s konečným rozvojem.
        */
        
        Assert.assertTrue("0.1 has factors 2 and 5", new BigDecimal(0.1).compareTo(new BigDecimal("0.1")) != 0);
        Assert.assertTrue("0.2 has factor 5", new BigDecimal(0.2).compareTo(new BigDecimal("0.2")) != 0);
        Assert.assertTrue("0.5 has factor 2", new BigDecimal(0.5).compareTo(new BigDecimal("0.5")) == 0);
        Assert.assertTrue("5 has factor 1", new BigDecimal(5.0).compareTo(new BigDecimal("5.0")) == 0);
    }

    private String getSignPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 31, 32);
    }
    
    private String getExponentPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 23, 31);
    } 
    
    private String getMantisaPart(float f) {
        int a = Float.floatToRawIntBits(f);
        return toBinary(a, 0, 23);
    }
    
    private String toBinary(int num, int from, int to) {
        StringBuilder result = new StringBuilder();
        int mask = 1;
        for (int i = from; i < to; i++) {
            int tmp = num >>> i;
            int bit = tmp & mask;
            result.insert(0, bit);
        }
        return result.toString();        
    }
    
    private String prettyPrint(float f) {
        return "|" + getSignPart(f) + "|" + getExponentPart(f) + "|" + getMantisaPart(f) + "|";
    }
    
}
