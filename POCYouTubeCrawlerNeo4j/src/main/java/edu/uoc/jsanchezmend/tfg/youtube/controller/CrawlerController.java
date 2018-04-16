package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.common.base.Joiner;

import edu.uoc.jsanchezmend.tfg.youtube.data.ChannelEntity;
import edu.uoc.jsanchezmend.tfg.youtube.data.VideoEntity;
import edu.uoc.jsanchezmend.tfg.youtube.data.repository.ChannelRepository;
import edu.uoc.jsanchezmend.tfg.youtube.data.repository.VideoRepository;
import edu.uoc.jsanchezmend.tfg.youtube.service.converter.base.ConverterService;

@Controller
@RequestMapping("/crawler")
public class CrawlerController {
	
	@Value("${youtube.api.key}")
	private String apiKey;

	@Autowired
	private YouTube youtube;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	@Qualifier("videoConverterService")
	protected ConverterService<VideoEntity, Video> videoConverterService;
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	@Qualifier("channelConverterService")
	protected ConverterService<ChannelEntity, Channel> channelConverterService;

	
	@ResponseBody
	@RequestMapping(value = "/{keyword}/{count}", method = RequestMethod.GET)
	public Boolean keywordCrawler(@PathVariable(value = "keyword", required = true) String keyword,
			@PathVariable(value = "count", required = true) int count) {				
		try {
			System.out.println("Getting " + count + " Youtube videos for keyword [" + keyword + "]...");
			
			// Define the API request for retrieving search results.
	        final YouTube.Search.List search = youtube.search().list("id");      
	        // Set your developer key from the {{ Google Cloud Console }} for non-authenticated requests.
	        search.setKey(apiKey);
	        search.setQ(keyword);
	        // Restrict the search results to only include videos.
	        search.setType("video");
	        // To increase efficiency, only retrieve the fields that the application uses.
	        search.setFields("items(id/videoId)");
	        search.setMaxResults(new Long(count));
	        
	        // Call the API and save results.
	        final SearchListResponse searchResponse = search.execute();
	        final List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	        	System.out.println("Results size=" + searchResultList.size());
	        	// Merge video IDs
	        	final List<String> videoIds = new ArrayList<String>();
	        	for (SearchResult searchResult : searchResultList) {
	    			final ResourceId id = searchResult.getId();
	    			videoIds.add(id.getVideoId());
	    		}
	        	final Joiner stringJoiner = Joiner.on(',');
	        	final String searchVideoIds = stringJoiner.join(videoIds);
	        	// Retrieve related id videos and persist their info
	        	this.saveVideoInfo(false, keyword, searchVideoIds);	
	        }
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return false;
        }  

		return true;
	}
	
	/**
	 * 
	 * @param keyword
	 * @param from [MM/dd/yyyy]
	 * @param to [MM/dd/yyyy]
	 * @param order [date, rating, relevance, title, videoCount, viewCount]
	 * @param count
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{keyword}/{from}/{to}/{order}/{count}", method = RequestMethod.GET)
	public Boolean keywordFiltersCrawler(@PathVariable(value = "keyword", required = true) String keyword,
			@PathVariable(value = "from", required = true) String from,
			@PathVariable(value = "to", required = true) String to,
			@PathVariable(value = "order", required = true) String order,
			@PathVariable(value = "count", required = true) int count) {				
		try {
			System.out.println("Getting " + count + " Youtube videos for keyword [" + keyword + "]...");
			
			// Define the API request for retrieving search results.
	        final YouTube.Search.List search = youtube.search().list("id");      
	        // Set your developer key from the {{ Google Cloud Console }} for non-authenticated requests.
	        search.setKey(apiKey);
	        search.setQ(keyword);
	        search.setPublishedAfter(this.getDateTimeFromString(from));
	        search.setPublishedBefore(this.getDateTimeFromString(to));
	        search.setOrder(order);
	        // Restrict the search results to only include videos.
	        search.setType("video");
	        // To increase efficiency, only retrieve the fields that the application uses.
	        search.setFields("items(id/videoId)");
	        search.setMaxResults(new Long(count));
	        
	        // Call the API and save results.
	        final SearchListResponse searchResponse = search.execute();
	        final List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	        	System.out.println("Results size=" + searchResultList.size());
	        	// Merge video IDs
	        	final List<String> videoIds = new ArrayList<String>();
	        	for (SearchResult searchResult : searchResultList) {
	    			final ResourceId id = searchResult.getId();
	    			videoIds.add(id.getVideoId());
	    		}
	        	final Joiner stringJoiner = Joiner.on(',');
	        	final String searchVideoIds = stringJoiner.join(videoIds);
	        	// Retrieve related id videos and persist their info
	        	this.saveVideoInfo(false, keyword, searchVideoIds);	
	        }
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return false;
        }  

		return true;
	}
	
	private DateTime getDateTimeFromString(String strDate) {
		DateTime dt = null;
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			final Date date = simpleDateFormat.parse(strDate);
			dt = new DateTime(date);
		} catch (ParseException e) {
			System.err.println("There was an error parsing to date: " + strDate);
            return null;
		}
		return dt;
	}
	
	@ResponseBody
	@RequestMapping(value = "/related/{relatedToVideoId}/{count}", method = RequestMethod.GET)
	public Boolean relatedToVideoIdCrawler(@PathVariable(value = "relatedToVideoId", required = true) String relatedToVideoId,
			@PathVariable(value = "count", required = true) int count) {	
		try {
			System.out.println("Getting " + count + " Youtube related videos for vieoId [" + relatedToVideoId + "]...");
			
			//HOT FIX to display the graph correctly with relations!!!!!
			this.saveVideoToFindRelatedVideos(relatedToVideoId);
			
			// Define the API request for retrieving search results.
	        final YouTube.Search.List search = youtube.search().list("id");      
	        // Set your developer key from the {{ Google Cloud Console }} for non-authenticated requests.
	        search.setKey(apiKey);
	        search.setRelatedToVideoId(relatedToVideoId);
	        // Restrict the search results to only include videos.
	        search.setType("video");
	        // To increase efficiency, only retrieve the fields that the application uses.
	        search.setFields("items(id/videoId)");
	        search.setMaxResults(new Long(count));
	        
	        // Call the API and save results.
	        final SearchListResponse searchResponse = search.execute();
	        final List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	        	System.out.println("Results size=" + searchResultList.size());
	        	// Merge video IDs
	        	final List<String> videoIds = new ArrayList<String>();
	        	for (SearchResult searchResult : searchResultList) {
	    			final ResourceId id = searchResult.getId();
	    			videoIds.add(id.getVideoId());
	    		}
	        	final Joiner stringJoiner = Joiner.on(',');
	        	final String searchVideoIds = stringJoiner.join(videoIds);
	        	// Retrieve related id videos and persist their info
	        	this.saveVideoInfo(true, relatedToVideoId, searchVideoIds);	
	        }
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return false;
        }  

		return true;
	}
	
	/**
	 * HOT FIX!!!
	 * 
	 * @param string
	 * @throws IOException 
	 */
	private void saveVideoToFindRelatedVideos(String relatedToVideoId) throws IOException {			
		// Call the YouTube Data API's youtube.videos.list method to 
    	// retrieve the resources that represent the specified videos.
        final YouTube.Videos.List videosListMultipleIdsRequest = youtube.videos().list("snippet, contentDetails, recordingDetails, statistics, player");
        videosListMultipleIdsRequest.setKey(apiKey);
        // To increase efficiency, only retrieve the fields that the application uses.
        videosListMultipleIdsRequest.setFields("items("
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
        		+ ")");
        videosListMultipleIdsRequest.setId(relatedToVideoId);
        
        final VideoListResponse videoListResponse = videosListMultipleIdsRequest.execute();
        final List<Video> videos = videoListResponse.getItems();
        for(Video video : videos) {
        	final VideoEntity videoEntity = videoConverterService.toEntity(video);
        	final VideoSnippet snippet = video.getSnippet();
            final String channelId = snippet.getChannelId();
            // Save the video channel if not exist in the DB and calc the "scopeRange" (this is only a possible implementation example!)
            final ChannelEntity channelEntity = this.getChannel(channelId);
            videoEntity.setChannel(channelEntity);
            final BigInteger scopeRange = channelEntity.getSubscriberCount().divide(channelEntity.getVideoCount()).multiply(videoEntity.getViewCount());
            videoEntity.setScopeRange(scopeRange);
			try {
				System.out.println("Saving the original video to find relations!!");
				videoRepository.save(videoEntity);
			} catch (Exception e) {
				System.out.println("Connection Error : " + e.getMessage());
			}
        }
		
	}

	private void saveVideoInfo(boolean related, final String keyword, final String searchVideoIds) throws IOException {		
		// Call the YouTube Data API's youtube.videos.list method to 
    	// retrieve the resources that represent the specified videos.
        final YouTube.Videos.List videosListMultipleIdsRequest = youtube.videos().list("snippet, contentDetails, recordingDetails, statistics, player");
        videosListMultipleIdsRequest.setKey(apiKey);
        // To increase efficiency, only retrieve the fields that the application uses.
        videosListMultipleIdsRequest.setFields("items("
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
        		+ ")");
        videosListMultipleIdsRequest.setId(searchVideoIds);
        
        final VideoListResponse videoListResponse = videosListMultipleIdsRequest.execute();
        final List<Video> videos = videoListResponse.getItems();
        for(Video video : videos) {
        	final VideoEntity videoEntity = videoConverterService.toEntity(video);
        	final VideoSnippet snippet = video.getSnippet();
            final String channelId = snippet.getChannelId();
            // Save the video channel if not exist in the DB and calc the "scopeRange" (this is only a possible implementation example!)
            final ChannelEntity channelEntity = this.getChannel(channelId);
            videoEntity.setChannel(channelEntity);
            final BigInteger scopeRange = channelEntity.getSubscriberCount().divide(channelEntity.getVideoCount()).multiply(videoEntity.getViewCount());
            videoEntity.setScopeRange(scopeRange);
            
			try {
				videoRepository.save(videoEntity);
	            if(related) {
					this.createRelation(keyword, videoEntity);
				}
			} catch (Exception e) {
				System.out.println("MongoDB Connection Error : " + e.getMessage());
			}
        }
	}
	
	private void createRelation(String sourceId, VideoEntity target) {
		try {
			final Optional<VideoEntity> source = videoRepository.findById(sourceId);
			if(source.isPresent()) {
				source.get().addRelated(target);
				videoRepository.save(source.get());
			}
		} catch (Exception e) {
			System.out.println("Connection Error : " + e.getMessage());
		}
	}
	
	private ChannelEntity getChannel(final String channelId) throws IOException {
		ChannelEntity channelEntity = null;

		final Optional<ChannelEntity> dbChannel = channelRepository.findById(channelId);
		if(dbChannel.isPresent()) {
			System.out.println("Video channel read from DB");
			channelEntity = dbChannel.get();
		} else {
			final Channel channel = this.getChannelFromAPI(channelId);
			if(channel != null) {
				System.out.println("Video channel read from API");
				channelEntity = channelConverterService.toEntity(channel);
			}
		}
		if(channelEntity == null) {
			System.out.println("Video channel impossible to find");
		}
		
		return channelEntity;
	}

	private Channel getChannelFromAPI(final String channelId) throws IOException {
		final YouTube.Channels.List channelsListByIdRequest = youtube.channels().list("id,snippet,statistics");
		channelsListByIdRequest.setKey(apiKey);
        // To increase efficiency, only retrieve the fields that the application uses.
		channelsListByIdRequest.setFields("items("
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
        		+ ")");
	    channelsListByIdRequest.setId(channelId);
	    final ChannelListResponse channelListResponse = channelsListByIdRequest.execute();
	    final List<Channel> channels = channelListResponse.getItems();
	    final Channel channel = channels.get(0);
	    final ChannelEntity channelEntity = channelConverterService.toEntity(channel);

		try {
			channelRepository.save(channelEntity);
		} catch (Exception e) {
			System.out.println("MongoDB Connection Error : " + e.getMessage());
		}

	    return channel;
	}
	
}