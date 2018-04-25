package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;

/**
 * Crawler @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface CrawlerRepository extends Neo4jRepository<Crawler, Long> {

}
