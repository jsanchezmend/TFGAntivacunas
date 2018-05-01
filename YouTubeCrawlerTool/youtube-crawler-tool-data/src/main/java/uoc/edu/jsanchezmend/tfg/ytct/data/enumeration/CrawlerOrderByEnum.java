package uoc.edu.jsanchezmend.tfg.ytct.data.enumeration;

/**
 * Crawler possible orders enumeration
 * 
 * @author jsanchezmend
 *
 */
public enum CrawlerOrderByEnum {
	
	RATING("Rating", "rating"),
	DATE("Date", "date"),
	RELEVANCE("Relevance","relevance"),
	TITLE("Title", "title"),
	VIEW_COUNT("View count", "viewCount");
	
	protected String name;
	protected String value;
	
	CrawlerOrderByEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public static CrawlerOrderByEnum getByName(String name) {
		for(CrawlerOrderByEnum item : CrawlerOrderByEnum.values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

}
