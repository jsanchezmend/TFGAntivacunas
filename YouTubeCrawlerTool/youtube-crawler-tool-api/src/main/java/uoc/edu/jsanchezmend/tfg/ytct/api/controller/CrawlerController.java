package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.CrawlerService;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;

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
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CrawlerItem> listCrawlers() {
		final List<CrawlerItem> crawlers = crawlerService.listCrawlers();
		return crawlers;				
	}
	
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CrawlerItem getCrawler(@PathVariable(value = "id", required = true) Long id) {
		final CrawlerItem crawler = crawlerService.getCrawler(id);
		return crawler;				
	}
	
	/*
	 * TEMPORAL APIS
	 */
	
	@ResponseBody
	@RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
	public CrawlerItem search(@PathVariable(value = "keyword", required = true) String keyword) {
		final CrawlerItem result = crawlerService.newCrawler(keyword, null, null, null, null, null, null, null);
		return result;				
	}
	
	@ResponseBody
	@RequestMapping(value = "/search/related/{videoId}", method = RequestMethod.GET)
	public CrawlerItem searchRelated(@PathVariable(value = "videoId", required = true) String videoId) {
		final CrawlerItem result = crawlerService.newRelatedCrawler(videoId, null, null, null, null);
		return result;				
	}

}
