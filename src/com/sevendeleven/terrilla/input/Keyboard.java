package com.sevendeleven.terrilla.input;

public class Keyboard {
	
	private static boolean[] keysPressed = new boolean[65565];
	
	public boolean isPressed(int keyCode) {
		if (keyCode < 0 || keyCode >= 65565) {
			System.err.println("keyCode " + keyCode + " was input but is outside of the keysPressed boundaries! (isPressed)");
			return false;
		}
		return keysPressed[keyCode];
	}
	
	public static void keyPressed(int keyCode) {
		if (keyCode < 0 || keyCode >= 65565) {
			System.err.println("keyCode " + keyCode + " was input but is outside of the keysPressed boundaries! (keyPressed)");
			return;
		}
		keysPressed[keyCode] = true;
	}
	
	public static void keyReleased(int keyCode) {
		if (keyCode < 0 || keyCode >= 65565) {
			System.err.println("keyCode " + keyCode + " was input but is outside of the keysPressed boundaries! (keyReleased)");
			return;
		}
		keysPressed[keyCode] = false;
	}
	
}
