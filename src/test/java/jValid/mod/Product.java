package jValid.mod;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Product {
	private long id;
	
	private BigDecimal amount;//总额
	private BigDecimal price;//价格 ,价格必须小于等于总额
	
	private Time createTime;//时间，存：时分秒
	private Date createDate;//日期时间，存：年月日时分秒
	private Timestamp createdtm;//时间戳，存：年月日时分秒毫秒
	private java.sql.Date createdm;//日期时间，存：年月日
	
	private Time updateTime;//时间，存：时分秒
	private Date updateDate;//日期时间，存：年月日时分秒
	private Timestamp updatedtm;//时间戳，存：年月日时分秒毫秒
	private java.sql.Date updatedm;//日期时间，存：年月日
	
	public Product(long id, Time createTime) {
		super();
		this.id = id;
		this.createTime = createTime;
	}
	public Product(Time createTime, Time updateTime) {
		super();
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public Product(Date createDate, Date updateDate) {
		super();
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
	public Product(Timestamp createdtm, Timestamp updatedtm) {
		super();
		this.createdtm = createdtm;
		this.updatedtm = updatedtm;
	}
	public Product(BigDecimal amount, BigDecimal price) {
		super();
		this.amount = amount;
		this.price = price;
	}
	
	public Product(java.sql.Date createdm, java.sql.Date updatedm) {
		super();
		this.createdm = createdm;
		this.updatedm = updatedm;
	}
	public Product(long id, Date createDate) {
		super();
		this.id = id;
		this.createDate = createDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Time getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Time createTime) {
		this.createTime = createTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Timestamp getCreatedtm() {
		return createdtm;
	}
	public void setCreatedtm(Timestamp createdtm) {
		this.createdtm = createdtm;
	}
	public java.sql.Date getCreatedm() {
		return createdm;
	}
	public void setCreatedm(java.sql.Date createdm) {
		this.createdm = createdm;
	}
	public Time getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Time updateTime) {
		this.updateTime = updateTime;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Timestamp getUpdatedtm() {
		return updatedtm;
	}
	public void setUpdatedtm(Timestamp updatedtm) {
		this.updatedtm = updatedtm;
	}
	public java.sql.Date getUpdatedm() {
		return updatedm;
	}
	public void setUpdatedm(java.sql.Date updatedm) {
		this.updatedm = updatedm;
	}
}
