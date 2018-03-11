package edu.uoc.jsanchezmend.tfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@Configuration
public class MongoDBConfiguration {
	
	@Bean
	@SuppressWarnings("deprecation")
	public DB mongoDB() throws MongoException {
		System.out.println("Connecting to Mongo DB..");
		final Mongo mongo = new Mongo("127.0.0.1");
		return mongo.getDB("POCTwitterCrawler");
	}

}
