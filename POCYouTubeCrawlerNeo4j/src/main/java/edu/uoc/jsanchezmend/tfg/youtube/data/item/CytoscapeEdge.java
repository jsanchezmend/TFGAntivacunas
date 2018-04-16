package edu.uoc.jsanchezmend.tfg.youtube.data.item;

import java.io.Serializable;

public class CytoscapeEdge implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected CytoscapeEdgeData data;
	
	public CytoscapeEdge(String source, String target) {
		super();
		this.data = new CytoscapeEdgeData(source, target);
	}

	public CytoscapeEdgeData getData() {
		return data;
	}
	public void setData(CytoscapeEdgeData data) {
		this.data = data;
	}
	
}
