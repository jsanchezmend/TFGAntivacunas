package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
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
		final List<VideoItem> videoItemsCandidates = this.analysisSearch(analysisSearch);
		
		// Programmatic filters 
		final Pair<List<String>,List<VideoItem>> programmaticFiltersResults = this.applyProgrammaticFilters(analysisSearch, videoItemsCandidates);
		final List<String> videoIds = programmaticFiltersResults.getLeft();
		final List<VideoItem> videoItems = programmaticFiltersResults.getRight();
		
		// Generate graph nodes
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
		
		// Generate graph edges
		final List<EdgeItem> edgeItems = this.analysisSearchEdges(analysisSearch, videoIds);
		if(edgeItems != null && !edgeItems.isEmpty()) {
			elements.addEdges(edgeItems);
		}
		
		result.setElements(elements);
		return result;
	}
	
	/**
	 * Given a @AnalysisSearchItem return the search results in a list of @VideoItem
	 * 
	 * @param analysisSearch
	 * @return
	 */
	protected List<VideoItem> analysisSearch(AnalysisSearchItem analysisSearch) {	
		// Search for videos
		final Pair<String, String> searchDatesStr = this.getAnalysisSearchDates(analysisSearch);
		final String fromDate = searchDatesStr.getLeft();
		final String toDate = searchDatesStr.getRight();
		
		final List<Video> entitiesResults = this.videoRepository.analysisSearchNodes(fromDate, toDate);
		final List<VideoItem> results = this.videoConverterService.toListItem(entitiesResults);
		
		return results;		
	}
	
	/**
	 * Given a AnalysisSearchItem and a list of VideoItem, returns a pair object with the filtered videos ids and items.
	 * The programmatic filter of performed by @Crawler and @Category
	 * 
	 * TODO: Replace it with a proper repository query
	 * 
	 * @param videoItems
	 * @return
	 */
	protected Pair<List<String>,List<VideoItem>> applyProgrammaticFilters(AnalysisSearchItem analysisSearch, List<VideoItem> videoItems) {
		List<String> videoIds = null;
		
		final List<VideoItem> resultVideoItems = new ArrayList<VideoItem>();
		if(this.programmaticFiltersIsRequired(analysisSearch)) {
			for(VideoItem videoItem : videoItems) {
				// Favorites filter
				if(analysisSearch.isIncludeOnlyFavorites() != null && analysisSearch.isIncludeOnlyFavorites()
						&& (videoItem.getFavorite() == null || videoItem.getFavorite() == false)) {
					continue;
				}
				// Categories filters
				if(videoItem.getCategory() != null) {
					if(analysisSearch.getCategories() != null && !analysisSearch.getCategories().isEmpty()){
						if(!analysisSearch.getCategories().contains(videoItem.getCategory().getName())) {
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
						&& !analysisSearch.getCrawlers().contains(videoItem.getCrawler().getId())) {
					continue;
				}
				
				// If passed all filters, add the video
				resultVideoItems.add(videoItem);
				if(videoIds == null) {
					videoIds = new ArrayList<String>();
				}
				videoIds.add(videoItem.getId());
			}
		} else {
			resultVideoItems.addAll(videoItems);
			// Initialize videoIds to know that all retrieved edges should be included
			videoIds = new ArrayList<String>();
		}

		return Pair.of(videoIds, resultVideoItems);
	}
	
	/**
	 * Given a AnalysisSearchItem and a list of video id's, returns a search result of a list of @EdgeItem 
	 * with the filtered videos id's.
	 * 
	 * TODO: Replace it with a proper repository query to filter also by @Crawler and @Category.
	 * 
	 * @param analysisSearch
	 * @param filteredVideoIds
	 * @return
	 */
	protected List<EdgeItem> analysisSearchEdges(AnalysisSearchItem analysisSearch, List<String> filteredVideoIds) {	
		final List<EdgeItem> result = new ArrayList<EdgeItem>();
		
		if(filteredVideoIds != null) {
			// Search for videos
			final Pair<String, String> searchDatesStr = this.getAnalysisSearchDates(analysisSearch);
			final String fromDate = searchDatesStr.getLeft();
			final String toDate = searchDatesStr.getRight();
			final List<EdgeDataItem> videoRelatedEdgesData = this.videoRepository.analysisSearchVideoEdges(fromDate, toDate);
			for(EdgeDataItem edgeDataItem : videoRelatedEdgesData) {
				// Apply the programmatic filters 
				if(filteredVideoIds.isEmpty() 
						|| (filteredVideoIds.contains(edgeDataItem.getOutgoing()) && filteredVideoIds.contains(edgeDataItem.getIncoming()))) {
					final EdgeItem videoRelatedEdge = new EdgeItem(edgeDataItem);
					result.add(videoRelatedEdge);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Given a @AnalysisSearchItem returns the from and to date to perform an analysis search.
	 * 
	 * @param analysisSearch
	 * @return
	 */
	protected Pair<String, String> getAnalysisSearchDates(AnalysisSearchItem analysisSearch) {
		String fromDate = null;
		String toDate = null;
		
		try {
			fromDate = analysisSearch.getFromDate() != null && !analysisSearch.getFromDate().isEmpty() ? DateUtil.toNeo4jString(analysisSearch.getFromDate()) : DEFAULT_FROM_DATE;
			toDate = analysisSearch.getToDate() != null && !analysisSearch.getToDate().isEmpty() ? DateUtil.toNeo4jString(analysisSearch.getToDate()) : DateUtil.toNeo4jString(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return Pair.of(fromDate, toDate);		
	}
	
	/**
	 * Given a @AnalysisSearchItem return if a programmatic filter is required
	 * A programmatic filter is required when the results should by filtered by crawlers or/and categories
	 * 
	 * @param analysisSearch
	 * @return
	 */
	protected boolean programmaticFiltersIsRequired(AnalysisSearchItem analysisSearch) {
		if(analysisSearch.isIncludeOnlyFavorites() != null && analysisSearch.isIncludeOnlyFavorites()) {
			return true;
		}
		if(analysisSearch.isIncludeUncategorized() != null) {
			return true;
		}
		if(analysisSearch.getCrawlers() != null && !analysisSearch.getCrawlers().isEmpty()) {
			return true;
		}
		return false;
	}
	
}
