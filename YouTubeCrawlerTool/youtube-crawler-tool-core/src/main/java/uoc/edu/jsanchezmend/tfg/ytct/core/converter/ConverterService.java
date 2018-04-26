package uoc.edu.jsanchezmend.tfg.ytct.core.converter;

import java.util.List;

/**
 * Contract for a converter service
 * 
 * @author jsanchezmend
 *
 * @param <ENTITY>
 * @param <ITEM>
 */
public interface ConverterService<ENTITY, ITEM> {
	
	/**
	 * Given a @ENTITY returns a @ITEM
	 * 
	 * @param entity
	 * @return
	 */
	public ITEM toItem(ENTITY entity);
	
	/**
	 * Given a list of @ENTITY returns a list of @ITEM
	 * 
	 * @param entity
	 * @return
	 */
	public List<ITEM> toListItem(Iterable<ENTITY> entities);

	/**
	 * Given a @ITEM returns a @ENTITY
	 * @param item
	 * @return
	 */
	public ENTITY toEntity(ITEM item);
	
	/**
	 * Given a list of @ITEM returns a list of @ENTITY
	 * 
	 * @param items
	 * @return
	 */
	public List<ENTITY> toListEntity(List<ITEM> items);

}
