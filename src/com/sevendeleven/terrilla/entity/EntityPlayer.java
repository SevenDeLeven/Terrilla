package com.sevendeleven.terrilla.entity;

import com.sevendeleven.terrilla.main.Main;
import com.sevendeleven.terrilla.main.Renderer;
import com.sevendeleven.terrilla.render.Camera;
import com.sevendeleven.terrilla.util.AABB;
import com.sevendeleven.terrilla.util.ConcurrentHandler;
import com.sevendeleven.terrilla.util.ResourcesManager;
import com.sevendeleven.terrilla.world.World;

public class EntityPlayer extends Entity {
	
	private World world;
	
	private Camera camera;
	
	public EntityPlayer(World world, float x, float y, long uniqueID) {
		super(world, ResourcesManager.getSprite("player"), uniqueID, x, y, new AABB(-0.4f, -0.98f, 0.4f, 0.98f));
		this.world = world;
		this.camera = Renderer.getRenderer().getCamera();
		this.vx = -0.01f;
		this.vy = 0.5f;
	}
		

	@Override
	public void update(long milliTime, long deltaTick, ConcurrentHandler handler) {
		this.camera.setX(this.x*Main.getBlockSize());
		this.camera.setY(this.y*Main.getBlockSize());
		this.move();
	}
	
}
