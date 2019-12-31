package com.sevendeleven.terrilla.util;

public class Image {
	private String path;
	
	private int textureID;
	private int width;
	private int height;
	public Image(String path, int textureID, int width, int height) {
		this.path = path;
		this.textureID = textureID;
		this.width = width;
		this.height = height;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public int getID() {
		return this.textureID;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
