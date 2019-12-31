package com.sevendeleven.terrilla.item;

import org.json.JSONObject;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.util.ResourcesManager;

public class Item {

	protected String name;
	protected Sprite sprite;
	protected int uniqueNumber;
	protected int maxStackSize;
	
	public Item(JSONObject obj) {
		if (!obj.has("name")) {
			System.err.println("Item does not have name!");
			System.exit(-1);
		}
		if (!obj.has("uniqueNumber")) {
			System.err.println("Item " + obj.getString("name") + " does not have a unique number!");
			System.exit(-1);
		}
		if (!obj.has("texture")) {
			System.err.println("Item " + obj.getString("name") + " does not have a texture!");
			System.exit(-1);
		}
		if (!obj.has("maxStackSize")) {
			System.err.println("Item " + obj.getString("name") + " does not have a max stack size!");
			System.exit(-1);
		}
		this.name = obj.getString("name");
		this.sprite = ResourcesManager.getSprite(obj.getString("texture"));
		this.uniqueNumber = obj.getNumber("uniqueNumber").intValue();
		this.maxStackSize = obj.getNumber("maxStackSize").intValue();
	}
	
	protected Item(String name, Sprite sprite, int uniqueNumber, int maxStackSize) {
		this.name = name;
		this.sprite = sprite;
		this.uniqueNumber = uniqueNumber;
		this.maxStackSize = maxStackSize;
	}
	
	protected Item() {
		
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void onUse(ItemStack stack, BlockPos blockPos, Entity ent) {
		
	}
	
	public void onClick(ItemStack stack, BlockPos blockPos, Entity ent) {
		
	}
	
	public void onDestroyBlock(ItemStack stack, BlockPos blockPos, Entity ent) {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getUniqueNumber() {
		return this.uniqueNumber;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
}
