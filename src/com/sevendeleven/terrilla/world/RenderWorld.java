package com.sevendeleven.terrilla.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.SpriteData;
import com.sevendeleven.terrilla.util.Util;
import com.sevendeleven.terrilla.util.Vec2f;

public class RenderWorld {
	
	private List<RenderChunk> renderChunks = new ArrayList<RenderChunk>();
	
	private HashMap<Long, SpriteData> spritesByID = new HashMap<Long, SpriteData>();
	
	//				Model			 Texture		  ID	Sprite
	private HashMap<Integer, HashMap<Integer, HashMap<Long, SpriteData>>> spritesByModelAndTexture = new HashMap<>();
	
	private Queue<SpriteData> addSpriteQueue = new LinkedList<>();
	private Queue<SpriteData> removeSpriteQueue = new LinkedList<>();
	
	private List<Vec2f> requestedChunks = new ArrayList<Vec2f>();
	
	private ConcurrentHandler concurrentHandler;
	
	public RenderWorld(Renderer renderer) {
		this.concurrentHandler = renderer.getConcurrentHandler();
		
	}
	
	public List<RenderChunk> getRenderChunks() {
		return this.renderChunks;
	}
	
	public HashMap<Integer, HashMap<Integer, HashMap<Long, SpriteData>>> getSpriteMap() {
		return spritesByModelAndTexture;
	}
	
	public void update() {
		BlockPos minPos = Util.translateScreenPosToBlockPos(0, 0);
		BlockPos maxPos = Util.translateScreenPosToBlockPos(Main.getScreenWidth(), 0);
		int minX = minPos.getX();
		int maxX = maxPos.getX();
		for (int i = 0; i < renderChunks.size(); i++) {
			RenderChunk chunk = renderChunks.get(i);
			if (chunk == null || (chunk.getChunkLeft() > maxX || chunk.getChunkRight() < minX)) {
				renderChunks.remove(i);
				i--;
			}
		}
		
		for (int i = minX; i <= maxX; i += Chunk.CHUNK_WIDTH) {
			int mod = i%Chunk.CHUNK_WIDTH;
			int left = i-mod;
			int right = left+Chunk.CHUNK_WIDTH;
			boolean cont = false;
			for (Vec2f b : requestedChunks) {
				if (i >= b.x && i < b.y) {
					cont = true;
				}
			}
			if (cont) continue;
			requestedChunks.add(new Vec2f(left, right));
			if (getChunkAtX(i) == null/*CHECK THAT THE CHUNK HASN'T BEEN REQUESTED YET*/) {
				concurrentHandler.requestChunk(i);
				System.out.println("requested chunk at x " + i);
			}
		}
	}
	
	public void pollSpriteQueues() {
		while (!addSpriteQueue.isEmpty()) {
			addSprite(addSpriteQueue.poll());
		}
		while (!removeSpriteQueue.isEmpty()) {
			removeSprite(removeSpriteQueue.poll());
		}
		while (!concurrentHandler.getRenderChunkUpdates().isEmpty()) {
			System.out.println("Chunk in addChunkQueue being polled");
			RenderChunk chunk = concurrentHandler.getRenderChunkUpdates().poll();
			boolean removed = false;
			for (int i = 0; i < requestedChunks.size(); i++) {
				Vec2f requestedChunk = requestedChunks.get(i);
				if (chunk.getChunkX() >= requestedChunk.x && chunk.getChunkX() <= requestedChunk.y) {
					requestedChunks.remove(i);
					removed = true;
					break;
				}
			}
			if (!removed) {
				System.err.println("Added Chunk was not in requestedChunk list");
			}
			addChunk(chunk);
		}
	}
	
	public synchronized void scheduleRemoveSprite(SpriteData sprite) {
		removeSpriteQueue.add(sprite);
	}
	
	public synchronized void scheduleAddSprite(SpriteData sprite) {
		addSpriteQueue.add(sprite);
	}
	
	public void addChunk(RenderChunk chunk) {
		renderChunks.add(chunk);
	}
	
	public void addSprite(SpriteData sprite) {
		spritesByID.put(sprite.getID(), sprite);
		int modelID = sprite.getSprite().getModel().getVAOID();
		int texID = sprite.getSprite().getTexture().getID();
		if (!spritesByModelAndTexture.containsKey(modelID)) {
			spritesByModelAndTexture.put(modelID, new HashMap<Integer, HashMap<Long, SpriteData>>());
		}
		HashMap<Integer, HashMap<Long, SpriteData>> spritesByTexture = spritesByModelAndTexture.get(modelID);
		if (!spritesByTexture.containsKey(texID)) {
			spritesByTexture.put(texID, new HashMap<Long, SpriteData>());
		}
		HashMap<Long, SpriteData> sprites = spritesByTexture.get(texID);
		if (!sprites.containsKey(sprite.getID())) {
			sprites.put(sprite.getID(), sprite);
		} else {
			SpriteData oldSprite = sprites.get(sprite.getID());
			oldSprite.update(sprite);
		}
	}
	
	public void removeSprite(SpriteData sprite) {
		spritesByID.remove(sprite.getID());
		int modelID = sprite.getSprite().getModel().getVAOID();
		int texID = sprite.getSprite().getTexture().getID();
		if (!spritesByModelAndTexture.containsKey(modelID)) {
			System.err.println("Tried to remove spritedata that didnt exist in model-texture-id map!");
			return;
		}
		HashMap<Integer, HashMap<Long, SpriteData>> spritesByTexture = spritesByModelAndTexture.get(modelID);
		if (!spritesByTexture.containsKey(texID)) {
			System.err.println("Tried to remove spritedata that didnt exist in texture-id map!");
			return;
		}
		HashMap<Long, SpriteData> sprites = spritesByTexture.get(texID);
		if (!sprites.containsKey(sprite.getID())) {
			System.err.println("Tried to remove nonexistant sprite from id map!");
			return;
		}
		sprites.remove(sprite.getID());
	}
	
	public RenderChunk getChunkAtX(int x) {
		for (RenderChunk chunk : renderChunks) {
			if (x >= chunk.getChunkLeft() && x < chunk.getChunkRight()) {
				return chunk;
			} else {
				System.out.println(chunk.getChunkLeft());
			}
		}
		return null;
	}
	
	public HashMap<Integer, HashMap<Long, SpriteData>> getSpriteMapByModel(int modelID) {
		return spritesByModelAndTexture.get(modelID);
	}
	
	public SpriteData getSpriteById(long id) {
		return this.spritesByID.get(id);
	}
	
}
