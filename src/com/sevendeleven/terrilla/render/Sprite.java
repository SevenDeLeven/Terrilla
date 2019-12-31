package com.sevendeleven.terrilla.render;

import com.sevendeleven.terrilla.util.Image;

public class Sprite {
	
	private Image texture;
	private Model model;
	
	public Sprite(Image texture, Model model) {
		this.texture = texture;
		this.model = model;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public Image getTexture() {
		return this.texture;
	}
	
}
