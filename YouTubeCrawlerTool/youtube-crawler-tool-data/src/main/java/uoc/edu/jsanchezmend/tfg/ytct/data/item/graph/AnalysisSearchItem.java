package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;

import static uoc.edu.jsanchezmend.tfg.ytct.data.repository.CategoryRepository.UNCATEGORIZED;

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
	
	protected Boolean includeUncategorized;
	
	protected List<String> categories;
	
	protected List<Long> crawlers;
	
	
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

	public Boolean isIncludeUncategorized() {
		return includeUncategorized;
	}
	
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		if(categories!= null && !categories.isEmpty()) {
			if(categories.contains(UNCATEGORIZED)) {
				categories.remove(UNCATEGORIZED);
				this.includeUncategorized = true;
			} else {
				this.includeUncategorized = false;
			}
		}
		this.categories = categories;
	}

	public List<Long> getCrawlers() {
		return crawlers;
	}
	public void setCrawlers(List<Long> crawlers) {
		this.crawlers = crawlers;
	}

}
