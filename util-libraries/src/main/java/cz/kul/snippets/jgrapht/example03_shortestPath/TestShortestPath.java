package cz.kul.snippets.jgrapht.example03_shortestPath;

import org.apache.commons.lang3.RandomUtils;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestShortestPath {
    
    private Graph<Integer, DefaultEdge> g;
    
    @Before
    public void before() {
        g = GraphTypeBuilder
                .<Integer, DefaultEdge>directed()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultEdge.class)
                .weighted(false)
                .buildGraph();
        g = new GraphBuilder<>(g)
                .addEdgeChain(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .addEdge(2, 4)
                .addEdge(5, 9)
                .buildAsUnmodifiable();
    }
    
    @Test
    public void shortestPathByDijkstra() {
        ShortestPathAlgorithm<Integer, DefaultEdge> alg = new DijkstraShortestPath<>(g);
        GraphPath<Integer, DefaultEdge> path = alg.getPath(1, 9);
        assertEquals(Arrays.asList(1, 2, 4, 5, 9), path.getVertexList());
    }
    
    @Test
    public void shorrtestPath_moreSameLengthPaths_justReturnsOneOfThem() {
        Graph<Integer, DefaultEdge> customGraph = GraphTypeBuilder
                .<Integer, DefaultEdge>directed()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultEdge.class)
                .weighted(false)
                .buildGraph();
        
        /*
            1
           / \
           2 3
           \ /
            4
         */
        customGraph = new GraphBuilder<>(customGraph)
                .addEdge(1, 2)
                .addEdge(1, 3)
                .addEdge(2, 4)
                .addEdge(3, 4)
                .buildAsUnmodifiable();
        
        ShortestPathAlgorithm<Integer, DefaultEdge> alg = new DijkstraShortestPath<>(customGraph);
        GraphPath<Integer, DefaultEdge> path = alg.getPath(1, 4);
        assertEquals(Arrays.asList(1, 2, 4), path.getVertexList());
    }
    
    @Test
    public void kShortestPaths() {
        KShortestPathAlgorithm<Integer, DefaultEdge> alg = new KShortestSimplePaths<>(g);
        List<GraphPath<Integer, DefaultEdge>> paths = alg.getPaths(1, 9, 2);
        assertEquals(Arrays.asList(1, 2, 4, 5, 9), paths.get(0).getVertexList());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 9), paths.get(1).getVertexList());
    }
    
    @Test
    public void notExistingPath() {
        DijkstraShortestPath<Integer, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(g);
        SingleSourcePaths<Integer, DefaultEdge> paths = dijkstraAlg.getPaths(2);
        assertNull(paths.getPath(1));
    }

}
