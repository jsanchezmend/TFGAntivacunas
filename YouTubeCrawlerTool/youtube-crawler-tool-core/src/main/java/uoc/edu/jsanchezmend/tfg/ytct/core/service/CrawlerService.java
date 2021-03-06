package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.CrawlerStatsItem;

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
	 * @param crawlerItem
	 * @return
	 */
	CrawlerItem createCrawler(CrawlerItem crawlerItem);

	/**
	 * Executes a crawler process
	 * 
	 * @param crawlerId
	 * @return
	 */
	CompletableFuture<CrawlerItem> executeCrawler(Long crawlerId);

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
	 * @param newStatusName
	 * @return
	 */
	CrawlerItem editCrawlerStatus(Long id, String newStatusName);
	
	
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
