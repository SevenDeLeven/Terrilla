package com.sevendeleven.terrilla.main;

import com.sevendeleven.terrilla.gui.screen.MenuScreen;
import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.event.RenderChangeScreen;
import com.sevendeleven.terrilla.util.event.UpdateChangeScreen;
import com.sevendeleven.terrilla.world.World;

public class Main {

	public static final int WIDTH = 1200;
	public static final int HEIGHT = WIDTH / 16 * 9;
	
	private static final int BLOCK_SIZE = 8;
	
	private static int screenWidth = WIDTH;
	private static int screenHeight = HEIGHT;
	
	private Renderer renderer = new Renderer();
	private Updater updater = new Updater();
	World world = null;
	
	private ConcurrentHandler concurrentHandler;
	
	private static Main main;
	
	private static long nextEntityID = 0;
	
	private Screen currentScreen;
	
	public static void main(String[] args) {
		main = new Main();
		main.init();
		main.loop();
		main.destroy();
	}
	
	Renderer getRenderer() {
		return renderer;
	}
	
	Updater getUpdater() {
		return updater;
	}

	public void destroy() {
		if (renderer.isInitialized()) {
			renderer.deinit();
		}
		if (updater.isInitialized()) {
			updater.deinit();
		}
		System.exit(0);
	}
	
	public static long getNextEntityID() {
		return nextEntityID++;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	public static void setScreenSize(int screenWidth, int screenHeight) {
		Main.screenWidth = screenWidth;
		Main.screenHeight = screenHeight;
	}
	
	public void loop() {
		currentScreen = new MenuScreen(renderer);
		renderer.registerEvent(new RenderChangeScreen(renderer, currentScreen));
		updater.registerEvent(new UpdateChangeScreen(updater, currentScreen));
		updater.loop();
	}
	
	public void changeScreen(Screen newScreen) {
		updater.registerEvent(new UpdateChangeScreen(updater, newScreen));
		renderer.registerEvent(new RenderChangeScreen(renderer, newScreen));
	}
	
	public static int getBlockSize() {
		return BLOCK_SIZE;
	}

	public static Main getMain() {
		return main;
	}
	
	public void init() {
		concurrentHandler = new ConcurrentHandler();
		renderer.start(concurrentHandler);
		while (!renderer.isInitialized()) {}
		updater.start(concurrentHandler);
		updater.init();
	}
	
	public void stop() {
		destroy();
	}

}
