package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.CategoryStatsItem;

/**
 * Business logic for @Category
 * 
 * @author jsanchezmend
 *
 */
public interface CategoryService {
	
	/**
	 * Return all existing categories
	 * 
	 * @return
	 */
	List<CategoryItem> listCategories();
	
	/**
	 * Creates a new category
	 * 
	 * @param categoryItem
	 * @return
	 */
	CategoryItem createCategory(CategoryItem categoryItem);
	
	/**
	 * Given a category name, returns a category item
	 * 
	 * @param name
	 * @return
	 */
	CategoryItem getCategory(String name);
	
	/**
	 * Edit category
	 * 
	 * @param categoryItem
	 * @return
	 */
	CategoryItem editCategory(CategoryItem categoryItem);
	
	/**
	 * Deletes a category by name
	 * 
	 * @param name
	 * @return
	 */
	CategoryItem deleteCategory(String name);
	
	/**
	 * Given a category name returns their stats
	 * 
	 * @param name
	 * @return
	 */
	CategoryStatsItem getCategoryStats(String name);
	
	/**
	 * Given a category name, returns a list of their categorized videos
	 * 
	 * @param name
	 * @return
	 */
	List<VideoItem> getCategoryVideos(String name);

}
