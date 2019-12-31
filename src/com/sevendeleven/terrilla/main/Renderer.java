package com.sevendeleven.terrilla.main;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.sevendeleven.terrilla.gui.font.FontLoader;
import com.sevendeleven.terrilla.gui.screen.Screen;
import com.sevendeleven.terrilla.input.Keyboard;
import com.sevendeleven.terrilla.input.Mouse;
import com.sevendeleven.terrilla.render.Camera;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.util.event.RenderEvent;
import com.sevendeleven.terrilla.util.event.WindowResizedEvent;

public class Renderer implements Runnable {
	
	private ConcurrentHandler concurrentHandler;
	private Thread thread;
	
	private Camera camera;
	private long window;
	
	private volatile boolean initialized = false;
	
	private Queue<RenderEvent> eventBus = new LinkedList<RenderEvent>();
	
	private Screen currentScreen;
	
	private static volatile Renderer renderer;
	
	public Renderer() {
		renderer = this;
		camera = new Camera(0,0);
	}
	
	
	
	
	
	
	
	
	
	public void init() {
		//GLINIT
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		window = glfwCreateWindow(Main.WIDTH, Main.HEIGHT, "Terrilla", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				Keyboard.keyPressed(key);
			}
			if (action == GLFW_RELEASE) {
				Keyboard.keyReleased(key);
			}
		});
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			registerEvent(new WindowResizedEvent(this, width, height));
		});
		glfwSetMouseButtonCallback(window, (window, button, action, modifiers) -> {
			if (button == 1) Mouse.updateRightPressed(action == GLFW_PRESS);
			else if (button == 0) Mouse.updateLeftPressed(action == GLFW_PRESS);
		});
		glfwSetCursorPosCallback(window, (window, mx, my) -> {
			Mouse.updatePos((int)(mx), (int)(Main.getScreenHeight()-my));
		});
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		GL.createCapabilities();	//TODO Any renderer initialization has to happen after this line, otherwise there will be a "No context is current" error
		Loader.loadShaders();
		FontLoader.loadDefaultFont();
		
		//END GLINIT
		
		
		//Sprite INIT
		
		Model.initQuad();
		//TODO Generate list of items to get instead of loading each one individually
		Loader.loadAssets();
		//END Sprite INIT
		initialized = true;
	}
	
	public void deinit() {
		initialized = false;
		Main.getMain().stop();
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
		Loader.unload();
		ResourcesManager.offloadTextures();
	}
	
	public static synchronized Renderer getRenderer() {
		return renderer;
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * Drawing Methods
	 * 
	 * 
	 */
	
	public void start(ConcurrentHandler concurrentHandler) {
		this.concurrentHandler = concurrentHandler;
		thread = new Thread(this, "Renderer");
		thread.start();
	}
	
	public void run() {
		System.out.println("Starting Renderer");
		init();
		System.out.println("Renderer initialized");
		loop();
		System.out.println("Stopping Renderer");
		deinit();
	}
	
	public void draw() {

		if (currentScreen != null) {
			currentScreen.drawScreen(concurrentHandler);
		}
		
	}
	
	
	
	
	
	/*
	 * 
	 * Event Callbacks
	 * 
	 */
	
	public void onWindowResize(int nWidth, int nHeight) {
		glViewport(0,0,nWidth,nHeight);
//		glMatrixMode(GL_PROJECTION);
//		glLoadIdentity();
//		glOrtho(0,1,0,1,0,1);
		if (currentScreen != null) currentScreen.screenSizeChanged();
	}
	
	public void setScreen(Screen newScreen) {
		if (this.currentScreen != null)
			this.currentScreen.renderDeInit();
		this.currentScreen = newScreen;
		if (this.currentScreen != null)
			this.currentScreen.renderInit();
	}
	
	public void loop() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0,0,0,0);
			draw();
			glfwSwapBuffers(window);
			glfwPollEvents();
			callEvents();
		}
	}
	
	public void callEvents() {
		while (!eventBus.isEmpty()) {
			RenderEvent event = eventBus.poll();
			event.callback();
		}
	}
	
	public synchronized void registerEvent(RenderEvent event) {
		eventBus.add(event);
	}
	
	public Screen getCurrentScreen() {
		return this.currentScreen;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public ConcurrentHandler getConcurrentHandler() {
		return this.concurrentHandler;
	}
	
}
