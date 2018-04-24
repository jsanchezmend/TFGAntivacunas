package uoc.edu.jsanchezmend.tfg.ytct.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * 
 * @author jsanchezmend
 *
 */
@SpringBootApplication
@EnableNeo4jRepositories("uoc.edu.jsanchezmend.tfg.ytct.data.repository")
public class YouTubeCrawlerToolApp extends SpringBootServletInitializer {
	
	private final static Logger log = LoggerFactory.getLogger(YouTubeCrawlerToolApp.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(YouTubeCrawlerToolApp.class);
	}

    public static void main(String[] args) {
    	log.info("Starting YouTubeCrawlerToolApp...");
        SpringApplication.run(YouTubeCrawlerToolApp.class, args);
    }

}