package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.ChannelService;

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
	
	//TODO

}
