package com.ss.weekone;

public class Book {
	private int id;
	private String title;
	private int authId;
	private int pubId;
	
	public Book() {
		id = 0;
		title = "";
		authId = 0;
		pubId = 0;
	}
	
	public Book(int bookId, String bookTitle, int bookAuth, int bookPub) {
		id = bookId;
		title = bookTitle;
		authId = bookAuth;
		pubId = bookPub;
	}
	
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
	}
	public int getPubId() {
		return pubId;
	}
	public void setPubId(int pubId) {
		this.pubId = pubId;
	}
}
