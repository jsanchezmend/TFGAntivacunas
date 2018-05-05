package uoc.edu.jsanchezmend.tfg.ytct.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.ConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.converter.YouTubeConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.service.CategoryService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Video;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.CategoryStatsItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.CategoryRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.ChannelRepository;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.VideoRepository;

/**
 * @CategoryService implementation
 * 
 * @author jsanchezmend
 *
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	@Qualifier("categoryConverterService")
	private ConverterService<Category, CategoryItem> categoryConverterService;
	
	@Autowired
	@Qualifier("videoConverterService")
	private YouTubeConverterService<com.google.api.services.youtube.model.Video, Video, VideoItem> videoConverterService;
	
	
	@Override
	public List<CategoryItem> listCategories() {
		final Iterable<Category> categoryIterable = this.categoryRepository.findAll();
		final List<CategoryItem> categoryItems = this.categoryConverterService.toListItem(categoryIterable);
		return categoryItems;
	}

	@Override
	public CategoryItem createCategory(CategoryItem categoryItem) {
		// Create a new category
		Category category = this.categoryConverterService.toEntity(categoryItem);
		category = this.categoryRepository.save(category);
				
		// Return the created category as a result
		final CategoryItem result = this.categoryConverterService.toItem(category);
		return result;
	}

	@Override
	public CategoryItem getCategory(String name) {
		final Category category = this.categoryRepository.findById(name).orElse(null);
		final CategoryItem result = this.categoryConverterService.toItem(category);
		return result;
	}

	@Override
	public CategoryItem editCategory(CategoryItem categoryItem) {
		CategoryItem result = null;
		
		if(categoryItem != null) {
			Category category = this.categoryRepository.findById(categoryItem.getName()).orElse(null);
			if(category!= null) {
				category.setColor(categoryItem.getColor());
				category = this.categoryRepository.save(category);
				result = this.categoryConverterService.toItem(category);
			}
		}
		
		return result;
	}

	@Override
	public CategoryItem deleteCategory(String name) {
		CategoryItem result = null;
		
		final Category category = this.categoryRepository.findById(name).orElse(null);
		if(category != null) {
			// Delete first all category related videos
			this.videoRepository.removeByCategoryName(name);
			// Delete orphan channels 
			this.channelRepository.removeOrphanChannels();
			// Delete the category 
			this.categoryRepository.delete(category);
			result = this.categoryConverterService.toItem(category);
		}

		return result;
	}

	@Override
	public CategoryStatsItem getCategoryStats(String name) {
		CategoryStatsItem categoryStatsItem = null;
		// TODO
		return categoryStatsItem;
	}

	@Override
	public List<VideoItem> getCategoryVideos(String name) {
		final List<VideoItem> results = new ArrayList<VideoItem>();
		final List<Video> entities = this.videoRepository.findByCategoryName(name);
		if(entities != null && !entities.isEmpty()) {
			results.addAll(this.videoConverterService.toListItem(entities));
		}
		return results;
	}

}
