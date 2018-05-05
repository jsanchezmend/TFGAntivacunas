package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.VideoService;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.VideoStatsItem;

/**
 * Controller for videos api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/videos")
public class VideoController {
	
	@Autowired
	private VideoService videoService;

	
	/**
	 * List all videos
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<VideoItem> listVideos() {
		final List<VideoItem> results = this.videoService.listVideos();
		return results;	
	}
	
	/**
	 * Retrieve videos stats
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public VideoStatsItem getVideosStats() {
		final VideoStatsItem result = this.videoService.getVideosStats();
		return result;				
	}
	
	/**
	 * Retrieve a video
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public VideoItem getVideo(@PathVariable(value = "id", required = true) String id) {
		final VideoItem result = this.videoService.getVideo(id);
		return result;				
	}
	
	/**
	 * Edit a video
	 * Requires identification
	 * 
	 * @param id
	 * @param video
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public VideoItem editVideo(@PathVariable(value = "id", required = true) String id, @RequestBody VideoItem video) {
		final String newCategoryName;
		if(video != null && video.getCategory() != null) {
			newCategoryName = video.getCategory().getName();
		} else {
			newCategoryName = null;
		}
		final VideoItem result = this.videoService.editVideoCategory(id, newCategoryName);
		return result;	
	}
	
	/**
	 * Delete a video
	 * Requires identification
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public VideoItem deleteVideo(@PathVariable(value = "id", required = true) String id) {
		final VideoItem result = this.videoService.deleteVideo(id);
		return result;				
	}
	
	/**
	 * Retrieve all video related videos
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/videos", method = RequestMethod.GET)
	public List<VideoItem> getRelatedVideos(@PathVariable(value = "id", required = true) String id) {
		final List<VideoItem> results = this.videoService.getRelatedVideos(id);
		return results;				
	}
	
}
