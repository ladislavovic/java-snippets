package cz.kul.snippets.mxgraph.example02_cross_graph_renderer;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import org.apache.xerces.dom.ElementImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphRendererSnippets {

	public static void renderGraph(mxGraph graph) {
		// Find the master cell
		mxCell masterObject = null;
//		Object[] childCells = graph.getChildCells(graph.getDefaultParent());
		Set childCells = graph.getView().getStates().keySet();
		for (Object childCell : childCells) {
			String iwMasterAttr = ((mxCell) childCell).getAttribute("iw_master");
			if (Boolean.parseBoolean(iwMasterAttr)) {
				masterObject = (mxCell) childCell;
			}
		}
		Object graphObjectToExport = masterObject != null ? masterObject.getParent() : graph.getDefaultParent();


		// Set labels
		final String VERTEX_LABEL = "iw_vertex_label";
		for (Object c : getAllGraphCells(graph)) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				if (cell.getValue() instanceof ElementImpl) { // TODO probably can use cell.getAttribute()
					ElementImpl value = (ElementImpl) cell.getValue();
					String name = value.getAttribute("name");
//					cell.setValue(name);
					cell.setAttribute(VERTEX_LABEL, name);
				}
			}
		}


		// compute font size for u labels
		Double scaleCoef = getDoubleAttribute(masterObject, "iw_graph_scale");
		Double iwHeight = getDoubleAttribute(masterObject, "iw_u_height");
		Double fontSizeULabels = computeFontSizeForULabels("10", iwHeight, scaleCoef);


		// set font sizes
		final String IW_SLAVE_TAG_NAME = "member";
		final String U_LABEL_TAG_NAME = "uLabel";
		for (Object c : getAllGraphCells(graph)) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				if (cell.getValue() instanceof ElementImpl) {
					ElementImpl value = (ElementImpl) cell.getValue();
					String tagName = value.getTagName();

					Double updatedFontSize = null;
					if (IW_SLAVE_TAG_NAME.equals(tagName)) {
						String label = cell.getAttribute(VERTEX_LABEL);
						mxCellState cellState = graph.getView().getState(cell);
						updatedFontSize = computeFontSizeForSlaveObjects(label, cellState);
					} else if (U_LABEL_TAG_NAME.equals(tagName)) {
						updatedFontSize = fontSizeULabels;
					}

					if (updatedFontSize != null) {
						graph.setCellStyles(mxConstants.STYLE_FONTSIZE, updatedFontSize.toString(), new Object[] {cell});
					}
				}
			}
		}

		graph.refresh();

		// Compute the min font size
		final int DEFAULT_FONT_SIZE = 11;
		double minFontSize = DEFAULT_FONT_SIZE;
		Set cells2 = graph.getView().getStates().keySet();
		for (Object c : cells2) {
			if (c instanceof mxCell) {
				mxCell cell = (mxCell) c;
				Map<String, Object> cellStyles = graph.getCellStyle(cell);
				Double cellFontSize = getFontSize(cellStyles);
				if (cellFontSize != null) {
					minFontSize = Math.min(minFontSize, cellFontSize);
				}
			}
		}

		mxRectangle boundingBox = ((mxCell) graphObjectToExport).getGeometry();




//		double x = 200;
//		double y = 0;
//		double width = 500;
//		double height = 500;
//		mxRectangle mxRectangle = new mxRectangle(x, y, width, height);



		// Compute the right scale
		// 12px - desired min font size in image export
		final double MAX_SCALE_FOR_EXPORT = 15;
		double targetScale = Math.min(MAX_SCALE_FOR_EXPORT, 12d / minFontSize); // the final scale value of export image (determine picture resolution)
		double viewScale = graph.getView().getScale();
		double exportScale = targetScale / viewScale;



		mxRectangle mxRectangle = new mxRectangle(boundingBox);

		// add border
		double border = 30;
		mxRectangle.setX(mxRectangle.getX() - border);
		mxRectangle.setY(mxRectangle.getY() - border);
		mxRectangle.setWidth(mxRectangle.getWidth() + 2 * border);
		mxRectangle.setHeight(mxRectangle.getHeight() + 2 * border);

		// adjust according to scale
		mxRectangle.setX(mxRectangle.getX() * exportScale);
		mxRectangle.setY(mxRectangle.getY() * exportScale);
		mxRectangle.setWidth(mxRectangle.getWidth() * exportScale);
		mxRectangle.setHeight(mxRectangle.getHeight() * exportScale);


		// Create an image Way0
		try {
			BufferedImage image = mxCellRenderer.createBufferedImage(
					graph,
					null,
					exportScale,
//					1,
					Color.WHITE,
					true,
					mxRectangle);
//					null);
			ImageIO.write(image, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets-xml.png"));
		} catch (Exception e) {
			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Create an image Way1
//		try {
//
//
//			BufferedImage image = mxCellRenderer.createBufferedImage(
//					  graph,
//					  new Object[] {graphObjectToExport},
//					  exportScale,
//					  Color.WHITE,
//					  true,
//					  boundingBox);
//			ImageIO.write(image, "PNG", new File("/home/ladislav/tmp/IW/graph-snippets-xml.png"));
//		} catch (Exception e) {
//			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
	}

	private static Double computeFontSizeForULabels(String str, Double uHeight, Double scaleCoef) {

		if (str == null || uHeight == null || scaleCoef == null) {
			return null;
		}

		double MAX_SIZE = 12;

		String fontFamily = mxConstants.DEFAULT_FONTFAMILIES;
		int fontSize = mxConstants.DEFAULT_FONTSIZE;
		int swingFontStyle = Font.PLAIN;

		Font font = new Font(fontFamily, swingFontStyle, fontSize);

		double availableHeight = uHeight * scaleCoef;
		mxRectangle textSize = mxUtils.getSizeForString(str, font, 1.0);
		return Math.min(
				  (mxConstants.DEFAULT_FONTSIZE * availableHeight) / textSize.getHeight(),
				  MAX_SIZE);
	}

	private static Double getFontSize(mxCellState cellState) {
		if (cellState == null || cellState.getStyle() == null) {
			return null;
		}

		Object fontSize = cellState.getStyle().get(mxConstants.STYLE_FONTSIZE);
		if (!(fontSize instanceof Number)) {
			return null;
		}

		return ((Number) fontSize).doubleValue();
	}

	private static Double getFontSize(Map<String, Object> styles) {
		if (styles == null) {
			return null;
		}

		Object fontSize = styles.get(mxConstants.STYLE_FONTSIZE);
		if (fontSize == null) {
			return null;
		}

		if (fontSize instanceof Number) {
			return ((Number) fontSize).doubleValue();
		}

		if (fontSize instanceof String) {
			try {
				return Double.parseDouble((String) fontSize);
			} catch (NumberFormatException e) {
				// nothing to do
			}
		}

		return null;
	}

	private static Double computeFontSizeForSlaveObjects(String str, mxCellState cellState) {

		Double fontSize = getFontSize(cellState);

		if (fontSize == null) {
			return null;
		}

		double availableWidth = cellState.getWidth() - (cellState.getWidth() * 0.05) - 2;
		double availableHieght = cellState.getHeight() - (cellState.getHeight() * 0.05) - 2;

		String fontFamily = mxConstants.DEFAULT_FONTFAMILIES;
		int defaultFontsize = mxConstants.DEFAULT_FONTSIZE;
		int swingFontStyle = Font.PLAIN;
		Font font = new Font(fontFamily, swingFontStyle, defaultFontsize);
		mxRectangle textSize = mxUtils.getSizeForString(str, font, 1.0);

		double widthBasedFontSize = fontSize * availableWidth / textSize.getWidth();
		double heightBasedFontSize = fontSize * availableHieght / textSize.getHeight();

		double updatedFontSize = Math.min(fontSize, Math.min(widthBasedFontSize, heightBasedFontSize));

		// small correction to be safe
		updatedFontSize *= 0.9;

		return updatedFontSize;
	}

	private static Set getAllGraphCells(mxGraph graph) {
		return new HashSet<>(graph.getView().getStates().keySet());
	}



	private static void printCoordinates(String msg, mxRectangle boundingBox) {
		System.out.println("\n" + msg);
		System.out.println("x: " + boundingBox.getX());
		System.out.println("y: " + boundingBox.getY());
		System.out.println("w: " + boundingBox.getWidth());
		System.out.println("h: " + boundingBox.getHeight());
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

	private static Double getDoubleAttribute(mxCell cell, String attrName) {
		if (cell == null) {
			return null;
		}

		String attrStr = cell.getAttribute(attrName);
		try {
			return Double.parseDouble(attrStr);
		} catch (NumberFormatException e) {
			// nothing to do
		}

		return null;
	}

}
