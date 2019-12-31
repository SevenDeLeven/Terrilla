package com.sevendeleven.terrilla.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.world.RenderChunk;

public class ConcurrentHandler {
	
	private Queue<SpriteData> renderUpdates = new ConcurrentLinkedQueue<>();
	private Queue<RenderChunk> renderChunkUpdates = new ConcurrentLinkedQueue<>();
	private Queue<Integer> updateChunkRequests = new ConcurrentLinkedQueue<>();
	
	public ConcurrentHandler() {
		
	}
	
	public void requestChunk(int x) {
		updateChunkRequests.add(x);
	}
	
	public void addRenderChunk(RenderChunk chunk) {
		renderChunkUpdates.add(chunk);
	}
	
	public void updateRenderer(long milliTime, long deltaTick, Entity ent) {
		renderUpdates.add(new SpriteData(milliTime, deltaTick, ent));
	}
	
	public Queue<SpriteData> getRenderUpdates() {
		return renderUpdates;
	}
	
	public Queue<RenderChunk> getRenderChunkUpdates() {
		return renderChunkUpdates;
	}
	
	public Queue<Integer> getUpdateChunkRequests() {
		return updateChunkRequests;
	}
	
}
