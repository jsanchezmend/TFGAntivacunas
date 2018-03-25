package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.io.IOException;
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
import com.google.api.services.youtube.model.SearchResultSnippet;
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
	        final YouTube.Search.List search = youtube.search().list("id,snippet");      
	        // Set your developer key from the {{ Google Cloud Console }} for non-authenticated requests.
	        search.setKey(apiKey);
	        search.setQ(keyword);
	        // Restrict the search results to only include videos.
	        search.setType("video");
	        // To increase efficiency, only retrieve the fields that the application uses.
	        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description)");
	        search.setMaxResults(new Long(count));
	        
	        // Call the API and save results.
	        final SearchListResponse searchResponse = search.execute();
	        final List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	        	System.out.println("Results size=" + searchResultList.size());
	    		for (SearchResult searchResult : searchResultList) { 	
	    			final ResourceId id = searchResult.getId();
	    			final SearchResultSnippet snippet = searchResult.getSnippet();	
	    			// Confirm that the result represents a video.
	                if (id.getKind().equals("youtube#video")) {
		    			final BasicDBObject basicObj = new BasicDBObject();
		    			basicObj.put("video_ID", id.getVideoId());
		                basicObj.put("video_title", snippet.getTitle());
		                basicObj.put("video_description", snippet.getDescription());
		    			try {
		    				items.insert(basicObj);
		    			} catch (Exception e) {
		    				System.out.println("MongoDB Connection Error : " + e.getMessage());
		    			}
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
			
			// make the video_ID unique in the database
			BasicDBObject index = new BasicDBObject("video_ID", 1);
			items.createIndex(index, new BasicDBObject("unique", true));
		} catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		
		return items;
	}
	
}