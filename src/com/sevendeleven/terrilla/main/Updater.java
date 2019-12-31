package com.sevendeleven.terrilla.main;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.event.UpdateEvent;
import com.sevendeleven.terrilla.world.Physics;

public class Updater implements Runnable {
	
	private Queue<UpdateEvent> eventBus = new ConcurrentLinkedQueue<UpdateEvent>();	
	private ConcurrentHandler concurrentHandler;
	private volatile boolean initialized = false;

	private Screen currentScreen;

	public Updater() {
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public void init() {
		initialized = true;
	}
	
	public void deinit() {
		initialized = false;
	}
	
	public void loop() {
		long now = System.currentTimeMillis();
		long last = now;
		long lastTick = now;
		double delta = 0;
		double tps = Physics.TICK_RATE;
		while (true) {
			now = System.currentTimeMillis();
			delta += (now-last)*tps/1000.0;
			if (delta >= 1.0) {
				delta = 0;	//No need to catch up in updating, so instead of subtracting 1, just set it to 0
				tick(now, now-lastTick);
				lastTick = now;
			}
			pollEvents();
			last = now;
		}
	}
	
	public void pollEvents() {
		while (!eventBus.isEmpty()) {
			eventBus.poll().callback();
		}
	}
	
	public void run() {
		init();
		loop();
		deinit();
	}
	
	public void tick(long currentTime, long deltaTime) {
		if (currentScreen != null) {
			currentScreen.update(concurrentHandler, currentTime, deltaTime);
		}
	}
	
	public void start(ConcurrentHandler concurrentHandler) {
		this.concurrentHandler = concurrentHandler;
	}
	
	public synchronized void registerEvent(UpdateEvent event) {
		this.eventBus.add(event);
	}
	
	public void setScreen(Screen newScreen) {
		if (this.currentScreen != null)
			this.currentScreen.updateDeInit();
		this.currentScreen = newScreen;
		if (this.currentScreen != null)
			this.currentScreen.updateInit();
	}
	
	public Screen getCurrentScreen() {
		return currentScreen;
	}
	
}