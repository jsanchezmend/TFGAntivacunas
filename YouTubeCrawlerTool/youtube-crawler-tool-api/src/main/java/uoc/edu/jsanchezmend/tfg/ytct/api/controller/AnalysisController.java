package uoc.edu.jsanchezmend.tfg.ytct.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uoc.edu.jsanchezmend.tfg.ytct.core.service.AnalysisService;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.AnalysisSearchItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.GraphItem;

/**
 * Controller for analysis api
 * 
 * @author jsanchezmend
 *
 */
@Controller
@RequestMapping("/api")
public class AnalysisController {
	
	@Autowired
	private AnalysisService analysisService;
	
	
	/**
	 * Create a new graph
	 * 
	 * @param analysisSearch
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/graphs", method = RequestMethod.POST)
	public GraphItem createGraph(@RequestBody AnalysisSearchItem analysisSearch) {
		final GraphItem result = this.analysisService.createGraph(analysisSearch);
		return result;				
	}
	
}
