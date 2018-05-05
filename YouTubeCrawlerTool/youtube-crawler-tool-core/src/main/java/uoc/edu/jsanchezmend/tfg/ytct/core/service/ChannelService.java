package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import java.util.List;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.stats.ChannelStatsItem;

/**
 * Business logic for @Channel
 * 
 * @author jsanchezmend
 *
 */
public interface ChannelService {
	
	/**
	 * Return all existing channels
	 * 
	 * @return
	 */
	List<ChannelItem> listChannels();
		
	/**
	 * Given a channel id, returns a channel item
	 * 
	 * @param id
	 * @return
	 */
	ChannelItem getChannel(String id);
	
	/**
	 * Deletes a channel by id
	 * 
	 * @param id
	 * @return
	 */
	ChannelItem deleteChannel(String id);
	
	/**
	 * Returns the stats of a channel
	 * 
	 * @return
	 */
	ChannelStatsItem getChannelStats(String id);
	
	/**
	 * Given a channel id, returns a list of their related videos
	 * 
	 * @param id
	 * @return
	 */
	List<VideoItem> getChannelVideos(String id);

}
