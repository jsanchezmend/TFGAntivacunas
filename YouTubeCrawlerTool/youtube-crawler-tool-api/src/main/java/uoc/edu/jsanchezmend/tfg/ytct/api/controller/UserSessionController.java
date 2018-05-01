package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.UserSessionService;

/**
 * Controller for session api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/session")
public class UserSessionController {
	
	@Autowired
	private UserSessionService userSessionService;

	//TODO

}