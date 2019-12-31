package com.sevendeleven.terrilla.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.util.PerlinNoise;

public class World {
	
	private List<Chunk> chunks;
	
	private List<Entity> entities;
	
	private HashMap<Long, Entity> entitiesById;
	
	private PerlinNoise noise;
	private long seed;
	
	public World(long seed) {
		this.seed = seed;
		noise = new PerlinNoise(this.seed);
		this.entities = new ArrayList<Entity>();
		this.entitiesById = new HashMap<Long, Entity>();
		this.chunks = new ArrayList<Chunk>();
	}
	
	public void removeEntity(Entity entity) {
		if (!entities.contains(entity)) {
			System.out.println("Entity already removed!");
			if (entitiesById.containsKey(entity.getUniqueID())) {
				entitiesById.remove(entity.getUniqueID());
				System.err.println("WARNING: ENTITY REMOVED WAS NOT IN ENTITIES LIST BUT IN ENTITIESBYID MAP");
				System.err.println("ENTITY DATA:");
				System.err.println(entity);
				System.err.println("uniqueID: " + entity.getUniqueID());
				System.err.println("x: " + entity.x + "\ny: " + entity.y);
			}
		} else {
			entities.remove(entity);
			entitiesById.remove(entity.getUniqueID());
		}
	}
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
		this.entitiesById.put(entity.getUniqueID(), entity);
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public List<Chunk> getChunks() {
		return this.chunks;
	}
	
	public Chunk getChunkAtX(int x) {
		for (Chunk chunk : chunks) {
			if (chunk.containsX(x)) {
				return chunk;
			}
		}
		System.out.println("returning null as chunk for x " + x);
		return null;
	}
	
	public Chunk loadChunk(int chunkX) {
		generateChunk(chunkX);
		return getChunkAtX(chunkX*Chunk.CHUNK_WIDTH);
	}
	
	public void generateChunk(int chunkX) {
		if (getChunkAtX(chunkX*Chunk.CHUNK_WIDTH) != null) {
			return;
		}
		Chunk chunk = new Chunk(chunkX);
		chunk.generate(noise, 40, 5);
		chunks.add(chunk);
	}
	
	public int getBlock(BlockPos pos) {
		if (getChunkAtX(pos.getX()) != null) {
			return getChunkAtX(pos.getX()).getBlock(pos.getX(), pos.getY());
		} else {
			System.err.println("Block attempted to be accessed outside of load area ("+pos.getX() + ", "+pos.getY()+")");
			return 0;
		}
	}
	
	public void placeBlock(int x, int y, int id, boolean calculated) { //Calculated: whether or not the screen pos was calculated to world pos
		
	}

	public boolean outOfLoadedBounds(float x) {
		for (Chunk c : chunks) {
			if (c.containsX(x)) {
				return false;
			}
		}
		return true;
	}
	
}
