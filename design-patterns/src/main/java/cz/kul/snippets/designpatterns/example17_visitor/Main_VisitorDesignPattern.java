package cz.kul.snippets.designpatterns.example17_visitor;

import static org.junit.Assert.assertEquals;

/**
 * 
 * 
 */
public class Main_VisitorDesignPattern {

    public static void main(String args[]) {
        Matrix matrix = new Matrix(4, 3);
        MatrixAddition matrixAddition = new MatrixAddition(10.0);
        matrix.accept(matrixAddition);
        assertEquals(10.0, matrix.get(0, 0), 0.0001);
    }

}

class Matrix {
    double[][] numbers;

    public Matrix(int width, int height) {
        numbers = new double[width][height];
    }

    public double get(int x, int y) {
        return numbers[x][y];
    }

    public void set(double num, int x, int y) {
        numbers[x][y] = num;
    }

    public int getWidth() {
        return numbers.length;
    }

    public int getHeight() {
        return numbers[0].length;
    }

    public void accept(MatrixVisitor visitor) {
        visitor.preVisit(this);
        visitor.visit(this);
        visitor.postVisit(this);
    }
}

interface MatrixVisitor {

    default void preVisit(Matrix m) {
    }

    default void visit(Matrix m) {
    }

    default void postVisit(Matrix m) {
    }

}

class MatrixAddition implements MatrixVisitor {

    private double num;

    public MatrixAddition(double num) {
        this.num = num;
    }

    @Override
    public void visit(Matrix m) {
        for (int i = 0; i < m.getWidth(); i++) {
            for (int j = 0; j < m.getHeight(); j++) {
                double newVal = m.get(i, j) + num;
                m.set(newVal, i, j);
            }
        }
    }

}