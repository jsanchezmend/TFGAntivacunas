package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.AnalysisService;
import uoc.edu.jsanchezmend.tfg.ytct.core.util.DateUtil;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.GraphNodeTypeEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.AnalysisSearchItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.EdgeDataItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.EdgeItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.GraphElementsItem;
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
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
		

	@Override
	public GraphItem createGraph(AnalysisSearchItem analysisSearch) {
		final GraphItem result = new GraphItem();
		final GraphElementsItem elements = new GraphElementsItem();
						
		// Search for videos
		String fromDate = null;
		String toDate = null;
		try {
			fromDate = analysisSearch.getFromDate() != null && !analysisSearch.getFromDate().isEmpty() ? DateUtil.toNeo4jString(analysisSearch.getFromDate()) : DEFAULT_FROM_DATE;
			toDate = analysisSearch.getToDate() != null && !analysisSearch.getToDate().isEmpty() ? DateUtil.toNeo4jString(analysisSearch.getToDate()) : DateUtil.toNeo4jString(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		final List<Video> videos = this.videoRepository.analysisSearchNodes(fromDate, toDate);
		final List<VideoItem> videoItemsCandidates = this.videoConverterService.toListItem(videos);
		
		// Programmatic filters 
		// TODO: Do it with a repository query
		List<String> videoIds = null;
		final List<VideoItem> videoItems = new ArrayList<VideoItem>();
		if(this.programmaticFiltersIsRequired(analysisSearch)) {
			for(VideoItem videoItemsCandidate : videoItemsCandidates) {
				// Categories filters
				if(videoItemsCandidate.getCategory() != null) {
					if(analysisSearch.getCategories() != null && !analysisSearch.getCategories().isEmpty()){
						if(!analysisSearch.getCategories().contains(videoItemsCandidate.getCategory().getName())) {
							continue;
						}
					} else if(analysisSearch.isIncludeUncategorized() != null && analysisSearch.isIncludeUncategorized()) {
						continue;
					}
				} else if(analysisSearch.isIncludeUncategorized() != null && !analysisSearch.isIncludeUncategorized()) {
					continue;
				}
				
				// Crawler filters
				if(analysisSearch.getCrawlers() != null && !analysisSearch.getCrawlers().isEmpty() 
						&& !analysisSearch.getCrawlers().contains(videoItemsCandidate.getCrawler().getId())) {
					continue;
				}
				
				// If passed all filters, add the video
				videoItems.add(videoItemsCandidate);
				if(videoIds == null) {
					videoIds = new ArrayList<String>();
				}
				videoIds.add(videoItemsCandidate.getId());
			}
		} else {
			videoItems.addAll(videoItemsCandidates);
			// Initialize videoIds to know that all retrieved edges should be included
			videoIds = new ArrayList<String>();
		}
		
		// Generate nodes
		final List<String> channelIds = new ArrayList<String>();
		boolean includeChannels = analysisSearch.isIncludeChannels() != null ? analysisSearch.isIncludeChannels() : false;
		for(VideoItem videoItem : videoItems) {
			// Create video node
			final NodeDataItem videoNodeData = new NodeDataItem(GraphNodeTypeEnum.VIDEO);
			videoNodeData.setResourceId(videoItem.getId());
			final CategoryItem categoryItem = videoItem.getCategory();
			if(categoryItem != null) {
				videoNodeData.setColor(categoryItem.getColor());
			}
			videoNodeData.setSize(videoItem.getScopeRange().intValue());
			// Make the json lighter
			final ChannelItem channelItem = videoItem.getChannel();
			final ChannelItem lightChannelItem = new ChannelItem();
			lightChannelItem.setId(channelItem.getId());
			videoItem.setChannel(lightChannelItem); 
			videoNodeData.setVideo(videoItem);
			final NodeItem videoNode = new NodeItem(videoNodeData);
			elements.addNode(videoNode);

			if(includeChannels && channelItem != null) {
				final NodeDataItem channelNodeData = new NodeDataItem(GraphNodeTypeEnum.CHANNEL);
				channelNodeData.setResourceId(channelItem.getId());
				
				if(!channelIds.contains(channelNodeData.getResourceId())) {
					// Add the channel id to the list to avoid include them again
					channelIds.add(channelNodeData.getResourceId());
					// Create channel node
					channelNodeData.setChannel(channelItem);
					final NodeItem channelNode = new NodeItem(channelNodeData);
					elements.addNode(channelNode);
				}
				
				// Create video-channel edge
				final EdgeDataItem videoChannelEdgeData = new EdgeDataItem();
				videoChannelEdgeData.setSource(videoNode.getData().getId());
				videoChannelEdgeData.setTarget(channelNodeData.getId());
				videoChannelEdgeData.setOutgoing(videoNode.getData().getResourceId());
				videoChannelEdgeData.setIncoming(channelNodeData.getResourceId());
				videoChannelEdgeData.setOutgoingType(videoNode.getData().getTypeCode());
				videoChannelEdgeData.setIncomingType(channelNodeData.getTypeCode());
				final EdgeItem videoChannelEdge = new EdgeItem(videoChannelEdgeData);
				elements.addEdge(videoChannelEdge);
			}
		}
		
		// Generate video related edges
		if(videoIds != null) {
			final List<EdgeDataItem> videoRelatedEdgesData = this.videoRepository.analysisSearchVideoEdges(fromDate, toDate);
			for(EdgeDataItem edgeDataItem : videoRelatedEdgesData) {
				// Apply the programmatic filters 
				// TODO: Do it with a repository query
				if(videoIds.isEmpty() 
						|| (videoIds.contains(edgeDataItem.getOutgoing()) && videoIds.contains(edgeDataItem.getIncoming()))) {
					final EdgeItem videoRelatedEdge = new EdgeItem(edgeDataItem);
					elements.addEdge(videoRelatedEdge);
				}
			}
		}
		
		result.setElements(elements);
		return result;
	}
	
	private boolean programmaticFiltersIsRequired(AnalysisSearchItem analysisSearch) {
		if(analysisSearch.isIncludeUncategorized() != null) {
			return true;
		}
		if(analysisSearch.getCrawlers() != null && !analysisSearch.getCrawlers().isEmpty()) {
			return true;
		}
		return false;
	}
	
}
