package uoc.edu.jsanchezmend.tfg.ytct.core.converter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoPlayer;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.AbstractConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

/**
 * @ConverterService implementation for
 * @com.google.api.services.youtube.model.Video YouTube entity and @Video entity and @VideoItem item
 * 
 * @author jsanchezmend
 *
 */
@Service("videoConverterService")
public class VideoConverterServiceImpl 
		extends AbstractConverterService<Video, VideoItem> 
		implements YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> {
	
	@Autowired
	@Qualifier("channelConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Channel, Channel, ChannelItem> channelConverterService;
	

	@Override
	public Video getEntity() {
		return new Video();
	}

	@Override
	public VideoItem getItem() {
		return new VideoItem();
	}
	
	@Override
	protected void customToItem(Video entity, VideoItem item) {
		final Channel channel = entity.getChannel();
		if(channel != null) {
			final ChannelItem channelItem = this.channelConverterService.toItem(channel);
			/*
			final ChannelItem channelItem = new ChannelItem();
			channelItem.setId(channel.getId());
			channelItem.setName(channel.getName());
			*/
			item.setChannel(channelItem);
		}
		
		final Category category = entity.getCategory();
		if(category != null) {
			final CategoryItem categoryItem = new CategoryItem();
			BeanUtils.copyProperties(category, categoryItem);
			item.setCategory(categoryItem);			
		}
		
		final Crawler crawler = entity.getCrawler();
		if(crawler != null) {
			final CrawlerItem crawlerItem = new CrawlerItem();
			crawlerItem.setId(crawler.getId());
			item.setCrawler(crawlerItem);
		}
	}
	
	@Override
	public VideoItem fromYouTubeToItem(com.google.api.services.youtube.model.Video video) {
		final VideoItem item = this.getItem();
		
		final VideoSnippet snippet = video.getSnippet();
    	final VideoStatistics statistics = video.getStatistics();
    	final VideoContentDetails contentDetails = video.getContentDetails();
    	final VideoPlayer player = video.getPlayer();
    	
    	item.setId(video.getId());
    	if(snippet != null) {
    		item.setTitle(snippet.getTitle());
    		item.setDescription(snippet.getDescription());
    		final ChannelItem channelItem = new ChannelItem();
    		channelItem.setId(snippet.getChannelId());
    		item.setChannel(channelItem);
    		item.setPublishedAt(new Date(snippet.getPublishedAt().getValue()));
		}
		if(contentDetails != null) {
			item.setDuration(contentDetails.getDuration());
		}
		if(statistics != null) {
			item.setViewCount(statistics.getViewCount());
			item.setLikeCount(statistics.getLikeCount());
			item.setDislikeCount(statistics.getDislikeCount());
			item.setCommentCount(statistics.getCommentCount());
		}
		if(player != null) {
			item.setEmbedHtml(player.getEmbedHtml());
		}		
		
		return item;
	}

	@Override
	public List<VideoItem> fromYouTubeListToListItem(List<com.google.api.services.youtube.model.Video> youTubeEntities) {
		List<VideoItem> list = new ArrayList<VideoItem>();
		for (com.google.api.services.youtube.model.Video youTubeEntity : youTubeEntities) {
			list.add(this.fromYouTubeToItem(youTubeEntity));
		}
		return Collections.unmodifiableList(list);
	}

}
