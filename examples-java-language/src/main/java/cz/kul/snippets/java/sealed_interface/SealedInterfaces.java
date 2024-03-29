package cz.kul.snippets.java.sealed_interface;

public class SealedInterfaces
{

    // The permits list can contain:
    //  * sealed interfaces
    //  * non-sealed interfaces
    //  * final classes
    public sealed interface Vehicle permits Car, Mazda {
        default int getMaxLoadInKilograms() {
            return 0;
        }
    }

    // * Extending interface can permit other types
    // * There must be something in "permits" section. Empty "permits" section is not allowed.
    public sealed interface Car extends Vehicle permits Saloon {}

    public non-sealed interface Saloon extends Car {}

    public static class VWPassat implements Saloon {}

    public static final class Mazda implements Vehicle {}

}
