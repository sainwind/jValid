package org.vector.jv.mod;

import java.util.Date;

public class User {
	private long id;
	private int age;
	private String name;
	private Date birth;
	private String email;
	public User() {
		super();
	}
	public User(long id, String name, Date birth, String email) {
		super();
		this.id = id;
		this.name = name;
		this.birth = birth;
		this.email = email;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
