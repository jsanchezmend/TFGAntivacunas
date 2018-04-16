package edu.uoc.jsanchezmend.tfg.youtube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("edu.uoc.jsanchezmend.tfg.youtube.data.repository")
@Import({YouTubeConfiguration.class, JsonConfiguration.class})
public class POCYoutubeCrawlerNeo4jApp extends SpringBootServletInitializer {
	
	private final static Logger log = LoggerFactory.getLogger(POCYoutubeCrawlerNeo4jApp.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(POCYoutubeCrawlerNeo4jApp.class);
	}

    public static void main(String[] args) {
    	log.info("Starting POCYoutubeCrawlerNeo4jApp...");
        SpringApplication.run(POCYoutubeCrawlerNeo4jApp.class, args);
    }

}