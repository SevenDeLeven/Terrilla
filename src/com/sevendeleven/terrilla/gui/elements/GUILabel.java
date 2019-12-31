package com.sevendeleven.terrilla.gui.elements;

import com.sevendeleven.terrilla.gui.font.RenderFont;
import com.sevendeleven.terrilla.gui.screen.Screen;

public class GUILabel extends GUIElement {
	
	private Screen screen;
	private String text;
	private int size, maxWidth, x, y;
	private boolean centered;
	
	public GUILabel(Screen screen, String text, int size, int x, int y, boolean centered) {
		this.screen = screen;
		this.text = text;
		this.size = size;
		this.maxWidth = 0;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw() {
		//Assuming the text atlas has already been assigned
		RenderFont font = RenderFont.getCurrentFont();
		font.drawString(screen.getRenderer(), text, size, x, y, maxWidth, true);
	}
	
	public static void beginDraw(RenderFont font) {
		font.startDraw();
	}
	
	public static void endDraw() {
		RenderFont.getCurrentFont().endDraw();
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
	public boolean getCentered() {
		return this.centered;
	}
	
}
