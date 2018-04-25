package uoc.edu.jsanchezmend.tfg.ytct.data.item;

import java.math.BigInteger;
import java.util.Date;

/**
 * @Channel POJO representation
 * 
 * @author jsanchezmend
 *
 */
public class ChannelItem extends AbstractItem {

	private static final long serialVersionUID = -3278860714701714824L;
	
	
	protected String id;

	protected String name;
	
	protected String description;
	
	protected Date publishedAt;
	
	protected String thumbnailUrl;
	
	protected BigInteger subscribersCount;
	
	protected BigInteger videoCount;
	
	protected BigInteger viewCount;
	
	protected BigInteger commentCount;
	
	
	public ChannelItem() {
		super();
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
