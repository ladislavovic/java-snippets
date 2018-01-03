package cz.kul.snippets.java._06_generics;

public class Pair<E> {
    
    private E one;
    private E another;

    public Pair(E one, E another){
        this.one = one;
        this.another = another;
    }

    public E getOne() {
        return one;
    }

    public void setOne(E one) {
        this.one = one;
    }

    public E getAnother() {
        return another;
    }

    public void setAnother(E another) {
        this.another = another;
    }
    
    @Override
    public String toString() {
        return one.toString() + ", " + another.toString();
    }
    
}
