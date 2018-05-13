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
    public String getCrawlerView(Model model) {
    	model.addAttribute("activeMenuOption", CRAWLERS_VIEW);
        return CRAWLER_VIEW;
    }
    
    @RequestMapping(value="videos/{id}")
    public String getVideoView(Model model) {
    	model.addAttribute("activeMenuOption", VIDEO_VIEW);
        return VIDEO_VIEW;
    }
    
    @RequestMapping(value="channels")
    public String getChannelsView(Model model) {
    	model.addAttribute("activeMenuOption", CHANNELS_VIEW);
        return CHANNELS_VIEW;
    }
    
    @RequestMapping(value="channels/{id}")
    public String getChannelView(Model model) {
    	model.addAttribute("activeMenuOption", CHANNELS_VIEW);
        return CHANNEL_VIEW;
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
    
    @RequestMapping(value="analysis")
    public String getAnalysisView(Model model) {
    	model.addAttribute("activeMenuOption", ANALYSIS_VIEW);
        return ANALYSIS_VIEW;
    }

}
