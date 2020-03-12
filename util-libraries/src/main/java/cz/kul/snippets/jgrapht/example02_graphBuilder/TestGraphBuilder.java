package cz.kul.snippets.jgrapht.example02_graphBuilder;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestGraphBuilder {
    
    @Test
    public void graphTypeBuilder() {
        // You can create graph by this builder, or you can instantiate particular
        // implementation. The builder is only helper class, which choose an implementaion
        // according to given parameters.
        // The preferred way is to use builder, because it less couple your code with
        // particular implementation.
        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder.<Integer, DefaultEdge>directed()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultEdge.class)
                .weighted(false)
                .buildGraph();
        assertEquals(SimpleDirectedGraph.class, graph.getClass());
    }
    
    @Test
    public void graphBuilder() {
        SimpleGraph<Integer, DefaultEdge> baseGraph = new SimpleGraph<>(DefaultEdge.class);
        baseGraph.addVertex(10_000);
        baseGraph.addVertex(20_000);
        baseGraph.addEdge(10_000, 20_000);
        
        // You can create graph by graph builder. Then you must provide base graph.
        SimpleGraph<Integer, DefaultEdge> graph = new GraphBuilder<>(baseGraph)
                .addEdgeChain(30_000, 40_000)
                .build();
        
        // Builded graph contains vertexes and edges of base graph
        Assert.assertTrue(graph.vertexSet().contains(10_000));

        // Vertexes in baseGraph and graph are the same instances
        Integer baseVertex = baseGraph.vertexSet().stream().filter(x -> x.equals(10_000)).findAny().get();
        Integer vertex = graph.vertexSet().stream().filter(x -> x.equals(10_000)).findAny().get();
        Assert.assertTrue(baseVertex == vertex);

        // Edges in baseGraph and graph are the same instances
        DefaultEdge baseEdge = baseGraph.edgesOf(10_000).iterator().next();
        DefaultEdge edge = graph.edgesOf(10_000).iterator().next();
        Assert.assertTrue(baseEdge == edge);
    } 
    
    
    
}
