package com.sevendeleven.terrilla.gui.elements;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL15;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.shaders.ProgressBarShader;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.Transform;

public class GUIProgressBar extends GUIElement {
	
	private float x, y, size;
	private float progress;
	
	public GUIProgressBar(float x, float y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public float getProgress() {
		return this.progress;
	}
	
	public static void beginDraw(Screen screen) {
		Renderer renderer = screen.getRenderer();
		ProgressBarShader progressBarShader = Loader.progressBarShader;
		glUniformMatrix4fv(progressBarShader.getCameraUniform(), false, renderer.getCamera().toMatrix());
		
		glBindVertexArray(Model.getQuad().getVAOID());
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Model.getQuad().getIndicesVBOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
	}
	
	@Override
	public void draw() {
		ProgressBarShader progressBarShader = Loader.progressBarShader;
		glUniformMatrix4fv(progressBarShader.getTransformUniform(), false, Transform.getTransformMatrix(x, y, size, size));
		glUniform1f(progressBarShader.getProgressUniform(), progress);
		glUniform4f(progressBarShader.getColorUniform(), 1,1,1,1);
		glDrawElements(GL_TRIANGLES, Model.getQuad().getIndexCount(), GL_UNSIGNED_BYTE, 0);
	}
	
	public static void endDraw() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setSize(float size) {
		this.size = size;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getSize() {
		return this.size;
	}
	
}
