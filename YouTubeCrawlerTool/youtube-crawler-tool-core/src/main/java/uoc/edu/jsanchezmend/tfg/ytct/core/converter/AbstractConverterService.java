package uoc.edu.jsanchezmend.tfg.ytct.core.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * Abstract implementation of @ConverterService
 * 
 * @author jsanchezmend
 *
 * @param <ENTITY>
 * @param <ITEM>
 */
public abstract class AbstractConverterService<ENTITY, ITEM> 
		implements ConverterService<ENTITY, ITEM> {
	
	public abstract ENTITY getEntity();
	public abstract ITEM getItem();

	@Override
	public ITEM toItem(ENTITY entity) {
		ITEM item = getItem();
		BeanUtils.copyProperties(entity, item);
		customToItem(entity, item);
		return item;
	}

	@Override
	public List<ITEM> toListItem(List<ENTITY> entities) {
		List<ITEM> list = new ArrayList<ITEM>();
		for (ENTITY entity : entities) {
			list.add(this.toItem(entity));
		}
		return Collections.unmodifiableList(list);
	}

	@Override
	public ENTITY toEntity(ITEM item) {
		ENTITY entity = getEntity();
		BeanUtils.copyProperties(item, entity);
		customToEntity(item,entity);
		return entity;
	}

	@Override
	public List<ENTITY> toListEntity(List<ITEM> items) {
		List<ENTITY> list = new ArrayList<ENTITY>(items.size());
		for (ITEM item : items) {
			list.add(this.toEntity(item));
		}
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * Extra conversion steps to @ITEM
	 * 
	 * @param entity
	 * @param item
	 */
	protected void customToItem(ENTITY entity, ITEM item) {

	}

	/**
	 * Extra conversion steps to @ENTITY
	 * @param item
	 * @param entity
	 */
	protected void customToEntity(ITEM item, ENTITY entity) {

	}

}
