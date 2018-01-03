package cz.kul.snippets.mockito._1_mockcreating;

public class Foo {

    private String name = "";

    private String surname = "";

    private int age;

    public Foo() {
        System.out.println("Constructor Person()");
    }

    public String strMethod() {
        System.out.println("strMethod()");
        return "STR";
    }

    public String strMethod2() {
        System.out.println("strMethod2()");
        return "STR2";
    }

    public String methodWithArg(String str) {
        return str;
    }

    public int intMethod() {
        System.out.println("intMethod()");
        return 10;
    }

    public int intMethod2() {
        System.out.println("intMethod2()");
        return 20;
    }

    public boolean booleanMethod() {
        System.out.println("booleanMethod()");
        return true;
    }

    public boolean isOlderThan(int anotherAge) {
        return age > anotherAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        System.out.println("getAge() real impl");
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
