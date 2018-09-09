package com.example.model;

/**
 * 
 * @author Sunny Gupta
 *
 */
public class Link {

	private String url;
	private String text;

	public Link(String url, String text) {
		super();
		this.url = url;
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "Link [url=" + url + ", text=" + text + "]";
	}

}
