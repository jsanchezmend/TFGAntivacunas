package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

/**
 * A analysis search variables POJO
 * 
 * @author jsanchezmend
 *
 */
public class AnalysisSearchItem extends AbstractItem {

	private static final long serialVersionUID = -6571756971998985488L;
	
	
	protected String fromDate;
	
	protected String toDate;
	
	protected Boolean includeChannels;
	
	
	public AnalysisSearchItem() {
		super();
	}


	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Boolean isIncludeChannels() {
		return includeChannels;
	}
	public void setIncludeChannels(Boolean includeChannels) {
		this.includeChannels = includeChannels;
	}

}
