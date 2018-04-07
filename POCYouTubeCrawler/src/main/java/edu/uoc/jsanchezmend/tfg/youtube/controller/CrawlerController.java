package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoPlayer;
import com.google.api.services.youtube.model.VideoRecordingDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

@Controller
@RequestMapping("/crawler")
public class CrawlerController {
	
	@Value("${youtube.api.key}")
	private String apiKey;

	private YouTube youtube;
	private DB mongoDB;

	@Inject
	public CrawlerController(YouTube youtube, DB mongoDB) {
		this.youtube = youtube;
		this.mongoDB = mongoDB;
	}

	@ResponseBody
	@RequestMapping(value = "/{keyword}/{count}", method = RequestMethod.GET)
	public Boolean keywordCrawler(@PathVariable(value = "keyword", required = true) String keyword,
			@PathVariable(value = "count", required = true) int count) {		
		final DBCollection items = this.connectdb(keyword);
		
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
	        	final String searchForvideoIds = stringJoiner.join(videoIds);

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
		            		+ "snippet/channelTitle,"
		            		+ "contentDetails/duration,"
		            		+ "recordingDetails/location,"
		            		+ "statistics/viewCount,"
		            		+ "statistics/likeCount,"
		            		+ "statistics/dislikeCount,"
		            		+ "statistics/commentCount,"
		            		+ "player/embedHtml"
	            		+ ")");
	            videosListMultipleIdsRequest.setId(searchForvideoIds);
	            
	            final VideoListResponse videoListResponse = videosListMultipleIdsRequest.execute();
	            final List<Video> videos = videoListResponse.getItems();
	            for(Video video : videos) {
	            	final VideoSnippet snippet = video.getSnippet();
	            	final VideoStatistics statistics = video.getStatistics();
	            	final VideoContentDetails contentDetails = video.getContentDetails();
	            	final VideoPlayer player = video.getPlayer();
	            	final BasicDBObject basicObj = new BasicDBObject();
	    			basicObj.put("videoId", video.getId());
	    			if(snippet != null) {
		                basicObj.put("title", snippet.getTitle());
		                basicObj.put("description", snippet.getDescription());
		                basicObj.put("publishedAt", snippet.getPublishedAt() != null ? snippet.getPublishedAt().toString() : null);
		                basicObj.put("channelId", snippet.getChannelId());
		                basicObj.put("channelTitle", snippet.getChannelTitle());
	    			}
	    			if(contentDetails != null) {
	                basicObj.put("duration", contentDetails.getDuration());
	    			}
	    			if(statistics != null) {
		                basicObj.put("viewCount", statistics.getViewCount() != null ? statistics.getViewCount().toString() : null);
		                basicObj.put("likeCount",  statistics.getLikeCount() != null ? statistics.getLikeCount().toString() : null);
		                basicObj.put("dislikeCount", statistics.getDislikeCount() != null ? statistics.getDislikeCount().toString() : null);
		                basicObj.put("commentCount", statistics.getCommentCount() != null ? statistics.getCommentCount().toString() : null);
	    			}
	    			if(player != null) {
	    				basicObj.put("embedHtml", player.getEmbedHtml());
	    			}
	    			try {
	    				items.insert(basicObj);
	    			} catch (Exception e) {
	    				System.out.println("MongoDB Connection Error : " + e.getMessage());
	    			}
	            }
	        }
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return false;
        }  

		return true;
	}

	public DBCollection connectdb(String keyword) {
		DBCollection items = null;
		try {
			items = mongoDB.getCollection(keyword);
			
			// make the "videoId" field as unique in the database
			BasicDBObject index = new BasicDBObject("videoId", 1);
			items.createIndex(index, new BasicDBObject("unique", true));
		} catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		
		return items;
	}
	
}