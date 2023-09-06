package cz.kul.snippets.mxgraph;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainLoadGraphFromFile {

	public static void main(String[] args) throws Exception {

		mxGraph graph = new mxGraph();

		// put styles
		putStyles(graph);

		// Load xml
		URI uri = MainLoadGraphFromFile.class.getResource("/mxgraph/rack-graph.xml").toURI();
		Path path = Paths.get(uri);
		byte[] bytes = Files.readAllBytes(path);
		String xml = new String(bytes, StandardCharsets.UTF_8);

		// Create graph
		Document document = mxXmlUtils.parseXml(xml);
		mxCodec codec = new mxCodec(document);
		codec.decode(document.getDocumentElement(), graph.getModel());

		// Set labels
		Set cells = graph.getView().getStates().keySet();
		for (Object c : cells) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				if (cell.getValue() instanceof org.apache.xerces.dom.ElementImpl) { // TODO probably can use cell.getAttribute()
					org.apache.xerces.dom.ElementImpl value = (org.apache.xerces.dom.ElementImpl) cell.getValue();
					String name = value.getAttribute("name");
					cell.setValue(name);
				}
			}
		}

		// Compute the min font size
		final int DEFAULT_FONT_SIZE = 11;
		int minFontSize = DEFAULT_FONT_SIZE;
		Set cells2 = graph.getView().getStates().keySet();
		for (Object c : cells2) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				Map<String, Object> cellStyles = graph.getCellStyle(cell);
				Object cellFontSize = cellStyles.get("fontSize");
				if (cellFontSize instanceof Integer) {
					minFontSize = Math.min(minFontSize, (Integer) cellFontSize);
				}
			}
		}

		// Compute the right scale
		// 12px - desired min font size in image export
		final double MAX_SCALE_FOR_EXPORT = 15;
		double targetScale = Math.min(MAX_SCALE_FOR_EXPORT, 12d / minFontSize); // the final scale value of export image (determine picture resolution)
		double viewScale = graph.getView().getScale();
		double exportScale = targetScale / viewScale;

		// Find the master cell
		mxCell masterObject = null;
		for (Object childCell : graph.getChildCells(graph.getDefaultParent())) {
			String iwMasterAttr = ((mxCell) childCell).getAttribute("iw_master");
			if (Boolean.parseBoolean(iwMasterAttr)) {
				masterObject = (mxCell) childCell;
			}
		}
		Object graphObjectToExport = masterObject != null ? masterObject.getParent() : graph.getDefaultParent();
		mxRectangle boundingBox = graph.getView().getBoundingBox(graph.getView().getState(graphObjectToExport));
//		mxRectangle boundingBox = graph.getView().getBoundingBox(graph.getView().getState(masterObject));

		// Create an image Way1
		try {


			BufferedImage image = mxCellRenderer.createBufferedImage(
					graph,
					new Object[] {graphObjectToExport},
					exportScale,
					Color.WHITE,
					true,
					boundingBox);
			ImageIO.write(image, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets-xml.png"));
		} catch (Exception e) {
			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Create an image Way0
//		try {
//			BufferedImage image = mxCellRenderer.createBufferedImage(
//					graph,
//					null,
//					exportScale,
//					Color.WHITE,
//					true,
//					boundingBox);
//			ImageIO.write(image, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets-xml.png"));
//		} catch (Exception e) {
//			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}



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
