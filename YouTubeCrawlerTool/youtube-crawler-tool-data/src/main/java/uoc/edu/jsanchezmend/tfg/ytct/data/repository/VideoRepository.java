package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;

/**
 * Video @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface VideoRepository extends Neo4jRepository<Video, String> {
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} WITH video "
			+ "MATCH v=(video)-[r*0..1]-() RETURN video, nodes(v), rels(v)")
	List<Video> findByCrawlerId(Long crawlerId);
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:UPLOADED_BY]-> (c:Channel) WHERE ID(c)={0} WITH video "
			+ "MATCH v=(video)-[r*0..1]-() RETURN video, nodes(v), rels(v)")
	List<Video> findByChannelId(String channelId);
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:CATEGORIZED_AS]-> (c:Category) WHERE ID(c)={0} WITH video "
			+ "MATCH v=(video)-[r*0..1]-() RETURN video, nodes(v), rels(v)")
	List<Video> findByCategoryName(String channelId);
	
	// TODO: Test it!
	//"MATCH (v:Video) WHERE v.publishedAt >= '2015-02-27' and v.publishedAt <='2018-03-01' RETURN v"
	@Depth(1)
	@Query("select v from Video v where "
			  +"(:fromDate = '' or v.publishedAt >= :fromDate) and "
			  +"(:toDate = '' or v.publishedAt <= :toDate) and "
		      +"(:categoryName = '' or v.category.name = :categoryName) and "
		      +"(:channelId = '' or v.channel.id = :channelId) and "
		      +"(:crawlerId = '' or v.crawler.id = :crawlerId)")
	List<Video> analysisSearch(@Param("fromDate") String fromDate,
									@Param("toDate") String toDate,
		                            @Param("categoryName") String categoryName,
		                            @Param("channelId") String channelId,
		                            @Param("crawlerId") String crawlerId);
			
	@Query("MATCH (v:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} DETACH DELETE v")
	void removeByCrawlerId(Long crawlerId);
	
	@Query("MATCH (v:Video) -[:UPLOADED_BY]-> (c:Channel) WHERE ID(c)={0} DETACH DELETE v")
	void removeByChannelId(String channelId);
	
	@Query("MATCH (v:Video) -[:CATEGORIZED_AS]-> (c:Category) WHERE ID(c)={0} DETACH DELETE v")
	void removeByCategoryName(String categoryName);
	
}
