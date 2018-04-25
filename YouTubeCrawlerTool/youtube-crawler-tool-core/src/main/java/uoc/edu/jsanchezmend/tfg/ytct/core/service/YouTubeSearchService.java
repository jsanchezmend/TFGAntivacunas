package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.io.IOException;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

/**
 * Allows connection to the YouTube Data API to perform search operations
 * 
 * @author jsanchezmend
 *
 */
public interface YouTubeSearchService {
	
	/**
	 * Given a search criteria, returns a set of @SearchResult
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
	List<SearchResult> searchVideos(String keyword, DateTime fromDateTime, DateTime toDateTime, String order, String pageToken, Long count) throws IOException;
	
	/**
	 * Given a list of video ids, returns a set of @Video
	 * 
	 * @param videoIds
	 * @return
	 * @throws IOException
	 */
	List<Video> findVideos(String videoIds) throws IOException;
	
	/**
	 * Given a channel id, returns a @Channel
	 * 
	 * @param channelId
	 * @return
	 * @throws IOException
	 */
	Channel findChannel(String channelId) throws IOException;

}
