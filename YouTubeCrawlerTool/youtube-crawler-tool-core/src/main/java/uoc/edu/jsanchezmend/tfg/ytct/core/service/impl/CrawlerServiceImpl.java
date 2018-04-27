package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.ConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.CrawlerService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.YouTubeSearchService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
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
	
	@Autowired
	private CrawlerRepository crawlerRepository;
		
	@Autowired
	private YouTubeSearchService youTubeSearchService;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	@Qualifier("crawlerConverterService")
	private ConverterService<Crawler, CrawlerItem> crawlerConverterService;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	
	
	@Override
	public List<CrawlerItem> listCrawlers() {
		final Iterable<Crawler> crawlerIterable = crawlerRepository.findAll();
		final List<CrawlerItem> crawlerItems = crawlerConverterService.toListItem(crawlerIterable);
		return crawlerItems;
	}

	@Override
	public CrawlerItem newCrawler(String keyword, DateTime fromDateTime, DateTime toDateTime, CrawlerOrderByEnum order,
			Integer relatedLevels, Integer maxRelatedVideosPerLevel, Integer maxVideos, String defaultCategoryName) {
		// TODO: To complete!!
		
		final Crawler crawler = new Crawler();
		crawler.setSearch(keyword);
		/*
		crawler.setFromDate(new Date(fromDateTime.getValue()));
		crawler.setToDate(new Date(toDateTime.getValue()));
		crawler.setOrderByByEnum(order);
		crawler.setRelatedLevels(relatedLevels);
		crawler.setMaxVideosPerLevel(maxRelatedVideosPerLevel);
		crawler.setMaxVideos(maxVideos);
		*/
		crawler.setStatusByEnum(CrawlerStatusEnum.RUNNING);
		this.crawlerRepository.save(crawler);
		
		try {
			final Long startTime = new Date().getTime();
			
			final List<String> videoIds = this.youTubeSearchService.searchVideos(keyword, fromDateTime, toDateTime, order, null, null);
			final List<VideoItem> videoItems = this.youTubeSearchService.findVideos(videoIds);
			crawler.setVideosFound(videoItems.size());
			final List<Video> videos = this.videoConverterService.toListEntity(videoItems);
			this.videoRepository.saveAll(videos);
			
			crawler.setStatusByEnum(CrawlerStatusEnum.FINISHED);			
			final Long endTime = new Date().getTime();
			final Long executionTime = endTime - startTime;
			crawler.setExecutionTime(executionTime);
			this.crawlerRepository.save(crawler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final CrawlerItem result = crawlerConverterService.toItem(crawler);
		return result;
	}

	@Override
	public CrawlerItem newRelatedCrawler(String videoId, Integer relatedLevels, Integer maxRelatedVideosPerLevel,
			Integer maxVideos, String defaultCategoryName) {
		// TODO: To complete!!
		
		final Crawler crawler = new Crawler();
		crawler.setSearch(videoId);
		/*
		crawler.setRelatedLevels(relatedLevels);
		crawler.setMaxVideosPerLevel(maxRelatedVideosPerLevel);
		crawler.setMaxVideos(maxVideos);
		*/
		crawler.setStatusByEnum(CrawlerStatusEnum.RUNNING);
		this.crawlerRepository.save(crawler);
		
		try {
			final Long startTime = new Date().getTime();
			
			final List<String> videoIds = this.youTubeSearchService.searchRelatedVideos(videoId, null, null);
			final List<VideoItem> videoItems = this.youTubeSearchService.findVideos(videoIds);
			crawler.setVideosFound(videoItems.size());
			final List<Video> videos = this.videoConverterService.toListEntity(videoItems);
			this.videoRepository.saveAll(videos);
			
			crawler.setStatusByEnum(CrawlerStatusEnum.FINISHED);			
			final Long endTime = new Date().getTime();
			final Long executionTime = endTime - startTime;
			crawler.setExecutionTime(executionTime);
			this.crawlerRepository.save(crawler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final CrawlerItem result = crawlerConverterService.toItem(crawler);
		return result;
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
				crawler.setStatusByEnum(newStatus);
				crawlerRepository.save(crawler);
				result = crawlerConverterService.toItem(crawler);
			}
		}
		
		return result;
	}

	@Override
	public CrawlerItem deleteCrawler(Long id) {
		CrawlerItem result = null;
		
		final Optional<Crawler> optionalCrawler = crawlerRepository.findById(id);
		if(optionalCrawler.isPresent()) {
			final Crawler crawler = optionalCrawler.get();
			crawlerRepository.delete(crawler);
			result = crawlerConverterService.toItem(crawler);
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
		List<VideoItem> results = null;
		
		//TODO
		
		return results;
	}

}
