package com.sevendeleven.terrilla.util.event;

import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;

public class WindowResizedEvent implements RenderEvent {

	private int nwidth;
	private int nheight;
	
	private Renderer renderer;
	
	public WindowResizedEvent(Renderer renderer, int nwidth, int nheight) {
		this.renderer = renderer;
		this.nwidth = nwidth;
		this.nheight = nheight;
	}
	
	@Override
	public RenderEventType getType() {
		return RenderEventType.WINDOWRESIZED;
	}

	@Override
	public void callback() {
		renderer.onWindowResize(nwidth, nheight);
		Main.setScreenSize(nwidth, nheight);
	}

}
