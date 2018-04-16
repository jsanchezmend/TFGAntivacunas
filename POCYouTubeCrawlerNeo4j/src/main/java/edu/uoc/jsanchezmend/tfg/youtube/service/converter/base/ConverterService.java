package edu.uoc.jsanchezmend.tfg.youtube.service.converter.base;

public interface ConverterService<ENTITY, ITEM> {

	/**
	 * Given a item return an entity
	 * 
	 * @param Item
	 * @return
	 */
	public ENTITY toEntity(ITEM item);


}