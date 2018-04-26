package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.io.IOException;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;

/**
 * Allows connection to the YouTube Data API to perform search and find operations
 * See https://developers.google.com/youtube/v3/docs/
 * 
 * @author jsanchezmend
 *
 */
public interface YouTubeSearchService {
	
	/**
	 * Given a search criteria returns a list of @Video ids
	 * See https://developers.google.com/youtube/v3/docs/search/list
	 * 
	 * @param keyword
	 * @param fromDateTime
	 * @param toDateTime
	 * @param order
	 * @param pageToken
	 * @param count
	 * @return
	 * @throws IOException
	 */
	List<String> searchVideos(String keyword, DateTime fromDateTime, DateTime toDateTime, CrawlerOrderByEnum order, String pageToken, Long count) throws IOException;
	
	
	/**
	 * Given a video id, returns a list of related @Video ids
	 * See https://developers.google.com/youtube/v3/docs/search/list
	 * (using 'relatedToVideoId' search criteria)
	 * 
	 * @param videoId
	 * @param pageToken
	 * @param count
	 * @return
	 * @throws IOException
	 */
	List<String> searchRelatedVideos(String videoId, String pageToken, Long count) throws IOException;
	
	/**
	 * Given a list of video ids, returns a set of @Video
	 * See https://developers.google.com/youtube/v3/docs/videos/list
	 * 
	 * @param videoIds
	 * @return
	 * @throws IOException
	 */
	List<Video> findVideos(List<String> videoIds) throws IOException;
		
	/**
	 * Given a channel id, returns a @Channel
	 * See https://developers.google.com/youtube/v3/docs/channels/list
	 * 
	 * @param channelId
	 * @return
	 * @throws IOException
	 */
	Channel findChannel(String channelId) throws IOException;

}
