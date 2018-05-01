package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.base.Joiner;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.YouTubeSearchService;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.YouTubeSearchResponseItem;

/**
 * @YouTubeSearchService service implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("youTubeSearchService")
public class YouTubeSearchServiceImpl implements YouTubeSearchService {
	
	private final static Logger log = LoggerFactory.getLogger(YouTubeSearchServiceImpl.class);
	
	protected static final String SEARCH_PARTS = "snippet, id";
	protected static final String SEARCH_TYPE = "video";
	protected static final Long SEARCH_MAX_COUNT = 50L;
	
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

	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<Video, uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video, VideoItem> videoConverterService;
	
	@Autowired
	@Qualifier("channelConverterService")
	private YouTubeConverterService<Channel, uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel, ChannelItem> channelConverterService;

	
	@Override
	public YouTubeSearchResponseItem searchVideos(String keyword, DateTime fromDateTime, DateTime toDateTime, CrawlerOrderByEnum order, String pageToken, Long count) throws IOException {
        return this.performSearchOperation(keyword, null, fromDateTime, toDateTime, order, pageToken, count);
	}
	
	@Override
	public YouTubeSearchResponseItem searchRelatedVideos(String videoId, String pageToken, Long count) throws IOException {       
        return this.performSearchOperation(null, videoId, null, null, null, pageToken, count);
	}
	
	/**
	 * Given a search parameters, returns a list of @List<String> that contains a @Video ids 
	 * At least, 'keyword' or 'videoId' parameter should be provided, but not both.
	 * Note that if it is a related by id search (using the 'videoId' parameter), the only other supported parameters are:
	 * 	- count
	 * 	- pageToken
	 * 
	 * @param keyword
	 * @param videoId
	 * @param fromDateTime
	 * @param toDateTime
	 * @param order
	 * @param pageToken
	 * @param count
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 */
	private YouTubeSearchResponseItem performSearchOperation(String keyword, String videoId, DateTime fromDateTime, DateTime toDateTime, CrawlerOrderByEnum order, String pageToken, Long count) throws IOException, UnsupportedOperationException {
		final YouTubeSearchResponseItem result = new YouTubeSearchResponseItem();
		
		// General search criteria validations
		if(keyword != null && videoId != null) {
			throw new UnsupportedOperationException("Search criteria must be 'keyword' or 'videoId', but not both!");
		} else if(keyword == null && videoId == null) {
			throw new UnsupportedOperationException("Search criteria must contain 'keyword' or 'videoId' parameter!");
		}
		// Search by keyword validations
		if(keyword != null) {
			if(fromDateTime != null && toDateTime != null) {
				if(toDateTime.getValue() < fromDateTime.getValue()) {
					throw new UnsupportedOperationException("Search criteria 'toDateTime' must be after 'fromDateTime'");
				}
			}
		// Search by related video validations
		} else if(videoId != null) {
			if(fromDateTime != null) {
				throw new UnsupportedOperationException("Search by related videos doesn't allow 'fromDateTime' parameter");
			}
			if(toDateTime != null) {
				throw new UnsupportedOperationException("Search by related videos doesn't allow 'toDateTime' parameter");
			}
			if(order != null) {
				throw new UnsupportedOperationException("Search by related videos doesn't allow 'order' parameter");
			}
		}
				
		// Create a new search
        final YouTube.Search.List search = youtube.search().list(SEARCH_PARTS);      
        search.setKey(this.apiKey);
        search.setType(SEARCH_TYPE);
        search.setRelevanceLanguage(this.relevanceLanguage);
        
        // Set search criteria
        if(keyword != null) {
        	log.info("New video search using keyword: " + keyword);
        	search.setQ(keyword);
            if(fromDateTime != null) {
            	search.setPublishedAfter(fromDateTime);
            }
            if(toDateTime != null) {
            	search.setPublishedBefore(toDateTime);
            }
            if(order != null) {
            	search.setOrder(order.getValue());        	
            }
        } else if(videoId != null) {
        	log.info("New realted video search using videoId: " + videoId);
        	search.setRelatedToVideoId(videoId);
        }
        if(pageToken != null) {
        	search.setPageToken(pageToken);
        }
        final Long searchCount = count != null && count.compareTo(SEARCH_MAX_COUNT) < 0  ? count : SEARCH_MAX_COUNT;
        search.setMaxResults(searchCount);
        
        // Execute the search and obtain the results
        final SearchListResponse searchResponse = search.execute();
        result.setNextPageToken(searchResponse.getNextPageToken());
        result.setPrevPageToken(searchResponse.getPrevPageToken());
        if(searchResponse.getPageInfo() != null) {
        	result.setTotalResults(searchResponse.getPageInfo().getTotalResults());
        }
        final List<String> videoIds = new ArrayList<String>();
        if(searchResponse != null) {
        	final List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	        	log.info("Video search results found: " + searchResultList.size());
	        	for (SearchResult searchResult : searchResultList) {
	    			final ResourceId id = searchResult.getId();
	    			videoIds.add(id.getVideoId());
	    		}
	        }
        }
        result.setItems(videoIds);
        return result;
	}

	@Override
	public List<VideoItem> findVideos(List<String> videoIds) throws IOException {
		log.info("New find for " + videoIds.size() + " videos");
		List<VideoItem> videoItems = null;
		
		// Convert a @List<String> to a comma separate string ready to be used as a search criteria
		final Joiner stringJoiner = Joiner.on(',');
    	final String searchVideoIds = stringJoiner.join(videoIds);
		
		// Create a new videos search
		final YouTube.Videos.List videosListMultipleIdsRequest = youtube.videos().list(VIDEOS_PARTS);
        videosListMultipleIdsRequest.setKey(this.apiKey);
        videosListMultipleIdsRequest.setFields(VIDEOS_FIELDS);
        videosListMultipleIdsRequest.setId(searchVideoIds);
        
        // Execute the search and obtain the results
        final VideoListResponse videoListResponse = videosListMultipleIdsRequest.execute();
        if(videoListResponse != null) {
        	final List<Video> videos = videoListResponse.getItems();
        	videoItems = videoConverterService.fromYouTubeListToListItem(videos);
        	log.info("Finded videos: " + videoItems.size());
        }
 
        return videoItems;
	}
	
	@Override
	public ChannelItem findChannel(String channelId) throws IOException {
		log.info("New find for " + channelId + " channel");
		ChannelItem channelItem = null;
		
		// Create a new channel search
		final YouTube.Channels.List channelsListByIdRequest = youtube.channels().list(CHANNEL_PARTS);
		channelsListByIdRequest.setKey(this.apiKey);
		channelsListByIdRequest.setFields(CHANNEL_FIELDS);
	    channelsListByIdRequest.setId(channelId);
	 
	    // Execute the search and obtain the results
	    final ChannelListResponse channelListResponse = channelsListByIdRequest.execute();
	    if(channelListResponse != null) {
	    	final List<Channel> channels = channelListResponse.getItems();
	    	if(channels != null) {
	    		final Channel channel = channels.get(0);
	    		if(channel != null) {
	    			channelItem = channelConverterService.fromYouTubeToItem(channel);
	    			log.info("Finded channel: " + channelId);
	    		}
	    	}
	    }
	   
	    return channelItem;
	}

}
