package edu.uoc.jsanchezmend.tfg.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MongoDBConfiguration.class, YouTubeConfiguration.class, JsonConfiguration.class})
public class POCYoutubeCrawlerApp extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(POCYoutubeCrawlerApp.class);
	}

    public static void main(String[] args) {
        SpringApplication.run(POCYoutubeCrawlerApp.class, args);
    }

}