package uoc.edu.jsanchezmend.tfg.ytct.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;

/**
 * Category @Neo4jRepository
 * 
 * @author jsanchezmend
 *
 */
public interface CategoryRepository extends Neo4jRepository<Category, String> {

}