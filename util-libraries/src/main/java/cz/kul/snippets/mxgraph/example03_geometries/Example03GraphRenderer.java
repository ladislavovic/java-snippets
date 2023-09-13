package cz.kul.snippets.mxgraph.example03_geometries;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Can render the given graph to {@link BufferedImage}.
 */
public class Example03GraphRenderer {

	public static final String VERTEX_LABEL = "iw_vertex_label";
	public static final String IW_MASTER = "iw_master";
	public static final String IW_SCALE_COEF = "iw_graph_scale";
	public static final String IW_U_HEIGHT_MM = "iw_u_height";


	public BufferedImage renderGraph(mxGraph graph) {

		// Find the master cell
		mxCell masterObject = getAllGraphCells(graph).stream()
				.filter(mxCell -> Boolean.parseBoolean(mxCell.getAttribute(IW_MASTER)))
				.findFirst()
				.orElse((mxCell) graph.getDefaultParent());
		Object graphObjectToExport = masterObject != null ? masterObject.getParent() : graph.getDefaultParent();

		// OP 1
		setLabels(graph);

		// Compute font size for u-labels
		Double scaleCoef = getDoubleAttribute(masterObject, IW_SCALE_COEF);
		Double iwHeight = getDoubleAttribute(masterObject, IW_U_HEIGHT_MM);
		Double fontSizeULabels = computeFontSizeForULabels("10", iwHeight, scaleCoef);

		// OP 2
//		setFontSizes(graph, fontSizeULabels);

		// OP 3
		graph.refresh(); // TODO is it needed?

		// Compute the min font size
		final int DEFAULT_FONT_SIZE = 11;
		double minFontSize = DEFAULT_FONT_SIZE;
		for (mxCell cell : getAllGraphCells(graph)) {
			Map<String, Object> cellStyles = graph.getCellStyle(cell);
			Double cellFontSize = getFontSize(cellStyles);
			if (cellFontSize != null) {
				minFontSize = Math.min(minFontSize, cellFontSize);
			}
		}

		// Compute the right scale
		// 12px - desired min font size in image export
		final double MAX_SCALE_FOR_EXPORT = 15;
		double targetScale = Math.min(MAX_SCALE_FOR_EXPORT, 12d / minFontSize); // the final scale value of export image (determine picture resolution)
		double viewScale = graph.getView().getScale();
		double exportScale = targetScale / viewScale;

		// Get the bounding box
		mxRectangle boundingBox = graph.getView().getBoundingBox(new Object[] {masterObject, graphObjectToExport});

		mxRectangle mxRectangle = new mxRectangle(boundingBox);

		// Add border to bounding box
		double border = 30;
//		mxRectangle.setX(mxRectangle.getX() - border);
//		mxRectangle.setY(mxRectangle.getY() - border);
		mxRectangle.setWidth(mxRectangle.getWidth() + 2 * border);
		mxRectangle.setHeight(mxRectangle.getHeight() + 2 * border);
		graph.getView().setTranslate(new mxPoint(border, border));

		// Adjust bounding box according to scale
		mxRectangle.setX(mxRectangle.getX() * exportScale);
		mxRectangle.setY(mxRectangle.getY() * exportScale);
		mxRectangle.setWidth(mxRectangle.getWidth() * exportScale);
		mxRectangle.setHeight(mxRectangle.getHeight() * exportScale);


//		graph.getView().setScale(2);
		for (mxCell cell : getAllGraphCells(graph)) {
			printGeometries(graph, cell);
		}

//		graph.refresh();
//		graph.repaint();


		// Create an image Way0
		BufferedImage image = mxCellRenderer.createBufferedImage(
				graph,
				null,
				exportScale,
//				1,
				Color.WHITE,
				true,
//				null);
				mxRectangle);

		return image;
	}

	private void printGeometries(mxGraph graph, mxCell cell) {
		mxGeometry cellGeometry = cell.getGeometry();

		mxCellState state = graph.getView().getState(cell);
		mxRectangle stateBoundingBox = state.getBoundingBox();
		mxRectangle labelBounds = state.getLabelBounds();

		System.out.println("Cell id=" + cell.getId() + " geometry:");
		System.out.println("  cell geom: " + cellGeometry);
		System.out.println("  state bb : " + stateBoundingBox);
		System.out.println("  label bb : " + labelBounds);
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

		if (StringUtils.hasText(name)) {
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

	private void setLabels(mxGraph graph) {
		for (mxCell cell : getAllGraphCells(graph)) {
			String name = cell.getAttribute("name");
			String description = abbreviateVertexDescriptionBasedOnAvailSpace(graph, cell);

			// join name and (abbreviated) description into one label
			String vertexLabelToShow = StringUtils.hasText(description)
					  ? name + "<br> <span class='iwSlaveLabelDescription'>" + description + "</span>"
					  : name;

			cell.setAttribute(VERTEX_LABEL, vertexLabelToShow);
		}
	}

	private void setFontSizes(mxGraph graph, Double fontSizeULabels) {
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
	}

}
