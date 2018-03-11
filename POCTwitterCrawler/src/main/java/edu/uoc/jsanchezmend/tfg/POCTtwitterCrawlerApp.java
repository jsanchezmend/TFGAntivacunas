package edu.uoc.jsanchezmend.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MongoDBConfiguration.class)
public class POCTtwitterCrawlerApp {

    public static void main(String[] args) {
        SpringApplication.run(POCTtwitterCrawlerApp.class, args);
    }

}