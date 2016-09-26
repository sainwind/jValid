package jValid.mod;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Product {
	private long id;
	private Time createTime;//时间，存：时分秒
	private Date createDate;//日期时间，存：年月日时分秒
	private Timestamp createdtm;//时间戳，存：年月日时分秒毫秒
	
	public Product(long id, Time createTime) {
		super();
		this.id = id;
		this.createTime = createTime;
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
}
