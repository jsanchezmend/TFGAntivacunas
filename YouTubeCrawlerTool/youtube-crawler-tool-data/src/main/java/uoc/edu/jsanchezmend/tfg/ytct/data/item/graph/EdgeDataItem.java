package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import org.springframework.data.neo4j.annotation.QueryResult;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A graph edge data POJO
 * 
 * @author jsanchezmend
 *
 */
@QueryResult
public class EdgeDataItem extends AbstractItem {
	
	private static final long serialVersionUID = -6081169770827222402L;
	
	
	protected String source;
	
	protected String target;
	
	protected String outgoing;
	
	protected String incoming;
	
	protected String outgoingType;
	
	protected String incomingType;
	
	
	public EdgeDataItem() {
		super();
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

	public String getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(String outgoing) {
		this.outgoing = outgoing;
	}

	public String getIncoming() {
		return incoming;
	}
	public void setIncoming(String incoming) {
		this.incoming = incoming;
	}

	public String getOutgoingType() {
		return outgoingType;
	}
	public void setOutgoingType(String outgoingType) {
		this.outgoingType = outgoingType;
	}

	public String getIncomingType() {
		return incomingType;
	}
	public void setIncomingType(String incomingType) {
		this.incomingType = incomingType;
	}

}
