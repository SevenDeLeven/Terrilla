package com.sevendeleven.terrilla.util;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Register;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.shaders.ButtonShader;
import com.sevendeleven.terrilla.shaders.EntityShader;
import com.sevendeleven.terrilla.shaders.ForegroundShader;
import com.sevendeleven.terrilla.shaders.ImageShader;
import com.sevendeleven.terrilla.shaders.ProgressBarShader;
import com.sevendeleven.terrilla.shaders.ShaderProgram;
import com.sevendeleven.terrilla.shaders.TextShader;
import com.sevendeleven.terrilla.world.Tile;

public class Loader {
	
	public static List<Integer> vaos = new ArrayList<Integer>();
	public static List<Integer> vbos = new ArrayList<Integer>();
	
	public static List<ShaderProgram> shaders = new ArrayList<ShaderProgram>();
	
	//Game Shaders
	public static EntityShader entityShader;
	public static ForegroundShader foregroundShader;
	
	//GUI Shaders
	public static ButtonShader buttonShader;
	public static TextShader textShader;
	public static ImageShader imageShader;
	public static ProgressBarShader progressBarShader;
	
	public static void loadShaders() {
		System.out.println("loading shaders");
		entityShader = new EntityShader();
		foregroundShader = new ForegroundShader();
		
		buttonShader = new ButtonShader();
		textShader = new TextShader();
		imageShader = new ImageShader();
		progressBarShader = new ProgressBarShader();
		System.out.println("loaded shaders");
	}
	
	public static void unload() {
		deleteArraysAndBuffers();
		deleteShaders();
		deleteTextures();
	}
	
	public static void deleteTextures() {
		ResourcesManager.offloadTextures();
	}
	
	public static void deleteShaders() {
		for (ShaderProgram s : shaders) {
			s.cleanUp();
		}
	}
	
	public static void deleteArraysAndBuffers() {
		for (int i = 0; i < vaos.size(); i++) {
			glDeleteVertexArrays(vaos.get(i));
		}
		for (int i = 0; i < vbos.size(); i++) {
			glDeleteBuffers(vbos.get(i));
		}
	}
	
	public static int genVAO() {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vaos.add(vao);
		return vao;
	}
	
	public static List<String> readFile(String path) {
		InputStream fis = Main.class.getResourceAsStream(path);
		if (fis == null) {
			System.err.println("Could not read from file " + path);
			return null;
		}
		Scanner sc = new Scanner(fis);
		List<String> ret = new ArrayList<String>();
		while (sc.hasNextLine()) {
			ret.add(sc.nextLine());
		}
		sc.close();
		return ret;
	}
	
//	public static final File directory = new File(System.getProperty("user.dir"));
	public static final File directory = new File("./");
	
	private static List<File> spriteFiles;
	private static List<File> blockFiles;
	private static List<File> itemFiles;
	private static List<File> toolFiles;
	
	public static void loadAssets() {
		spriteFiles = new ArrayList<File>();
		blockFiles = new ArrayList<File>();
		itemFiles = new ArrayList<File>();
		toolFiles = new ArrayList<File>();
//		File assetsFile = new File(directory.getAbsolutePath() + "\\assets");
		loadDirectory(directory);
		for (File sprite : spriteFiles) {
			try {
				System.out.println(sprite.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			loadSprite(sprite);
		}
		for (File block : blockFiles) {
			System.out.println("Loading block: " + block.getPath());
			loadBlock(block);
		}
		for (File item : itemFiles) {
			loadItem(item);
		}
		for (File tool : toolFiles) {
			loadTool(tool);
		}
	}
	
	public static void loadDirectory(File directory) {
		String origin = directory.getAbsolutePath() + "\\";
		for (String s : directory.list()) {
			File file = new File(origin + s);
			if (file.isDirectory()) {
				loadDirectory(file);
			} else {
				loadFile(file);
			}
		}
	}
	
	public static void loadFile(File file) {
		String[] ext = file.getName().split("\\.");
		String extension;
		if (ext.length > 1) {
			extension = ext[ext.length-1];
		} else {
			extension = file.getName();
		}
		switch (extension) {
		case "block":
			blockFiles.add(file);
			break;
		case "item":
			itemFiles.add(file);
			break;
		case "tool":
			toolFiles.add(file);
			break;
		case "sprite":
			spriteFiles.add(file);
			break;
		default:
			break;
		}
	}
	
	public static void loadItem(File file) {
//		Register.registerItem(ItemLoader.loadItem(file));
	}
	
	public static void loadTool(File file) {
//		Register.registerTool(ToolLoader.loadTool(file));
	}
	
	public static void loadSprite(File file) {
		String fileContents = loadFileContents(file);	
		JSONObject json = new JSONObject(fileContents);
		String spriteName = json.getString("name");
		Sprite sprite = new Sprite(ResourcesManager.loadTexture(json.getString("location")), Model.getQuad());
		ResourcesManager.registerSprite(spriteName, sprite);
	}
	
	public static void loadBlock(File file) {
		String fileContents = loadFileContents(file);
		JSONObject blockObj = new JSONObject(fileContents);
		Tile block = new Tile(blockObj);
		Register.registerTile(new ID(block.getUniqueNumber(), block.getName()), block);
	}
	
	public static String loadFileContents(File file) {
		try {
			FileInputStream st = new FileInputStream(file);
			String fileContents = "";
			while (st.available() > 0) {
				byte[] buffer = new byte[st.available() < 128 ? st.available() : 128];
				st.read(buffer, 0, buffer.length);
				String read = new String(buffer);
				fileContents = fileContents.concat(read);
			}
			st.close();
			return fileContents;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not load file " + file.getAbsolutePath() + " for block");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
