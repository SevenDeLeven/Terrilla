package com.sevendeleven.terrilla.item;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.util.BlockPos;
import com.sevendeleven.terrilla.world.Tile;

public class TileItem {

	protected String name;
	protected Sprite sprite;
	protected int uniqueNumber;
	protected int maxStackSize;
	
	public TileItem(Tile block) {
		this.name = block.getName();
		this.sprite = block.getSprite();
		this.uniqueNumber = block.getUniqueNumber();
		this.maxStackSize = 1000;
	}
	
	public void onUse(ItemStack stack, BlockPos blockPos, Entity ent) {
		if (blockPos.getWorld().getBlock(blockPos) == 0) {
			BlockPos top = new BlockPos(blockPos.getWorld(), blockPos.getX(), blockPos.getY()+1);
			BlockPos bottom = new BlockPos(blockPos.getWorld(), blockPos.getX(), blockPos.getY()-1);
			BlockPos right = new BlockPos(blockPos.getWorld(), blockPos.getX()+1, blockPos.getY());
			BlockPos left = new BlockPos(blockPos.getWorld(), blockPos.getX()-1, blockPos.getY());
			if (blockPos.getWorld().getBlock(top) != 0 || blockPos.getWorld().getBlock(bottom) != 0 || blockPos.getWorld().getBlock(right) != 0 || blockPos.getWorld().getBlock(left) != 0) {
				blockPos.getWorld().placeBlock(blockPos.getX(), blockPos.getY(), this.getUniqueNumber(), true);
				stack.setSize(stack.getSize()-1);
			}
		}
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void onRightClick(ItemStack stack, BlockPos blockPos, Entity ent) {
		
	}
	
	public void onDestroyBlock(ItemStack stack, BlockPos blockPos, Entity ent) {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getUniqueNumber() {
		return this.uniqueNumber;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
}
