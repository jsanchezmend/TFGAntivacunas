package edu.uoc.jsanchezmend.tfg.youtube.service.converter;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;

import edu.uoc.jsanchezmend.tfg.youtube.data.ChannelEntity;
import edu.uoc.jsanchezmend.tfg.youtube.service.converter.base.ConverterService;

@Service("channelConverterService")
public class ChannelConverterServiceImpl implements ConverterService<ChannelEntity, Channel>{
	
	@Override
	public ChannelEntity toEntity(Channel channel) {
		final ChannelEntity entity = new ChannelEntity();
		
	    final ChannelSnippet snippet = channel.getSnippet();
    	final ChannelStatistics statistics = channel.getStatistics();
    	entity.setId(channel.getId());
		if(snippet != null) {
			entity.setTitle(snippet.getTitle());
			entity.setDescription(snippet.getDescription());
			entity.setPublishedAt(new Date(snippet.getPublishedAt().getValue()));
			entity.setThumbnailUrl(snippet.getThumbnails().getDefault().getUrl());
		}
		if(statistics != null) {
			entity.setViewCount(statistics.getViewCount());
			entity.setCommentCount(statistics.getCommentCount());
			entity.setSubscriberCount(statistics.getSubscriberCount());
			entity.setVideoCount(statistics.getVideoCount());
		}
		
		return entity;
	}

}