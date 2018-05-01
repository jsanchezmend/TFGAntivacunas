package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;

/**
 * Video @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface VideoRepository extends Neo4jRepository<Video, String> {
	
	@Query("MATCH (v:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} RETURN v")
	List<Video> findByCrawlerId(Long crawlerId);
	
	@Query("MATCH (v:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} DETACH DELETE v")
	void removeByCrawlerId(Long crawlerId);
	
}
