package uoc.edu.jsanchezmend.tfg.ytct.data.enumeration;

/**
 * Graph node possible types enumeration
 * 
 * @author jsanchezmend
 *
 */
public enum GraphNodeTypeEnum {
	
	VIDEO("Video", "v", "ellipse", "#999999", 50),
	CHANNEL("Channel", "c", "diamond", "#999999", 100);
	
	protected String name;
	protected String code;
	protected String shape;
	protected String defaultColor;
	protected int defaultSize;
	
	
	GraphNodeTypeEnum(String name, String code, String shape, String defaultColor, int defaultSize) {
		this.name = name;
		this.code = code;
		this.shape = shape;
		this.defaultColor = defaultColor;
		this.defaultSize = defaultSize;
	}

	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getShape() {
		return shape;
	}

	public String getDefaultColor() {
		return defaultColor;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public static GraphNodeTypeEnum getByName(String name) {
		for(GraphNodeTypeEnum item : GraphNodeTypeEnum.values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

}
