package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.VideoStatsItem;

/**
 * Business logic for @Video
 * 
 * @author jsanchezmend
 *
 */
public interface VideoService {
	
	/**
	 * Return all existing videos
	 * 
	 * @return
	 */
	List<VideoItem> listVideos();
	
	/**
	 * Return all favorites videos
	 * 
	 * @return
	 */
	List<VideoItem> listFavoritesVideos();
	
	/**
	 * Returns the stats of all videos
	 * 
	 * @return
	 */
	VideoStatsItem getVideosStats();
	
	
	/**
	 * Given a video id, returns a video item
	 * 
	 * @param id
	 * @return
	 */
	VideoItem getVideo(String id);
	
	/**
	 * Edit video category
	 * 
	 * @param id
	 * @param newCategoryName
	 * @return
	 */
	VideoItem editVideoCategory(String id, String newCategoryName);
	
	/**
	 * Edit video favorite
	 * 
	 * @param id
	 * @param favorite
	 * @return
	 */
	VideoItem editVideoFavorite(String id, Boolean favorite);
	
	/**
	 * Deletes a video by id
	 * 
	 * @param id
	 * @return
	 */
	VideoItem deleteVideo(String id);
	
	/**
	 * Given a video id, returns a list of their related videos
	 * 
	 * @param id
	 * @return
	 */
	List<VideoItem> getRelatedVideos(String id);

}
