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

}
