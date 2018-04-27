package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;

import com.google.api.client.util.DateTime;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerOrderByEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

/**
 * Business logic for @Crawler
 * 
 * @author jsanchezmend
 *
 */
public interface CrawlerService {
	
	/**
	 * Return all existing crawlers
	 * 
	 * @return
	 */
	List<CrawlerItem> listCrawlers();
	
	/**
	 * Creates a new crawler process
	 * 
	 * @param keyword
	 * @param fromDateTime
	 * @param toDateTime
	 * @param order
	 * @param relatedLevels
	 * @param maxRelatedVideosPerLevel
	 * @param maxVideos
	 * @param defaultCategoryName
	 * @return
	 */
	CrawlerItem newCrawler(String keyword, DateTime fromDateTime, DateTime toDateTime, CrawlerOrderByEnum order,
			Integer relatedLevels, Integer maxRelatedVideosPerLevel, Integer maxVideos, String defaultCategoryName);

	/**
	 * Creates a new crawler process for related videos
	 * 
	 * @param videoId
	 * @param relatedLevels
	 * @param maxRelatedVideosPerLevel
	 * @param maxVideos
	 * @param defaultCategoryName
	 * @return
	 */
	CrawlerItem newRelatedCrawler(String videoId, Integer relatedLevels, Integer maxRelatedVideosPerLevel,
			Integer maxVideos, String defaultCategoryName);

	/**
	 * Given a crawler id, returns a crawler item
	 * 
	 * @param id
	 * @return
	 */
	CrawlerItem getCrawler(Long id);
	
	/**
	 * Edit crawler status
	 * 
	 * @param id
	 * @param newStatus
	 * @return
	 */
	CrawlerItem editCrawlerStatus(Long id, CrawlerStatusEnum newStatus);
	
	
	/**
	 * Deletes a crawler by id
	 * 
	 * @param id
	 * @return
	 */
	CrawlerItem deleteCrawler(Long id);
	
	/**
	 * Given a crawler id returns their stats
	 * 
	 * @param id
	 * @return
	 */
	CrawlerStatsItem getCrawlerStats(Long id);
	
	/**
	 * Given a crawler id, returns a list of their related videos
	 * 
	 * @param id
	 * @return
	 */
	List<VideoItem> getCrawlerVideos(Long id);

}
