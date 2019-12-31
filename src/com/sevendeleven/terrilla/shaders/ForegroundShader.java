package com.sevendeleven.terrilla.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class ForegroundShader extends ShaderProgram {
	
	private int cameraUniform;
	private int transformUniform;
	
	public ForegroundShader() {
		super("src/com/sevendeleven/terrilla/shaders/foregroundShader.vert", "src/com/sevendeleven/terrilla/shaders/foregroundShader.frag");
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "vposition");
		bindAttribute(1, "vcolor");
		bindAttribute(2, "vuv");
	}
	
	@Override
	protected void getUniformLocations() {
		cameraUniform = glGetUniformLocation(this.programID, "camera");
		transformUniform = glGetUniformLocation(this.programID, "transform");
	}
	
	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
}
