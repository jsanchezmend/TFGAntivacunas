package uoc.edu.jsanchezmend.tfg.ytct.data.item;

/**
 * @Category POJO representation
 * 
 * @author jsanchezmend
 *
 */
public class CategoryItem extends AbstractItem {

	private static final long serialVersionUID = 961597489806592434L;
	
	
	protected String name;

	protected String color;
	
	
	public CategoryItem() {
		super();
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
