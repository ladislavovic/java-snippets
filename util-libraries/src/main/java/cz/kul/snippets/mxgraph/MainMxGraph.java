package cz.kul.snippets.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Set;

public class MainMxGraph {

	public static void main(String[] args) {

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		mxCell mxCell1 = (mxCell) graph.insertVertex(
				parent,
				null, // id
//				"Hello!", // value
				new MyGraphValue("Foo"), // value
				0,
				0,
				100,
				50);

		mxCell mxCell2 = (mxCell) graph.insertVertex(
				parent,
				null, // id
//				"Hello!", // value
				new MyGraphValue("Foo"), // value
				200,
				30,
				100,
				50);


		//
		// Change the cell label
		//
		Set cells = graph.getView().getStates().keySet();
		for (Object c : cells) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				if (cell.getValue() instanceof MyGraphValue) {
					cell.setValue("Changed!");
				}
			}
		}


		try {
			BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
			ImageIO.write(image, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets.png"));
		} catch (Exception e) {
			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}




	}

	public static class MyGraphValue {

		private String value;

		public MyGraphValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "TO STR: " + value;
		}

	}


}
