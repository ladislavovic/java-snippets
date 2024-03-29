package cz.kul.snippets.java.sealed_classes;

import cz.kul.snippets.java.sealed_interface.SealedInterfaces;

public class SealedClasses
{
    // The permits list can contain:
    //  * sealed interfaces
    //  * non-sealed interfaces
    //  * final classes
    public static sealed class Vehicle permits Car, Mazda
    {
        public int getMaxLoadInKilograms() {
            return 0;
        }
    }

    // * Extending interface can permit other types
    // * There must be something in "permits" section. Empty "permits" section is not allowed.
    public static sealed class Car extends Vehicle permits Saloon
    {}

    public static non-sealed class Saloon extends Car
    {}

    public static class VWPassat implements SealedInterfaces.Saloon
    {}

    public static final class Mazda extends Vehicle
    {}

}
