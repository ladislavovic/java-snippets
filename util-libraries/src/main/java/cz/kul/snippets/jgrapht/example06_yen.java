package cz.kul.snippets.jgrapht;

import cz.kul.snippets.jgrapht.example04_Benchmarks.customYen.YenShortestPathsIterator_Custom;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenShortestPathIterator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultGraphType;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.junit.Test;

import java.util.Objects;

public class example06_yen {

	public static class TheEdge {
		private long id;

		public TheEdge() {
		}

		public TheEdge(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "TheEdge{" +
					"id=" + id +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			TheEdge theEdge = (TheEdge) o;
			return id == theEdge.id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
	}

	@Test
	public void test() {
		Graph<Integer, TheEdge> g = GraphTypeBuilder
				.<Integer, TheEdge>undirected()
				.weighted(true)
				.allowingSelfLoops(true)
				.edgeClass(TheEdge.class)
				.vertexClass(Integer.class)
				.allowingMultipleEdges(true)
				.buildGraph();

		g = new GraphBuilder<>(g)
				.addVertex(100)
				.addVertex(200)
				.addEdge(100, 200, new TheEdge(1))
				.addEdge(100, 200, new TheEdge(2))
				.buildAsUnmodifiable();


		YenShortestPathIterator<Integer, TheEdge> iter = new YenShortestPathIterator<>(g, 100, 200);

		int i = 0;
		for (; iter.hasNext(); i++) {
			if (i > 10) {
				break;
			}
			iter.next();
		}
		System.out.println("i:" + i);
	}




}
