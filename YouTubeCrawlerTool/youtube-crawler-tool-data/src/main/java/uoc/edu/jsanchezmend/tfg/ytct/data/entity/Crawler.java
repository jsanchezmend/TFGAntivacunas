package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;

/**
 * Crawler @NodeEntity
 * 
 * @author jsanchezmend
 *
 */
@NodeEntity
public class Crawler {
	
	@Id
	@GeneratedValue
	protected Long id;

	protected String search;
	
	protected String relatedVideoId;
	
	protected Date fromDate;
	
	protected Date toDate;
	
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
	
	@Relationship(type = "CATEGORIZED_BY_DEFAULT", direction = Relationship.OUTGOING)
	protected Category categoryByDefault;
	
	
	public Crawler() {
		
	}

	public Crawler(Long id, String search, String relatedVideoId, Date fromDate, Date toDate, String orderBy, Integer relatedLevels,
			Integer maxVideosPerLevel, Integer maxVideos, Integer videosFound, Integer newVideos, Float completed, Long executionTime,
			String pageToken, Date createdDate, String status) {
		super();
		this.id = id;
		this.search = search;
		this.relatedVideoId = relatedVideoId;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.orderBy = orderBy;
		this.relatedLevels = relatedLevels;
		this.maxVideosPerLevel = maxVideosPerLevel;
		this.maxVideos = maxVideos;
		this.videosFound = videosFound;
		this.newVideos = newVideos;
		this.completed = completed;
		this.executionTime = executionTime;
		this.pageToken = pageToken;
		this.createdDate = createdDate;
		this.status = status;
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
	public CrawlerOrderByEnum getOrderByByEnum() {
		return CrawlerOrderByEnum.getByName(this.orderBy);
	}
	public void setOrderByByEnum(CrawlerOrderByEnum orderBy) {
		this.orderBy = orderBy.getName();
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
	public CrawlerStatusEnum getStatusByEnum() {
		return CrawlerStatusEnum.getByName(this.status);
	}
	public void setStatusByEnum(CrawlerStatusEnum status) {
		this.status = status.getName();
	}

	public Category getCategoryByDefault() {
		return categoryByDefault;
	}
	public void setCategoryByDefault(Category categoryByDefault) {
		this.categoryByDefault = categoryByDefault;
	}
	
}
