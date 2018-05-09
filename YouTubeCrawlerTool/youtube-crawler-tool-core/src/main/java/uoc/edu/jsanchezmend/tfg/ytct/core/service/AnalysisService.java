package uoc.edu.jsanchezmend.tfg.ytct.core.service;

import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.AnalysisSearchItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.graph.GraphItem;

/**
 * Business logic for analysis
 * 
 * @author jsanchezmend
 *
 */
public interface AnalysisService {

	/**
	 * Given a @AnalysisSearchItem, returns a new graph
	 * 
	 * @param analysisSearch
	 * @return
	 */
	GraphItem createGraph(AnalysisSearchItem analysisSearch);

}
