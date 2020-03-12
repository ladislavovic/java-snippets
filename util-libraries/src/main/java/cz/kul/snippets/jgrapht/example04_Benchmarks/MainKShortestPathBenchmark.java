package cz.kul.snippets.jgrapht.example04_Benchmarks;

import cz.kul.snippets.jgrapht.common.GraphData;
import cz.kul.snippets.jgrapht.common.Utils;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainKShortestPathBenchmark {

    enum Alg {
        YEN, 
        BELLMAN_FORD, 
        YEN_NOT_ITERATIVE,
        _1ST_ONLY_DIJKSTRA,
        _1ST_ONLY_DELTA,
        EPPSTEIN
    }

    public static void main(String[] arg) {

//        final int MATRIX_GRAPH_SIZE = 150;
        final int MATRIX_GRAPH_SIZE = 20;
//        final int MATRIX_GRAPH_SIZE = 5;
        
//        final int K_PATHS = 10;
//        final int K_PATHS = 100;
//        final int K_PATHS = 1000;
        final int K_PATHS = 30000;
        
        final Alg ALG = Alg.YEN;
//        final Alg ALG = Alg.EPPSTEIN;
//        final Alg ALG = Alg.YEN_NOT_ITERATIVE;
//        final Alg ALG = Alg.BELLMAN_FORD;
//        final Alg ALG = Alg._1ST_ONLY_DELTA;
//        final Alg ALG = Alg._1ST_ONLY_DIJKSTRA;

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
//        long startVertex = Utils.matrixCoordinatesToLong(0, 0, MATRIX_GRAPH_SIZE);
        long startVertex = Utils.matrixCoordinatesToLong(10, 10, MATRIX_GRAPH_SIZE);
        
//        long endVertex = Utils.matrixCoordinatesToLong(MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE - 1, MATRIX_GRAPH_SIZE);
//        long endVertex = Utils.matrixCoordinatesToLong(60, 60, MATRIX_GRAPH_SIZE);
        long endVertex = Utils.matrixCoordinatesToLong(15, 15, MATRIX_GRAPH_SIZE);

        if (ALG == Alg.YEN) {
            long start = System.currentTimeMillis();

            YenShortestPathIterator<Long, DefaultWeightedEdge> iter = new YenShortestPathIterator<>(graphData.graph, startVertex, endVertex);

            if (iter.hasNext()) {
                GraphPath<Long, DefaultWeightedEdge> path = iter.next();
                printPath(path, MATRIX_GRAPH_SIZE);
            }
            long end = System.currentTimeMillis();
            System.out.println(String.format("First path found in %,d ms", end - start));

            //
            // Find other N path and measure
            //
            ArrayList<Long> kTimes = new ArrayList<>();
            start = System.currentTimeMillis();
            for (int i = K_PATHS; i > 0 && iter.hasNext(); i--) {
                long startK = System.currentTimeMillis();
                GraphPath<Long, DefaultWeightedEdge> nextPath = iter.next();
                if (i % 1000 == 0) {
                    System.out.println();
                    printPath(nextPath, MATRIX_GRAPH_SIZE);
                }
                long endK = System.currentTimeMillis();
//                System.out.println(String.format("  next path found in %,d (ms)", endK - startK));
                kTimes.add(endK - startK);
            }
            end = System.currentTimeMillis();
            double kAvg = kTimes.stream().mapToLong(Number::longValue).average().getAsDouble();
            System.out.println("K times: " + kTimes);
            System.out.println(String.format("Next %,d paths found in %,d ms, avg %,f", K_PATHS, end - start, kAvg));
        } else if (ALG == Alg.BELLMAN_FORD) {
            long start = System.currentTimeMillis();
            KShortestSimplePaths<Long, DefaultWeightedEdge> alg = new KShortestSimplePaths<>(graphData.graph);
            List<GraphPath<Long, DefaultWeightedEdge>> paths = alg.getPaths(startVertex, endVertex, K_PATHS);
            long end = System.currentTimeMillis();
            System.out.println(String.format("%d shortest paths found in %,d ms", K_PATHS, end - start));
        } else if (ALG == Alg.YEN_NOT_ITERATIVE) {
            long start = System.currentTimeMillis();
            YenKShortestPath<Long, DefaultWeightedEdge> alg = new YenKShortestPath<>(graphData.graph);
            List<GraphPath<Long, DefaultWeightedEdge>> paths = alg.getPaths(startVertex, endVertex, K_PATHS);
            long end = System.currentTimeMillis();
            System.out.println(String.format("%d shortest paths found in %,d ms", K_PATHS, end - start));
        } else if (ALG == Alg._1ST_ONLY_DIJKSTRA) {
            long start = System.currentTimeMillis();
            DijkstraShortestPath<Long, DefaultWeightedEdge> alg = new DijkstraShortestPath<>(graphData.graph);
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(startVertex, endVertex);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Shortest path found in %,d ms", end - start));
        } else if (ALG == Alg._1ST_ONLY_DELTA) {
            long start = System.currentTimeMillis();
            DeltaSteppingShortestPath<Long, DefaultWeightedEdge> alg = new DeltaSteppingShortestPath<>(graphData.graph, 4);
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(startVertex, endVertex);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Shortest path found in %,d ms", end - start));
        } else if (ALG == Alg.EPPSTEIN) {
            long start = System.currentTimeMillis();

            // Convert to directed graph, because Eppstein can work with directed graph only
            Graph<Long, DefaultWeightedEdge> directed = GraphTypeBuilder
                    .<Long, DefaultWeightedEdge>directed()
                    .allowingMultipleEdges(false)
                    .allowingSelfLoops(false)
                    .edgeClass(DefaultWeightedEdge.class)
                    .weighted(true)
                    .buildGraph();
            for (Long v : graphData.graph.vertexSet()) {
                directed.addVertex(v);
            }
            for (DefaultWeightedEdge edge : graphData.graph.edgeSet()) {
                Long s = graphData.graph.getEdgeSource(edge);
                Long t = graphData.graph.getEdgeTarget(edge);
                double w = graphData.graph.getEdgeWeight(edge);
                
                DefaultWeightedEdge oneDirection = directed.addEdge(s, t);
                directed.setEdgeWeight(oneDirection, w);
                
                DefaultWeightedEdge theOtherDirection = directed.addEdge(t, s);
                directed.setEdgeWeight(theOtherDirection, w);
            }
            
            EppsteinShortestPathIterator<Long, DefaultWeightedEdge> iter = new EppsteinShortestPathIterator<>(directed, startVertex, endVertex);

            if (iter.hasNext()) {
                GraphPath<Long, DefaultWeightedEdge> path = iter.next();
            }
            long end = System.currentTimeMillis();
            System.out.println(String.format("First path found in %,d ms", end - start));

            //
            // Find other N path and measure
            //
            ArrayList<Long> kTimes = new ArrayList<>();
            start = System.currentTimeMillis();
            for (int i = K_PATHS; i > 0 && iter.hasNext(); i--) {
                long startK = System.currentTimeMillis();
                GraphPath<Long, DefaultWeightedEdge> nextPath = iter.next();
                long endK = System.currentTimeMillis();
//                System.out.println(String.format("  next path found in %,d (ms)", endK - startK));
                kTimes.add(endK - startK);
            }
            end = System.currentTimeMillis();
            double kAvg = kTimes.stream().mapToLong(Number::longValue).average().getAsDouble();
            System.out.println("K times: " + kTimes);
            System.out.println(String.format("Next %,d paths found in %,d ms, avg %,f", K_PATHS, end - start, kAvg));
        }
    }
    
    public static void printPath(GraphPath<Long, ?> path, int matrixGraphSize) {
        Set<int[]> coordinates = path.getVertexList().stream()
                .map(x -> Utils.toCoordinates(x, matrixGraphSize))
                .collect(Collectors.toSet());
        for (int row = 0; row < matrixGraphSize; row++) {
            for (int column = 0; column < matrixGraphSize; column++) {
                boolean pathVertex = false;
                int[] coordinate = new int[] {column, row};
                for (int[] c : coordinates) {
                    if (Arrays.equals(coordinate, c)) {
                        pathVertex = true;
                        break;
                    }
                }
                if (pathVertex) {
                    System.out.print("x");    
                } else {
                    System.out.print("_");    
                }
                
            }
            System.out.println();
        }        
    }
    
}
