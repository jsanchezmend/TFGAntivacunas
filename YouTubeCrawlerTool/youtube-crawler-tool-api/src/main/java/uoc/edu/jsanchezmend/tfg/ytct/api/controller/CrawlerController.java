package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.text.ParseException;
import java.util.List;

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
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

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
		final List<CrawlerItem> results = crawlerService.listCrawlers();
		return results;				
	}
	
	/**
	 * Starts a new crawler process
	 * Requires identification
	 * 
	 * @param crawler
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public CrawlerItem startNewCrawler(@RequestBody CrawlerItem crawler) {
		final CrawlerItem result = crawlerService.newCrawler(crawler);		
		return result;				
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
		final CrawlerItem result = crawlerService.getCrawler(id);
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
		CrawlerItem result = null;
		final String newStatus = crawler.getStatus();
		final CrawlerStatusEnum newStatusEnum = CrawlerStatusEnum.getByName(newStatus);
		if(newStatusEnum != null) {
			result = crawlerService.editCrawlerStatus(id, newStatusEnum);
		}
		return result;				
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
		final CrawlerItem result = crawlerService.getCrawler(id);
		if(result != null) {
			crawlerService.deleteCrawler(id);
		}	
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
		final CrawlerStatsItem result = crawlerService.getCrawlerStats(id);
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
		final List<VideoItem> results = crawlerService.getCrawlerVideos(id);
		return results;				
	}
	
	/********************************
	 * TEMPORAL APIS!!
	 * @throws ParseException 
	 ********************************/
	@ResponseBody
	@RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
	public CrawlerItem search(@PathVariable(value = "keyword", required = true) String keyword) throws ParseException {
		final CrawlerItem crawler = new CrawlerItem();
		crawler.setSearch(keyword);
		final CrawlerItem result = crawlerService.newCrawler(crawler);
		return result;				
	}
	
	@ResponseBody
	@RequestMapping(value = "/search/related/{videoId}", method = RequestMethod.GET)
	public CrawlerItem searchRelated(@PathVariable(value = "videoId", required = true) String videoId) throws ParseException {
		final CrawlerItem crawler = new CrawlerItem();
		crawler.setRelatedVideoId(videoId);
		final CrawlerItem result = crawlerService.newCrawler(crawler);
		return result;					
	}

}
