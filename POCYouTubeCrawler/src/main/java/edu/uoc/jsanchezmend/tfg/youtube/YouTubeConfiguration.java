package edu.uoc.jsanchezmend.tfg.youtube;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

@Configuration
public class YouTubeConfiguration {
	
	@Bean
	public YouTube youTube() {
		// This object is used to make YouTube Data API requests.
		final YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), 
	    	    (request) -> {}).setApplicationName("poc-youtube-crawler-app").build();
		
		return youtube;
	}

}
