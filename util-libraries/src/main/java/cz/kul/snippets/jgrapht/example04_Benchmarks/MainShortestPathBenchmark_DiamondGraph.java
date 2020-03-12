package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.alg.shortestpath.DeltaSteppingShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class MainShortestPathBenchmark_DiamondGraph {

    /**
     * Result for Core-i7 at 3.4GHz
     * dijkstra vertex count ~ 400K (half size 12)  : 0.7 s
     * dijkstra vertex count ~ 1M (half size 13)    : 2.5 s
     * dijkstra vertex count ~ 3M (half size 14)    : 18.5 s
     * <p>
     * bi-dijkstra vertex count ~ 3M (half size 14) : 0.2 s
     * bi-dijkstra vertex count ~ 10M (half size 15): 12 s
     */
    public static void main(String[] args) {
        int EDGES = 3;
        for (int halfSize = 15; halfSize < 16; halfSize++) {
            GraphData graphData = Utils.createDiamondGraph(halfSize, EDGES);
            System.out.println("Half size: " + halfSize);
            System.out.println("Vertex count: " + graphData.vertexCount);

//            ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg = new DijkstraShortestPath<>(graphData.graph);
            ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg = new BidirectionalDijkstraShortestPath<>(graphData.graph);
//            DeltaSteppingShortestPath<Long, DefaultWeightedEdge> alg = new DeltaSteppingShortestPath<>(graphData.graph, 10);

            long start = System.currentTimeMillis();
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(1L, graphData.vertexCount);
            long end = System.currentTimeMillis();
            System.out.println("Time to find out shortest path (ms): " + (end - start));
            System.out.println();
        }
    }


}
