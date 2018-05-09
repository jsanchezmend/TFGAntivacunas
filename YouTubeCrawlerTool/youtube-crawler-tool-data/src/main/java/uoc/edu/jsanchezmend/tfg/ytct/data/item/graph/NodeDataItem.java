package uoc.edu.jsanchezmend.tfg.ytct.data.item.graph;

import uoc.edu.jsanchezmend.tfg.ytct.data.enumeration.GraphNodeTypeEnum;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.AbstractItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.ChannelItem;
import uoc.edu.jsanchezmend.tfg.ytct.data.item.VideoItem;

/**
 * A graph node data POJO
 * 
 * @author jsanchezmend
 *
 */
public class NodeDataItem extends AbstractItem {
	
	private static final long serialVersionUID = 7293284953107931279L;
	

	protected String id;
	
	protected String resourceId;
	
	protected String typeCode;
	
	protected String shape;
	
	protected String color;
	
	protected int size;
	
	protected VideoItem video;
	
	protected ChannelItem channel;
	
	
	public NodeDataItem(GraphNodeTypeEnum type) {
		super();
		this.typeCode = type.getCode();
		this.shape = type.getShape();
		this.color = type.getDefaultColor();
		this.size = type.getDefaultSize();
	}
	
	public String getId() {
		return id;
	}

	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
		this.id = this.typeCode + "-" + resourceId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public String getShape() {
		return shape;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public VideoItem getVideo() {
		return video;
	}
	public void setVideo(VideoItem video) {
		this.video = video;
	}

	public ChannelItem getChannel() {
		return channel;
	}
	public void setChannel(ChannelItem channel) {
		this.channel = channel;
	}
	
}
