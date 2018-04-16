package edu.uoc.jsanchezmend.tfg.youtube.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import edu.uoc.jsanchezmend.tfg.youtube.data.VideoEntity;

public interface VideoRepository extends Neo4jRepository<VideoEntity, String> {

	VideoEntity findByTitle(String title);
    
}