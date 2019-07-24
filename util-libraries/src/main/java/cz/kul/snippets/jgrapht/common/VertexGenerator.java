package cz.kul.snippets.jgrapht.common;

public class VertexGenerator {

    long nextVertex = 1;

    public long next() {
        if (nextVertex % 1_000_000 == 0) {
            System.out.println("vertex " + nextVertex);
        }
        return nextVertex++;
    }

}
