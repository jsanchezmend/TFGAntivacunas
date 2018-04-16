package edu.uoc.jsanchezmend.tfg.youtube.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import edu.uoc.jsanchezmend.tfg.youtube.data.ChannelEntity;

public interface ChannelRepository extends Neo4jRepository<ChannelEntity, String> {

	ChannelEntity findByTitle(String title);
    
}