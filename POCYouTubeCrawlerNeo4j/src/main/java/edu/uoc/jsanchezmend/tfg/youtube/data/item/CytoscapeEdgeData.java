package edu.uoc.jsanchezmend.tfg.youtube.data.item;

/**
 * 
 * { source: 'j', target: 'e' }
 *
 */
public class CytoscapeEdgeData {
	
	protected String source;
	protected String target;
	
	public CytoscapeEdgeData(String source, String target) {
		super();
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}


}
