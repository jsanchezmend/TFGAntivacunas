package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph edge POJO
 * 
 * @author jsanchezmend
 *
 */
public class EdgeItem extends AbstractItem {

	private static final long serialVersionUID = 1785413512457378902L;
	
	
	protected EdgeDataItem data;
	
	
	public EdgeItem(EdgeDataItem data) {
		super();
		this.data = data;
	}

	
	public EdgeDataItem getData() {
		return data;
	}
	public void setData(EdgeDataItem data) {
		this.data = data;
	}
	
}
