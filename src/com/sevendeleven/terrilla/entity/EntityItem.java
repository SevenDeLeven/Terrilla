package com.sevendeleven.terrilla.entity;

import com.sevendeleven.terrilla.util.AABB;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.world.World;

public class EntityItem extends Entity {
	
	public EntityItem(World world, String itemName, long uniqueID, float x, float y) {
		super(world, ResourcesManager.getSprite("item_"+itemName), uniqueID, x, y, new AABB(-0.25f, -0.25f, 0.25f, 0.25f));
	}
	
	@Override
	public void update(long milliTime, long deltaTick, ConcurrentHandler concurrentHandler) {
//		System.out.println(this.x + " " + this.y + " " + this.vx + " " + this.vy);
		this.applyGravity();
		this.move();
		this.updateEntity(milliTime, deltaTick, concurrentHandler);
	}
	
}
