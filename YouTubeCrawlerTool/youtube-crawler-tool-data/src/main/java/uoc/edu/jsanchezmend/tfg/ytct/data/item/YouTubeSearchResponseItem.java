package uoc.edu.jsanchezmend.tfg.ytct.data.item;

import java.util.List;

/**
 * A YouTube response info POJO
 * 
 * @author jsanchezmend
 *
 */
public class YouTubeSearchResponseItem extends AbstractItem {

	private static final long serialVersionUID = -6254417504725850586L;
	
	
	protected List<String> items;

	protected String nextPageToken;

	protected String prevPageToken;
	
	protected Integer totalResults;

	
	public YouTubeSearchResponseItem() {
		super();
	}
	
	
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}
	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public String getPrevPageToken() {
		return prevPageToken;
	}
	public void setPrevPageToken(String prevPageToken) {
		this.prevPageToken = prevPageToken;
	}

	public Integer getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

}
