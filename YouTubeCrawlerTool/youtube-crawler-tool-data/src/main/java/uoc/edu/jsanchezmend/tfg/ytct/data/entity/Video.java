package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Video @NodeEntity
 * 
 * @author jsanchezmend
 *
 */
@NodeEntity
public class Video {
	
	@Id
	protected String id;

	protected String title;
	
	protected String description;
	
	protected Date publishedAt;
		
	protected String duration;

	protected BigInteger viewCount;
	
	protected BigInteger likeCount;
	
	protected BigInteger dislikeCount;
	
	protected BigInteger commentCount;
	
	protected BigInteger scopeRange;
	
	protected String embedHtml;
	
	protected Boolean favorite;
	
	@Relationship(type = "UPLOADED_BY", direction = Relationship.OUTGOING)
	protected Channel channel;
	
	@Relationship(type = "RELATED_TO", direction = Relationship.OUTGOING)
	public Set<Video> related;
	
	@Relationship(type = "DISCOVERED_BY", direction = Relationship.OUTGOING)
	protected Crawler crawler;
	
	@Relationship(type = "CATEGORIZED_AS", direction = Relationship.OUTGOING)
	protected Category category;
	

	public Video() {
		
	};

	public Video(String id, String title, String description, Date publishedAt, String duration, BigInteger viewCount,
			BigInteger likeCount, BigInteger dislikeCount, BigInteger commentCount, BigInteger scopeRange,
			String embedHtml, Boolean favorite) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.publishedAt = publishedAt;
		this.duration = duration;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.commentCount = commentCount;
		this.scopeRange = scopeRange;
		this.embedHtml = embedHtml;
		this.favorite = favorite;
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
	
	public Boolean getFavorite() {
		return favorite;
	}
	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}	

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Set<Video> getRelated() {
		return related;
	}
	public void addRelated(Video video) {
		if (related == null) {
			related = new HashSet<>();
		}
		related.add(video);
	}

	public Crawler getCrawler() {
		return crawler;
	}
	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
}
