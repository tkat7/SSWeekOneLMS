package com.ss.weekone;

public class Author {

	private int id;
	private String name;
	
	public Author() {
		id = 0;
		name = "";
	}
	
	public Author(int authId, String authName) {
		id = authId;
		name = authName;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
