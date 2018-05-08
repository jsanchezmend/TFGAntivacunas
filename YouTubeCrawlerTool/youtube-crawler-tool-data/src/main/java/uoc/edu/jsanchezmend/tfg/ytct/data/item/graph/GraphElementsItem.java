package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import java.util.ArrayList;
import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph elements POJO
 * 
 * @author jsanchezmend
 *
 */
public class GraphElementsItem  extends AbstractItem {

	private static final long serialVersionUID = 2559471683278783309L;
	
	
	protected List<NodeItem> nodes;
	
	protected List<EdgeItem> edges;
	
	
	public GraphElementsItem() {
		super();
	}
	
	public List<NodeItem> getNodes() {
		return nodes;
	}
	public void setNodes(List<NodeItem> nodes) {
		this.nodes = nodes;
	}
	public void addNode(NodeItem node) {
		if(this.nodes == null) {
			this.nodes = new ArrayList<NodeItem>();
		}
		this.nodes.add(node);
	}

	public List<EdgeItem> getEdges() {
		return edges;
	}
	public void setEdges(List<EdgeItem> edges) {
		this.edges = edges;
	}
	public void addEdge(EdgeItem edge) {
		if(this.edges == null) {
			this.edges = new ArrayList<EdgeItem>();
		}
		this.edges.add(edge);
	}

}
