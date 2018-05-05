package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.CategoryService;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.CategoryStatsItem;

/**
 * Controller for categories api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	
	/**
	 * List all categories
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CategoryItem> listCategories() {
		final List<CategoryItem> results = this.categoryService.listCategories();
		return results;	
	}
	
	/**
	 * Create a new category
	 * Requires identification
	 * 
	 * @param category
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public CategoryItem createCategory(@RequestBody CategoryItem category) {
		// Create a new category
		final CategoryItem result = this.categoryService.createCategory(category);
		return result;				
	}
	
	/**
	 * Retrieve a category
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public CategoryItem getCategory(@PathVariable(value = "name", required = true) String name) {
		final CategoryItem result = this.categoryService.getCategory(name);
		return result;				
	}
	
	/**
	 * Edit a category
	 * Requires identification
	 * 
	 * @param name
	 * @param category
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{name}", method = RequestMethod.PUT)
	public CategoryItem editCategory(@PathVariable(value = "name", required = true) String name, @RequestBody CategoryItem category) {
		category.setName(name);
		final CategoryItem result = this.categoryService.editCategory(category);
		return result;	
	}
	
	/**
	 * Delete a category
	 * Requires identification
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
	public CategoryItem deleteCategory(@PathVariable(value = "name", required = true) String name) {
		final CategoryItem result = this.categoryService.deleteCategory(name);
		return result;				
	}
	
	/**
	 * Retrieve category stats
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{name}/stats", method = RequestMethod.GET)
	public CategoryStatsItem getCategoryStats(@PathVariable(value = "name", required = true) String name) {
		final CategoryStatsItem result = this.categoryService.getCategoryStats(name);
		return result;				
	}
	
	/**
	 * Retrieve all category related videos
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{name}/videos", method = RequestMethod.GET)
	public List<VideoItem> getCategoryVideos(@PathVariable(value = "name", required = true) String name) {
		final List<VideoItem> results = this.categoryService.getCategoryVideos(name);
		return results;				
	}

}
