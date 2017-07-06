package com.fmi.fkt.data;

public class Article {

	private String id;

	private String content;

	public Article() {
		super();
	}

	public Article(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
