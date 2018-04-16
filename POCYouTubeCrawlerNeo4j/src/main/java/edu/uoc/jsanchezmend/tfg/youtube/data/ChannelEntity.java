package edu.uoc.jsanchezmend.tfg.youtube.data;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity
public class ChannelEntity {
	
	@Id
	protected String id;

	protected String title;
	
	protected String description;
	
	protected Date publishedAt;
	
	protected String thumbnailUrl;
	
	protected BigInteger viewCount;
	
	protected BigInteger commentCount;
	
	protected BigInteger subscriberCount;
	
	protected BigInteger videoCount;
	
	@Relationship(type = "CHANNEL", direction = Relationship.UNDIRECTED)
	public Set<VideoEntity> videos;
	
	public ChannelEntity() {
	}

	public ChannelEntity(String id, String title, String description, Date publishedAt, String thumbnailUrl,
			BigInteger viewCount, BigInteger commentCount, BigInteger subscriberCount, BigInteger videoCount) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.publishedAt = publishedAt;
		this.thumbnailUrl = thumbnailUrl;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.subscriberCount = subscriberCount;
		this.videoCount = videoCount;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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

	public BigInteger getSubscriberCount() {
		return subscriberCount;
	}
	public void setSubscriberCount(BigInteger subscriberCount) {
		this.subscriberCount = subscriberCount;
	}

	public BigInteger getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(BigInteger videoCount) {
		this.videoCount = videoCount;
	}

	public Set<VideoEntity> getVideos() {
		return videos;
	}
	public void addVideo(VideoEntity video) {
		if (videos == null) {
			videos = new HashSet<>();
		}
		videos.add(video);
	}

}
