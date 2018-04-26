package uoc.edu.jsanchezmend.tfg.ytct.core.converter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.AbstractConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;

/**
 * @ConverterService implementation for 
 * @com.google.api.services.youtube.model.Channel YouTube entity and @Channel entity and @ChannelItem item
 * 
 * @author jsanchezmend
 *
 */
@Service("channelConverterService")
public class ChannelConverterServiceImpl 
		extends AbstractConverterService<Channel, ChannelItem>
		implements YouTubeConverterService<com.google.api.services.youtube.model.Channel, Channel, ChannelItem>{

	@Override
	public Channel getEntity() {
		return new Channel();
	}

	@Override
	public ChannelItem getItem() {
		return new ChannelItem();
	}

	@Override
	public ChannelItem fromYouTubeToItem(com.google.api.services.youtube.model.Channel channel) {
		final ChannelItem item = this.getItem();
		
	    final ChannelSnippet snippet = channel.getSnippet();
    	final ChannelStatistics statistics = channel.getStatistics();
    	item.setId(channel.getId());
		if(snippet != null) {
			item.setName(snippet.getTitle());
			item.setDescription(snippet.getDescription());
			item.setPublishedAt(new Date(snippet.getPublishedAt().getValue()));
			item.setThumbnailUrl(snippet.getThumbnails().getDefault().getUrl());
		}
		if(statistics != null) {
			item.setViewCount(statistics.getViewCount());
			item.setCommentCount(statistics.getCommentCount());
			item.setSubscribersCount(statistics.getSubscriberCount());
			item.setVideoCount(statistics.getVideoCount());
		}
		
		return item;
	}

	@Override
	public List<ChannelItem> fromYouTubeListToListItem(List<com.google.api.services.youtube.model.Channel> youTubeEntities) {
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		for (com.google.api.services.youtube.model.Channel youTubeEntity : youTubeEntities) {
			list.add(this.fromYouTubeToItem(youTubeEntity));
		}
		return Collections.unmodifiableList(list);
	}

}
