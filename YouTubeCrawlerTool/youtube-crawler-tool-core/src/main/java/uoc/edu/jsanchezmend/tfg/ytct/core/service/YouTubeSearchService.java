package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.io.IOException;
import java.util.List;

import com.google.api.client.util.DateTime;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

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
	 * Given a list of video ids, returns a list of @VideoItem
	 * See https://developers.google.com/youtube/v3/docs/videos/list
	 * 
	 * @param videoIds
	 * @return
	 * @throws IOException
	 */
	List<VideoItem> findVideos(List<String> videoIds) throws IOException;
		
	/**
	 * Given a channel id, returns a @Channel
	 * See https://developers.google.com/youtube/v3/docs/channels/list
	 * 
	 * @param channelId
	 * @return
	 * @throws IOException
	 */
	ChannelItem findChannel(String channelId) throws IOException;

}
