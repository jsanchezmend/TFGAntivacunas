package edu.uoc.jsanchezmend.tfg.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MongoDBConfiguration.class, YouTubeConfiguration.class})
public class POCYoutubeCrawlerApp {

    public static void main(String[] args) {
        SpringApplication.run(POCYoutubeCrawlerApp.class, args);
    }

}