package com.gl.anything.vo;

import java.util.Date;

public class GreetingVo {
	private int id;
	private String words;
	private Date greetAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public Date getGreetAt() {
		return greetAt;
	}

	public void setGreetAt(Date greetAt) {
		this.greetAt = greetAt;
	}

}
