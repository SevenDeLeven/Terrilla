package com.sevendeleven.terrilla.util;

public class Vec2f {
	public float x,y;
	public Vec2f(BlockPos pos) {
		this.x = pos.getX();
		this.y = pos.getY();
	}
	public Vec2f(Vec2f v) {
		this.x = v.x;
		this.y = v.y;
	}
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public float magnitude() {
		return (float)Math.sqrt((x*x)+(y*y));
	}
	public float magnitudeSq() {
		return (x*x)+(y*y);
	}
	public float distanceFrom(Vec2f vec) {
		return this.subtract(vec).magnitude();
	}
	public float distanceFromSq(Vec2f vec) {
		return this.subtract(vec).magnitudeSq();
	}
	public Vec2f setMagnitude(float magnitude) {
		float currentMagnitude = this.magnitude();
		if (currentMagnitude != 0) {
			float mult = magnitude/currentMagnitude;
			this.x *= mult;
			this.y *= mult;
		}
		return this;
	}
	public void limit(float mag) {
		if (this.magnitudeSq() > mag*mag) {
			this.setMagnitude(mag);
		}
	}
	public Vec2f add(Vec2f a) {
		return new Vec2f(x+a.x, y+a.y);
	}
	public Vec2f subtract(Vec2f a) {
		return add(a.multiply(-1));
	}
	public Vec2f multiply(float a) {
		return new Vec2f(this.x*a, this.y*a);
	}
	public Vec2f divide(float a) {
		return multiply(1.0f/a);
	}
	public Vec2f copy() {
		return new Vec2f(this);
	}
	public Vec2f setDirection(float d) {
		float magnitude = magnitude();
		this.x = (float)Math.cos(d)*magnitude;
		this.y = (float)Math.sin(d)*magnitude;
		return this;
	}
}
