package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph node data POJO
 * 
 * @author jsanchezmend
 *
 */
public class NodeDataItem extends AbstractItem {
	
	private static final long serialVersionUID = 7293284953107931279L;
	

	protected String id;
	
	protected String name;
	
	
	public NodeDataItem() {
		super();
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
