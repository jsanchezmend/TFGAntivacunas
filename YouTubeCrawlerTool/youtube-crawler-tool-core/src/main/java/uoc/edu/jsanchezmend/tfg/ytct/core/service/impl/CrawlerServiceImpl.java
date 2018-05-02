package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.ConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.CrawlerService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.YouTubeSearchService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Channel;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.YouTubeSearchResponseItem;
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
	@Qualifier("crawlerConverterService")
	private ConverterService<Crawler, CrawlerItem> crawlerConverterService;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	
	@Autowired
	@Qualifier("channelConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Channel, Channel, ChannelItem> channelConverterService;
	
	
	@Override
	public CrawlerItem newCrawler(CrawlerItem crawlerItem) {	
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

		// Create a new crawler
		Crawler crawler = crawlerConverterService.toEntity(crawlerItem);
		crawler.setVideosFound(0);
		crawler.setNewVideos(0);
		crawler.setCompleted(0F);
		crawler.setExecutionTime(0L);
		crawler.setCreatedDate(new Date());
		crawler.setStatusByEnum(CrawlerStatusEnum.RUNNING);
		crawler = this.crawlerRepository.save(crawler);
		final Long crawlerId = crawler.getId();
		
		// Execute the new crawler
		if(this.isSearchCrawler(crawlerItem)) {
			this.executeSearchCrawler(crawlerId);
		} else if(this.isRelatedCrawler(crawlerItem)) {
			this.executeRelatedCrawler(crawlerId);
		} else {
			crawler.setStatusByEnum(CrawlerStatusEnum.FINISHED);
			crawler = this.crawlerRepository.save(crawler);
		}
		
		// Return the created crawler as a result
		final CrawlerItem result = crawlerConverterService.toItem(crawler);
		return result;
	}
	
	private void executeSearchCrawler(Long crawlerId) {
		// TODO: Make as a thread && search for related videos
		
		final Optional<Crawler> optCrawler = this.crawlerRepository.findById(crawlerId);
		final Crawler crawler = optCrawler.orElse(null);
		if(crawler == null) {
			return;
		}
		// If the crawler finished or if it was stopped by the user, then finish the execution
		if(crawler.getStatusByEnum() != CrawlerStatusEnum.RUNNING) {
			return;
		}
		
		try {
			// Start the execution
			final Long startTime = new Date().getTime();
			
			// Search for videos
			final Long count = new Long(crawler.getMaxVideos() - crawler.getVideosFound());
			final YouTubeSearchResponseItem youTubeSearchResponseItem = this.youTubeSearchService.searchVideos(
					crawler.getSearch(),
					new DateTime(crawler.getFromDate().getTime()),
					new DateTime(crawler.getToDate().getTime()), 
					crawler.getOrderByByEnum(),
					crawler.getPageToken(),
					count
				);
			final List<String> videoIds = youTubeSearchResponseItem.getItems();
			crawler.setPageToken(youTubeSearchResponseItem.getNextPageToken());
			crawler.setVideosFound(crawler.getVideosFound() + videoIds.size());

			// Retrieve videos
			int savedVideos = 0;
			final List<VideoItem> videoItems = this.youTubeSearchService.findVideos(videoIds);
			for(VideoItem videoItem : videoItems) {
				if(!this.videoRepository.existsById(videoItem.getId())) {
					final Video newVideo = this.videoConverterService.toEntity(videoItem);
		            final Channel channel = this.getChannel(videoItem.getChannelId());
					newVideo.setChannel(channel);
					newVideo.setCrawler(crawler);
					if(crawler.getCategoryByDefault() != null) {
						newVideo.setCategory(crawler.getCategoryByDefault());
					}
					// Calculation of video scopeRange value
					final BigInteger scopeRange = channel.getSubscribersCount().divide(channel.getVideoCount()).multiply(newVideo.getViewCount());
					newVideo.setScopeRange(scopeRange);
					this.videoRepository.save(newVideo);
					savedVideos++;
				}
				
			}
			crawler.setNewVideos(crawler.getNewVideos() + savedVideos);

			// Calculate process completion
			Integer totalVideosToCrawler = crawler.getMaxVideos();
			if(totalVideosToCrawler.compareTo(youTubeSearchResponseItem.getTotalResults()) > 0) {
				totalVideosToCrawler = youTubeSearchResponseItem.getTotalResults();
			}
			crawler.setCompleted(new Float(crawler.getVideosFound() * 100 / totalVideosToCrawler));
			
			// Prepare the crawler to execute again
			if(crawler.getPageToken() == null || crawler.getMaxVideos().compareTo(crawler.getVideosFound()) <= 0) {
				// Finish the execution
				crawler.setStatusByEnum(CrawlerStatusEnum.FINISHED);	
				crawler.setCompleted(100F);
			} 
			crawler.setExecutionTime(crawler.getExecutionTime() + (new Date().getTime() - startTime));
			this.crawlerRepository.save(crawler);
			this.executeSearchCrawler(crawlerId);
			
		} catch (IOException e) {
			// Exception throw by YouTube to block our petition
			crawler.setStatusByEnum(CrawlerStatusEnum.BLOCKED);
			this.crawlerRepository.save(crawler);
			e.printStackTrace();
		}
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
	
	private void executeRelatedCrawler(Long crawlerId) {
		// TODO: Execute as a new thread
	}
	
	@Override
	public List<CrawlerItem> listCrawlers() {
		final Iterable<Crawler> crawlerIterable = crawlerRepository.findAll();
		final List<CrawlerItem> crawlerItems = crawlerConverterService.toListItem(crawlerIterable);
		return crawlerItems;
	}

	@Override
	public CrawlerItem getCrawler(Long id) {
		final Optional<Crawler> optionalCrawler = crawlerRepository.findById(id);
		final CrawlerItem crawlerItem = crawlerConverterService.toItem(optionalCrawler.orElse(null));
		return crawlerItem;
	}

	@Override
	public CrawlerItem editCrawlerStatus(Long id, CrawlerStatusEnum newStatus) {
		CrawlerItem result = null;
		
		if(newStatus != null) {
			final Optional<Crawler> optionalCrawler = crawlerRepository.findById(id);
			if(optionalCrawler.isPresent()) {
				final Crawler crawler = optionalCrawler.get();
				if(!crawler.getStatusByEnum().equals(CrawlerStatusEnum.FINISHED)) {
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
		
		final Optional<Crawler> optionalCrawler = this.crawlerRepository.findById(id);
		if(optionalCrawler.isPresent()) {
			final Crawler crawler = optionalCrawler.get();
			// Delete first all crawler related videos
			this.videoRepository.removeByCrawlerId(id);
			// Delete orphan channels 
			this.channelRepository.removeOrphanChannels();
			// Delete the crawler 
			this.crawlerRepository.delete(crawler);
			result = this.crawlerConverterService.toItem(crawler);
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
	
	private boolean isRelatedCrawler(CrawlerItem crawlerItem) {
		return crawlerItem.getRelatedVideoId() != null && !crawlerItem.getRelatedVideoId().isEmpty() ? true : false;
	}
	
}
