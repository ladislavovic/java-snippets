package cz.kul.snippets.jgrapht.example05_myDijkstra;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.GraphWalk;

import java.util.*;

/**
 * Dijkstra algorithm found shortest path from start node to all nodes in the graph.
 * 
 * The algorithm can be slighly modified to found path to one target vertex only. Then
 * you can stop the alogithm earlier, otherwise it works identically.
 */
public class DijkstraAlgorithm<V, E> implements ShortestPathAlgorithm<V, E> {

    private static final double INFINITY = Double.MAX_VALUE;
    
    /** Graph to go through */
    private Graph<V, E> graph;

    /** 
     * Distance to each node in the graph. At the beginning the distance is infiginy to
     * all nodes excepth the start node, it has distance zero.
     */
    private Map<V, Double> d;

    /**
     * Found shortest paths. 
     * Key - the vertex where you want to go
     * Value - the previous vertex on the path to the key vertex
     */
    private Map<V, V> p;

    /**
     * Set of not visited nodes. At the beginning it contains all graph vertex.
     */
    private Set<V> n;

    public DijkstraAlgorithm(Graph<V, E> graph) {
        this.graph = graph;
    }

    @Override
    public GraphPath<V, E> getPath(V start, V end) {
        //
        // Initilization
        //
        d = new HashMap<>();
        p = new HashMap<>();
        n = new HashSet<>();
        graph.vertexSet().forEach(x -> {
            d.put(x, INFINITY);
            n.add(x);
        });
        d.put(start, 0.0);
                
        //
        // Find path
        //
        while (!n.isEmpty()) {
            V closest = findClosest(); // found the closest unvisited node
            
            // This is optimization which breaks the cycle when the
            // end vertex is found.
            // Explanation: findClosest() always returns the closest unvisited vertex
            // to the start vertex. If the end vertex is the closest it means 
            // there can not be any other path through other unvisited node, because
            // all other unvisited nodes are farther than the end node.
            if (closest == end) break;
                        
            n.remove(closest);
            Set<V> neighbours = Graphs.neighborSetOf(graph, closest);
            for (V neighbour : neighbours) {
                E edge = graph.getEdge(closest, neighbour);
                double newWeight = d.get(closest) + graph.getEdgeWeight(edge);
                double currentWeight = d.get(neighbour);
                if (newWeight < currentWeight) {
                    d.put(neighbour, newWeight);
                    p.put(neighbour, closest);
                }
            }
        }
        
        //
        // Create path from "p" hasmap
        //
        GraphPath result = null;
        if (p.get(end) != null) {
            LinkedList<E> edgeList = new LinkedList<>();
            V v = end;
            while (!v.equals(start)) {
                V u = p.get(v);
                edgeList.addFirst(graph.getEdge(u, v));
                v = u;
            }
            double pathWeight = edgeList.stream().mapToDouble(x -> graph.getEdgeWeight(x)).sum();
            result = new GraphWalk(graph, start, end, edgeList, pathWeight);
        }
        return result;
    }

    /**
     * Retuns the closest unvisited node.
     * 
     * In production algorithm this must be optimised to not iterate through the all vertex.
     * Unvisited vertexes must be sorted according to distance.
     */
    private V findClosest() {
        double min = Double.MAX_VALUE;
        V result = null;
        for (V v : n) {
            double vMin = d.get(v);
            if (vMin < min) {
                min = vMin;
                result = v;
            }
        }
        return result;
    }

    @Override
    public double getPathWeight(V v, V v1) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SingleSourcePaths<V, E> getPaths(V v) {
        throw new RuntimeException("Not implemented");
    }
}
