package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph POJO
 * 
 * @author jsanchezmend
 *
 */
public class GraphItem extends AbstractItem {

	private static final long serialVersionUID = 2985223510157751440L;
	
	
	protected GraphElementsItem elements;
	
	
	public GraphItem() {
		super();
	}


	public GraphElementsItem getElements() {
		return elements;
	}
	public void setElements(GraphElementsItem elements) {
		this.elements = elements;
	}

}
