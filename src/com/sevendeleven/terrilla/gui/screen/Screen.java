package com.sevendeleven.terrilla.gui.screen;

import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.util.ConcurrentHandler;

public abstract class Screen {
	
	private Renderer renderer;
	
	protected Screen(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public abstract void screenSizeChanged();
	
	public abstract void update(ConcurrentHandler concurrentHandler, long currentTime, long deltaTick);
	public abstract void updateInit();
	public abstract void updateDeInit();
	
	public abstract void drawScreen(ConcurrentHandler concurrentHandler);
	public abstract void renderInit();
	public abstract void renderDeInit();
	
}
