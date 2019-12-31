package com.sevendeleven.terrilla.gui.screen;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL15;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Camera;
import com.sevendeleven.terrilla.render.Model;
import com.sevendeleven.terrilla.shaders.EntityShader;
import com.sevendeleven.terrilla.shaders.ForegroundShader;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.Loader;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.util.SpriteData;
import com.sevendeleven.terrilla.world.Chunk;
import com.sevendeleven.terrilla.world.RenderChunk;
import com.sevendeleven.terrilla.world.RenderWorld;
import com.sevendeleven.terrilla.world.World;

public class GameScreen extends Screen {
	
	private RenderWorld renderWorld;
	private World world;
	
	private long currentTime;
	
	private EntityShader entityShader;
	private ForegroundShader foregroundShader;
	
	public GameScreen(Renderer renderer, World world) {
		super(renderer);
		this.entityShader = Loader.entityShader;
		this.foregroundShader = Loader.foregroundShader;
		this.world = world;
	}
	
	@Override
	public void update(ConcurrentHandler concurrentHandler, long currentTime, long deltaTick) {
		for (Entity e : world.getEntities()) {
			e.update(currentTime, deltaTick, concurrentHandler);
		}
		while (!concurrentHandler.getUpdateChunkRequests().isEmpty()) {
			int x = concurrentHandler.getUpdateChunkRequests().poll();
			Chunk c = world.loadChunk(x);
			RenderChunk rChunk = new RenderChunk(c);
			rChunk.updateBlocks(c.getBlocks());
			System.out.println("chunk added " + (c == null) + " " + (rChunk == null));
			concurrentHandler.addRenderChunk(rChunk);
		}
	}
	
	public void renderAllSprites(Integer spriteModel, HashMap<Integer, HashMap<Long, SpriteData>> ents) {
		glBindVertexArray(spriteModel);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ResourcesManager.getModelByVAOID(spriteModel).getIndicesVBOID());
		for (Entry<Integer, HashMap<Long, SpriteData>> spriteList : ents.entrySet()) {
			renderSprites(spriteList.getKey(), spriteList.getValue());
		}
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void renderSprites(Integer spriteTexture, HashMap<Long, SpriteData> ents) {
		glBindTexture(GL15.GL_TEXTURE_2D, spriteTexture);
		for (Entry<Long, SpriteData> sd : ents.entrySet()) {
			if (sd.getValue().getRemove()) {
				renderWorld.scheduleRemoveSprite(sd.getValue());
				continue;
			}
			glUniformMatrix4fv(this.entityShader.getTransformUniform(), false, sd.getValue().getTransform(currentTime, sd.getValue().getSprite()));
			glDrawElements(GL_TRIANGLES, sd.getValue().getSprite().getModel().getIndexCount(), GL_UNSIGNED_BYTE, 0);
		}
		glBindTexture(GL15.GL_TEXTURE_2D, 0);
	}
	
	
	
	@Override
	public void drawScreen(ConcurrentHandler concurrentHandler) {
		currentTime = System.currentTimeMillis();
		this.entityShader.start();
		
		//RENDER SPRITES
		
		glUniformMatrix4fv(this.entityShader.getCameraUniform(), false, this.getRenderer().getCamera().toMatrix());
		
		HashMap<Integer, HashMap<Integer, HashMap<Long, SpriteData>>> sprites = renderWorld.getSpriteMap();
		
		for (Entry<Integer, HashMap<Integer, HashMap<Long, SpriteData>>> sdMap : sprites.entrySet()) {
			renderAllSprites(sdMap.getKey(), sdMap.getValue());
		}
		//END RENDER SPRITES
		
		this.entityShader.stop();
		
		this.foregroundShader.start();
		
		//RENDER CHUNKS
		
		glBindVertexArray(Model.getQuad().getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, Model.getQuad().getIndicesVBOID());

		
		glUniformMatrix4fv(this.foregroundShader.getCameraUniform(), false, this.getRenderer().getCamera().toMatrix());
		
		
		for (RenderChunk chunk : renderWorld.getRenderChunks()) {
			Camera cam = this.getRenderer().getCamera();
			
			Vector4f view = new Vector4f();
			view.x = cam.getX();
			view.y = cam.getY();
			view.w = Main.getScreenWidth();
			view.z = Main.getScreenHeight();
			chunk.draw(view);
		}
		
		//END RENDER CHUNKS
		
		this.foregroundShader.stop();
		
		pollUpdates(concurrentHandler);
		renderWorld.pollSpriteQueues();
		renderWorld.update();
	}
	
	public void pollUpdates(ConcurrentHandler concurrentHandler) {
		Queue<SpriteData> updates = concurrentHandler.getRenderUpdates();
		while (!updates.isEmpty()) {
			SpriteData data = updates.poll();
			renderWorld.addSprite(data);
		}
		
		
		
//		Queue<RenderChunk> chunkData = concurrentHandler.getChunkUpdates();
//		while (!chunkData.isEmpty()) {
//			RenderChunk data = chunkData.poll();
//		}
		
	}
	
	public World getWorld() {
		return this.world;
	}

	@Override
	public void updateInit() {
		//World is handed in the constructor, no need to create new world.
	}

	@Override
	public void updateDeInit() {
		
	}

	@Override
	public void renderInit() {
		renderWorld = new RenderWorld(this.getRenderer());
	}

	@Override
	public void renderDeInit() {
		
	}

	@Override
	public void screenSizeChanged() {
		
	}
	
	
}
