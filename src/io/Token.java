package io;

/**
 * Created by Roy Gao on 11/18/2015.
 */
public class Token {

	private String key;
	private String tag;

	public Token(String key, String tag) {
		this.key = key;
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public String getTag() {
		return tag;
	}
}
