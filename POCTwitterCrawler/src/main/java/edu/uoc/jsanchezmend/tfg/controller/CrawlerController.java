package edu.uoc.jsanchezmend.tfg.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

@Controller
@RequestMapping("/crawler")
public class CrawlerController {

	private Twitter twitter;
	private DB mongoDB;

	@Inject
	public CrawlerController(Twitter twitter, DB mongoDB) {
		this.twitter = twitter;
		this.mongoDB = mongoDB;
	}

	@ResponseBody
	@RequestMapping(value = "/{keyword}/{count}", method = RequestMethod.GET)
	public Boolean keywordCrawler(@PathVariable(value = "keyword", required = true) String keyword,
			@PathVariable(value = "count", required = true) int count) {
		final DBCollection items = this.connectdb(keyword);
		
		System.out.println("Getting " + count + " Tweets for keyword [" + keyword + "]...");
		final SearchParameters searchParameters = new SearchParameters(keyword)
				.count(count)
				.includeEntities(true);
		final SearchResults result = twitter.searchOperations().search(searchParameters);
		final List<Tweet> tweets = result.getTweets();
		System.out.println("Results size=" + tweets.size());
		for (Tweet tweet : tweets) {
			BasicDBObject basicObj = new BasicDBObject();
            basicObj.put("tweet_ID", tweet.getId());
            basicObj.put("tweet_text", tweet.getText());
			basicObj.put("user_name", tweet.getUser().getScreenName());
			basicObj.put("retweet_count", tweet.getRetweetCount());
			basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());
			basicObj.put("source", tweet.getSource());
			basicObj.put("coordinates", tweet.getUser().getLocation());
			final Entities mentioned = tweet.getEntities();
            basicObj.put("tweet_mentioned_count", mentioned.getMentions().size());
			try {
				items.insert(basicObj);
			} catch (Exception e) {
				System.out.println("MongoDB Connection Error : " + e.getMessage());
			}
		}

		return true;
	}

	public DBCollection connectdb(String keyword) {
		DBCollection items = null;
		try {
			items = mongoDB.getCollection(keyword);

			// make the tweet_ID unique in the database
			BasicDBObject index = new BasicDBObject("tweet_ID", 1);
			items.createIndex(index, new BasicDBObject("unique", true));
		} catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		
		return items;
	}
	
}