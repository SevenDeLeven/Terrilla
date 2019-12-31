package com.sevendeleven.terrilla.gui.font;

import java.util.HashMap;
import java.util.List;

import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;

public class FontLoader {
	
	private static RenderFont defaultFont;	//This way nobody has to go into the hashmap in order to get the default font
	
	private static HashMap<String, RenderFont> fonts = new HashMap<>();
	
	public static void init() {
		loadDefaultFont();
	}
	
	public static RenderFont getDefaultFont() {
		return defaultFont;
	}
	
	public static void loadDefaultFont() {
		defaultFont = loadFont("Segoe Script");
	}
	
	public static RenderFont loadFont(String fontName) {
		if (fonts.containsKey(fontName)) {
			return fonts.get(fontName);
		}
		List<String> lines = Loader.readFile("/fonts/"+fontName+".csv");
		String imgFile = fontName + ".bmp";
		HashMap<Character, FontCharacter> characters = new HashMap<>();
		int imgWidth = 0;
		int charWidth = 0;
		int charHeight = 0;
		int startChar = 0;
		for (String line : lines) {
			String[] brokenLine = line.split(",");
			if (line.startsWith("Image Width,")) {
				imgWidth = Integer.parseInt(brokenLine[1]);
			}
			if (line.startsWith("Cell Width,")) {
				charWidth = Integer.parseInt(brokenLine[1]);
			}
			if (line.startsWith("Cell Height,")) {
				charHeight = Integer.parseInt(brokenLine[1]);
			}
			if (line.startsWith("Start Char,")) {
				startChar = Integer.parseInt(brokenLine[1]);
			}
			if (line.startsWith("Char ")) {
				String[] spaceBrokenLine = line.split(" ");
				int charNumber = Integer.parseInt(spaceBrokenLine[1]);
				int charPos = (charNumber >= startChar) ? charNumber - startChar : 0;
				int charX = (charPos%(imgWidth/charWidth))*charWidth;
				int charY = Math.floorDiv(charPos, (imgWidth/charWidth))*charHeight;
				int charEffectiveWidth = Integer.parseInt(brokenLine[1]);
				FontCharacter character = new FontCharacter((char)charNumber, charX, charY, charEffectiveWidth);
				characters.put(character.getCharacter(), character);
			}
		}
		RenderFont ret = new RenderFont(characters, charWidth, charHeight, ResourcesManager.loadTexture("/fonts/"+imgFile).getID());
		fonts.put(fontName, ret);
		return ret;
	}
	
}
