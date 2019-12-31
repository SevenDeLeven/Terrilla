package com.sevendeleven.terrilla.util;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Transform {
	
	public static FloatBuffer getTransformMatrix(float x, float y, Image texture) {
		return getTransformMatrix(x, y, texture.getWidth(), texture.getHeight());
	}
	
	public static FloatBuffer getTransformMatrix(float x, float y, float scaleX, float scaleY, Image texture) {
		return getTransformMatrix(x, y, texture.getWidth()*scaleX, texture.getHeight() * scaleY);
	}
	
	public static FloatBuffer getTransformMatrix(float x, float y, float roll, Image texture) {
		return getTransformMatrix(x, y, texture.getWidth(), texture.getHeight(), roll);
	}

	public static FloatBuffer getTransformMatrix(float x, float y, float scaleX, float scaleY, float roll, Image texture) {
		return getTransformMatrix(x, y, texture.getWidth()*scaleX, texture.getHeight()*scaleY, roll);
	}
	
	public static FloatBuffer getTransformMatrix(float x, float y, float width, float height) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		new Matrix4f().translate(x,y,0).scale(width, height, 1.0f).get(buffer);
		return buffer;
	}
	
	public static FloatBuffer getTransformMatrix(float x, float y, float width, float height, float roll) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		new Matrix4f().translate(x,y,0).scale(width, height, 1.0f).rotate(roll,0,0,1).get(buffer);
		return buffer;
	}
	
}
