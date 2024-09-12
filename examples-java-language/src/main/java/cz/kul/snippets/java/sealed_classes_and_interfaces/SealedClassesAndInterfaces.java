package cz.kul.snippets.java.sealed_classes_and_interfaces;

public class SealedClassesAndInterfaces
{
    // A sealed class or interface can be extended or implemented only by those
    // classes and interfaces permitted to do so.
    //
    // It is useful, when your goal is not to share code in class hierarchy, but
    // model some limited set of things from real world.

    // A typical example
    abstract sealed class A permits B, C {
    }

    final class B extends A
    {
    }

    final class C extends A {

    }

    // When the permitted subclases are declared in the same source file, or even
    // as nested classes, you can omit the permits clause and the java compiler will
    // infer the permitted subclasses:
    abstract sealed class D {}
    final class E extends D {}
    final class F extends D {}

    // The permitted classes MUST use one of these modifiers:
    // * finale - prevent from being extracted
    // * sealed - allow being extracted by defined set of classes
    // * non-sealed - allow being extracted freely
    abstract sealed class G {}
    non-sealed class H extends G {}
    // class I extends G {} - this is not allowed

    // The sealed class MUST have a subclass:
    sealed class I {}
    non-sealed class J extends I {} // If this class would not exist, there was a compillation error on class I

    // You can also seal an interface. With interface the permit clause specify
    // who can implement or extend the interface.
    // It can be final class or sealed/non-sealed interface
    sealed interface K {}
    sealed interface L extends K {}
    non-sealed interface M extends L {} // must be here, because L need to have at leas one child
    final class N implements K {}
    non-sealed class O implements K {}

}
