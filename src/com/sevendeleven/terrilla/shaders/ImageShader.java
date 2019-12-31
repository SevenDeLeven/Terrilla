package com.sevendeleven.terrilla.shaders;

import org.lwjgl.opengl.GL20;

public class ImageShader extends ShaderProgram {

	private int cameraUniform, transformUniform;
	
	public ImageShader() {
		super("src/com/sevendeleven/terrilla/shaders/imageShader.vert", "src/com/sevendeleven/terrilla/shaders/imageShader.frag");
	}
	
	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "v_pos");
		bindAttribute(1, "v_color");
		bindAttribute(2, "v_uv");
	}

	@Override
	protected void getUniformLocations() {
		cameraUniform = GL20.glGetUniformLocation(programID, "camera");
		transformUniform = GL20.glGetUniformLocation(programID, "transform");
	}
	
	
	
}
