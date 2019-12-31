package com.sevendeleven.terrilla.shaders;

import org.lwjgl.opengl.GL20;

public class TextShader extends ShaderProgram {
	
	private int cameraUniform, transformUniform, posUniform, sizeUniform;
	
	public TextShader() {
		super("src/com/sevendeleven/terrilla/shaders/textShader.vert", "src/com/sevendeleven/terrilla/shaders/textShader.frag");
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
		posUniform = GL20.glGetUniformLocation(programID, "pos");
		sizeUniform = GL20.glGetUniformLocation(programID, "size");
	}
	
	public int getCameraUniform() {
		return this.cameraUniform;
	}
	
	public int getTransformUniform() {
		return this.transformUniform;
	}
	
	public int getPosUniform() {
		return this.posUniform;
	}
	
	public int getSizeUniform() {
		return this.sizeUniform;
	}
	
}
