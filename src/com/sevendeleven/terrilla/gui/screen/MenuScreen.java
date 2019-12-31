package com.sevendeleven.terrilla.gui.screen;

import java.util.ArrayList;
import java.util.List;

import com.sevendeleven.terrilla.gui.elements.GUIButton;
import com.sevendeleven.terrilla.gui.elements.GUILabel;
import com.sevendeleven.terrilla.gui.font.FontLoader;
import com.sevendeleven.terrilla.gui.font.RenderFont;
import com.sevendeleven.terrilla.input.Mouse;
import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.shaders.ButtonShader;
import com.sevendeleven.terrilla.shaders.TextShader;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.Loader;

public class MenuScreen extends Screen {
	
	List<GUIButton> buttons = new ArrayList<GUIButton>();
	
	ButtonShader buttonShader;
	TextShader textShader;
	
	private boolean mousePressed = false;
	private boolean mouseCurPressed = false;
	
	private GUIButton singlePlayerButton;
	private GUIButton quitButton;
	
	public MenuScreen(Renderer renderer) {
		super(renderer);
		this.buttonShader = Loader.buttonShader;
		this.textShader = Loader.textShader;
	}
	
	@Override
	public void drawScreen(ConcurrentHandler concurrentHandler) {
		this.buttonShader.start();
		GUIButton.beginDraw(this);
		for (GUIButton button : buttons) {
			button.draw();
		}
		GUIButton.endDraw();
		this.buttonShader.stop();
		
		RenderFont font = FontLoader.getDefaultFont();
		GUILabel.beginDraw(font);
		for (GUIButton button : buttons) {
			button.getLabel().draw();
		}
		GUILabel.endDraw();
	}

	@Override
	public void update(ConcurrentHandler concurrentHandler, long currentTime, long deltaTick) {
		mousePressed = mouseCurPressed;
		mouseCurPressed = Mouse.getPressed();
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).update(Mouse.getX(), Mouse.getY());
		}
		if (!mouseCurPressed && mousePressed != mouseCurPressed) {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).mouseLeftClick()) {
					
					
					if (buttons.get(i) == singlePlayerButton) {
						WorldLoadingScreen newScreen = new WorldLoadingScreen(this.getRenderer());
						Main.getMain().changeScreen(newScreen);
					} else if (buttons.get(i) == quitButton) {
						Main.getMain().destroy();
					}
					
					
				}
			}
		}
	}

	@Override
	public void updateInit() {
		
	}

	@Override
	public void updateDeInit() {
		
	}

	@Override
	public void renderInit() {
		singlePlayerButton = new GUIButton(this, "Single Player", 50, Main.getScreenWidth()/2, Main.getScreenHeight()/2+55, 200, 76);
		quitButton = new GUIButton(this, "Quit", 50, Main.getScreenWidth()/2, Main.getScreenHeight()/2-55, 200, 60);
		buttons.add(singlePlayerButton);
		buttons.add(quitButton);
	}

	@Override
	public void renderDeInit() {
		
	}

	@Override
	public void screenSizeChanged() {
		buttons.clear();
		renderInit();
	}
	
}
