package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.ConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.CrawlerService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.YouTubeSearchService;
import uoc.edu.jsanchezmend.tfg.ytct.core.util.DateUtil;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.YouTubeSearchResponseItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.CategoryRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.ChannelRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.CrawlerRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.VideoRepository;

/**
 * @CrawlerService implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("crawlerService")
public class CrawlerServiceImpl implements CrawlerService {
	
	@Value("${crawler.search.default.fromdate}")
	private String defaultFromDate;
	
	@Value("${crawler.search.default.todate}")
	private String defaultToDate;
	
	@Value("${crawler.search.default.order}")
	private String defaultOrder;
	
	@Value("${crawler.search.default.relatedlevels}")
	private int defaultRelatedlevels;
	
	@Value("${crawler.search.default.maxrelated}")
	private int defaultMaxrelated;
	
	@Value("${crawler.search.default.totalvideos}")
	private int defaultTotalvideos;
	
	
	@Autowired
	private YouTubeSearchService youTubeSearchService;
	
	@Autowired
	private CrawlerRepository crawlerRepository;
			
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	@Qualifier("crawlerConverterService")
	private ConverterService<Crawler, CrawlerItem> crawlerConverterService;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	
	@Autowired
	@Qualifier("channelConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Channel, Channel, ChannelItem> channelConverterService;
	
	
	@Override
	public List<CrawlerItem> listCrawlers() {
		final Iterable<Crawler> crawlerIterable = crawlerRepository.findAll();
		final List<CrawlerItem> crawlerItems = crawlerConverterService.toListItem(crawlerIterable);
		return crawlerItems;
	}
	
	@Override
	public CrawlerItem createCrawler(CrawlerItem crawlerItem) {	
		// Initialize crawler variables to start a new crawler process
		crawlerItem = this.initilizeCrawlerItem(crawlerItem);

		// Create a new crawler
		Crawler crawler = crawlerConverterService.toEntity(crawlerItem);
		crawler = this.crawlerRepository.save(crawler);
				
		// Return the created crawler as a result
		final CrawlerItem result = crawlerConverterService.toItem(crawler);
		return result;
	}
	
	/**
	 * Initialize a @CrawlerItem to start a new crawler process
	 * 
	 * @param crawlerItem
	 * @return
	 */
	private CrawlerItem initilizeCrawlerItem(CrawlerItem crawlerItem) {	
		if(crawlerItem == null) {
			return null;
		}
		
		// Set default search values if null
		if(this.isSearchCrawler(crawlerItem)) {
			if(crawlerItem.getFromDate() == null) {
				crawlerItem.setFromDate(defaultFromDate);
			}
			if(crawlerItem.getToDate() == null) {
				crawlerItem.setToDate(defaultToDate);
			}	
			if(crawlerItem.getOrderBy() == null) {
				crawlerItem.setOrderBy(defaultOrder);
			}
		}
		if(crawlerItem.getRelatedLevels() == null) {
			crawlerItem.setRelatedLevels(defaultRelatedlevels);
		}
		if(crawlerItem.getMaxVideosPerLevel() == null) {
			crawlerItem.setMaxVideosPerLevel(defaultMaxrelated);
		}
		if(crawlerItem.getMaxVideos() == null) {
			crawlerItem.setMaxVideos(defaultTotalvideos);
		}
		// Initialize crawler process values
		crawlerItem.setVideosFound(0);
		crawlerItem.setNewVideos(0);
		crawlerItem.setCompleted(0F);
		crawlerItem.setExecutionTime(0L);
		crawlerItem.setCreatedDate(new Date());
		crawlerItem.setStatus(CrawlerStatusEnum.RUNNING.getName());
		
		return crawlerItem;
	}
	
	@Async
	@Override
	public CompletableFuture<CrawlerItem> executeCrawler(Long crawlerId) {
		final Crawler crawler = this.crawlerRepository.findById(crawlerId).orElse(null);
		if(crawler == null) {
			return null;
		} else if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.RUNNING)) {
			return null;
		}
		CrawlerItem result = this.crawlerConverterService.toItem(crawler);

		// Start the execution
		if(this.isSearchCrawler(result)) {
			// Do a search crawler
			result = this.doCrawler(result);
		} else if(this.isRelatedCrawler(result)) {
			// Do a related by video id crawler
			result = this.doRelatedCrawler(result);
		} else {
			return null;
		}
		
		return CompletableFuture.completedFuture(result);
	}
	
	/**
	 * Performs a search crawler process defined by a @CrawlerItem
	 * 
	 * @param crawler
	 * @return
	 */
	private CrawlerItem doCrawler(CrawlerItem crawlerItem) {
		Long startTime = new Date().getTime();
		int searchedVideos = 0;
		Integer totalVideosToCrawler = null;
		
		do {
			try {
				// Search for videos
				final Long count = new Long(crawlerItem.getMaxVideos() - searchedVideos);
				final Date fromDate = DateUtil.toDate(crawlerItem.getFromDate());
				final Date toDate = DateUtil.toDate(crawlerItem.getToDate());
				final YouTubeSearchResponseItem youTubeSearchResponseItem = this.youTubeSearchService.searchVideos(
						crawlerItem.getSearch(),
						new DateTime(fromDate.getTime()),
						new DateTime(toDate.getTime()), 
						CrawlerOrderByEnum.getByName(crawlerItem.getOrderBy()),
						crawlerItem.getPageToken(),
						count
					);
				final List<String> videoIds = youTubeSearchResponseItem.getItems();
				crawlerItem.setPageToken(youTubeSearchResponseItem.getNextPageToken());
				crawlerItem.addVideosFound(videoIds.size());
				searchedVideos += videoIds.size();
				
				// Calculate total videos to crawler
				if(totalVideosToCrawler == null) {
					totalVideosToCrawler = crawlerItem.getMaxVideos();
					if(totalVideosToCrawler.compareTo(youTubeSearchResponseItem.getTotalResults()) > 0) {
						totalVideosToCrawler = youTubeSearchResponseItem.getTotalResults();
					}
				}
				
				// Obtain the videos
				crawlerItem.addExecutionTime(new Date().getTime() - startTime);
				crawlerItem = this.obtainVideos(crawlerItem, videoIds, searchedVideos, totalVideosToCrawler);
				startTime = new Date().getTime();
		
				// Prepare the crawler to next search
				if(crawlerItem.getPageToken() == null || crawlerItem.getMaxVideos().compareTo(searchedVideos) <= 0) {
					// Finish the execution
					crawlerItem.setStatus(CrawlerStatusEnum.FINISHED.getName());	
					//crawlerItem.setCompleted(100F);
				} 
			} catch (IOException e) {
				// Exception throw by YouTube to block our petition
				crawlerItem.setStatus(CrawlerStatusEnum.BLOCKED.getName());
				e.printStackTrace();
			} catch (Exception e) {
				crawlerItem.setStatus(CrawlerStatusEnum.ERROR.getName());
				e.printStackTrace();
			}
			
			// Synchronized: Ensure crawler status consistency through different threads
			synchronized (this) {
				// Check if the process is stopped by user
				final Crawler crawler = this.crawlerRepository.findById(crawlerItem.getId()).orElse(null);
				if(crawler.getStatusByEnum().equals(CrawlerStatusEnum.STOPPING)) {
					if(CrawlerStatusEnum.RUNNING.getName().equals(crawlerItem.getStatus())) {
						crawlerItem.setStatus(CrawlerStatusEnum.STOPPED.getName());
					}
				}
				// Save the current crawler state
				crawlerItem.addExecutionTime(new Date().getTime() - startTime);
				startTime = new Date().getTime();
				final Crawler resultEntity = this.crawlerConverterService.toEntity(crawlerItem);
				this.crawlerRepository.save(resultEntity);
			}
		} while(CrawlerStatusEnum.RUNNING.getName().equals(crawlerItem.getStatus()));
	
		return crawlerItem;
	}
		
	/**
	 * Performs a related by videoId crawler process defined by a @CrawlerItem
	 * 
	 * @param crawler
	 * @return
	 */
	private CrawlerItem doRelatedCrawler(CrawlerItem crawlerItem) {
		if(!this.isRelatedSearchRequired(crawlerItem)) {
			return crawlerItem;
		}
		
		Long startTime = new Date().getTime();
		int searchedVideos = 0;
		Integer totalVideosToCrawler = null;
		
		do {
			try {
				// Search for related videos
				final Long count = new Long(crawlerItem.getMaxVideosPerLevel() - searchedVideos);
				final YouTubeSearchResponseItem youTubeSearchResponseItem = this.youTubeSearchService.searchRelatedVideos(
						crawlerItem.getRelatedVideoId(),
						crawlerItem.getPageToken(),
						count
					);
				final List<String> videoIds = youTubeSearchResponseItem.getItems();
				crawlerItem.setPageToken(youTubeSearchResponseItem.getNextPageToken());
				crawlerItem.addVideosFound(videoIds.size());
				searchedVideos += videoIds.size();
				
				// Calculate total videos to crawler
				if(totalVideosToCrawler == null) {
					totalVideosToCrawler = crawlerItem.getMaxVideosPerLevel();
					if(totalVideosToCrawler.compareTo(youTubeSearchResponseItem.getTotalResults()) > 0) {
						totalVideosToCrawler = youTubeSearchResponseItem.getTotalResults();
					}
				}
				
				// Obtain the videos
				// Decrees the related levels value to take in consideration the current searched level
				crawlerItem.setRelatedLevels(crawlerItem.getRelatedLevels()-1);
				crawlerItem.addExecutionTime(new Date().getTime() - startTime);
				crawlerItem = this.obtainVideos(crawlerItem, videoIds, searchedVideos, totalVideosToCrawler);
				startTime = new Date().getTime();
				// Restore the related levels to not change the value introduced by the user
				crawlerItem.setRelatedLevels(crawlerItem.getRelatedLevels()+1);
						
				// Prepare the crawler to next search
				if(crawlerItem.getPageToken() == null || crawlerItem.getMaxVideosPerLevel().compareTo(searchedVideos) <= 0) {
					// Finish the execution
					crawlerItem.setStatus(CrawlerStatusEnum.FINISHED.getName());
					//crawlerItem.setCompleted(100F);
				} 
			} catch (IOException e) {
				// Exception throw by YouTube to block our petition
				crawlerItem.setStatus(CrawlerStatusEnum.BLOCKED.getName());
				e.printStackTrace();
			} catch (Exception e) {
				crawlerItem.setStatus(CrawlerStatusEnum.ERROR.getName());
				e.printStackTrace();
			}
			
			// Synchronized: Ensure crawler status consistency through different threads
			synchronized (this) {
				// Check if the process is stopped by user
				final Crawler crawler = this.crawlerRepository.findById(crawlerItem.getId()).orElse(null);
				if(crawler.getStatusByEnum().equals(CrawlerStatusEnum.STOPPING)) {
					if(CrawlerStatusEnum.RUNNING.getName().equals(crawlerItem.getStatus())) {
						crawlerItem.setStatus(CrawlerStatusEnum.STOPPED.getName());
					}
				}
				// Save the current crawler state
				crawlerItem.addExecutionTime(new Date().getTime() - startTime);
				startTime = new Date().getTime();
				final Crawler resultEntity = this.crawlerConverterService.toEntity(crawlerItem);
				this.crawlerRepository.save(resultEntity);
			}
		} while(CrawlerStatusEnum.RUNNING.getName().equals(crawlerItem.getStatus()));
		
		return crawlerItem;
	}
	
	/**
	 * Given a crawler process and a list of video id's, retrieve it from YouTube and persist them
	 * 
	 * @param crawler
	 * @param videoIds
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private CrawlerItem obtainVideos(CrawlerItem crawler, List<String> videoIds, Integer searchedVideos, Integer totalVideosToCrawler) throws IOException, Exception {			
		Long startTime = new Date().getTime();
		
		// Retrieve videos
		final List<VideoItem> videoItems = this.youTubeSearchService.findVideos(videoIds);
		int videosToProcess = videoItems.size();
		int videosProcessed = 0;
		
		for(VideoItem videoItem : videoItems) {
			videosProcessed++;
			final String videoId = videoItem.getId();
			// Synchronized: Ensure that the retrieved video from YouTube doesn't exist 
			// and it will be not created by another thread during his creation
			synchronized (this) {
				final boolean videoExist = this.videoRepository.existsById(videoId);
				if(!videoExist) {
					// Create the video
					final Video newVideo = this.videoConverterService.toEntity(videoItem);
		            // Set the channel
					final Channel channel = this.getChannel(videoItem.getChannel().getId());
					newVideo.setChannel(channel);
					// Set the crawler
					final Crawler crawlerEntity = this.crawlerRepository.findById(crawler.getId()).orElse(null);
					newVideo.setCrawler(crawlerEntity);
					// Set the category
					if(crawler.getCategoryByDefault() != null) {
						final Category category = this.categoryRepository.findById(crawler.getCategoryByDefault()).orElse(null);
						if(category != null) {
							newVideo.setCategory(category);
						}
					}
					// Calculation of video scopeRange value
					final BigInteger scopeRange = newVideo.getViewCount() != null ? newVideo.getViewCount() : BigInteger.valueOf(0L);
					newVideo.setScopeRange(scopeRange);
					this.videoRepository.save(newVideo);
					crawler.addNewVideos(1);
				}
			}
			
			// If it is a related crawler process, create a new relationship
			if(this.isRelatedCrawler(crawler)) {
				this.createVideoRelationship(crawler.getRelatedVideoId(), videoId);
			}
			
			// If necessary, create a new search for related videos
			if(this.isRelatedSearchRequired(crawler)) {
				CrawlerItem relatedCrawler = new CrawlerItem();
				relatedCrawler.setId(crawler.getId());
				relatedCrawler.setRelatedVideoId(videoId);
				relatedCrawler.setRelatedLevels(crawler.getRelatedLevels()-1);
				relatedCrawler.setMaxVideosPerLevel(crawler.getMaxVideosPerLevel());
				relatedCrawler.setCategoryByDefault(crawler.getCategoryByDefault());
				relatedCrawler = this.initilizeCrawlerItem(relatedCrawler);
				// Search for related videos
				relatedCrawler = this.searchRelatedVideos(relatedCrawler);
				crawler.addVideosFound(relatedCrawler.getVideosFound());
				crawler.addNewVideos(relatedCrawler.getNewVideos());
			}
			
			// Update crawler status
			if(searchedVideos != null && totalVideosToCrawler != null) {
				// Calculate process completation
				int totalVideosProcessed = searchedVideos - videosToProcess + videosProcessed;
				float completation = new Float(totalVideosProcessed * 100 / totalVideosToCrawler);	
				crawler.setCompleted(completation);
				// Synchronized: Ensure crawler consistency through different threads
				synchronized (this) {					
					// Check if the process is stopped by user
					final Crawler crawlerEntity = this.crawlerRepository.findById(crawler.getId()).orElse(null);
					// Save the current crawler state
					crawlerEntity.setNewVideos(crawler.getNewVideos());
					crawlerEntity.setVideosFound(crawler.getVideosFound());
					crawlerEntity.setCompleted(crawler.getCompleted());
					crawler.addExecutionTime(new Date().getTime() - startTime);
					crawlerEntity.setExecutionTime(crawler.getExecutionTime());
					startTime = new Date().getTime();
					this.crawlerRepository.save(crawlerEntity);
				}
			}
		}
		
		crawler.addExecutionTime(new Date().getTime() - startTime);
		return crawler;
	}
		
	/**
	 * Creates a 'RELATED_TO' relationship between two @Video
	 * 
	 * @param fromVideoId
	 * @param toVideoId
	 */
	private synchronized void createVideoRelationship(String fromVideoId, String toVideoId) {
		// Synchronized method: Ensure that the new relationship it's not being created by another thread
		final Video fromVideo = this.videoRepository.findById(fromVideoId).orElse(null);
		final Video toVideo = this.videoRepository.findById(toVideoId).orElse(null);
		if(fromVideo != null && toVideo != null) {
			fromVideo.addRelated(toVideo);
			this.videoRepository.save(fromVideo);
		}
	}
	
	/**
	 * Given a @CrawlerItem performs a search for a related videos
	 * 
	 * @param crawlerItem
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private CrawlerItem searchRelatedVideos(CrawlerItem crawlerItem) throws IOException, Exception {
		int searchedVideos = 0;
		do {
			// Search for related videos
			final Long count = new Long(crawlerItem.getMaxVideosPerLevel() - searchedVideos);
			final YouTubeSearchResponseItem youTubeSearchResponseItem = this.youTubeSearchService.searchRelatedVideos(
					crawlerItem.getRelatedVideoId(),
					crawlerItem.getPageToken(),
					count
				);
			final List<String> videoIds = youTubeSearchResponseItem.getItems();
			crawlerItem.setPageToken(youTubeSearchResponseItem.getNextPageToken());
			crawlerItem.addVideosFound(videoIds.size());
			searchedVideos += videoIds.size();
			
			// Obtain the videos
			crawlerItem = this.obtainVideos(crawlerItem, videoIds, null, null);
	
			// Prepare the crawler to next search
			if(crawlerItem.getPageToken() == null || crawlerItem.getMaxVideosPerLevel().compareTo(searchedVideos) <= 0) {
				// Finish the execution
				crawlerItem.setStatus(CrawlerStatusEnum.FINISHED.getName());	
			} 
		} while(CrawlerStatusEnum.RUNNING.getName().equals(crawlerItem.getStatus()));

		return crawlerItem;
	}
	
	/**
	 * Given a channel id, returns a @Channel from database or fall back into YouTube API
	 * 
	 * @param channelId
	 * @return
	 * @throws IOException
	 */
	private Channel getChannel(final String channelId) throws IOException {
		// Synchronized: This method will be executed synchronized as it is being called inside a synchronized block
		Channel channel = this.channelRepository.findById(channelId).orElse(null);
		if(channel != null) {
			System.out.println("Video channel read from DB");
		} else {
			final ChannelItem channelItem = this.youTubeSearchService.findChannel(channelId);
			if(channelItem != null) {
				System.out.println("Video channel read from API");
				channel = this.channelConverterService.toEntity(channelItem);
				channel = this.channelRepository.save(channel);
			}
		}
		if(channel == null) {
			System.out.println("Video channel impossible to find");
		}
		return channel;
	}

	@Override
	public CrawlerItem getCrawler(Long id) {
		final Crawler crawler = crawlerRepository.findById(id).orElse(null);
		final CrawlerItem crawlerItem = crawlerConverterService.toItem(crawler);
		return crawlerItem;
	}

	@Override
	public CrawlerItem editCrawlerStatus(Long id, String newStatusName) {
		CrawlerItem result = null;
		final CrawlerStatusEnum newStatus = CrawlerStatusEnum.getByName(newStatusName);
		if(newStatus != null) {
			// Synchronized: Ensure crawler status consistency through different threads
			synchronized (this) {
				final Crawler crawler = crawlerRepository.findById(id).orElse(null);
				if(crawler != null) {
					// Allow change crawler status if:
					// - crawler not 'Finished'
					// - crawler not 'Error'
					// - crawler not 'Stopping'
					// - new status is 'Running' or 'Stopping'
					// - new status is different than the actual one
					if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.FINISHED)
						&& !crawler.getStatusByEnum().equals(CrawlerStatusEnum.ERROR)
						&& !crawler.getStatusByEnum().equals(CrawlerStatusEnum.STOPPING)
						&& (newStatus.equals(CrawlerStatusEnum.RUNNING) || newStatus.equals(CrawlerStatusEnum.STOPPING))
						&& !crawler.getStatusByEnum().equals(newStatus)) {
						crawler.setStatusByEnum(newStatus);
						crawlerRepository.save(crawler);
						result = crawlerConverterService.toItem(crawler);
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public CrawlerItem deleteCrawler(Long id) {
		CrawlerItem result = null;
		// Synchronized: Ensure crawler status consistency through different threads
		synchronized (this) {
			final Crawler crawler = this.crawlerRepository.findById(id).orElse(null);
			if(crawler != null) {
				// Delete crawler process is only allowed if it's not 'Running'
				if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.RUNNING)
						&& !crawler.getStatusByEnum().equals(CrawlerStatusEnum.STOPPING)) {
					// Delete first all crawler related videos
					this.videoRepository.removeByCrawlerId(id);
					// Delete orphan channels 
					this.channelRepository.removeOrphanChannels();
					// Delete the crawler 
					this.crawlerRepository.delete(crawler);
					result = this.crawlerConverterService.toItem(crawler);
				}
			}
		}
		
		return result;
	}

	@Override
	public CrawlerStatsItem getCrawlerStats(Long id) {
		CrawlerStatsItem result = null;
		//TODO
		return result;
	}

	@Override
	public List<VideoItem> getCrawlerVideos(Long id) {
		final List<VideoItem> results = new ArrayList<VideoItem>();
		final List<Video> entities = this.videoRepository.findByCrawlerId(id);
		if(entities != null) {
			results.addAll(this.videoConverterService.toListItem(entities));
		}
		return results;
	}
	
	/**
	 * Returns if a crawler process represented by a @CrawlerItem is a search process
	 * 
	 * @param crawlerItem
	 * @return
	 */
	private boolean isSearchCrawler(CrawlerItem crawlerItem) {
		return crawlerItem.getSearch() != null && !crawlerItem.getSearch().isEmpty() ? true : false;
	}
	
	/**
	 * Returns if a crawler process represented by a @CrawlerItem is a related process
	 * 
	 * @param crawlerItem
	 * @return
	 */
	private boolean isRelatedCrawler(CrawlerItem crawlerItem) {
		return crawlerItem.getRelatedVideoId() != null && !crawlerItem.getRelatedVideoId().isEmpty() ? true : false;
	}
	
	/**
	 * Given a @CrawlerItem returns if a related videos search is required
	 * 
	 * @param crawlerItem
	 * @return
	 */
	private boolean isRelatedSearchRequired(CrawlerItem crawlerItem) {
		if(crawlerItem.getRelatedLevels() == null || crawlerItem.getRelatedLevels().compareTo(0) == 0) {
			return false;
		} else if (crawlerItem.getMaxVideosPerLevel() == null || crawlerItem.getMaxVideosPerLevel().compareTo(0) == 0) {
			return false;
		}
		return true;
	}
	
}
