package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.YouTubeSearchService;

/**
 * @YouTubeSearchService service implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("youTubeSearchService")
public class YouTubeSearchServiceImpl implements YouTubeSearchService {
	
	// TODO: Add search default values defined by Johanna!!
	
	protected static final String SEARCH_PARTS = "id";
	protected static final String SEARCH_FIELDS = "items(id/videoId)";
	protected static final String SEARCH_TYPE = "video";
	protected static final Long SEARCH_DEFAULT_COUNT = 50L;
	
	protected static final String VIDEOS_PARTS = "snippet, contentDetails, recordingDetails, statistics, player";
	protected static final String VIDEOS_FIELDS = "items("
    		+ "id,"
    		+ "snippet/title,"
    		+ "snippet/description,"
    		+ "snippet/publishedAt,"
    		+ "snippet/channelId,"
    		+ "contentDetails/duration,"
    		+ "statistics/viewCount,"
    		+ "statistics/likeCount,"
    		+ "statistics/dislikeCount,"
    		+ "statistics/commentCount,"
    		+ "player/embedHtml"
		+ ")";
	
	protected static final String CHANNEL_PARTS = "id,snippet,statistics";
	protected static final String CHANNEL_FIELDS = "items("
    		+ "id,"
    		+ "snippet/title,"
    		+ "snippet/description,"
    		+ "snippet/publishedAt,"
    		+ "snippet/customUrl,"
    		+ "snippet/thumbnails/default/url,"
    		+ "statistics/viewCount,"
    		+ "statistics/commentCount,"
    		+ "statistics/subscriberCount,"
    		+ "statistics/videoCount"
		+ ")";
		
	@Value("${youtube.api.key}")
	private String apiKey;
	
	@Value("${youtube.search.language}")
	private String relevanceLanguage;

	@Autowired
	private YouTube youtube;

	
	@Override
	public List<SearchResult> searchVideos(String keyword, DateTime fromDateTime, DateTime toDateTime, String order, String pageToken, Long count) throws IOException {
		List<SearchResult> searchResultList = null;
		
        final YouTube.Search.List search = youtube.search().list(SEARCH_PARTS);      
        search.setKey(this.apiKey);
        search.setType(SEARCH_TYPE);
        search.setFields(SEARCH_FIELDS);
        search.setRelevanceLanguage(this.relevanceLanguage);
        search.setQ(keyword);
        if(fromDateTime != null) {
        	search.setPublishedAfter(fromDateTime);
        }
        if(toDateTime != null) {
        	search.setPublishedBefore(toDateTime);
        }
        if(order != null) {
        	search.setOrder(order);        	
        }
        if(pageToken != null) {
        	search.setPageToken(pageToken);
        }
        final Long searchCount = count != null ? count : SEARCH_DEFAULT_COUNT;
        search.setMaxResults(searchCount);
        
        final SearchListResponse searchResponse = search.execute();
        if(searchResponse != null) {
        	searchResultList = searchResponse.getItems();
        }
  
        return searchResultList;
	}

	@Override
	public List<Video> findVideos(String videoIds) throws IOException {
		List<Video> videos = null;
		
		final YouTube.Videos.List videosListMultipleIdsRequest = youtube.videos().list(VIDEOS_PARTS);
        videosListMultipleIdsRequest.setKey(this.apiKey);
        videosListMultipleIdsRequest.setFields(VIDEOS_FIELDS);
        videosListMultipleIdsRequest.setId(videoIds);
        
        final VideoListResponse videoListResponse = videosListMultipleIdsRequest.execute();
        if(videoListResponse != null) {
        	videos = videoListResponse.getItems();
        }
 
        return videos;
	}

	@Override
	public Channel findChannel(String channelId) throws IOException {
		Channel channel = null;
		
		final YouTube.Channels.List channelsListByIdRequest = youtube.channels().list(CHANNEL_PARTS);
		channelsListByIdRequest.setKey(this.apiKey);
		channelsListByIdRequest.setFields(CHANNEL_FIELDS);
	    channelsListByIdRequest.setId(channelId);
	    
	    final ChannelListResponse channelListResponse = channelsListByIdRequest.execute();
	    if(channelListResponse != null) {
	    	final List<Channel> channels = channelListResponse.getItems();
	    	if(channels != null) {
	    		channel = channels.get(0);
	    	}
	    }
	   
	    return channel;
	}

}
