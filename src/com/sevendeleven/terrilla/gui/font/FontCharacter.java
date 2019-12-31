package com.sevendeleven.terrilla.gui.font;

public class FontCharacter {
	
	private char character;
	private int x, y, width;
	
	public FontCharacter(char character, int x, int y, int width) {
		this.character = character;
		this.x = x;
		this.y = y;
		this.width = width;
		System.out.println((int)character + " " + character + " " + x + " " + y + " " + width);
	}
	
	public char getCharacter() {
		return this.character;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
}
