package uoc.edu.jsanchezmend.tfg.ytct.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

/**
 * Configure YouTube service
 * 
 * @author jsanchezmend
 *
 */
@Configuration
public class YouTubeConfiguration {
	
	protected static final String YOUTUBE_APPLICATION_NAME="youtube-crawler-tool-app";
	
	@Bean
	public YouTube youTube() {
		// This object is used to make YouTube Data API requests.
		final YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), 
	    	    (request) -> {}).setApplicationName(YOUTUBE_APPLICATION_NAME).build();
		return youtube;
	}

}
