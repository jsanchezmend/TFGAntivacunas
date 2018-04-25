package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import java.util.Date;

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
	
	@Relationship(type = "CATEGORIZED_BY_DEFAULT", direction = Relationship.UNDIRECTED)
	protected Category categoryByDefault;
	
	
	public Crawler() {
		
	}

	public Crawler(Long id, String search, Date fromDate, Date toDate, String orderBy, Integer relatedLevels,
			Integer maxVideosPerLevel, Integer videosFound, Integer newVideos, Integer completed, Long executionTime,
			Date createdDate, String status) {
		super();
		this.id = id;
		this.search = search;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.orderBy = orderBy;
		this.relatedLevels = relatedLevels;
		this.maxVideosPerLevel = maxVideosPerLevel;
		this.videosFound = videosFound;
		this.newVideos = newVideos;
		this.completed = completed;
		this.executionTime = executionTime;
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
		return CrawlerOrderByEnum.valueOf(this.orderBy);
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
	public CrawlerStatusEnum getStatusByEnum() {
		return CrawlerStatusEnum.valueOf(this.status);
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
