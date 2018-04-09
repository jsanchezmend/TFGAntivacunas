package edu.uoc.jsanchezmend.tfg.youtube;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

@Configuration
public class MongoDBConfiguration {
	
	@SuppressWarnings({ "resource", "deprecation" })
	@Bean
	public DB mongoDB() throws MongoException {
		System.out.println("Connecting to Mongo DB..");
		
		//final Mongo mongo = new Mongo("127.0.0.1");
		//return mongo.getDB("POCYoutubeCrawler");
		
		final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://youtubecrawlertooldb:iC76QMhIQ9rQ03V3pbBra5xKcsG8NbI9rZxBXL0f2VDHBv5IC84f5rgcTybr4krxGI0cl7fkhLc9anZh223sxQ==@youtubecrawlertooldb.documents.azure.com:10255/?ssl=true&replicaSet=globaldb"));
		return mongoClient.getDB("POCYoutubeCrawler");
	}

}
