package com.sevendeleven.terrilla.shaders;

import org.lwjgl.opengl.GL20;

public class ProgressBarShader extends ShaderProgram {
	
	private int cameraUniform;
	private int transformUniform;
	private int progressUniform;
	private int colorUniform;
	
	public ProgressBarShader() {
		super("src/com/sevendeleven/terrilla/shaders/progressBarShader.vert","src/com/sevendeleven/terrilla/shaders/progressBarShader.frag");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "v_pos");
		bindAttribute(1, "v_color");
		bindAttribute(2, "v_uv");
	}

	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
	public int getProgressUniform() {
		return this.progressUniform;
	}
	
	public int getColorUniform() {
		return this.colorUniform;
	}
	
	@Override
	protected void getUniformLocations() {
		cameraUniform = GL20.glGetUniformLocation(programID, "camera");
		transformUniform = GL20.glGetUniformLocation(programID, "transform");
		progressUniform = GL20.glGetUniformLocation(programID, "progress");
		colorUniform = GL20.glGetUniformLocation(programID, "color");
	}
	
}
