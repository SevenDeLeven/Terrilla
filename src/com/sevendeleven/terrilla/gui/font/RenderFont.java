package com.sevendeleven.terrilla.gui.font;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL15;

import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.shaders.TextShader;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.Transform;

public class RenderFont {

	private int charWidth, charHeight;
	
	private HashMap<Character, FontCharacter> characters;
	int texID;
	
	private static RenderFont currentFont;
	
	public RenderFont(HashMap<Character, FontCharacter> chars, int charWidth, int charHeight, int textureID) {
		this.characters = chars;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.texID = textureID;
	}
	
	public static RenderFont getCurrentFont() {
		return currentFont;
	}
	
	public void startDraw() {
		if (currentFont != null) {
			System.err.println("Tried to bind a font when a font is already bound!");
			return;
		}
		currentFont = this;
		Loader.textShader.start();
		glBindVertexArray(Model.getQuad().getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Model.getQuad().getIndicesVBOID());
		glBindTexture(GL15.GL_TEXTURE_2D, this.texID);
	}
	
	public void endDraw() {
		if (currentFont != this) {
			System.err.println("Tried to unbind a font that is not currently bound!");
		}
		currentFont = null;
		glBindTexture(GL15.GL_TEXTURE_2D, 0);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(Model.getQuad().getVAOID());
		Loader.textShader.stop();
	}
	
	public int getTextWidth(String s, int height) {
		double size = height/128.0;
		double width = 0;
		for (char c : s.toCharArray()) {
			width += characters.get(c).getWidth()*size;
		}
		return (int) width;
	}
	
	/**
	 * Draws a string to the current active framebuffer
	 * @param renderer The renderer with which to get the camera
	 * @param s The string to draw
	 * @param x The x coordinate of the cursor
	 * @param y The y coordinate of the cursor
	 * @param maxWidth The maximum width before the cursor returns to the next line. Set this to 0 to disable the maximum width
	 */
	
	public void drawString(Renderer renderer, String s, int height, int x, int y, int maxWidth, boolean centered) {
		if (maxWidth == 0) maxWidth = Integer.MAX_VALUE;
		double size = (double)height/128.0;
		
		List<String> lines = new ArrayList<String>();
		
		if (maxWidth != 0) {
			String[] words = s.split(" ");
			String currentLine = "";
			for (String word : words) {
				if (getTextWidth(currentLine+" "+word, height) > maxWidth) {
					if (currentLine == "") {
						lines.add(word);
					} else {
						lines.add(currentLine.substring(0,currentLine.length()-1));
						currentLine = word + " ";
					}
				} else {
					currentLine += word + " ";
				}
			}
			if (!currentLine.isEmpty()) {
				lines.add(currentLine.substring(0,currentLine.length()-1));
			}
		} else {
			lines.add(s);
		}
		int[] lineStartPositions = new int[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			lineStartPositions[i] = x - (getTextWidth(lines.get(i), height)/2);
		}
		
		int cursorX = lineStartPositions[0];
		int cursorY = y-(int)(height*(1+(0.25f*(lines.size()-1))))+(height*lines.size()/2);
		FloatBuffer cameraMatrix = renderer.getCamera().toMatrix();
		TextShader textShader = Loader.textShader;
		glUniformMatrix4fv(textShader.getCameraUniform(), false, cameraMatrix);
		glUniform2f(textShader.getSizeUniform(), this.charWidth, this.charHeight);
		for (int l = 0; l < lines.size(); l++) {
			String line = lines.get(l);
			cursorX = lineStartPositions[l];
			for (int i = 0; i < line.length(); i++) {
				cursorX += drawCharacter(cameraMatrix, line.charAt(i), cursorX, cursorY, size)*size;
			}
			cursorY -= height/2;
		}
	}
	
	private int drawCharacter(FloatBuffer camera, Character c, int x, int y, double size) {
		FontCharacter character = characters.get(c);
		TextShader textShader = Loader.textShader;
		if (character == null) {
			System.err.println("error: dont have character '" + c + "' in the texture atlas");
			return 0;
		}
		glUniformMatrix4fv(textShader.getTransformUniform(), false, Transform.getTransformMatrix((int)(x+(64*size)), (int)(y+(64*size)), (int)(this.charHeight*size), (int)(this.charHeight*size)));
		glUniform2f(textShader.getPosUniform(), character.getX(), character.getY());
		glDrawElements(GL_TRIANGLES, Model.getQuad().getIndexCount(), GL_UNSIGNED_BYTE, 0);
		return character.getWidth();
	}
	
}
