package com.sevendeleven.terrilla.util.event;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.main.Updater;

public class UpdateChangeScreen implements UpdateEvent {

	private Screen newScreen;
	private Updater updater;
	
	public UpdateChangeScreen(Updater updater, Screen newScreen) {
		this.updater = updater;
		this.newScreen = newScreen;
	}
	
	public Screen getNewScreen() {
		return this.newScreen;
	}

	@Override
	public UpdateEventType getType() {
		return UpdateEventType.CHANGESCREEN;
	}

	@Override
	public void callback() {
		updater.setScreen(newScreen);
	}
	
}
