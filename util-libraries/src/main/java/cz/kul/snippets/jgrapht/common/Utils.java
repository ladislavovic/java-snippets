package cz.kul.snippets.jgrapht.common;

import org.apache.commons.lang3.RandomUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import sun.security.provider.certpath.Vertex;

import java.util.ArrayList;

public class Utils {

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
    public static GraphData createDiamondGraph(int halfSize, int edges) {
        Graph<Long, DefaultWeightedEdge> graph = GraphTypeBuilder
                .<Long, DefaultWeightedEdge>undirected()
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
    
    @SuppressWarnings("Duplicates")
    public static GraphData createMatrixGraph(int width, int height) {
        Graph<Long, DefaultWeightedEdge> graph = GraphTypeBuilder
                .<Long, DefaultWeightedEdge>undirected()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultWeightedEdge.class)
                .weighted(true)
                .buildGraph();

        // Create vertex matrix
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                graph.addVertex(row * (long)width + column);
                graph.addVertex(matrixCoordinatesToLong(column, row, width));
            }            
        }

        // Create horizontal edges 
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width - 1; column++) {
                long vertex1 = matrixCoordinatesToLong(column, row, width);
                long vertex2 = matrixCoordinatesToLong(column + 1, row, width);
                DefaultWeightedEdge edge = graph.addEdge(vertex1, vertex2);
                graph.setEdgeWeight(edge, RandomUtils.nextDouble(0.1, 1));
            }
        }
        
        // Create vertical edges 
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                long vertex1 = matrixCoordinatesToLong(column, row, width);
                long vertex2 = matrixCoordinatesToLong(column, row + 1, width);
                DefaultWeightedEdge edge = graph.addEdge(vertex1, vertex2);
                graph.setEdgeWeight(edge, RandomUtils.nextDouble(0.1, 1));
            }
        }
        
        GraphData graphData = new GraphData();
        graphData.graph = graph;
        graphData.vertexCount = graph.vertexSet().size();
        return graphData;
    }
    
    public static long matrixCoordinatesToLong(int column, int row, int width) {
        return row * (long)width + column; 
    }
    
    public static int[] toCoordinates(long coordinates, int width) {
        int row = (int) (coordinates / width);
        int column = (int) (coordinates - row * width);
        return new int [] {column, row};
    }
}
