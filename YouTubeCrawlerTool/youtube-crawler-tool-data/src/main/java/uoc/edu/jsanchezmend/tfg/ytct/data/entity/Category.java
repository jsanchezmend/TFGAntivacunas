package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Category @NodeEntity
 * 
 * @author jsanchezmend
 *
 */
@NodeEntity
public class Category {
	
	@Id
	protected String name;

	protected String color;
	
	
	public Category() {
		
	}

	public Category(String name, String color) {
		super();
		this.name = name;
		this.color = color;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
