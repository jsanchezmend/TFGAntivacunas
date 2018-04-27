package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.VideoService;

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
	
	//TODO

}
