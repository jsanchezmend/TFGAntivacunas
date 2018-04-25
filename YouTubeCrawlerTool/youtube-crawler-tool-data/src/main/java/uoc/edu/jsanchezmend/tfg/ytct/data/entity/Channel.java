package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import java.math.BigInteger;
import java.util.Date;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Channel @NodeEntity
 * 
 * @author jsanchezmend
 *
 */
@NodeEntity
public class Channel {
	
	@Id
	protected String id;

	protected String name;
	
	protected String description;
	
	protected Date publishedAt;
	
	protected String thumbnailUrl;
	
	protected BigInteger subscribersCount;
	
	protected BigInteger videoCount;
	
	protected BigInteger viewCount;
	
	protected BigInteger commentCount;
	
	
	public Channel() {
		
	}

	public Channel(String id, String name, String description, Date publishedAt, String thumbnailUrl,
			BigInteger subscribersCount, BigInteger videoCount, BigInteger viewCount, BigInteger commentCount) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.publishedAt = publishedAt;
		this.thumbnailUrl = thumbnailUrl;
		this.subscribersCount = subscribersCount;
		this.videoCount = videoCount;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public BigInteger getSubscribersCount() {
		return subscribersCount;
	}
	public void setSubscribersCount(BigInteger subscribersCount) {
		this.subscribersCount = subscribersCount;
	}

	public BigInteger getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(BigInteger videoCount) {
		this.videoCount = videoCount;
	}

	public BigInteger getViewCount() {
		return viewCount;
	}
	public void setViewCount(BigInteger viewCount) {
		this.viewCount = viewCount;
	}

	public BigInteger getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(BigInteger commentCount) {
		this.commentCount = commentCount;
	}

}
