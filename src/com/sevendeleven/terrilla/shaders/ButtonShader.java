package com.sevendeleven.terrilla.shaders;

import org.lwjgl.opengl.GL20;

public class ButtonShader extends ShaderProgram {

	private int cameraUniform;
	private int transformUniform;
	private int sizeUniform;
	private int hoveredUniform;
	
	public ButtonShader() {
		super("src/com/sevendeleven/terrilla/shaders/buttonShader.vert", "src/com/sevendeleven/terrilla/shaders/buttonShader.frag");
	}

	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
	public int getSizeUniform() {
		return this.sizeUniform;
	}
	
	public int getHoveredUniform() {
		return this.hoveredUniform;
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "v_pos");
		super.bindAttribute(1, "v_color");
		super.bindAttribute(2, "v_uv");
	}

	@Override
	protected void getUniformLocations() {
		sizeUniform = GL20.glGetUniformLocation(programID, "size");
		cameraUniform = GL20.glGetUniformLocation(programID, "camera");
		hoveredUniform = GL20.glGetUniformLocation(programID, "hovered");
		transformUniform = GL20.glGetUniformLocation(programID, "transform");
	}

}
