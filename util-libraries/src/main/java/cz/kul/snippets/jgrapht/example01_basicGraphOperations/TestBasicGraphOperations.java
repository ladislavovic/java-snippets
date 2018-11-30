package cz.kul.snippets.jgrapht.example01_basicGraphOperations;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
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
            String edgeSource = g.getEdgeTarget(g.getEdge(j, m));
            assertEquals(m, edgeSource);
        }
        
        // edgesOf() - returns all edges which start or ends on the given vertex
        {
            Set<DefaultEdge> edges = g.edgesOf(s);
            HashSet<Object> expected = new HashSet<>();
            expected.add(g.getEdge(j, s));
            expected.add(g.getEdge(m, s));
            assertEquals(expected, edges);
        }
        
    }
    
}
