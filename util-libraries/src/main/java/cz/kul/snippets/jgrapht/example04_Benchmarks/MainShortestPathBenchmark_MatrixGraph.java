package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class MainShortestPathBenchmark_MatrixGraph {

    enum Alg {DIJKSTRA, BI_DIJKSTRA, DELTA}
    
    public static void main(String[] arg) {
        final int MATRIX_GRAPH_SIZE = 2000;
        final Alg ALG = Alg.DELTA;
//        final Alg ALG = Alg.DIJKSTRA;

        GraphData graphData = Utils.createMatrixGraph(MATRIX_GRAPH_SIZE, MATRIX_GRAPH_SIZE);
        System.out.println(String.format(
                "Matrix graph created. Size: %,d x %,d, nodes: %,d, edges: %,d",
                MATRIX_GRAPH_SIZE,
                MATRIX_GRAPH_SIZE,
                graphData.vertexCount,
                graphData.graph.edgeSet().size()));

        long upperLeftCorner = Utils.matrixCoordinatesToLong(0, 0, MATRIX_GRAPH_SIZE);
        long lowerRightCorner = Utils.matrixCoordinatesToLong(MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE);

        long start = System.currentTimeMillis();
        ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg;
        if (ALG == Alg.DIJKSTRA) {
            alg = new DijkstraShortestPath<>(graphData.graph);
        } else if (ALG == Alg.BI_DIJKSTRA) {
            alg = new BidirectionalDijkstraShortestPath<>(graphData.graph);
        } else if (ALG == Alg.DELTA) {
            alg = new DeltaSteppingShortestPath<>(graphData.graph, 4);
        } else {
            throw new RuntimeException("Ünknown algorithm");
        }
        GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(upperLeftCorner, lowerRightCorner);
        long end = System.currentTimeMillis();
        System.out.println(String.format("The path path found in %,d ms", end - start));
    }


}
