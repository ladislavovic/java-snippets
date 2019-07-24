package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.alg.shortestpath.YenShortestPathIterator;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class MainKShortestPathBenchmark {

    enum Alg {YEN, BELLMAN_FORD}

    public static void main(String[] arg) {
        final int MATRIX_GRAPH_SIZE = 500;
        final int K_PATHS = 10;
        final Alg ALG = Alg.YEN;
//        final Alg ALG = Alg.BELLMAN_FORD;

        GraphData graphData = Utils.createMatrixGraph(MATRIX_GRAPH_SIZE, MATRIX_GRAPH_SIZE);
        System.out.println(String.format(
                "Matrix graph created. Size: %,d x %,d, nodes: %,d, edges: %,d",
                MATRIX_GRAPH_SIZE,
                MATRIX_GRAPH_SIZE,
                graphData.vertexCount,
                graphData.graph.edgeSet().size()));

        //
        // Find first path and measure
        //
        long upperLeftCorner = Utils.matrixCoordinatesToLong(0, 0, MATRIX_GRAPH_SIZE);
        long lowerRightCorner = Utils.matrixCoordinatesToLong(MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE);

        if (ALG == Alg.YEN) {
            long start = System.currentTimeMillis();

            YenShortestPathIterator<Long, DefaultWeightedEdge> iter = new YenShortestPathIterator<>(graphData.graph, upperLeftCorner, lowerRightCorner);

            if (iter.hasNext()) {
                GraphPath<Long, DefaultWeightedEdge> path = iter.next();
            }
            long end = System.currentTimeMillis();
            System.out.println(String.format("First path found in %,d ms", end - start));

            //
            // Find other N path and measure
            //
            start = System.currentTimeMillis();
            long startK = System.currentTimeMillis();
            for (int i = K_PATHS; i > 0 && iter.hasNext(); i--) {
                GraphPath<Long, DefaultWeightedEdge> nextPath = iter.next();
                long endK = System.currentTimeMillis();
                System.out.println(String.format("  next path found in %,d (ms)", endK - startK));
                startK = endK;
            }
            end = System.currentTimeMillis();
            System.out.println(String.format("Next %,d paths found in %,d ms", K_PATHS, end - start));
        } else if (ALG == Alg.BELLMAN_FORD) {
            long start = System.currentTimeMillis();
            KShortestSimplePaths<Long, DefaultWeightedEdge> alg = new KShortestSimplePaths<>(graphData.graph);
            List<GraphPath<Long, DefaultWeightedEdge>> paths = alg.getPaths(upperLeftCorner, lowerRightCorner, K_PATHS);
            long end = System.currentTimeMillis();
            System.out.println(String.format("%d shortest paths found in %,d ms", K_PATHS, end - start));
        }
    }
}
