package cz.kul.snippets.javax_measure.example01_hw;

import com.google.common.collect.Lists;
import org.junit.Test;
import systems.uom.common.USCustomary;
import tec.uom.se.ComparableQuantity;
import tec.uom.se.function.RationalConverter;
import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.MetricPrefix;
import tec.uom.se.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.*;
import javax.measure.spi.ServiceProvider;
import javax.measure.spi.SystemOfUnits;
import javax.measure.spi.UnitFormatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class TestHw {

    /*
    
    Libraries
    * tec.uom:uom-se
      * API implementation, it contains SimpleFormatter
    * systems.uom:systems-common-java8
      * systems: USCustomary, Imperial, ...
    * si.uom:si-quantity
      * some more quantities - torque, density, ...
    * si.uom:si-units-java8
      * two more unit systems - SI, NonSI
    * 
    
    
    Quantity
    * the class (or subclass) represents "fyzikalni velicinu"
    * the instance has also value present
    * there is a lot of subclasses - Lenght, Power, Temperature, ...
    
    Unit
    * the unit of "fyzikalni veliciny"
    * it has Quantity as a parameterized type
    
    Dimension
    * ???
    
    SystemOfUnits service
    * tec.uom.se.unit.Units (uom-se) - common units
    * 
    
    UnitFormat
    * not thread safe 
    
    
    
    todo
    Dimension
    unit.getDimension()
    unit.getSystemUnit()
    unit.getBaseUnits()
    alternateUnit()
    
    
    */
    
    @Test
    public void unitsAndQuantity() {
        // build in units
        Unit<Length> metre = Units.METRE;
        assertEquals("m", metre.getSymbol());
        assertEquals(null, metre.getName());

        // create quantity
        ComparableQuantity<Length> quantity = Quantities.getQuantity(700, metre);
        assertEquals(Units.METRE, quantity.getUnit());
        assertEquals(700, quantity.getValue().intValue());

        // convert unit to another unit
        RationalConverter converter = new RationalConverter(1000, 1);
        Unit<Length> km = metre.transform(converter);
        
        // prepared converters
        Unit<Length> cm = MetricPrefix.CENTI(metre);
        
        // convert quantity to another unit
        Quantity<Length> kmQuantity = quantity.to(km);
        assertEquals(0.7, kmQuantity.getValue().doubleValue(), 0.1);
        
        // create unit from another units
        Unit<Speed> m_per_s = Units.METRE.divide(Units.SECOND).asType(Speed.class);
        
        // getSystemUnit
        // It returns the basic unit for the given quantity. For example m/s for speed,
        // metre for length etc.
        // System unit is unique for the quantity.
        assertEquals(m_per_s.getSystemUnit(), Units.KILOMETRE_PER_HOUR.getSystemUnit());
    }

    @Test
    public void unitFormat() {
        // Get UnitFormat
        // there are these formats available: [EBNF, Local, ASCII, Default]
        UnitFormatService unitFormatService = ServiceProvider.current().getUnitFormatService();
        UnitFormat format = unitFormatService.getUnitFormat();
        UnitFormat ebnfFormat = unitFormatService.getUnitFormat("EBNF");
        UnitFormat asciiformat = unitFormatService.getUnitFormat("ASCII");
        ArrayList<UnitFormat> unitFormats = Lists.newArrayList(format, ebnfFormat, asciiformat);

        // Parse basic unit
        Unit<Length> m = format.parse("m").asType(Length.class);
        assertEquals(Units.METRE, m);
        
        // Parse unit with prefix
        Unit<Length> km = format.parse("km").asType(Length.class);
        assertEquals(MetricPrefix.KILO(Units.METRE), km);
        
        // Parse fraction
        Unit<Pressure> pa = format.parse("Pa").asType(Pressure.class);
        Unit<Pressure> pressureUnit = format.parse("N/m²").asType(Pressure.class);
        assertTrue(pa.isCompatible(pressureUnit));
        
        // Parse multiplication
        Unit<Energy> kWh = format.parse("W*s").asType(Energy.class);
        assertTrue(kWh.isCompatible(Units.JOULE));
        
//        ArrayList<String> units = Lists.newArrayList("W*s");
//        for (UnitFormat uf : unitFormats) {
//            for (String unit : units) {
//                try {
//                    uf.parse(unit);
//                    System.out.println("OK. " + uf + " " + unit);
//                } catch (Exception e) {
//                    ;
//                }
//            }
//        }
    }
    
    @Test
    public void usUnits() {
        UnitFormatService unitFormatService = ServiceProvider.current().getUnitFormatService();
        UnitFormat format = unitFormatService.getUnitFormat();

        // It is important to do it.
        //
        // Because USCustomary adds units into SimpleUnitFormat during class initialization
        // (static constructor). So you must first touch the class to be able parse US
        // units.
        //
        // You also need to have systems-common-java8.jar in classpath, which contains USCustomary
        USCustomary.getInstance();

        Unit<Length> foot = format.parse("ft").asType(Length.class);
        assertTrue(foot.isCompatible(Units.METRE));
    }
    
    
}
