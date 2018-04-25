package uoc.edu.jsanchezmend.tfg.ytct.data.enumeration;

/**
 * Crawler possible orders enumeration
 * 
 * @author jsanchezmend
 *
 */
public enum CrawlerOrderByEnum {
	
	RATING("Running"),
	DATE("Date"),
	RELEVANCE("Relevance"),
	TITLE("Title"),
	VIEW_COUNT("View count");
	
	protected String name;
	
	CrawlerOrderByEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public CrawlerOrderByEnum getByName(String name) {
		for(CrawlerOrderByEnum item : CrawlerOrderByEnum.values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

}
