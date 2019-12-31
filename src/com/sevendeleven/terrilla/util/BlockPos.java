package com.sevendeleven.terrilla.util;

import com.sevendeleven.terrilla.world.World;

public class BlockPos {
	
	private int x, y;
	private World world;
	
	public BlockPos(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
