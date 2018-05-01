package uoc.edu.jsanchezmend.tfg.ytct.core.converter.impl;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.AbstractConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.core.util.DateUtil;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Category;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.repository.CategoryRepository;

/**
 * @ConverterService implementation for @Crawler entity and @CrawlerItem item
 * 
 * @author jsanchezmend
 *
 */
@Service("crawlerConverterService")
public class CrawlerConverterServiceImpl 
		extends AbstractConverterService<Crawler, CrawlerItem> {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Crawler getEntity() {
		return new Crawler();
	}

	@Override
	public CrawlerItem getItem() {
		return new CrawlerItem();
	}

	@Override
	protected void customToItem(Crawler entity, CrawlerItem item) {
		final Category categoryByDefault = entity.getCategoryByDefault();
		if(categoryByDefault != null) {
			item.setCategoryByDefault(categoryByDefault.getName());
		}
		
		final Date fromDate = entity.getFromDate();
		if(fromDate != null) {
			item.setFromDate(DateUtil.toString(fromDate));
		}
		
		final Date toDate = entity.getToDate();
		if(toDate != null) {
			item.setToDate(DateUtil.toString(toDate));
		}
	}
	
	@Override
	protected void customToEntity(CrawlerItem item, Crawler entity) {
		final String categoryByDefaultStr = item.getCategoryByDefault();
		if(categoryByDefaultStr != null && !categoryByDefaultStr.isEmpty()) {
			final Category categoryByDefault = categoryRepository.findById(categoryByDefaultStr).orElse(null);
			entity.setCategoryByDefault(categoryByDefault);
		}
		
		final String fromDateStr = item.getFromDate();
		if(fromDateStr != null) {
			try {
				entity.setFromDate(DateUtil.toDate(fromDateStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		final String toDateStr = item.getToDate();
		if(toDateStr != null) {
			try {
				entity.setToDate(DateUtil.toDate(toDateStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
}
