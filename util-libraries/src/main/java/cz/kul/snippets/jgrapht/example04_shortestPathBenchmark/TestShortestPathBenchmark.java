package cz.kul.snippets.jgrapht.example04_shortestPathBenchmark;

import org.apache.commons.lang3.RandomUtils;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Test;

import java.util.ArrayList;

public class TestShortestPathBenchmark {

    /**
     * Result for Core-i7 at 3.4GHz
     *   dijkstra vertex count ~ 400k (half size 12)  : 0.7 s
     *   dijkstra vertex count ~ 1M (half size 13)    : 2.5 s
     *   dijkstra vertex count ~ 3M (half size 14)    : 8.5 s
     *   
     *   bi-dijkstra vertex count ~ 3M (half size 14) : 0.2 s
     *   bi-dijkstra vertex count ~ 10M (half size 15): 0.3 s
     */
    @Test
    public void shortestPathBenchmark() {
        int EDGES = 3;
        for (int halfSize = 11; halfSize < 14; halfSize++) {
            GraphData graphData = createDiamond(halfSize, EDGES);
            System.out.println("Half size: " + halfSize);
            System.out.println("Vertex count: " + graphData.vertexCount);
            
//            long count = 0;
//            boolean halfReached = false;
//            for (int i = 1; i > 0; ) {
//                count += Math.pow(EDGES, i - 1);
//                i = halfReached ? --i : ++i;
//                halfReached = halfReached || i == halfSize;
//            }
//            System.out.println("Vertex count (should be): " + count);

//            ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg = new DijkstraShortestPath<>(graphData.graph);
            ShortestPathAlgorithm<Long, DefaultWeightedEdge> alg = new BidirectionalDijkstraShortestPath<>(graphData.graph);
            long start = System.currentTimeMillis();
            GraphPath<Long, DefaultWeightedEdge> path = alg.getPath(1L, graphData.vertexCount);
            long end = System.currentTimeMillis();
            System.out.println("Time to find out shortest path: " + (end - start));
            System.out.println();
        }
    }

    /**
     *  Creates graph like this:
     * 
     *           1
     *       /   |  \
     *     2     3     4
     *   / | \ / | \ / | \
     *          ...
     *   \ | / \ | / \ | /
     *     x     y     z
     *      \   |   /
     *         z1
     * 
     * @param halfSize half of the graph "layers"
     * @param edges number of edges from vertex to next layer
     */
    private GraphData createDiamond(int halfSize, int edges) {
        Graph<Long, DefaultWeightedEdge> graph = GraphTypeBuilder
                .<Long, DefaultWeightedEdge>directed()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultWeightedEdge.class)
                .weighted(true)
                .buildGraph();
        
        // Helpers
        VertexGenerator vertexGenerator = new VertexGenerator();
        ArrayList<Long> previousLayer = new ArrayList<>();
        ArrayList<Long> currentLayer;

        // create first vertex
        long firstVertex = vertexGenerator.next();
        graph.addVertex(firstVertex);
        previousLayer.add(firstVertex);

        // create first half
        for (int i = 2; i <= halfSize; i++) {
            currentLayer = new ArrayList<>();
            for (Long source : previousLayer) {
                for (int j = 0; j < edges; j++) {
                    long target = vertexGenerator.next();
                    graph.addVertex(target);
                    DefaultWeightedEdge edge = graph.addEdge(source, target);
                    graph.setEdgeWeight(edge, RandomUtils.nextDouble(0.1, 1));
                    currentLayer.add(target);
                }
            }
            previousLayer = currentLayer;
        }

        // create second half
        for (int i = 1; i <= halfSize-1; i++) {
            currentLayer = new ArrayList<>();
            for (int j = 0; j < previousLayer.size();) {
                ArrayList<Long> sources = new ArrayList<>();
                for (int k = 0; k < edges; k++) {
                    sources.add(previousLayer.get(j++));
                }
                long target = vertexGenerator.next();
                graph.addVertex(target);
                currentLayer.add(target);
                for (Long source : sources) {
                    DefaultWeightedEdge edge = graph.addEdge(source, target);
                    graph.setEdgeWeight(edge, RandomUtils.nextDouble(0.1, 1));
                }
            }
            previousLayer = currentLayer;
        }

        GraphData graphData = new GraphData();
        graphData.graph = graph;
        graphData.vertexCount = vertexGenerator.next() - 1;
        return graphData;
    }
    
    private static class GraphData {
        Graph<Long, DefaultWeightedEdge> graph;
        long vertexCount;
    }
    
    private static class VertexGenerator {
        
        long nextVertex = 1;
        
        long next() {
            if (nextVertex % 1_000_000 == 0) {
                System.out.println("vertex " + nextVertex);
            }
            return nextVertex++;
        }
        
    }
    
}
