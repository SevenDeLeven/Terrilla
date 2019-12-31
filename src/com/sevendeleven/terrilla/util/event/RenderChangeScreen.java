package com.sevendeleven.terrilla.util.event;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.main.Renderer;

public class RenderChangeScreen implements RenderEvent {

	private Screen newScreen;
	private Renderer renderer;
	
	public RenderChangeScreen(Renderer renderer, Screen newScreen) {
		this.renderer = renderer;
		this.newScreen = newScreen;
	}
	
	public Screen getNewScreen() {
		return this.newScreen;
	}

	@Override
	public RenderEventType getType() {
		return RenderEventType.CHANGESCREEN;
	}

	@Override
	public void callback() {
		renderer.setScreen(newScreen);
	}
	
}
