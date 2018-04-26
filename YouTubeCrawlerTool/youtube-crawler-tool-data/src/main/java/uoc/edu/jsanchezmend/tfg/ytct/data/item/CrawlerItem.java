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
	
	protected Date fromDate;
	
	protected Date toDate;
	
	protected String orderBy;
	
	protected Integer relatedLevels;
	
	protected Integer maxVideosPerLevel;
	
	protected Integer videosFound;
	
	protected Integer newVideos;
	
	protected Integer completed;
	
	protected Long executionTime;
	
	protected Date createdDate;
	
	protected String status;
	
	protected CategoryItem categoryByDefault;
	
	
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

	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
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

	public Integer getCompleted() {
		return completed;
	}
	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
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

	public CategoryItem getCategoryByDefault() {
		return categoryByDefault;
	}
	public void setCategoryByDefault(CategoryItem categoryByDefault) {
		this.categoryByDefault = categoryByDefault;
	}

}