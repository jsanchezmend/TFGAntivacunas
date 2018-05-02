package uoc.edu.jsanchezmend.tfg.ytct.data.enumeration;

/**
 * Crawler possible status enumeration
 * 
 * @author jsanchezmend
 *
 */
public enum CrawlerStatusEnum {
	
	RUNNING("Running"), // Crawler process is executed
	STOPPED("Stopped"), // Crawler process is stopped
	STOPPING("Stopping"), // Crawler process will be stopped
	FINISHED("Finished"), // Crawler process is finished
	BLOCKED("Blocked"), // Crawler process is blocked by YouTune (quota usage)
	ERROR("Error"); // For test development (it should not happen)
	
	protected String name;
	
	CrawlerStatusEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static CrawlerStatusEnum getByName(String name) {
		for(CrawlerStatusEnum item : CrawlerStatusEnum.values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}
	
}
