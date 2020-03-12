package cz.kul.snippets.javax_measure.example02_customUnitSystem;

import org.junit.Test;
import si.uom.SI;
import systems.uom.quantity.Information;
import systems.uom.quantity.InformationRate;
import tec.uom.se.AbstractSystemOfUnits;
import tec.uom.se.format.UnitStyle;
import tec.uom.se.unit.BaseUnit;
import tec.uom.se.unit.ProductUnit;
import tec.uom.se.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import javax.measure.spi.ServiceProvider;
import javax.measure.spi.UnitFormatService;

import static org.junit.Assert.assertTrue;

public class TestCustomUnitSystem {
    
    static class CustomUnitSystem extends AbstractSystemOfUnits {

        protected CustomUnitSystem() {
        }
        
        private static final CustomUnitSystem INSTANCE = new CustomUnitSystem();
        
        public static CustomUnitSystem getInstance() {
            return INSTANCE;
        }

        @Override
        public String getName() {
            return "custom";
        }

        public static final Unit<Information> BIT = addUnit(
                new BaseUnit<>("b"),
                "b",
                "bit",
                Information.class);
        
        public static final Unit<Information> BYTE = addUnit(
                BIT.multiply(8.0),
                "B",
                "byte",
                Information.class);
        
        public static final Unit<InformationRate> BIT_PER_SECOND = addUnit(
                BIT.divide(SI.SECOND).asType(InformationRate.class),
                "bps",
                "bit per second",
                InformationRate.class);

        private static <U extends Unit<?>> U addUnit(
                U unit,
                String symbol,
                String name,
                Class<? extends Quantity<?>> type) {
            unit = Helper.addUnit(INSTANCE.units, unit, name, symbol, UnitStyle.SYMBOL_AND_LABEL);
            if (type != null) {
                INSTANCE.quantityToUnit.put(type, unit);
            }
            return unit;
        }

    }
    
    @Test
    public void testParsing() {
        UnitFormatService unitFormatService = ServiceProvider.current().getUnitFormatService();
        UnitFormat format = unitFormatService.getUnitFormat();
        
        CustomUnitSystem.getInstance();

        format.parse("b").asType(Information.class);
        format.parse("bps").asType(InformationRate.class);
    }
    
}
