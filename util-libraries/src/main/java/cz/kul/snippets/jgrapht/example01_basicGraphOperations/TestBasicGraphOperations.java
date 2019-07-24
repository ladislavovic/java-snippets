package cz.kul.snippets.jgrapht.example01_basicGraphOperations;

import com.google.common.collect.Sets;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestBasicGraphOperations {
    
    @Test
    public void basicGraphOperatons() {
        Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        String j = "Jane";
        String m = "Monica";
        String s = "Susan";

        g.addVertex(j);
        g.addVertex(m);
        g.addVertex(s);
        
        g.addEdge(j, m);
        g.addEdge(j, s);
        g.addEdge(m, s);
        
        // getEdgeSource() - returns source vertex of the edge
        {
            String edgeSource = g.getEdgeSource(g.getEdge(j, m));
            assertEquals(j, edgeSource);
        }
        
        // getEdgeTarget() - returns target vertex of the edge
        {
            String edgeTarget = g.getEdgeTarget(g.getEdge(j, m));
            assertEquals(m, edgeTarget);
        }
        
        // edgesOf() - returns all edges which start or ends on the given vertex
        {
            Set<DefaultEdge> edges = g.edgesOf(s);
            assertEquals(Sets.newHashSet(g.getEdge(j, s), g.getEdge(m, s)), edges);
        }
        
    }
    
    @Test
    public void undirectedGraphAndEdgeStartAndEnd() {
        Graph<Integer, String> graph = GraphTypeBuilder.<Integer, String>undirected()
                .allowingMultipleEdges(true)
                .allowingSelfLoops(false)
                .edgeClass(String.class)
                .weighted(false)
                .buildGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2, "12");
        graph.addEdge(2, 1, "21");

        // In undirected graph the methods edgesOf(), incomingEdgesOf() and outgoingEdgesOf() are
        // identical. They returns all edges which are on the node, they do not distinguish
        // if the edge is incomming or outgoing
        Assert.assertEquals(2, graph.edgesOf(1).size());
        Assert.assertEquals(2, graph.incomingEdgesOf(1).size());
        Assert.assertEquals(2, graph.outgoingEdgesOf(1).size());
        Assert.assertEquals(2, graph.incomingEdgesOf(2).size());
        Assert.assertEquals(2, graph.outgoingEdgesOf(2).size());
        
        // But the information which vertex is end and which is end is still
        // present. You can get it by getEdgeSource() method.
        Assert.assertEquals(1, (int) graph.getEdgeSource("12"));
        Assert.assertEquals(2, (int) graph.getEdgeTarget("12"));
    }
    
}
