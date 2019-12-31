package com.sevendeleven.terrilla.shaders;

import org.lwjgl.opengl.GL20;

public class EntityShader extends ShaderProgram {

	private int cameraUniform;
	private int transformUniform;
	
	public EntityShader() {
		super("src/com/sevendeleven/terrilla/shaders/entityShader.vert", "src/com/sevendeleven/terrilla/shaders/entityShader.frag");
	}

	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "vcolor");
		super.bindAttribute(2, "uv");
	}
	
	@Override
	protected void getUniformLocations() {
		cameraUniform = GL20.glGetUniformLocation(programID, "camera");
		transformUniform = GL20.glGetUniformLocation(programID, "transform");
	}
	
}
