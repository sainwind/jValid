package jValid.mod;

public class Cup {
	private String name;
	private String type;
	private String color;
	private String brand;
	
	public Cup() {
		super();
	}
	public Cup(String name) {
		super();
		this.name = name;
	}
	public Cup(String name, String brand) {
		super();
		this.name = name;
		this.brand = brand;
	}
	public Cup(String name, String type, String color) {
		super();
		this.name = name;
		this.type = type;
		this.color = color;
	}
	public Cup(String name, String type, String color, String brand) {
		super();
		this.name = name;
		this.type = type;
		this.color = color;
		this.brand = brand;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
