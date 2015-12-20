package io;

/**
 * Created by Roy Gao on 11/18/2015.
 */
public class Token {

	private String key;
	private String tag;
	private String value;
	private boolean decl = false;   //该结点是否成功申明

	public Token(String key, String tag, String value) {
		this.key = key;
		this.tag = tag;
		this.value = value;
	}

	public Token(String key, String tag) {
		this.key = key;
		this.tag = tag;
		this.value = "";
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDecl(boolean decl) {
		this.decl = decl;
	}

	public String getKey() {
		return key;
	}

	public String getTag() {
		return tag;
	}

	public String getValue() {
		return value;
	}

	public boolean isDecl() {
		return decl;
	}

	@Override
	public String toString() {
		return key + "\t" + tag + "\t" + value + "\t" + decl;
	}
}