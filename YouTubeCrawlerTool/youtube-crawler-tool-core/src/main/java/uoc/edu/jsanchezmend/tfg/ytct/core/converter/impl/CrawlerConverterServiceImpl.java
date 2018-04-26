package uoc.edu.jsanchezmend.tfg.ytct.core.converter.impl;

import org.springframework.stereotype.Service;

import uoc.edu.jsanchezmend.tfg.ytct.core.converter.AbstractConverterService;
import uoc.edu.jsanchezmend.tfg.ytct.data.entity.Crawler;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.CrawlerItem;

/**
 * @ConverterService implementation for @Crawler entity and @CrawlerItem item
 * 
 * @author jsanchezmend
 *
 */
@Service("categoryConverterService")
public class CrawlerConverterServiceImpl 
		extends AbstractConverterService<Crawler, CrawlerItem> {

	@Override
	public Crawler getEntity() {
		return new Crawler();
	}

	@Override
	public CrawlerItem getItem() {
		return new CrawlerItem();
	}

}
