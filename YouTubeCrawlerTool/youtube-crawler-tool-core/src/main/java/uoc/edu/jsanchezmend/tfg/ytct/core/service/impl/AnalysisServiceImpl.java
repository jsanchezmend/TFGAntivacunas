package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.AnalysisService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.AnalysisSearchItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.EdgeDataItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.EdgeItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.GraphItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.NodeDataItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.NodeItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.VideoRepository;

/**
 * @AnalysisService implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("analysisService")
public class AnalysisServiceImpl implements AnalysisService {
	
	@Autowired
	private VideoRepository videoRepository;
	

	@Override
	public GraphItem createGraph(AnalysisSearchItem analysisSearch) {
		final GraphItem result = new GraphItem();
		
		boolean includeChannels = analysisSearch.isIncludeChannels() != null ? analysisSearch.isIncludeChannels() : false;
		
		// Generate nodes
		final List<Video> videos = videoRepository.analysisSearchNodes(analysisSearch.getFromDate(), analysisSearch.getToDate());
		for(Video video : videos) {
			// Create video node
			final NodeItem videoNode = new NodeItem();
			final NodeDataItem videoNodeData = new NodeDataItem();
			videoNodeData.setId(video.getId());
			videoNodeData.setName(video.getTitle());
			videoNode.setData(videoNodeData);
			result.addNode(videoNode);
			
			if(includeChannels) {
				// Create channel node
				final Channel channel = video.getChannel();
				final NodeItem channelNode = new NodeItem();
				final NodeDataItem channelNodeData = new NodeDataItem();
				channelNodeData.setId(channel.getId());
				channelNodeData.setName(channel.getName());
				channelNode.setData(channelNodeData);
				result.addNode(channelNode);
				// Create video-channel edge
				final EdgeItem videoChannelEdge = new EdgeItem();
				final EdgeDataItem videoChannelEdgeData = new EdgeDataItem();
				videoChannelEdgeData.setSource(videoNode.getData().getId());
				videoChannelEdgeData.setTarget(channelNode.getData().getId());
				videoChannelEdge.setData(videoChannelEdgeData);
				result.addEdge(videoChannelEdge);
			}
		}
		
		// Generate video related edges
		List<EdgeDataItem> videoRelatedEdgesData = videoRepository.analysisSearchEdges(analysisSearch.getFromDate(), analysisSearch.getToDate());
		for(EdgeDataItem edgeDataItem : videoRelatedEdgesData) {
			final EdgeItem videoRelatedEdge = new EdgeItem();
			videoRelatedEdge.setData(edgeDataItem);
			result.addEdge(videoRelatedEdge);
		}
		
		return result;
	}

	@Override
	public GraphItem createExport(AnalysisSearchItem analysisSearch) {
		GraphItem result = null;
		// TODO
		return result;
	}
	
}
