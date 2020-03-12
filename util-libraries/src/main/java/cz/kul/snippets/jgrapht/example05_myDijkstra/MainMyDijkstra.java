package cz.kul.snippets.jgrapht.example05_myDijkstra;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class MainMyDijkstra {


    public static void main(String[] args) {
        final int MATRIX_GRAPH_SIZE = 100;
        GraphData matrixGraph = Utils.createMatrixGraph(MATRIX_GRAPH_SIZE, MATRIX_GRAPH_SIZE);
        long upperLeftCorner = Utils.matrixCoordinatesToLong(0, 0, MATRIX_GRAPH_SIZE);
        long lowerRightCorner = Utils.matrixCoordinatesToLong(MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE);


        long start = System.currentTimeMillis();
        ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg = new DijkstraAlgorithm<>(matrixGraph.graph);
        GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(upperLeftCorner, lowerRightCorner);
        long end = System.currentTimeMillis();
        System.out.println(String.format("MyDijkstra result: Size: %s, weight: %s", path.getLength(), path.getWeight()));
        System.out.println("Time: " + (end - start));
        
        
        start = System.currentTimeMillis();
        alg = new DijkstraShortestPath(matrixGraph.graph);
        path = alg.getPath(upperLeftCorner, lowerRightCorner);
        end = System.currentTimeMillis();
        System.out.println(String.format("Library Dijkstra result: Size: %s, weight: %s", path.getLength(), path.getWeight()));
        System.out.println("Time: " + (end - start));
    }

}
