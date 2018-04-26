package uoc.edu.jsanchezmend.tfg.ytct.core.converter.impl;

import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.AbstractConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CategoryItem;

/**
 * @ConverterService implementation for @Category entity and @CategoryItem item
 * 
 * @author jsanchezmend
 *
 */
@Service("categoryConverterService")
public class CategoryConverterServiceImpl 
		extends AbstractConverterService<Category, CategoryItem> {

	@Override
	public Category getEntity() {
		return new Category();
	}

	@Override
	public CategoryItem getItem() {
		return new CategoryItem();
	}

}
