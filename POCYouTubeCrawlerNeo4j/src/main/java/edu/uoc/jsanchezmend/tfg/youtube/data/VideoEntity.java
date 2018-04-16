package edu.uoc.jsanchezmend.tfg.youtube.data;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class VideoEntity {
	
	@Id
	protected String id;

	protected String title;
	
	protected String description;
	
	protected Date publishedAt;
	
	@Relationship(type = "CHANNEL", direction = Relationship.UNDIRECTED)
	protected ChannelEntity channel;
	
	protected String duration;

	protected BigInteger viewCount;
	
	protected BigInteger likeCount;
	
	protected BigInteger dislikeCount;
	
	protected BigInteger commentCount;
	
	protected BigInteger scopeRange;
	
	protected String embedHtml;
	
	/**
	 * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
	 * to ignore the direction of the relationship.
	 * https://dzone.com/articles/modelling-data-neo4j
	 */
	@Relationship(type = "RELATED", direction = Relationship.UNDIRECTED)
	public Set<VideoEntity> related;

	public VideoEntity() {
	};
			
	public VideoEntity(String id, String title, String description, Date publishedAt, ChannelEntity channel, String duration,
			BigInteger viewCount, BigInteger likeCount, BigInteger dislikeCount, BigInteger commentCount,
			String embedHtml) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.publishedAt = publishedAt;
		this.channel = channel;
		this.duration = duration;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.commentCount = commentCount;
		this.embedHtml = embedHtml;
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

	public ChannelEntity getChannel() {
		return channel;
	}
	public void setChannel(ChannelEntity channel) {
		this.channel = channel;
	}

	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public BigInteger getViewCount() {
		return viewCount;
	}
	public void setViewCount(BigInteger viewCount) {
		this.viewCount = viewCount;
	}

	public BigInteger getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(BigInteger likeCount) {
		this.likeCount = likeCount;
	}

	public BigInteger getDislikeCount() {
		return dislikeCount;
	}
	public void setDislikeCount(BigInteger dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public BigInteger getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(BigInteger commentCount) {
		this.commentCount = commentCount;
	}

	public BigInteger getScopeRange() {
		return scopeRange;
	}
	public void setScopeRange(BigInteger scopeRange) {
		this.scopeRange = scopeRange;
	}

	public String getEmbedHtml() {
		return embedHtml;
	}
	public void setEmbedHtml(String embedHtml) {
		this.embedHtml = embedHtml;
	}

	public Set<VideoEntity> getRelated() {
		return related;
	}
	public void addRelated(VideoEntity video) {
		if (related == null) {
			related = new HashSet<>();
		}
		related.add(video);
	}

}
