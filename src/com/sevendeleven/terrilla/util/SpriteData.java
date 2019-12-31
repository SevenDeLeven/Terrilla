package com.sevendeleven.terrilla.util;

import java.nio.FloatBuffer;

import com.sevendeleven.terrilla.entity.Entity;
import com.sevendeleven.terrilla.render.Sprite;
import com.sevendeleven.terrilla.world.Physics;

public class SpriteData {
	
	private boolean remove;
	
	private Sprite sprite;
	private long id;
	private long milliTime, deltaTick;
	private float x, y;
	private float vx, vy;
	private float angle;
	
	public SpriteData(Entity ent) {
		this.sprite = ent.getSprite();
		this.remove = false;
		this.id = ent.getUniqueID();
	}
	
	public SpriteData(long milliTime, long deltaTick, Entity ent) {
		this.remove = false;
		this.sprite = ent.getSprite();
		this.milliTime = milliTime;
		this.deltaTick = deltaTick;
		this.id = ent.getUniqueID();
		this.x = ent.x;
		this.y = ent.y;
		this.vx = ent.vx;
		this.vy = ent.vy;
		this.angle = ent.angle;
	}
	
	public void update(SpriteData newData) {
		this.remove = newData.remove;
		this.milliTime = newData.milliTime;
		this.deltaTick = newData.deltaTick;
		this.x = newData.x;
		this.y = newData.y;
		this.vx = newData.vx;
		this.vy = newData.vy;
		this.angle = newData.angle;
	}
	
	public FloatBuffer getTransform(long nowTime, Sprite sprite) {
		if (this.angle != 0) {
			return Transform.getTransformMatrix(x + (float)(vx*((nowTime-milliTime)/1000.0f*Physics.TICK_RATE)), y + (float)(vy*((nowTime-milliTime)/1000.0f*Physics.TICK_RATE)), angle, sprite.getTexture());
		} else {
			return Transform.getTransformMatrix(x + (float)(vx*((nowTime-milliTime)/1000.0f*Physics.TICK_RATE)), y + (float)(vy*((nowTime-milliTime)/1000.0f*Physics.TICK_RATE)), sprite.getTexture());
		}
	}
	
	public boolean getRemove() {
		return this.remove;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public long getID() {
		return this.id;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getVX() {
		return this.vx;
	}
	
	public float getVY() {
		return this.vy;
	}
	
	public float getAngle() {
		return this.angle;
	}
	
}
