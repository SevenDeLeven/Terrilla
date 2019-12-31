package com.sevendeleven.terrilla.gui.elements;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.shaders.ImageShader;
import com.sevendeleven.terrilla.util.Image;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.util.Transform;

public class GUIImage extends GUIElement{
	
	private Image tex;
	private int x, y, width, height;
	public GUIImage(String textureName, int x, int y, int width, int height) {
		this.tex = ResourcesManager.loadTexture(textureName);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static void beginDraw(Screen screen) {
		ImageShader imageShader = Loader.imageShader;
		Renderer renderer = screen.getRenderer();
		GL20.glUniformMatrix4fv(imageShader.getCameraUniform(), false, renderer.getCamera().toMatrix());
		
		glBindVertexArray(Model.getQuad().getVAOID());
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Model.getQuad().getIndicesVBOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
	}
	
	@Override
	public void draw() {
		ImageShader imgShader = Loader.imageShader;
		glBindTexture(GL_TEXTURE_2D, tex.getID());
		GL20.glUniformMatrix4fv(imgShader.getTransformUniform(), false, Transform.getTransformMatrix(x, y, width, height));
		glDrawElements(GL_TRIANGLES, Model.getQuad().getIndexCount(), GL_UNSIGNED_BYTE, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public static void endDraw() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public Image getTex() {
		return this.tex;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
}
