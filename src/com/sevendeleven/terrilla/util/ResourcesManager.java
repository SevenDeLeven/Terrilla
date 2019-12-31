package com.sevendeleven.terrilla.util;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.render.Sprite;

public class ResourcesManager {

	private static volatile Map<Integer, Model> models = new HashMap<Integer, Model>();
	
	private static volatile Map<String, Image> textures = new HashMap<String, Image>();

	private static volatile Map<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public static void registerSprite(String name, Sprite sprite) {
		System.out.println("Registering Sprite: " + name);
		sprites.put(name, sprite);
	}
	
	public static void registerModel(Integer vaoID, Model model) {
		System.out.println("Registering Model with VAOID " + vaoID);
		models.put(vaoID, model);
	}
	
	public static Model getModelByVAOID(Integer vaoID) {
		return models.get(vaoID);
	}
	
	public static Sprite getSprite(String name) {
		return sprites.get(name);
	}
	
	public static Image loadTexture(String texturePath) {
		if (textures.containsKey(texturePath)) {
			return textures.get(texturePath);	//Return old texture if texture already exists
		} else {
			BufferedImage image;
			try {
				System.out.println("Loading texture: " + texturePath);
				image = ImageIO.read(Main.class.getResource(texturePath));
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	return null;
	        }
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) (pixel & 0xFF));
					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}

			buffer.flip();

			int textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					buffer);
			
			textures.put(texturePath, new Image(texturePath, textureID, image.getWidth(), image.getHeight()));
			return textures.get(texturePath);
		}
	}
	
	public static void offloadTextures() {
		for (Image img : textures.values()) {
			glDeleteTextures(img.getID());
		}
	}

}
