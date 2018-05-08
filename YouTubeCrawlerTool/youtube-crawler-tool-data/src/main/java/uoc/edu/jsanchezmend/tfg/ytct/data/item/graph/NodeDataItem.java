package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.GraphNodeTypeEnum;
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
	
	protected String resourceId;
	
	protected String typeCode;
	
	protected String name;
	
	protected String shape;
	
	protected String color;
	
	protected int size;
	
	
	public NodeDataItem(GraphNodeTypeEnum type) {
		super();
		this.typeCode = type.getCode();
		this.shape = type.getShape();
		this.color = type.getDefaultColor();
		this.size = type.getDefaultSize();
	}
	
	public String getId() {
		return id;
	}

	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
		this.id = this.typeCode + "-" + resourceId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public String getShape() {
		return shape;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
