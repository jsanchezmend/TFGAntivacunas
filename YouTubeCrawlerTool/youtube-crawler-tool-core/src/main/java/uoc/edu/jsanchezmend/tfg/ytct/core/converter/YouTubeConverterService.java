package uoc.edu.jsanchezmend.tfg.ytct.core.converter;

import java.util.List;

public interface YouTubeConverterService<YOUTUBE_ENTITY, ENTITY, ITEM> 
		extends ConverterService<ENTITY, ITEM> {
	
	/**
	 * Given a @YOUTUBE_ENTITY returns a @ITEM
	 * 
	 * @param youTubeEntity
	 * @return
	 */
	public ITEM fromYouTubeToItem(YOUTUBE_ENTITY youTubeEntity);
	
	/**
	 * Given a list of @YOUTUBE_ENTITY returns a list of @ITEM
	 * 
	 * @param youTubeEntity
	 * @return
	 */
	public List<ITEM> fromYouTubeListToListItem(List<YOUTUBE_ENTITY> youTubeEntity);

}
