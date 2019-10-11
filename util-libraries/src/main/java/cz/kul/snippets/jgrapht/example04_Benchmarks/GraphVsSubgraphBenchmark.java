package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Benchmark results:
 *   MATRIX_GRAPH_SIZE = 10:   graph: 10 ms,   subgraph 7 ms:    70 %
 *   MATRIX_GRAPH_SIZE = 100:  graph: 60 ms,   subgraph 82 ms:   136 %
 *   MATRIX_GRAPH_SIZE = 1000: graph: 1300 ms, subgraph 2540 ms: 195 %
 */
public class GraphVsSubgraphBenchmark {

    public static void main(String[] arg) {
        int MATRIX_GRAPH_SIZE = 1000;

        // create a graph
        GraphData graphData = Utils.createMatrixGraph(MATRIX_GRAPH_SIZE, MATRIX_GRAPH_SIZE);
        long startVertex = Utils.matrixCoordinatesToLong(0, 0, MATRIX_GRAPH_SIZE);
        long endVertex = Utils.matrixCoordinatesToLong(MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE);

        // create a subgraph
        int X = (MATRIX_GRAPH_SIZE * MATRIX_GRAPH_SIZE) / 3;
        Set<DefaultWeightedEdge> edges = new HashSet<>(graphData.graph.edgeSet());
        int i = 5;
        for (Iterator<DefaultWeightedEdge> j = edges.iterator(); j.hasNext(); ) {
            j.next();
            if (i++ % X == 0) {
                j.remove();
            }
        }
        i = 5;
        Set<Long> vertexes = new HashSet<>(graphData.graph.vertexSet());
        for (Iterator<Long> j = vertexes.iterator(); j.hasNext(); ) {
            j.next();
            if (i++ % X == 0) {
                j.remove();
            }
        }

        AsSubgraph<Long, DefaultWeightedEdge> subgraph = new AsSubgraph(graphData.graph, vertexes, edges);

        {
            long start = System.currentTimeMillis();
            DijkstraShortestPath<Long, DefaultWeightedEdge> alg = new DijkstraShortestPath<>(graphData.graph);
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(startVertex, endVertex);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Shortest path in graph found in %,d ms", end - start));
        }
        
        {
            long start = System.currentTimeMillis();
            DijkstraShortestPath<Long, DefaultWeightedEdge> alg = new DijkstraShortestPath<>(subgraph);
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(startVertex, endVertex);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Shortest path in subgraph found in %,d ms", end - start));
        }
    }
}
