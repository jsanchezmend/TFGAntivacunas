package edu.uoc.jsanchezmend.tfg.youtube.data;

import java.io.Serializable;

/**
 * 
 * { id: 'k', name: 'Kramer' }
 *
 */
public class CytoscapeNodeData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String id;
	protected String name;
	
	public CytoscapeNodeData(String id, String name) {
		super();
		this.id = id;
		this.name = name;
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
