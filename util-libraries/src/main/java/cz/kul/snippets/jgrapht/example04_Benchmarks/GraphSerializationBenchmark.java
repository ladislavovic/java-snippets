package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * It serializes graph into memmory and measure data and time. Result for Core-i7 at 3.4GHz:
 * 
 * Matrix dimensions: 1,000 x 1,000
 * Vertex count: 1,000,000
 * Edges count: 1,998,000
 * Time to serialization (ms): 11,890
 * Size of serialized data (KB): 221,493
 */
public class GraphSerializationBenchmark {
    
    public static void main(String[] args) {
        for (int size = 500, step = 250, limit = 2000; size < limit; size += step) {
            System.out.println(String.format("Matrix dimensions: %,d x %,d", size, size));
            GraphData graphData = Utils.createMatrixGraph(size, size);
            System.out.println(String.format("Vertex count: %,d", graphData.vertexCount));
            System.out.println(String.format("Edges count: %,d", graphData.graph.edgeSet().size()));

            long start = System.currentTimeMillis();
            byte[] data = SerializationUtils.serialize((Serializable) graphData.graph);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Time to serialization (ms): %,d", end - start));
            System.out.println(String.format("Size of serialized data (KB): %,d", data.length / 1024));

            System.out.println();
        }
    }
}
