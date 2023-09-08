package cz.kul.snippets.mxgraph.example02_cross_graph_renderer;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import cz.kul.snippets.mxgraph.CrossMxGraph;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainLoadGraphFromFile {

	public static void main(String[] args) throws Exception {

		mxGraph graph = new CrossMxGraph();
		graph.setHtmlLabels(true);

		// put styles
		putStyles(graph);

		// Load xml
		URI uri = MainLoadGraphFromFile.class.getResource("/mxgraph/odf-graph.xml").toURI();
//		URI uri = MainLoadGraphFromFile.class.getResource("/mxgraph/rack-graph.xml").toURI();
		Path path = Paths.get(uri);
		byte[] bytes = Files.readAllBytes(path);
		String xml = new String(bytes, StandardCharsets.UTF_8);

		// Create graph
		Document document = mxXmlUtils.parseXml(xml);
		mxCodec codec = new mxCodec(document);
		codec.decode(document.getDocumentElement(), graph.getModel());

		// draw graph to png file
//		GraphRendererSnippets.renderGraph(graph);

		BufferedImage bufferedImage = new GraphRendererCROSS().renderGraph(graph);
		ImageIO.write(bufferedImage, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets-xml.png"));
	}

	private static void putStyles(mxGraph graph) {
		Map<String, Map<String, Object>> styles = new HashMap<>();

		HashMap<String, Object> p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("verticalAlign", "top");
		p1.put("verticalLabelPosition", "bottom");
		p1.put("movable", 0);
		p1.put("shape", "rectangle");
		p1.put("editable", 0);
		p1.put("spacingTop", 5);
		p1.put("selectable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "#0000FF");
		p1.put("foldable", 0);
		styles.put("RACK_SCHEMA_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("movable", 0);
		p1.put("shape", "rectangle");
		p1.put("resizable", 0);
		p1.put("editable", 0);
		p1.put("selectable", 0);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "none");
		p1.put("foldable", 0);
		styles.put("MASTER_WRAPPER_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("overflow", "hidden");
		p1.put("shape", "rectangle");
		p1.put("resizable", 0);
		p1.put("labelPosition", "center");
		p1.put("editable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "#22B14C");
		p1.put("foldable", 0);
		styles.put("RACK_SCHEMA_CHILD_STYLE190", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("overflow", "hidden");
		p1.put("shape", "rectangle");
		p1.put("resizable", 0);
		p1.put("labelPosition", "center");
		p1.put("editable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "#22B14C");
		p1.put("foldable", 0);
		styles.put("RACK_SCHEMA_CHILD_STYLE274", p1);

		p1 = new HashMap<>();
		p1.put("strokeWidth", 1);
		p1.put("shape", "line");
		p1.put("labelPosition", "left");
		p1.put("verticalLabelPosition", "bottom"); // TODO added manualy!
		p1.put("selectable", 0);
		p1.put("strokeColor", "#0000FF");
		styles.put("RACK_U_SCHEMA_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("verticalAlign", "top");
		p1.put("verticalLabelPosition", "bottom");
		p1.put("movable", 0);
		p1.put("shape", "rectangle");
		p1.put("editable", 0);
		p1.put("spacingTop", 5);
		p1.put("selectable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "#0000FF");
		p1.put("foldable", 0);
		styles.put("SHELF_SCHEMA_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("verticalAlign", "middle");
		p1.put("verticalLabelPosition", "middle");
		p1.put("shape", "rectangle");
		p1.put("resizable", 0);
		p1.put("editable", 0);
		p1.put("align", "center");
		p1.put("fillColor", "#EEEEEE");
		p1.put("spacing", 0);
		p1.put("overflow", "hidden");
		p1.put("rounded", false);
		p1.put("rectangle", "#66ffcc");
		p1.put("fontSize", 12);
		p1.put("foldable", 0);
		styles.put("ROOM_SCHEMA_CHILD_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none") ;
		p1.put("overflow", "hidden") ;
		p1.put("shape", "rectangle") ;
		p1.put("resizable", 0);
		p1.put("labelPosition", "center") ;
		p1.put("editable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("foldable", 0);
		styles.put("SHELF_SCHEMA_CHILD_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("shape", "label");
		p1.put("resizable", 0);
		p1.put("editable", 0);
		p1.put("selectable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "none");
		p1.put("foldable", 0);
		styles.put("centralLable", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("overflow", "hidden");
		p1.put("shape", "rectangle");
		p1.put("resizable", 0);
		p1.put("labelPosition", "center");
		p1.put("editable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("foldable", 0);
		styles.put("RACK_SCHEMA_CHILD_STYLE", p1);

		p1 = new HashMap<>();
		p1.put("fillColor", "none");
		p1.put("verticalAlign", "top");
		p1.put("verticalLabelPosition", "bottom");
		p1.put("movable", 0);
		p1.put("shape", "rectangle");
		p1.put("editable", 0);
		p1.put("spacingTop", 5);
		p1.put("selectable", 0);
		p1.put("rounded", false);
		p1.put("fontSize", 12);
		p1.put("strokeColor", "#0000FF");
		p1.put("foldable", 0);
		styles.put("ROOM_SCHEMA_STYLE", p1);

		for (Map.Entry<String, Map<String, Object>> stylestheet : styles.entrySet()) {
			graph.getStylesheet().putCellStyle(stylestheet.getKey(), stylestheet.getValue());
		}
	}

}
