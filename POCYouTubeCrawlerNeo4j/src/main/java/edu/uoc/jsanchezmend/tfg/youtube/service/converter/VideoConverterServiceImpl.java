package edu.uoc.jsanchezmend.tfg.youtube.service.converter;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoPlayer;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;

import edu.uoc.jsanchezmend.tfg.youtube.data.VideoEntity;
import edu.uoc.jsanchezmend.tfg.youtube.service.converter.base.ConverterService;

@Service("videoConverterService")
public class VideoConverterServiceImpl implements ConverterService<VideoEntity, Video>{
	
	@Override
	public VideoEntity toEntity(Video video) {
		final VideoEntity entity = new VideoEntity();
		
		final VideoSnippet snippet = video.getSnippet();
    	final VideoStatistics statistics = video.getStatistics();
    	final VideoContentDetails contentDetails = video.getContentDetails();
    	final VideoPlayer player = video.getPlayer();
    	
    	entity.setId(video.getId());
    	if(snippet != null) {
    		entity.setTitle(snippet.getTitle());
    		entity.setDescription(snippet.getDescription());
    		entity.setPublishedAt(new Date(snippet.getPublishedAt().getValue()));
		}
		if(contentDetails != null) {
			entity.setDuration(contentDetails.getDuration());
		}
		if(statistics != null) {
			entity.setViewCount(statistics.getViewCount());
			entity.setLikeCount(statistics.getLikeCount());
			entity.setDislikeCount(statistics.getDislikeCount());
			entity.setCommentCount(statistics.getCommentCount());
		}
		if(player != null) {
			entity.setEmbedHtml(player.getEmbedHtml());
		}		
		
		return entity;
	}

}