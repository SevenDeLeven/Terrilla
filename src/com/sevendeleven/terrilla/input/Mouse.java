package com.sevendeleven.terrilla.input;

public class Mouse {

	private static volatile int mx = -1;
	private static volatile int my = -1;
	private static volatile int rmpx = -1;
	private static volatile int rmpy = -1;
	private static volatile int rmrx = -1;
	private static volatile int rmry = -1;
	private static volatile int lmpx = -1;
	private static volatile int lmpy = -1;
	private static volatile int lmrx = -1;
	private static volatile int lmry = -1;
	private static volatile boolean wasPressed = false;
	private static volatile boolean pressed = false;
	
	public static void updatePos(int mx, int my) {
		Mouse.mx = mx;
		Mouse.my = my;
	}
	
	public static void updateLeftPressed(boolean curPressed) {
		Mouse.wasPressed = Mouse.pressed;
		Mouse.pressed = curPressed;
		if (wasPressed != pressed && pressed) {
			Mouse.lmpx = mx;
			Mouse.lmpy = my;
		} else if (wasPressed != pressed && !pressed) {
			Mouse.lmrx = mx;
			Mouse.lmry = my;
		}
	}
	
	public static void updateRightPressed(boolean curPressed) {
		Mouse.wasPressed = Mouse.pressed;
		Mouse.pressed = curPressed;
		if (wasPressed != pressed && pressed) {
			Mouse.rmpx = mx;
			Mouse.rmpy = my;
		} else if (wasPressed != pressed && !pressed) {
			Mouse.rmrx = mx;
			Mouse.rmry = my;
		}
	}
	
	public static int getX() {
		return mx;
	}
	
	public static int getY() {
		return my;
	}
	
	public static int getLPressX() {
		return lmpx;
	}
	
	public static int getLPressY() {
		return lmpy;
	}
	
	public static int getLReleaseX() {
		return lmrx;
	}
	
	public static int getLReleaseY() {
		return lmry;
	}
	
	public static int getRPressX() {
		return rmpx;
	}
	
	public static int getRPressY() {
		return rmpy;
	}
	
	public static int getRReleaseX() {
		return rmrx;
	}
	
	public static int getRReleaseY() {
		return rmry;
	}
	
	public static boolean getPressed() {
		return pressed;
	}
	
}
