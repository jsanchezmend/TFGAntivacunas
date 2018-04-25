package uoc.edu.jsanchezmend.tfg.ytct.data.entity;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * User @NodeEntity
 * 
 * @author jsanchezmend
 *
 */
@NodeEntity
public class User {
	
	@Id
	protected String username;

	protected String password;
	
	
	public User() {
		
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
