package uoc.edu.jsanchezmend.tfg.ytct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/")
public class YouTubeCrawlerToolController {
	
	private static final String LOGIN_VIEW = "loginView";
	private static final String CRAWLERS_VIEW = "crawlersView";
	private static final String CRAWLER_VIEW = "crawlerView";
	private static final String FAVORITE_VIDEOS_VIEW = "favoriteVideosView";
	private static final String VIDEOS_VIEW = "videosView";
	private static final String VIDEO_VIEW = "videoView";
	private static final String CHANNELS_VIEW = "channelsView";
	private static final String CHANNEL_VIEW = "channelView";
	private static final String CATEGORIES_VIEW = "categoriesView";
	private static final String ADD_CATEGORY_VIEW = "addCategoryView";
	private static final String EDIT_CATEGORY_VIEW = "editCategoryView";
	private static final String ANALYSIS_VIEW = "analysisView";
	
	
	@RequestMapping(value="login")
    public String getLoginView() {
        return LOGIN_VIEW;
    }
	
    @RequestMapping(value="")
    public String getCrawlersView(Model model) {
    	model.addAttribute("activeMenuOption", CRAWLERS_VIEW);
        return CRAWLERS_VIEW;
    }
    
    @RequestMapping(value="crawlers/{id}")
    public String getCrawlerView(Model model, @PathVariable(value = "id", required = true) String id) {
    	model.addAttribute("activeMenuOption", CRAWLERS_VIEW);
    	model.addAttribute("crawlerId", id);
        return CRAWLER_VIEW;
    }
    
    @RequestMapping(value="videos/favorites")
    public String getFavoriteVideosView(Model model) {
    	model.addAttribute("activeMenuOption", FAVORITE_VIDEOS_VIEW);
        return FAVORITE_VIDEOS_VIEW;
    }
    
    @RequestMapping(value="videos")
    public String getVideosView(Model model) {
    	model.addAttribute("activeMenuOption", VIDEOS_VIEW);
        return VIDEOS_VIEW;
    }
    
    @RequestMapping(value="videos/{id}")
    public String getVideoView(Model model, @PathVariable(value = "id", required = true) String id) {
    	model.addAttribute("activeMenuOption", VIDEOS_VIEW);
    	model.addAttribute("videoId", id);
        return VIDEO_VIEW;
    }
    
    @RequestMapping(value="channels")
    public String getChannelsView(Model model) {
    	model.addAttribute("activeMenuOption", CHANNELS_VIEW);
        return CHANNELS_VIEW;
    }
    
    @RequestMapping(value="channels/{id}")
    public String getChannelView(Model model, @PathVariable(value = "id", required = true) String id) {
    	model.addAttribute("activeMenuOption", CHANNELS_VIEW);
    	model.addAttribute("channelId", id);
        return CHANNEL_VIEW;
    }
    
    @RequestMapping(value="analysis")
    public String getAnalysisView(Model model) {
    	model.addAttribute("activeMenuOption", ANALYSIS_VIEW);
        return ANALYSIS_VIEW;
    }
    
    @RequestMapping(value="categories")
    public String getCategoriesView(Model model) {
    	model.addAttribute("activeMenuOption", CATEGORIES_VIEW);
        return CATEGORIES_VIEW;
    }
    
    @RequestMapping(value="categories/new")
    public String getAddCategoryView(Model model) {
    	model.addAttribute("activeMenuOption", CATEGORIES_VIEW);
        return ADD_CATEGORY_VIEW;
    }
    
    @RequestMapping(value="categories/{name}")
    public String getEditCategoryView(Model model, @PathVariable(value = "name", required = true) String name) {
    	model.addAttribute("activeMenuOption", CATEGORIES_VIEW);
    	model.addAttribute("categoryName", name);
        return EDIT_CATEGORY_VIEW;
    }
    
}
