function computeFontSizeForULabels(string, uHeight, scaleCoef) {

	if (!(string && uHeight && scaleCoef)) {
		return undefined;
	}

	const MAX_SIZE = 12;
	let availableHeight = uHeight * scaleCoef;

	let textSize = mxUtils.getSizeForString(string);

	return Math.min((mxConstants.DEFAULT_FONTSIZE * availableHeight) / textSize.height, MAX_SIZE); // assume that U labels use default font size
}


function computeFontSizeForSlaveObjects(string, cellState) {

	// enhance font size only for slave objects
	if (!cellState.style.fontSize) {
		return undefined;
	}

	let availableWidth = cellState.width - (cellState.width / 100)*5 - 2;
	let availableHeight = cellState.height - (cellState.height / 100)*5 - 2;

	let textSize = mxUtils.getSizeForString(string);

	let widthBasedFontSize = (cellState.style.fontSize * availableWidth) / textSize.width;
	let heightBasedFontSize = (cellState.style.fontSize * availableHeight) / textSize.height;

	let updatedFontSize =  Math.min(cellState.style.fontSize, Math.min(widthBasedFontSize, heightBasedFontSize));

	// small correction to be safe
	updatedFontSize -= (updatedFontSize / 100.0)*10;

	return updatedFontSize;
}



let scaleCoef = masterObject && masterObject.getAttribute(IW_SCALE_COEF);
let iwUHeight = masterObject && masterObject.getAttribute(IW_U_HEIGHT);
const fontSizeULabels = computeFontSizeForULabels("10", iwUHeight, scaleCoef);

			// update cell label font size to fit into vertex
			// it seems that it must be done in JS, the equivalent for mxUtils.getSizeForString(cellLabel) in Java did not work correctly
			graph.getView()
				.getStates()
				.getValues()
				.forEach((cellState) => {

					// abbreviate description if it would take more space than available width after computing font size to fit name into vertex
					let name = cellState.cell.getAttribute('name', '');
					let description = abbreviateVertexDescriptionBasedOnAvailSpace(graph, cellState);

					// join name and (abbreviated) description into one label
					let vertexLabelToShow = description ? `${name} <br> <span class="iwSlaveLabelDescription">${description}</span>` : name;

					cellState.cell.setAttribute(VERTEX_LABEL, vertexLabelToShow);

					let cellLabel = graph.convertValueToString(cellState.cell);

					if (!cellLabel) {
						return;
					}

					let updatedFontSize;

					if (cellState.cell.value.tagName === IW_SLAVE_TAG_NAME) {
						updatedFontSize = computeFontSizeForSlaveObjects(cellLabel, cellState);

					} else if (cellState.cell.value.tagName === U_LABEL_TAG_NAME) {
						updatedFontSize = fontSizeULabels;
					}

					if (updatedFontSize) {
						graph.setCellStyles(mxConstants.STYLE_FONTSIZE, updatedFontSize, [cellState.cell]);
					}

				});
