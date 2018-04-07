package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Captions.Download;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoPlayer;
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
	        	this.saveVideoInfo(keyword, searchVideoIds);	
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
	        	this.saveVideoInfo(keyword, searchVideoIds);	
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
	        	this.saveVideoInfo(relatedToVideoId, searchVideoIds);	
	        }
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return false;
        }  

		return true;
	}
	
	private void saveVideoInfo(final String keyword, final String searchVideoIds) throws IOException {
		final DBCollection items = this.connectdb(keyword);
		
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
        videosListMultipleIdsRequest.setId(searchVideoIds);
        
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
			// Download asrCaptions: Not allowed for non video owners
			//basicObj.put("asrCaption", this.dowloadASRCaption(video.getId()));
			try {
				items.insert(basicObj);
			} catch (Exception e) {
				System.out.println("MongoDB Connection Error : " + e.getMessage());
			}
        }
	}

	/**
	 * Downloads a caption track for a YouTube video. (captions.download)
	 *
	 * @param captionId
	 *            The id parameter specifies the caption ID for the resource
	 *            that is being downloaded. In a caption resource, the id
	 *            property specifies the caption track's ID.
	 * @throws IOException
	 */
	private String dowloadASRCaption(final String videoId) throws IOException {
		final String asrCaptionId = this.getASRCaptionId(videoId);
		if (asrCaptionId == null) {
			return null;
		}

		// Create an API request to the YouTube Data API's captions.download
		// method to download an existing caption track.
		final Download captionDownload = youtube.captions().download(asrCaptionId).setTfmt("srt");
		captionDownload.setKey(apiKey);
	
		// Set the download type and add an event listener.
		final MediaHttpDownloader downloader = captionDownload.getMediaHttpDownloader();
		// Indicate whether direct media download is enabled. A value of
		// "True" indicates that direct media download is enabled and that
		// the entire media content will be downloaded in a single request.
		// A value of "False," which is the default, indicates that the
		// request will use the resumable media download protocol, which
		// supports the ability to resume a download operation after a
		// network interruption or other transmission failure, saving
		// time and bandwidth in the event of network failures.
		downloader.setDirectDownloadEnabled(false);

		// Set the download state for the caption track file.
		final MediaHttpDownloaderProgressListener downloadProgressListener = new MediaHttpDownloaderProgressListener() {
			@Override
			public void progressChanged(MediaHttpDownloader downloader) throws IOException {
				switch (downloader.getDownloadState()) {
				case MEDIA_IN_PROGRESS:
					System.out.println("Download in progress");
					System.out.println("Download percentage: " + downloader.getProgress());
					break;
				// This value is set after the entire media file has
				// been successfully downloaded.
				case MEDIA_COMPLETE:
					System.out.println("Download Completed!");
					break;
				// This value indicates that the download process has
				// not started yet.
				case NOT_STARTED:
					System.out.println("Download Not Started!");
					break;
				}
			}
		};
		downloader.setProgressListener(downloadProgressListener);

		final OutputStream outputByteArray = new ByteArrayOutputStream();
		// Download the caption track.
		captionDownload.executeAndDownloadTo(outputByteArray);
		final String asrCaption = outputByteArray.toString();
		return asrCaption;
	}

	private String getASRCaptionId(final String videoId) throws IOException {
		 final YouTube.Captions.List captionsListRequest = youtube.captions().list("id,snippet", videoId);
		 captionsListRequest.setKey(apiKey);
		 captionsListRequest.setFields("items("
         		+ "id,"
         		+ "snippet/trackKind"
     		+ ")");
	     final CaptionListResponse captionListResponse = captionsListRequest.execute();
	     final List<Caption> videoCaptions = captionListResponse.getItems();
	     for(Caption videoCaption : videoCaptions) {
	    	 final CaptionSnippet snippet = videoCaption.getSnippet();
	    	 if(snippet != null) {
	    		 final String trackKind = snippet.getTrackKind();
	    		 if(trackKind != null && trackKind.equals("ASR")) {
	    			 return videoCaption.getId();
	    		 }
	    	 }
	     }
	     return null;
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