package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.ChannelService;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.ChannelStatsItem;

/**
 * Controller for channels api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/channels")
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	
	
	/**
	 * List all channels
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ChannelItem> listChannels() {
		final List<ChannelItem> results = this.channelService.listChannels();
		return results;	
	}
	
	/**
	 * Retrieve a channel
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ChannelItem getChannel(@PathVariable(value = "id", required = true) String id) {
		final ChannelItem result = this.channelService.getChannel(id);
		return result;				
	}
	
	/**
	 * Delete a channel
	 * Requires identification
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ChannelItem deleteChannel(@PathVariable(value = "id", required = true) String id) {
		final ChannelItem result = this.channelService.deleteChannel(id);
		return result;				
	}
	
	/**
	 * Retrieve a channel stats
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/stats", method = RequestMethod.GET)
	public ChannelStatsItem getChannelStats(@PathVariable(value = "id", required = true) String id) {
		final ChannelStatsItem result = this.channelService.getChannelStats(id);
		return result;				
	}
	
	/**
	 * Retrieve all channel related videos
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}/videos", method = RequestMethod.GET)
	public List<VideoItem> getRelatedChannels(@PathVariable(value = "id", required = true) String id) {
		final List<VideoItem> results = this.channelService.getChannelVideos(id);
		return results;				
	}

}
