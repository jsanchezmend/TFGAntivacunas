package edu.uoc.jsanchezmend.tfg.youtube.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.uoc.jsanchezmend.tfg.youtube.data.VideoEntity;
import edu.uoc.jsanchezmend.tfg.youtube.data.item.CytoscapeEdge;
import edu.uoc.jsanchezmend.tfg.youtube.data.item.CytoscapeNode;
import edu.uoc.jsanchezmend.tfg.youtube.data.repository.VideoRepository;

@Controller
@RequestMapping("/graph")
public class GraphController {

	@Autowired
	private VideoRepository videoRepository;

	@ResponseBody
	@RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
	public List<CytoscapeNode> getNodes() {		
		final List<CytoscapeNode> cytoscapeNodes = new ArrayList<CytoscapeNode>();
		
		final Iterable<VideoEntity> videoEntityIterable =  videoRepository.findAll();
		if(videoEntityIterable!=null) {
			final Iterator<VideoEntity> videoEntityIterator = videoEntityIterable.iterator();
			while (videoEntityIterator.hasNext()) {
			   final VideoEntity obj = videoEntityIterator.next();
			   final CytoscapeNode cytoscapeNode = this.transformNode(obj);
			   cytoscapeNodes.add(cytoscapeNode);
			}
		}
		
		return cytoscapeNodes;
	}
	
	private CytoscapeNode transformNode(VideoEntity obj) {
		final CytoscapeNode node = new CytoscapeNode(obj.getId(), obj.getId());
		return node;		
	}
	
	@ResponseBody
	@RequestMapping(value = "/edges", method = RequestMethod.GET, produces = "application/json")
	public List<CytoscapeEdge> getEdges() {			
		final List<CytoscapeEdge> cytoscapeEdges = new ArrayList<CytoscapeEdge>();
		
		final Iterable<VideoEntity> videoEntityIterable =  videoRepository.findAll();
		if(videoEntityIterable!=null) {
			final Iterator<VideoEntity> videoEntityIterator = videoEntityIterable.iterator();
			while (videoEntityIterator.hasNext()) {
			   final VideoEntity obj = videoEntityIterator.next();
			   final Set<VideoEntity> relatedSet = obj.getRelated();
			   if(relatedSet!=null) {
				   final Iterator<VideoEntity> relatedIterator = relatedSet.iterator();
				   while(relatedIterator.hasNext()) {
					   final VideoEntity related = relatedIterator.next();
					   final CytoscapeEdge cytoscapeEdge = new CytoscapeEdge(obj.getId(), related.getId());
					   cytoscapeEdges.add(cytoscapeEdge);
				   }
			   }
			}
		}
				
		return cytoscapeEdges;
	}
	
}
