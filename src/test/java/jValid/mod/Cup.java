package jValid.mod;

import java.sql.Date;

public class Cup {
	private String name;
	private String type;
	private String color;
	private String brand;
	private Date createDate;//纯date，只存日期：年月日
	
	public Cup() {
		super();
	}
	
	public Cup(String name, Date createDate) {
		super();
		this.name = name;
		this.createDate = createDate;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
