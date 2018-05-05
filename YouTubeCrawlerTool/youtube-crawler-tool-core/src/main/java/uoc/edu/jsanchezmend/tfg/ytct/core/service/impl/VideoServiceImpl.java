package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.VideoService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.VideoStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.CategoryRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.ChannelRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.VideoRepository;

/**
 * @VideoService implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	

	@Override
	public List<VideoItem> listVideos() {
		final Iterable<Video> videoIterable = this.videoRepository.findAll();
		final List<VideoItem> result = this.videoConverterService.toListItem(videoIterable);
		return result;
	}

	@Override
	public VideoStatsItem getVideosStats() {
		VideoStatsItem videoStatsItem = null;
		// TODO 
		return videoStatsItem;
	}

	@Override
	public VideoItem getVideo(String id) {
		final Video video = this.videoRepository.findById(id).orElse(null);
		final VideoItem result = this.videoConverterService.toItem(video);
		return result;
	}

	@Override
	public VideoItem editVideoCategory(String id, String newCategoryName) {
		VideoItem result = null;
		
		Video video = this.videoRepository.findById(id).orElse(null);
		final Category category = this.categoryRepository.findById(newCategoryName).orElse(null);
		if(video != null && category != null) {
			video.setCategory(category);
			video = this.videoRepository.save(video);
			result = this.videoConverterService.toItem(video);
		}

		return result;
	}

	@Override
	public VideoItem deleteVideo(String id) {
		VideoItem result = null;
		
		final Video video = this.videoRepository.findById(id).orElse(null);
		if(video != null) {
			this.videoRepository.delete(video);
			// Delete orphan channels 
			this.channelRepository.removeOrphanChannels();
			result = this.videoConverterService.toItem(video);
		}

		return result;
	}

	@Override
	public List<VideoItem> getRelatedVideos(String videoId) {
		final List<VideoItem> results = new ArrayList<VideoItem>();
		final List<Video> entities = this.videoRepository.findRelatedVideos(videoId);
		if(entities != null && !entities.isEmpty()) {
			results.addAll(this.videoConverterService.toListItem(entities));
		}
		return results;
	}
	
}
