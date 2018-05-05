package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.CrawlerService;
import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.CrawlerStatusEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.CrawlerStatsItem;

/**
 * Controller for crawlers api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/crawlers")
public class CrawlerController {
	
	@Autowired
	private CrawlerService crawlerService;
	
	
	/**
	 * List all crawlers
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CrawlerItem> listCrawlers() {
		final List<CrawlerItem> results = this.crawlerService.listCrawlers();
		return results;				
	}
	
	/**
	 * Create and starts a new crawler process
	 * Requires identification
	 * 
	 * @param crawler
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public CrawlerItem createCrawler(@RequestBody CrawlerItem crawler) {
		// Create a new crawler process
		final CrawlerItem result = this.crawlerService.createCrawler(crawler);
		final Long crawlerId = result.getId();
		// Execute it in a new thread
		final CompletableFuture<CrawlerItem> futureResult = this.crawlerService.executeCrawler(crawlerId);
		return futureResult.getNow(result);				
	}
	
	/**
	 * Retrieve a crawler process
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CrawlerItem getCrawler(@PathVariable(value = "id", required = true) Long id) {
		final CrawlerItem result = this.crawlerService.getCrawler(id);
		return result;				
	}
	
	/**
	 * Edit a crawler process
	 * Only change the crawler status is allowed
	 * Requires identification
	 * 
	 * @param id
	 * @param crawler
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public CrawlerItem editCrawler(@PathVariable(value = "id", required = true) Long id, @RequestBody CrawlerItem crawler) {
		// Save the new crawler status
		final String newStatus = crawler.getStatus();
		final CrawlerItem result = this.crawlerService.editCrawlerStatus(id, newStatus);
		
		// If the new status is 'Running', execute the crawler process
		if(result != null && result.getStatus() != null && CrawlerStatusEnum.RUNNING.getName().equals(result.getStatus())) {
			final Long crawlerId = result.getId();
			// Execute it in a new thread
			final CompletableFuture<CrawlerItem> futureResult = this.crawlerService.executeCrawler(crawlerId);
			return futureResult.getNow(result);	
		} else {
			return result;	
		}
	}
	
	/**
	 * Delete a crawler process
	 * Requires identification
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public CrawlerItem deleteCrawler(@PathVariable(value = "id", required = true) Long id) {
		final CrawlerItem result = this.crawlerService.deleteCrawler(id);
		return result;				
	}
	
	/**
	 * Retrieve crawler stats
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/stats", method = RequestMethod.GET)
	public CrawlerStatsItem getCrawlerStats(@PathVariable(value = "id", required = true) Long id) {
		final CrawlerStatsItem result = this.crawlerService.getCrawlerStats(id);
		return result;				
	}
	
	/**
	 * Retrieve all crawler related videos
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/videos", method = RequestMethod.GET)
	public List<VideoItem> getCrawlerVideos(@PathVariable(value = "id", required = true) Long id) {
		final List<VideoItem> results = this.crawlerService.getCrawlerVideos(id);
		return results;				
	}
	
}
