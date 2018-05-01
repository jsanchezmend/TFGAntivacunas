package uoc.edu.jsanchezmend.tfg.ytct.data.item;

import java.util.Date;

/**
 * @Crawler POJO representation
 * 
 * @author jsanchezmend
 *
 */
public class CrawlerItem extends AbstractItem {

	private static final long serialVersionUID = 5337684825765947699L;
	
	
	protected Long id;

	protected String search;
	
	protected String relatedVideoId;
	
	protected String fromDate;
	
	protected String toDate;
	
	protected String orderBy;
	
	protected Integer relatedLevels;
	
	protected Integer maxVideosPerLevel;
	
	protected Integer maxVideos;
	
	protected Integer videosFound;
	
	protected Integer newVideos;
	
	protected Float completed;
	
	protected Long executionTime;
	
	protected String pageToken;
	
	protected Date createdDate;
	
	protected String status;
	
	protected String categoryByDefault;
	
	
	public CrawlerItem() {
		super();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	public String getRelatedVideoId() {
		return relatedVideoId;
	}
	public void setRelatedVideoId(String relatedVideoId) {
		this.relatedVideoId = relatedVideoId;
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

	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getRelatedLevels() {
		return relatedLevels;
	}
	public void setRelatedLevels(Integer relatedLevels) {
		this.relatedLevels = relatedLevels;
	}

	public Integer getMaxVideosPerLevel() {
		return maxVideosPerLevel;
	}
	public void setMaxVideosPerLevel(Integer maxVideosPerLevel) {
		this.maxVideosPerLevel = maxVideosPerLevel;
	}
	
	public Integer getMaxVideos() {
		return maxVideos;
	}
	public void setMaxVideos(Integer maxVideos) {
		this.maxVideos = maxVideos;
	}

	public Integer getVideosFound() {
		return videosFound;
	}
	public void setVideosFound(Integer videosFound) {
		this.videosFound = videosFound;
	}

	public Integer getNewVideos() {
		return newVideos;
	}
	public void setNewVideos(Integer newVideos) {
		this.newVideos = newVideos;
	}

	public Float getCompleted() {
		return completed;
	}
	public void setCompleted(Float completed) {
		this.completed = completed;
	}

	public Long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	
	public String getPageToken() {
		return pageToken;
	}
	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategoryByDefault() {
		return categoryByDefault;
	}
	public void setCategoryByDefault(String categoryByDefault) {
		this.categoryByDefault = categoryByDefault;
	}

}
