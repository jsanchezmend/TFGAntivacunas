package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph node POJO
 * 
 * @author jsanchezmend
 *
 */
public class NodeItem extends AbstractItem {

	private static final long serialVersionUID = 5097368106542159690L;
	
	
	protected NodeDataItem data;
	
	
	public NodeItem(NodeDataItem data) {
		super();
		this.data = data;
	}

	
	public NodeDataItem getData() {
		return data;
	}
	public void setData(NodeDataItem data) {
		this.data = data;
	}

}