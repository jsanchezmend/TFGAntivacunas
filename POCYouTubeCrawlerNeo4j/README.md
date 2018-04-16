# TFGAntivacunas - POCYouTubeCrawlerNeo4j

YouTube crawler prove of concept with Neo4j graph database

-Start application:
mvn spring-boot:run

- See a graph:
http://localhost:8080/

-Run a new crawler:
http://localhost:8080/crawler/{keyword}/{count}

-Find related videos:
http://localhost:8080/crawler/related/{videoId}/{count}
