package edu.uoc.jsanchezmend.tfg.youtube.data;

import java.io.Serializable;

public class CytoscapeNode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected CytoscapeNodeData data;
	
	public CytoscapeNode(String id, String name) {
		super();
		this.data = new CytoscapeNodeData(id, name);
	}

	public CytoscapeNodeData getData() {
		return data;
	}
	public void setData(CytoscapeNodeData data) {
		this.data = data;
	}

}