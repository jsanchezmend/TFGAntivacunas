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

		// Create a new crawler
		Crawler crawler = crawlerConverterService.toEntity(crawlerItem);
		crawler = this.crawlerRepository.save(crawler);
				
		// Return the created crawler as a result
		final CrawlerItem result = crawlerConverterService.toItem(crawler);
		return result;
	}
	
	@Async
	@Override
	public CompletableFuture<CrawlerItem> executeCrawler(Long crawlerId) {
		// TODO: Search for related videos && make it thread safe	
		
		Crawler crawler = this.crawlerRepository.findById(crawlerId).orElse(null);
		CrawlerItem result = this.crawlerConverterService.toItem(crawler);
		if(result == null) {
			return null;
		}else if(!CrawlerStatusEnum.RUNNING.getName().equals(result.getStatus())) {
			return null;
		}

		// Start the execution
		final Long startTime = new Date().getTime();
		do {
			try {
				result = this.doCrawler(result);
			} catch (IOException e) {
				// Exception throw by YouTube to block our petition
				result.setStatus(CrawlerStatusEnum.BLOCKED.getName());
				e.printStackTrace();
			} catch (Exception e) {
				result.setStatus(CrawlerStatusEnum.ERROR.getName());
				e.printStackTrace();
			}
			// Check if the process is stopped by user
			crawler = this.crawlerRepository.findById(crawlerId).orElse(null);
			if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.RUNNING)) {
				if(CrawlerStatusEnum.RUNNING.getName().equals(result.getStatus())) {
					result.setStatus(crawler.getStatus());
				}
			}
			// Save the current crawler state
			result.setExecutionTime(new Date().getTime() - startTime);
			final Crawler resultEntity = this.crawlerConverterService.toEntity(result);
			this.crawlerRepository.save(resultEntity);
		} while(CrawlerStatusEnum.RUNNING.getName().equals(result.getStatus()));
		
		return CompletableFuture.completedFuture(result);
	}
	
	private CrawlerItem doCrawler(CrawlerItem crawler) throws IOException, Exception {		
		// Search for videos
		final Long count = new Long(crawler.getMaxVideos() - crawler.getVideosFound());
		final Date fromDate = DateUtil.toDate(crawler.getFromDate());
		final Date toDate = DateUtil.toDate(crawler.getToDate());
		final YouTubeSearchResponseItem youTubeSearchResponseItem = this.youTubeSearchService.searchVideos(
				crawler.getSearch(),
				new DateTime(fromDate.getTime()),
				new DateTime(toDate.getTime()), 
				CrawlerOrderByEnum.getByName(crawler.getOrderBy()),
				crawler.getPageToken(),
				count
			);
		final List<String> videoIds = youTubeSearchResponseItem.getItems();
		crawler.setPageToken(youTubeSearchResponseItem.getNextPageToken());
		crawler.setVideosFound(crawler.getVideosFound() + videoIds.size());

		// Retrieve videos
		int savedVideos = crawler.getNewVideos();
		final List<VideoItem> videoItems = this.youTubeSearchService.findVideos(videoIds);
		for(VideoItem videoItem : videoItems) {
			if(!this.videoRepository.existsById(videoItem.getId())) {
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
				savedVideos++;
			}
			
		}
		crawler.setNewVideos(savedVideos);

		// Calculate process completion
		Integer totalVideosToCrawler = crawler.getMaxVideos();
		if(totalVideosToCrawler.compareTo(youTubeSearchResponseItem.getTotalResults()) > 0) {
			totalVideosToCrawler = youTubeSearchResponseItem.getTotalResults();
		}
		crawler.setCompleted(new Float(crawler.getVideosFound() * 100 / totalVideosToCrawler));
		
		// Prepare the crawler to next search
		if(crawler.getPageToken() == null || crawler.getMaxVideos().compareTo(crawler.getVideosFound()) <= 0) {
			// Finish the execution
			crawler.setStatus(CrawlerStatusEnum.FINISHED.getName());	
			crawler.setCompleted(100F);
		} 
		
		return crawler;
	}
	
	private Channel getChannel(final String channelId) throws IOException {
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
			final Crawler crawler = crawlerRepository.findById(id).orElse(null);
			if(crawler != null) {
				// Allow change crawler status if:
				// - crawler not 'Finished'
				// - crawler not 'Error'
				// - new status is 'Running' or 'Stopped'
				// - new status is different than the actual one
				if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.FINISHED)
					&& !crawler.getStatusByEnum().equals(CrawlerStatusEnum.ERROR)
					&& (newStatus.equals(CrawlerStatusEnum.RUNNING) || newStatus.equals(CrawlerStatusEnum.STOPPED))
					&& !crawler.getStatusByEnum().equals(newStatus)) {
					crawler.setStatusByEnum(newStatus);
					crawlerRepository.save(crawler);
					result = crawlerConverterService.toItem(crawler);
				}
			}
		}
		
		return result;
	}

	@Override
	public CrawlerItem deleteCrawler(Long id) {
		CrawlerItem result = null;
		
		final Crawler crawler = this.crawlerRepository.findById(id).orElse(null);
		if(crawler != null) {
			// Delete crawler process is only allowed if it's not 'Running'
			if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.RUNNING)) {
				// Delete first all crawler related videos
				this.videoRepository.removeByCrawlerId(id);
				// Delete orphan channels 
				this.channelRepository.removeOrphanChannels();
				// Delete the crawler 
				this.crawlerRepository.delete(crawler);
				result = this.crawlerConverterService.toItem(crawler);
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
	
	private boolean isSearchCrawler(CrawlerItem crawlerItem) {
		return crawlerItem.getSearch() != null && !crawlerItem.getSearch().isEmpty() ? true : false;
	}
		
}
