package cz.kul.snippets.mxgraph.example02_cross_graph_renderer;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import org.apache.xerces.dom.ElementImpl;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Can render the given graph to {@link BufferedImage}.
 */
public class GraphRendererCROSS {

	public static final String VERTEX_LABEL = "iw_vertex_label";
	public static final String IW_MASTER = "iw_master";
	public static final String IW_SCALE_COEF = "iw_graph_scale";
	public static final String IW_U_HEIGHT_MM = "iw_u_height";

	public BufferedImage renderGraph(mxGraph graph) {

		graph.refresh(); // TODO without that the stylesheets were not applied to the graph view

		// Find the master cell
		mxCell masterObject = getAllGraphCells(graph).stream()
				.filter(mxCell -> Boolean.parseBoolean(mxCell.getAttribute(IW_MASTER)))
				.findFirst()
				.orElse((mxCell) graph.getDefaultParent());
		Object graphObjectToExport = masterObject != null ? masterObject.getParent() : graph.getDefaultParent();

		// Move geometry of "graphObjectToExport" to [0, 0]. Otherwise it cause problems with label rendering - they are
		// considered as overflow and when the style is "overflow: hidden", they are not visible
		mxGeometry geom = graph.getCellGeometry(graphObjectToExport);
		geom.setX(0);
		geom.setY(0);

		// Set labels
		for (mxCell cell : getAllGraphCells(graph)) {
			String name = cell.getAttribute("name");
			String description = abbreviateVertexDescriptionBasedOnAvailSpace(graph, cell);
//			name = "<span style='transform: rotate(-90deg);'>" + name + "</span>";
//			name = "<span style='rotate: 180deg;'>" + name + "</span>";
//			name = "<span style='color: blue;'>" + name + "</span>";
//			name = "<span style='text-orientation: upright;'>" + name + "</span>";

			// join name and (abbreviated) description into one label
			String vertexLabelToShow = StringUtils.hasText(description)
					? name + "<br> <span class='iwSlaveLabelDescription'>" + description + "</span>"
					: name;

			cell.setAttribute(VERTEX_LABEL, vertexLabelToShow);
		}

		// Compute font size for u-labels
		Double scaleCoef = getDoubleAttribute(masterObject, IW_SCALE_COEF);
		Double iwHeight = getDoubleAttribute(masterObject, IW_U_HEIGHT_MM);
		Double fontSizeULabels = computeFontSizeForULabels("10", iwHeight, scaleCoef);

		// Set font sizes
		final String IW_SLAVE_TAG_NAME = "member";
		final String U_LABEL_TAG_NAME = "uLabel";
		for (mxCell cell : getAllGraphCells(graph)) {
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
					graph.setCellStyles(mxConstants.STYLE_FONTSIZE, updatedFontSize.toString(), new Object[]{cell});
				}
			}
		}

		// It is needed to refresh mxCellState instances after font size change. Otherwise
		// cells bounding boxes are not correct
		graph.refresh();

		// Compute the min font size
		final int DEFAULT_FONT_SIZE = 11;
		double minFontSize = DEFAULT_FONT_SIZE;
		for (mxCell cell : getAllGraphCells(graph)) {
			Map<String, Object> cellStyles = graph.getCellStyle(cell);
			Double cellFontSize = getFontSize(cellStyles);
			if (cellFontSize != null) {
				minFontSize = Math.min(minFontSize, cellFontSize);
				if (cellFontSize < 1) {
					graph.setCellStyles(mxConstants.STYLE_FONTSIZE, "1", new Object[]{cell});
				}
			}
		}

		if (minFontSize < 1) {
			double factor = (1 / minFontSize) * 1.1;
			for (mxCell cell : getAllGraphCells(graph)) {
				mxGeometry geometry = cell.getGeometry();
				if (geometry != null) {
					geometry.setX(geometry.getX() * factor);
					geometry.setY(geometry.getY() * factor);
					geometry.setHeight(geometry.getHeight() * factor);
					geometry.setWidth(geometry.getWidth() * factor);
				}
			}
			graph.refresh();
		}


		// Compute the right scale
		// 12px - desired min font size in image export
		final double MAX_SCALE_FOR_EXPORT = 15;
		double targetScale = Math.min(MAX_SCALE_FOR_EXPORT, 12d / Math.max(minFontSize, 1)); // the final scale value of export image (determine picture resolution)
		double viewScale = graph.getView().getScale();
		double exportScale = targetScale / viewScale;

		// Get the bounding box
		mxRectangle boundingBox = graph.getView().getBoundingBox(new Object[] {masterObject, graphObjectToExport});
		mxRectangle mxRectangle = new mxRectangle(boundingBox);

		// Add border to bounding box
		double border = 20;
		mxRectangle.setWidth(mxRectangle.getWidth() + 2 * border);
		mxRectangle.setHeight(mxRectangle.getHeight() + 2 * border);
		graph.getView().setTranslate(new mxPoint(border, border)); // it moves graph view to the bottom and right. It cause a border on the top side and left side

		// Adjust bounding box according to scale
		mxRectangle.setX(mxRectangle.getX() * exportScale);
		mxRectangle.setY(mxRectangle.getY() * exportScale);
		mxRectangle.setWidth(mxRectangle.getWidth() * exportScale);
		mxRectangle.setHeight(mxRectangle.getHeight() * exportScale);

		// Create an image Way0
		BufferedImage image = mxCellRenderer.createBufferedImage(
				graph,
				null,
				exportScale,
				Color.WHITE,
				true,
				mxRectangle);

		return image;
	}

	private Double computeFontSizeForULabels(String str, Double uHeight, Double scaleCoef) {

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

	private Double getFontSize(mxCellState cellState) {
		if (cellState == null || cellState.getStyle() == null) {
			return null;
		}

		Object fontSize = cellState.getStyle().get(mxConstants.STYLE_FONTSIZE);
		if (!(fontSize instanceof Number)) {
			return null;
		}

		return ((Number) fontSize).doubleValue();
	}

	private boolean is90degreesRotated(mxCellState cellState) {
		if (cellState == null || cellState.getStyle() == null) {
			return false;
		}

		Object rotation = cellState.getStyle().get(mxConstants.STYLE_ROTATION);
		if (!(rotation instanceof Number)) {
			return false;
		}

		return Math.abs(((Number) rotation).intValue()) == 90;
	}

	private Double getFontSize(Map<String, Object> styles) {
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

	private Double computeFontSizeForSlaveObjects(String str, mxCellState cellState) {

		Double fontSize = getFontSize(cellState);

		if (fontSize == null) {
			return null;
		}

		double availableWidth = cellState.getWidth() - (cellState.getWidth() * 0.05) - 2;
		double availableHieght = cellState.getHeight() - (cellState.getHeight() * 0.05) - 2;
		if (is90degreesRotated(cellState)) {
			double d = availableWidth;
			availableWidth = availableHieght;
			availableHieght = d;
		}

		String fontFamily = mxConstants.DEFAULT_FONTFAMILIES;
		int defaultFontsize = mxConstants.DEFAULT_FONTSIZE;
		int swingFontStyle = Font.PLAIN;
		Font font = new Font(fontFamily, swingFontStyle, defaultFontsize);
		mxRectangle textSize = mxUtils.getSizeForHtml(str, new HashMap<>(), 1.0, 0.0);

		double widthBasedFontSize = (fontSize * availableWidth) / textSize.getWidth();
		double heightBasedFontSize = (fontSize * availableHieght) / textSize.getHeight();

		double updatedFontSize = Math.min(fontSize, Math.min(widthBasedFontSize, heightBasedFontSize));

		// small correction to be safe
		updatedFontSize *= 0.9;

		return updatedFontSize;
	}

	private Double getDoubleAttribute(mxCell cell, String attrName) {
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

	private Set<mxCell> getAllGraphCells(mxGraph graph) {
		return (Set<mxCell>) new HashSet(graph.getView().getStates().keySet());
	}

	private String abbreviateVertexDescriptionBasedOnAvailSpace(mxGraph graph, mxCell cell) {

		String name = cell.getAttribute("name", "");
		String description = cell.getAttribute("description", "");

		if (!StringUtils.hasText(description)) {
			return null;
		}

		if (!StringUtils.hasText(name)) {
			// do not abbreaviate
			return description;
		}

		mxCellState cellState = graph.getView().getState(cell);
		Double origFontSize = getFontSize(cellState);

		if (origFontSize != null) {

			// abbreviate description if it is longer than available space
			// if we would not do it, computeFontSize would later set too small font for both name and description (= 1 vertex label)

			Double fontSizeForName = computeFontSizeForSlaveObjects(name, cellState);
			double availableWidth = (cellState.getWidth() * 0.95) - 2;
			double descriptionWidth = mxUtils.getSizeForString(description, createFont((int) Math.ceil(fontSizeForName)), 1.0).getWidth() * graph.getView().getScale();

			if (availableWidth < descriptionWidth) {
				double descriptionNewLength = Math.max(5, description.length() * (availableWidth / descriptionWidth) - 4);
				return (description.length() > descriptionNewLength) ? description.substring(0, ((int) Math.floor(descriptionNewLength))-1) + "&hellip;" : description;
			}
		}

		return description;
	}

	private Font createFont() {
		return createFont(mxConstants.DEFAULT_FONTSIZE);
	}

	private Font createFont(int fontSize) {
		String fontFamily = mxConstants.DEFAULT_FONTFAMILIES;
		int swingFontStyle = Font.PLAIN;
		return new Font(fontFamily, swingFontStyle, fontSize);
	}

}
