package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.AnalysisService;
import uoc.edu.jsanchezmend.tfg.ytct.core.util.DateUtil;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.GraphNodeTypeEnum;
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

	private static final String DEFAULT_FROM_DATE = "2005-02-14"; //YouTube foundation

	
	@Autowired
	private VideoRepository videoRepository;
	

	@Override
	public GraphItem createGraph(AnalysisSearchItem analysisSearch) {
		final GraphItem result = new GraphItem();
						
		// Search for videos
		String fromDate = null;
		String toDate = null;
		try {
			fromDate = analysisSearch.getFromDate() != null ? DateUtil.toNeo4jString(analysisSearch.getFromDate()) : DEFAULT_FROM_DATE;
			toDate = analysisSearch.getToDate() != null ? DateUtil.toNeo4jString(analysisSearch.getToDate()) : DateUtil.toNeo4jString(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		final List<Video> videos = this.videoRepository.analysisSearchNodes(fromDate, toDate);
		
		// Generate nodes
		final List<String> channelIds = new ArrayList<String>();
		boolean includeChannels = analysisSearch.isIncludeChannels() != null ? analysisSearch.isIncludeChannels() : false;
		for(Video video : videos) {
			// Create video node
			final NodeDataItem videoNodeData = new NodeDataItem(GraphNodeTypeEnum.VIDEO);
			videoNodeData.setVideoId(video.getId());
			videoNodeData.setName(video.getTitle());
			final Category category = video.getCategory();
			if(category != null) {
				videoNodeData.setColor(category.getColor());
			}
			videoNodeData.setSize(video.getScopeRange().intValue());
			final NodeItem videoNode = new NodeItem(videoNodeData);
			//result.addNode(videoNode);
			result.addElement(videoNode);

			if(includeChannels) {
				final Channel channel = video.getChannel();
				if(channel != null && !channelIds.contains(channel.getId())) {
					// Add the channel id to the list to avoid include them again
					channelIds.add(channel.getId());
					// Create channel node
					final NodeDataItem channelNodeData = new NodeDataItem(GraphNodeTypeEnum.CHANNEL);
					channelNodeData.setVideoId(channel.getId());
					channelNodeData.setName(channel.getName());
					final NodeItem channelNode = new NodeItem(channelNodeData);
					//result.addNode(channelNode);
					result.addElement(channelNode);
					// Create video-channel edge
					final EdgeDataItem videoChannelEdgeData = new EdgeDataItem();
					videoChannelEdgeData.setSource(videoNode.getData().getVideoId());
					videoChannelEdgeData.setTarget(channelNode.getData().getVideoId());
					final EdgeItem videoChannelEdge = new EdgeItem(videoChannelEdgeData);
					//result.addEdge(videoChannelEdge);
					result.addElement(videoChannelEdge);
				}
			}
		}
		
		// Generate video related edges
		final List<EdgeDataItem> videoRelatedEdgesData = this.videoRepository.analysisSearchVideoEdges(fromDate, toDate);
		for(EdgeDataItem edgeDataItem : videoRelatedEdgesData) {
			final EdgeItem videoRelatedEdge = new EdgeItem(edgeDataItem);
			//result.addEdge(videoRelatedEdge);
			result.addElement(videoRelatedEdge);
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
