package uoc.edu.jsanchezmend.tfg.ytct.data.enumeration;

/**
 * Crawler possible status enumeration
 * 
 * @author jsanchezmend
 *
 */
public enum CrawlerStatusEnum {
	
	RUNNING("Running"),
	STOPPED("Stopped"),
	FINISHED("Finished");
	
	protected String name;
	
	CrawlerStatusEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public CrawlerStatusEnum getByName(String name) {
		for(CrawlerStatusEnum item : CrawlerStatusEnum.values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}
	
}