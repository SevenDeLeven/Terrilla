package com.sevendeleven.terrilla.world;

import org.json.JSONObject;

import com.sevendeleven.terrilla.item.TileItem;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.util.ResourcesManager;

public class Tile {
	
	protected boolean solid = false;
	protected float hardness = 0;
	protected String name = "";
	protected Sprite sprite;
	protected final int uniqueNumber;
	protected TileItem tileItem;
	protected Type type;
	
	public static enum Type {	//Types so tools can tell which ones they are efficient in breaking
		SOLID,
		SOIL,
		WOOD,
	}
	
	protected Tile(int uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
		tileItem = new TileItem(this);
	}
	
	public Tile(JSONObject obj) {
		if (!obj.has("name")) {
			System.err.println("Block does not have value for name!");
			System.exit(-1);
		}
		if (!obj.has("uniqueNumber")) {
			System.err.println("Block does not have value for uniqueNumber!");
			System.exit(-1);
		}
		if (!obj.has("hardness")) {
			System.err.println("Block does not have value for hardness!");
			System.exit(-1);
		}
		if (!obj.has("sprite")) {
			this.sprite = null;
		}
		if (!obj.has("solid")) {
			System.err.println("Block does not have value for solid!");
			System.exit(-1);
		}
		if (!obj.has("type")) {
			System.err.println("Block does not have value for type!");
			System.exit(-1);
		}
		this.name = obj.getString("name");
		this.uniqueNumber = obj.getNumber("uniqueNumber").intValue();
		this.hardness = obj.getNumber("hardness").floatValue();
		this.solid = obj.getBoolean("solid");
		this.sprite = ResourcesManager.getSprite(obj.getString("sprite"));
		this.type = Type.valueOf(obj.getString("type").toUpperCase());
		tileItem = new TileItem(this);
	}
	
	public void onNeighborUpdated(BlockPos pos) {
		
	}
	public void onUpdate(boolean updateNeighbors, BlockPos pos) {
		
	}
	public void onUse(BlockPos pos) {
		
	}
	public boolean isSolid() {
		return solid;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public String getName() {
		return name;
	}
	public float getHardness() {
		return hardness;
	}
	public final int getUniqueNumber() {
		return this.uniqueNumber;
	}
	public TileItem getBlockItem() {
		return tileItem;
	}
	public Type getType() {
		return type;
	}
	
}
