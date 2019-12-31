package com.sevendeleven.terrilla.util;

import com.sevendeleven.terrilla.gui.screen.GameScreen;
import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Camera;
import com.sevendeleven.terrilla.world.Chunk;

public class Util {
	
	public static BlockPos translateScreenPosToBlockPos(int screenX, int screenY) {
		Camera cam = Renderer.getRenderer().getCamera();
		float x = (((float)cam.getX()/(float)Main.getBlockSize()) + (float)screenX/(float)Main.getBlockSize());
		float y = ((float)Chunk.CHUNK_HEIGHT-((float)cam.getY()/(float)Main.getBlockSize())) + ((float)screenY-4.0f)/(float)Main.getBlockSize() - 32f;
		y = Chunk.CHUNK_HEIGHT - y;
		return new BlockPos(((GameScreen) Renderer.getRenderer().getCurrentScreen()).getWorld(), (int)Math.floor(x), (int)Math.floor(y));
	}
	
	public static Vec2f translateScreenPosToWorldPos(int screenX, int screenY) {
		Camera cam = Renderer.getRenderer().getCamera();
		float x = (((float)cam.getX()/(float)Main.getBlockSize()) + (float)screenX/(float)Main.getBlockSize());
		float y = ((float)Chunk.CHUNK_HEIGHT-((float)cam.getY()/(float)Main.getBlockSize())) + ((float)screenY-4.0f)/(float)Main.getBlockSize() - 32f;
		y = (float)Chunk.CHUNK_HEIGHT - y;
		return new Vec2f(x, y);
	}
	
}
