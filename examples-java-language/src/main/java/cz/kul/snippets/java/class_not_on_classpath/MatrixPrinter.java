package cz.kul.snippets.java.class_not_on_classpath;

import cern.colt.matrix.DoubleMatrix2D;

public class MatrixPrinter {

	public String print(DoubleMatrix2D matrix) {
		try {
			Class.forName("cern.colt.matrix.DoubleMatrix2D");
			return "foo";
		} catch (ClassNotFoundException t) {
			throw new RuntimeException(t.getMessage(), t);
		}
	}

}
