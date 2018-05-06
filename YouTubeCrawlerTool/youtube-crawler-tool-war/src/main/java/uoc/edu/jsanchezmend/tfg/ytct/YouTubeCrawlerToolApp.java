package uoc.edu.jsanchezmend.tfg.ytct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import uoc.edu.jsanchezmend.tfg.ytct.api.configuration.YouTubeCrawlerToolSecurityConfiguration;
import uoc.edu.jsanchezmend.tfg.ytct.core.configuration.AsyncExecutorConfiguration;
import uoc.edu.jsanchezmend.tfg.ytct.core.configuration.YouTubeConfiguration;

/**
 * 
 * @author jsanchezmend
 *
 */
@SpringBootApplication
@EnableNeo4jRepositories("uoc.edu.jsanchezmend.tfg.ytct.data.repository")
@Import({
	YouTubeConfiguration.class, 
	AsyncExecutorConfiguration.class, 
	YouTubeCrawlerToolSecurityConfiguration.class
})
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