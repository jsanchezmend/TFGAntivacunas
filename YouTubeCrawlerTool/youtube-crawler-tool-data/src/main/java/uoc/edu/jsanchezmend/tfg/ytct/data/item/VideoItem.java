package uoc.edu.jsanchezmend.tfg.ytct.data.item;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Video POJO representation
 * 
 * @author jsanchezmend
 *
 */
public class VideoItem extends AbstractItem {

	private static final long serialVersionUID = 6633484677117936799L;
	
	
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

	protected ChannelItem channel;

	public Set<VideoItem> related;

	protected CrawlerItem crawler;
	
	protected CategoryItem category;
	
	
	public VideoItem() {
		super();
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

	public ChannelItem getChannel() {
		return channel;
	}
	public void setChannel(ChannelItem channel) {
		this.channel = channel;
	}

	public Set<VideoItem> getRelated() {
		return related;
	}
	public void setRelated(Set<VideoItem> related) {
		this.related = related;
	}
	public void addRelated(VideoItem video) {
		if (this.related == null) {
			this.related = new HashSet<>();
		}
		this.related.add(video);
	}

	public CrawlerItem getCrawler() {
		return crawler;
	}
	public void setCrawler(CrawlerItem crawler) {
		this.crawler = crawler;
	}

	public CategoryItem getCategory() {
		return category;
	}
	public void setCategory(CategoryItem category) {
		this.category = category;
	}

}
