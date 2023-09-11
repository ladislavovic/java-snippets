package cz.kul.snippets.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class CrossMxGraph extends mxGraph {

	@Override
	public String convertValueToString(Object o) {
		if (o instanceof mxCell) {
			String label = ((mxCell) o).getAttribute("iw_vertex_label");
			if (label != null && label.length() > 10) {
				System.out.println(label);
			}
			return label != null ? label : "";
		}
		return "";
	}

}