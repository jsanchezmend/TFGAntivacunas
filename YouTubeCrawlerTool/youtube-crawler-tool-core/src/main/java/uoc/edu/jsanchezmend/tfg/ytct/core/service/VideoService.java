package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

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
	
	//TODO

}
