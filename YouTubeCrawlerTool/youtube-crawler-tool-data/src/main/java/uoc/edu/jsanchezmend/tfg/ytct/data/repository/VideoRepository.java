package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.EdgeDataItem;

/**
 * Video @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface VideoRepository extends Neo4jRepository<Video, String> {
	
	@Depth(1)
	@Query("MATCH (video:Video) WHERE video.favorite=true WITH video "
			+ "MATCH v=(video)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN video, nodes(v), rels(v)")
	List<Video> findAllFavorite();
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} WITH video "
			+ "MATCH v=(video)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN video, nodes(v), rels(v)")
	List<Video> findByCrawlerId(Long crawlerId);
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:UPLOADED_BY]-> (c:Channel) WHERE c.id={0} WITH video "
			+ "MATCH v=(video)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN video, nodes(v), rels(v)")
	List<Video> findByChannelId(String channelId);
	
	@Depth(1)
	@Query("MATCH (video:Video) -[:CATEGORIZED_AS]-> (c:Category) WHERE c.name={0} WITH video "
			+ "MATCH v=(video)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN video, nodes(v), rels(v)")
	List<Video> findByCategoryName(String categoryName);
	
	@Depth(1)
	@Query("MATCH (realtedVideo:Video) <-[:RELATED_TO]- (video:Video) WHERE video.id={0} WITH realtedVideo "
			+ "MATCH v=(realtedVideo)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN realtedVideo, nodes(v), rels(v)")
	List<Video> findRelatedVideos(String videoId);
	
	
	@Depth(1)
	@Query("MATCH (video:Video) WHERE video.publishedAt >= {0} and video.publishedAt <= {1} WITH video "
			+ "MATCH v=(video)-[:UPLOADED_BY|DISCOVERED_BY|CATEGORIZED_AS]->() RETURN video, nodes(v), rels(v)")
	List<Video> analysisSearchNodes(String fromDate, String toDate);
		
	@Depth(1)
	@Query("MATCH (video:Video) -[:RELATED_TO]-> (realtedVideo:Video) "
			+ "WHERE video.publishedAt >= {0} and video.publishedAt <= {1} "
			+ "and realtedVideo.publishedAt >= {0} and realtedVideo.publishedAt <= {1} "
			+ "RETURN 'v-' + video.id as source, 'v-' + realtedVideo.id as target, "
			+ "video.id as outgoing, realtedVideo.id as incoming, "
			+ "'v' as outgoingType, 'v' as incomingType")
	List<EdgeDataItem> analysisSearchVideoEdges(String fromDate, String toDate);
	
				
	@Query("MATCH (v:Video) -[:DISCOVERED_BY]-> (c:Crawler) WHERE ID(c)={0} DETACH DELETE v")
	void removeByCrawlerId(Long crawlerId);
	
	@Query("MATCH (v:Video) -[:UPLOADED_BY]-> (c:Channel) WHERE c.id={0} DETACH DELETE v")
	void removeByChannelId(String channelId);
	
	@Query("MATCH (v:Video) -[:CATEGORIZED_AS]-> (c:Category) WHERE c.name={0} DETACH DELETE v")
	void removeByCategoryName(String categoryName);
	
}
