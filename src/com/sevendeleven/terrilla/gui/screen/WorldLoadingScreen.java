package com.sevendeleven.terrilla.gui.screen;

import com.sevendeleven.terrilla.entity.EntityPlayer;
import com.sevendeleven.terrilla.gui.elements.GUIProgressBar;
import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.world.World;

public class WorldLoadingScreen extends Screen implements Runnable {

	private World generatingWorld;
	private Thread generationThread;
	private volatile float amountDone = 0;
	private volatile boolean done = false;
	
	GUIProgressBar progressBar;
	
	public WorldLoadingScreen(Renderer renderer) {
		super(renderer);
		progressBar = new GUIProgressBar(Main.getScreenWidth()/2, Main.getScreenHeight()/2, 100);
	}

	public void run() {
		generatingWorld.generateChunk(0);

		EntityPlayer player = new EntityPlayer(this.generatingWorld, -1, 10, Main.getNextEntityID());
		generatingWorld.addEntity(player);
		
		amountDone = 100;
		done = true;
	}
	
	@Override
	public void screenSizeChanged() {
		progressBar.setX(Main.getScreenWidth()/2);
		progressBar.setY(Main.getScreenHeight()/2);
	}

	@Override
	public void update(ConcurrentHandler concurrentHandler, long currentTime, long deltaTick) {
		if (done) {
			GameScreen newScreen = new GameScreen(this.getRenderer(), generatingWorld);
			Main.getMain().changeScreen(newScreen);
		}
	}

	@Override
	public void updateInit() {
		generatingWorld = new World(5953);						//TODO SEED
		generationThread = new Thread(this, "Game/Generation");
		generationThread.start();
	}

	@Override
	public void updateDeInit() {
		
	}

	@Override
	public void drawScreen(ConcurrentHandler concurrentHandler) {
		progressBar.setProgress(amountDone/100.0f);
		GUIProgressBar.beginDraw(this);
		progressBar.draw();
		GUIProgressBar.endDraw();
	}

	@Override
	public void renderInit() {
		
	}

	@Override
	public void renderDeInit() {
		
	}
	
}
