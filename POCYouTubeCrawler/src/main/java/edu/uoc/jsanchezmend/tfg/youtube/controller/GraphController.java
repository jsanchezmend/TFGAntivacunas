package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import edu.uoc.jsanchezmend.tfg.youtube.data.CytoscapeEdge;
import edu.uoc.jsanchezmend.tfg.youtube.data.CytoscapeNode;

@Controller
@RequestMapping("/graph")
public class GraphController {

	private DB mongoDB;

	@Inject
	public GraphController(DB mongoDB) {
		this.mongoDB = mongoDB;
	}

	@ResponseBody
	@RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
	public List<CytoscapeNode> getNodes() {		
		final List<CytoscapeNode> cytoscapeNodes = new ArrayList<CytoscapeNode>();
		final DBCollection nodes = this.connectNodes();
		
		final DBCursor cursor = nodes.find();
		while (cursor.hasNext()) {
		   final DBObject obj = cursor.next();
		   final CytoscapeNode cytoscapeNode = this.transformNode(obj);
		   cytoscapeNodes.add(cytoscapeNode);
		}
		
		return cytoscapeNodes;
	}
	
	private CytoscapeNode transformNode(DBObject obj) {
		final CytoscapeNode node = new CytoscapeNode(obj.get("videoId").toString(), obj.get("videoId").toString());
		return node;		
	}
	
	@ResponseBody
	@RequestMapping(value = "/edges", method = RequestMethod.GET, produces = "application/json")
	public List<CytoscapeEdge> getEdges() {			
		final List<CytoscapeEdge> cytoscapeEdges = new ArrayList<CytoscapeEdge>();
		final DBCollection edges = this.connectEdges();
		
		final DBCursor cursor = edges.find();
		while (cursor.hasNext()) {
		   final DBObject obj = cursor.next();
		   final CytoscapeEdge cytoscapeEdge = this.transformEdge(obj);
		   cytoscapeEdges.add(cytoscapeEdge);
		}
		
		return cytoscapeEdges;
	}
	
	private CytoscapeEdge transformEdge(DBObject obj) {
		final CytoscapeEdge edge = new CytoscapeEdge(obj.get("source").toString(), obj.get("target").toString());
		return edge;		
	}
	
	public DBCollection connectNodes() {
		DBCollection nodes = null;
		try {
			nodes = mongoDB.getCollection("nodes");
		} catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		return nodes;
	}
	
	public DBCollection connectEdges() {
		DBCollection edges = null;
		try {
			edges = mongoDB.getCollection("edges");
		} catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		return edges;
	}

}
