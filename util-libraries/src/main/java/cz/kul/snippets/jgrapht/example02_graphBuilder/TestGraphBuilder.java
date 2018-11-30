package cz.kul.snippets.jgrapht.example02_graphBuilder;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Test;

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
        Graph<Integer, DefaultEdge> empty = GraphTypeBuilder.<Integer, DefaultEdge>directed()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .edgeClass(DefaultEdge.class)
                .weighted(false)
                .buildGraph();

        Graph<Integer, DefaultEdge> filled = new GraphBuilder<>(empty)
                .addEdgeChain(1, 2, 3, 4, 1)
                .addEdge(2, 4)
                .addEdge(3, 5)
                .buildAsUnmodifiable();
        
        assertEquals(5, filled.vertexSet().size());
    }
    
    
    
}
