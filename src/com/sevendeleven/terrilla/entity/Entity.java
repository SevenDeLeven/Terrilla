package com.sevendeleven.terrilla.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sevendeleven.terrilla.main.Register;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.util.AABB;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.Vec2f;
import com.sevendeleven.terrilla.world.Chunk;
import com.sevendeleven.terrilla.world.Physics;
import com.sevendeleven.terrilla.world.World;

public abstract class Entity {
	
	private final long uniqueID;
	
	private Sprite sprite;
	
	AABB bounds;
	public float x, y, vx, vy;
	Vec2f gravityVel = new Vec2f(0, 0);
	Vec2f knockbackVel = new Vec2f(0, 0);
	public float angle;
	private World world;
	
	protected Entity(World world, Sprite sprite, long uniqueID, float x, float y, AABB bounds) {
		this.world = world;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		this.angle = 0;
		this.uniqueID = uniqueID;
		this.bounds = bounds;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public final long getUniqueID() {
		return this.uniqueID;
	}
	
	public void move() {
		this.x += vx;
		this.y += vy;
	}
	
	public void move(float dx, float dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			return;
		}
		if (dx != 0) {
			float rem = dx%1.0f;
			float dir = (dx < 0 ? -1 : 1);
			boolean broke = false;
			for (int i = 0; i < (int)Math.floor(Math.abs(dx)); i++) {
				if (!collidesWithWorld(1.0f*dir, 0)) {
					x += 1.0f*dir;
					y += dy;
				} else {
					broke = true;
					vx = 0;
					break;
				}
			}
			if (!collidesWithWorld(rem, 0) && !broke) {
				x += rem;
				y += dy;
			} else {
				int highest = 1;
				for (int i = 2; i <= 5; i++) {
					if (!collidesWithWorld(dx/((float)i), 0)) {
						highest = i;
					}
				}
				if (!collidesWithWorld(dx/((float)highest), 0)) {
					x += (dx/((float)highest));
					y += dy;
					this.multiplyAllVelocitiesX(1.0f/highest);
				}
				if (Math.abs(dx/highest) < (1.0f/32.0f)) {
					multiplyAllVelocitiesX(0);
				}
			}
		}
		if (dy != 0) {
			float rem = dy%1.0f;
			float dir = (dy < 0 ? -1 : 1);
			boolean broke = false;
			for (int i = 0; i < (int)Math.floor(Math.abs(dy)); i++) {
				if (!collidesWithWorld(0, 1.0f*dir)) {
					x += dx;
					y += 1.0f*dir;
				} else {
					broke = true;
					vy = 0;
					break;
				}
			}
			if (!collidesWithWorld(dx, rem) && !broke) {
				x += dx;
				y += rem;
			} else {
				float highest = 1.0f;
				for (float i = 1.3f; i <= 5.0; i+=0.5f) {
					if (!collidesWithWorld(dx, rem/((float)i))) {
						highest = i;
						break;
					}
				}
				if (!collidesWithWorld(dx, rem/highest)) {
					x += dx;
					y += (rem/highest);
					this.multiplyAllVelocitiesY(1.0f/highest);
				}
				if (Math.abs(rem/highest) < (1.1f/32.0f)) {
					multiplyAllVelocitiesY(0);
				}
			}
		}
		//this.x = (float) world.planetaryPosX(this.x);
	}
	
	public void multiplyAllVelocities(float x) {
		this.knockbackVel.multiply(x);
		this.gravityVel.multiply(x);
		this.vx *= x;
	}

	public void multiplyAllVelocitiesY(float y) {
		this.knockbackVel.y *= y;
		this.gravityVel.y *= y;
		this.vy *= y;
	}
	
	public void multiplyAllVelocitiesX(float x) {
		this.knockbackVel.x *= x;
		this.gravityVel.x *= x;
	}
	
	public boolean collidesWithWorld(float dx, float dy) {
		float left = x + (dx) + (bounds.bottomLeft().x) - 1.0f;
		float right = x + (dx) + (bounds.bottomRight().x) + 1.0f;
		float top = y + (dy) + (bounds.topLeft().y) - 2.0f;
		float bottom = y + (dy) + (bounds.bottomLeft().y) + 2.0f;
		if (world.outOfLoadedBounds(left) || world.outOfLoadedBounds(right)) {
			return true;
		}
		HashMap<BlockPos, Integer> blocks = getSurroundingBlocks(left, right, top, bottom);
		for (Entry<BlockPos, Integer> blockInfo : blocks.entrySet()) {
			BlockPos pos = blockInfo.getKey();
			int block = blockInfo.getValue();
			if (Register.getTile(block) != null && Register.getTile(block).isSolid()) {
				if (this.collidesWithBlock(dx, dy, pos)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean collidesWithBlock(float dx, float dy, BlockPos pos) {
		return this.bounds.collides(new Vec2f(x, y), pos);
	}

	public HashMap<BlockPos, Integer> getSurroundingBlocks(float left, float right, float top, float bottom) {
		if (left > right) {
			float temp = left;
			left = right;
			right = temp;
		}
		if (bottom > top) {
			float temp = bottom;
			bottom = top;
			top = temp;
		}
		HashMap<BlockPos, Integer> blocks = new HashMap<BlockPos, Integer>();
		int blocksWidth = (int)Math.ceil(right-left);
		int blocksHeight = (int)Math.ceil(top-bottom);
		if (left%1.0 > right%1.0) {
			blocksWidth++;
		}
		if (bottom%1.0 > top%1.0) {
			blocksHeight++;
		}
//		for (Chunk c : surroundingChunks) {
//			if (c == null) continue;
//			for (int i = 0; i < blocksWidth; i++) {
//				for (int j = 0; j < blocksHeight; j++) {
//					if (c.containsX(left+i)) {
//						int blockX = (int) Math.floor(left+i);
//						int blockY = (int) Math.floor(bottom+j);
//						BlockPos pos = new BlockPos(world,blockX,blockY);
//						int block = world.getBlock(pos);
//						blocks.put(pos, block);
//					}
//				}
//			}
//		}
		for (int i = 0; i < blocksWidth; i++) {
			for (int j = 0; j < blocksHeight; j++) {
				int blockX = (int)Math.floor(left+i);
				int blockY = (int)Math.floor(bottom+j);
				BlockPos pos = new BlockPos(world, blockX, blockY);
				if (this.collidesWithBlock(0, 0, pos)) continue;
				int block = world.getBlock(pos);
				blocks.put(pos, block);
			}
		}
		return blocks;
	}
	
	public List<Chunk> getSurroundingChunks(float left, float right) {
		List<Chunk> ret = new ArrayList<Chunk>();
		float delta = right-left;
		int chunks = (int)Math.ceil(delta/16.0);
		float leftChunkPos = left%16;
		float rightChunkPos = right%16;
		if (leftChunkPos > rightChunkPos) {
			chunks++;
		}
		for (int i = 0; i < chunks; i++) {
			ret.add(world.getChunkAtX((int)left + i*16));
		}
		return ret;
	}
	
	public void applyGravity() {
		vy += Physics.GRAVITY;
	}
	
	public void updateEntity(long milliTime, long deltaTick, ConcurrentHandler handler) {
		handler.updateRenderer(milliTime, deltaTick, this);
	}
	
	public abstract void update(long milliTime, long deltaTick, ConcurrentHandler handler);
	
	
}
