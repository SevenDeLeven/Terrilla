package com.sevendeleven.terrilla.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;

public class Model {
	
	public static final float[] QUAD_VERTICES = {
			-0.5f, 0.5f,	//Left Top
			-0.5f, -0.5f,	//Left Bottom
			0.5f, -0.5f,	//Right Bottom
			0.5f, 0.5f		//Right Top
	};
	
	public static final float[] QUAD_COLORS = {
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f
	};
	
	public static final float[] QUAD_UVS = {
			0.0f, 0.0f,		//Top Left
			0.0f, 1.0f,		//Bottom Left
			1.0f, 1.0f,		//Bottom Right
			1.0f, 0.0f,		//Top Right
	};
	
	public static final byte[] QUAD_INDICES = {
			0, 1, 2,
			2, 3, 0
	};
	
	private static Model QUAD;
	
	private int vaoID = -1, indicesVBO;
	private float[] vertices;
	private float[] colors;
	private float[] uv;
	private byte[] indices;
	public Model(float[] vertices, float[] colors, float[] uv, byte[] indices) {
		this.vertices = vertices.clone();
		this.indices = indices.clone();
		this.colors = colors.clone();
		this.uv = uv.clone();
	}
	public static void initQuad() {
		QUAD = new Model(QUAD_VERTICES, QUAD_COLORS, QUAD_UVS, QUAD_INDICES);
		QUAD.init();
	}
	public static Model getQuad() {
		return QUAD;
	}
	public void init() {
		if (vaoID == -1) {
			vaoID = Loader.genVAO();
			storeDataInAttributes(0, 2, vertices);
			storeDataInAttributes(1, 4, colors);
			storeDataInAttributes(2, 2, uv);
			this.indicesVBO = GL15.glGenBuffers();
			Loader.vbos.add(this.indicesVBO);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, storeDataInByteBuffer(indices), GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			unbindVAO();
			ResourcesManager.registerModel(vaoID, this);
		}
	}
	public int getVAOID() {
		return this.vaoID;
	}
	public int getVertexCount() {
		return this.vertices.length/2; // Divided by two because that's how many dimensions the vertices are in
	}
	public float[] getVertices() {
		return this.vertices;
	}
	public int getIndexCount() {
		return this.indices.length;
	}
	public byte[] getIndices() {
		return this.indices;
	}
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	public int getIndicesVBOID() {
		return this.indicesVBO;
	}
	
	private void storeDataInAttributes(int attributeNumber, int attribSize, float[] data) {
		int vboID = glGenBuffers();
		Loader.vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, storeDataInFloatBuffer(data), GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, attribSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private ByteBuffer storeDataInByteBuffer(byte[] data) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
