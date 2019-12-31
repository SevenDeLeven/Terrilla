package com.sevendeleven.terrilla.world;

import com.sevendeleven.terrilla.main.Register;
import com.sevendeleven.terrilla.util.ID;
import com.sevendeleven.terrilla.util.PerlinNoise;

public class Chunk {
	
	public static final int CHUNK_WIDTH = 32;
	public static final int CHUNK_HEIGHT = 1024;
	
	private int[][] blocks = new int[CHUNK_WIDTH][CHUNK_HEIGHT];
	private int[][] walls = new int[CHUNK_WIDTH/2][CHUNK_HEIGHT/2];
	
	private int xOffset;
	
	public Chunk(int xOffset) {
		this.xOffset = xOffset;
	}
	
	public void generate(PerlinNoise noise, int height, int variety) {
		int[] heightMap = new int[CHUNK_WIDTH];
		for (int i = 0; i < 32; i++) {
			heightMap[i] = 100;
		}
		int topBlock = 3;
		int groundBlock = 3;
		for (int x = 0; x < heightMap.length; x++) {
			for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
				if (heightMap[x] > y) {
					blocks[x][y] = groundBlock;
				} else if (heightMap[x] == y) {
					blocks[x][y] = topBlock;
				} else {
					blocks[x][y] = 0; //AIR
				}
			}
		}
	}
	
	public int getBlock(int cx, int cy) {
		return this.blocks[cx][cy];
	}
	
	public int getXOffset() {
		return this.xOffset;
	}
	
	public int getLeft() {
		return this.xOffset*CHUNK_WIDTH;
	}
	
	public int getRight() {
		return (this.xOffset+1)*CHUNK_WIDTH;
	}
	
	public boolean containsX(float x) {
		return this.getLeft() <= x && this.getRight() >= x;
	}
	
	public int[][] getBlocks() {
		return this.blocks.clone();
	}
	
}
