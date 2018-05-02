package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;

/**
 * Channel @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface ChannelRepository extends Neo4jRepository<Channel, String> {
	
	@Query("MATCH (c:Channel) WHERE NOT (c)<-[:UPLOADED_BY]-() DETACH DELETE c")
	void removeOrphanChannels();

}
