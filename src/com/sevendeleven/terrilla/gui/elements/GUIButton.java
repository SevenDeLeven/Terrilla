package com.sevendeleven.terrilla.gui.elements;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.input.Mouse;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.shaders.ButtonShader;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.util.Transform;

public class GUIButton extends GUIElement {

	private Screen screen;
	
	private GUILabel label;
	private int x, y, width, height;
	private boolean hovered = false;
	
	public GUIButton(Screen screen, String text, int textSize, int x, int y, int width, int height) {
		this.screen = screen;
		this.label = new GUILabel(this.screen, text, textSize, x, y, true);
		this.label.setMaxWidth(width-10);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void draw() {
		ButtonShader buttonShader = Loader.buttonShader;
		GL20.glUniform2f(buttonShader.getSizeUniform(), this.width, this.height);
		GL20.glUniform1i(buttonShader.getHoveredUniform(), this.hovered ? 1 : 0);
		GL20.glUniformMatrix4fv(buttonShader.getTransformUniform(), false, Transform.getTransformMatrix(x, y, width, height));
		glDrawElements(GL_TRIANGLES, Model.getQuad().getIndexCount(), GL_UNSIGNED_BYTE, 0);
	}
	
	public static void beginDraw(Screen screen) {
		Renderer renderer = screen.getRenderer();
		ButtonShader buttonShader = Loader.buttonShader;
		GL20.glUniformMatrix4fv(buttonShader.getCameraUniform(), false, renderer.getCamera().toMatrix());
		
		glBindVertexArray(Model.getQuad().getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Model.getQuad().getIndicesVBOID());
		glBindTexture(GL15.GL_TEXTURE_2D, ResourcesManager.getSprite("tra:gui_button").getTexture().getID());
	}
	
	public static void endDraw() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	
	
	
	
	
	
	
	public boolean getHovered() {
		return this.hovered;
	}
	
	public void update(int mx, int my) {
		int hw = width/2;
		int hh = height/2;
		if (mx >= x-hw && mx <= x+hw && my >= y-hh && my <= y+hh) {
			this.hovered = true;
		} else {
			this.hovered = false;
		}
	}
	
	public boolean mouseLeftClick() {
		int mpx = Mouse.getLPressX();
		int mpy = Mouse.getLPressY();
		int mrx = Mouse.getLReleaseX();
		int mry = Mouse.getLReleaseY();
		return contains(mpx, mpy) && contains(mrx, mry);
	}
	
	private boolean contains(int mx, int my) {
		int hw = width/2;
		int hh = height/2;
		return (mx >= x-hw && mx <= x+hw && my >= y-hh && my <= y+hh);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void setX(int x) {
		this.x = x;
		this.label.setX(x);
	}
	
	public void setY(int y) {
		this.y = y;
		this.label.setY(y);
	}
	
	public void setWidth(int width) {
		this.width = width;
		this.label.setMaxWidth(width);
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public void setLabel(GUILabel label) {
		this.label = label;
		label.setX(this.x);
		label.setY(this.y);
		label.setMaxWidth(this.width-10);
		label.setCentered(true);
	}
	
	public void setText(String text) {
		this.label.setText(text);
	}
	
	public void setTextSize(int textSize) {
		this.label.setSize(textSize);
	}
	
	public void setTextCentered(boolean centered) {
		this.label.setCentered(centered);
	}
	
	public GUILabel getLabel() {
		return this.label;
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
	
	public FloatBuffer getTransform() {
		return Transform.getTransformMatrix(x, y, width, height);
	}
	
}
