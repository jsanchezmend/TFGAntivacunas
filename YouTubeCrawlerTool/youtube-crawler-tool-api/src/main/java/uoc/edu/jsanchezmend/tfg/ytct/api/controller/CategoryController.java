package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.CategoryService;

/**
 * Controller for categories api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	//TODO

}
