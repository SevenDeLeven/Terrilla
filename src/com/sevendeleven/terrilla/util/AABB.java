package com.sevendeleven.terrilla.util;

public class AABB {
	
	float minX, minY, maxX, maxY;
	
	public AABB(float x1, float y1, float x2, float y2) {
		minX = x1 < x2 ? x1 : x2;
		maxX = x1 > x2 ? x1 : x2;
		minY = y1 < y2 ? y1 : y2;
		maxY = y1 > y2 ? y1 : y2;
	}
	
	public boolean contains(Vec2f location, Vec2f pt) {
		return this.minX+location.x <= pt.x && this.maxX+location.x >= pt.x && this.minY+location.y <= pt.y && this.maxY+location.y >= pt.y;
	}
	
	public boolean collides(Vec2f location, Vec2f a1Location, AABB a1) {
		return this.collidesA(location, a1Location, a1) || a1.collidesA(a1Location, location, this);
	}
	
	public boolean collides(Vec2f location, BlockPos pos) {
		return this.collides(location, new Vec2f(pos), new AABB(0, 0, 1.0f, -1.0f));
	}
	
	public Vec2f topLeft() {
		return new Vec2f(this.minX, this.minY); 
	}
	
	public Vec2f topRight() {
		return new Vec2f(this.maxX, this.minY);
	}
	
	public Vec2f bottomLeft() {
		return new Vec2f(this.minX, this.maxY);
	}
	
	public Vec2f bottomRight() {
		return new Vec2f(this.maxX, this.maxY);
	}
	
	private boolean collidesA(Vec2f location, Vec2f a1Location, AABB a1) {
		return this.contains(location, a1.topLeft().add(a1Location)) || this.contains(location, a1.topRight().add(a1Location)) || this.contains(location, a1.bottomLeft().add(a1Location)) || this.contains(location, a1.bottomRight().add(a1Location));
	}
	
	public float getWidth() {
		return this.maxX-this.minX;
	}
	
	public float getHeight() {
		return this.maxY-this.minY;
	}
	
}
