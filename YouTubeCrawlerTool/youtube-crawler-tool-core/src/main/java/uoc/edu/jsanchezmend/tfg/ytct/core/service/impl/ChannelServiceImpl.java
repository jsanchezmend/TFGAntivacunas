package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.ChannelService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.ChannelStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.ChannelRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.VideoRepository;

/**
 * @ChannelService implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	@Qualifier("channelConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Channel, Channel, ChannelItem> channelConverterService;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	

	@Override
	public List<ChannelItem> listChannels() {
		final Iterable<Channel> channelIterable = this.channelRepository.findAll();
		final List<ChannelItem> result = this.channelConverterService.toListItem(channelIterable);
		return result;
	}

	@Override
	public ChannelItem getChannel(String id) {
		final Channel channel = this.channelRepository.findById(id).orElse(null);
		final ChannelItem result = this.channelConverterService.toItem(channel);
		return result;
	}

	@Override
	public ChannelItem deleteChannel(String id) {
		ChannelItem result = null;
		
		final Channel channel = this.channelRepository.findById(id).orElse(null);
		if(channel != null) {
			// First delete all channel videos
			this.videoRepository.removeByChannelId(id);
			this.channelRepository.delete(channel);
			result = this.channelConverterService.toItem(channel);
		}

		return result;
	}

	@Override
	public ChannelStatsItem getChannelStats(String id) {
		ChannelStatsItem result = null;
		// TODO
		return result;
	}

	@Override
	public List<VideoItem> getChannelVideos(String id) {
		final List<VideoItem> results = new ArrayList<VideoItem>();
		final List<Video> entities = this.videoRepository.findByChannelId(id);
		if(entities != null && !entities.isEmpty()) {
			results.addAll(this.videoConverterService.toListItem(entities));
		}
		return results;
	}

}
