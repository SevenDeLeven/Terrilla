package com.sevendeleven.terrilla.util;

import java.util.*;

public class PerlinNoise {
	
	public static class PRandom {
		
		private final long seed;
		private long nseed;
		
		private final long mult;
		private final long begin;
		
		PRandom(long seed) {
			Random r = new Random(seed);
			this.seed = seed;
			this.nseed = seed;
			this.mult = r.nextLong();
			this.begin = r.nextLong();
		}
		
		private double r(long seed) {
			return (((seed*mult) + begin)/(double)Long.MAX_VALUE);
		}
		
		double next() {
			return r(nseed++);
		}
		
		double get(int t) {
			return r(seed+t);
		}
	}
	
	PRandom rand;
	public PerlinNoise(long seed) {
		rand = new PRandom(seed);
	}
	
	public double getYatX(int x) {
		return rand.get(x);
	}
	public float cosInt(int x1, int x2, double mu, double height, double delta){
	   double mu2 = (1-Math.cos(mu*Math.PI))/2;
	   double y1 = getYatX(x1);
	   double y2 = getYatX(x2);
	   return (float)((((y1*(1.0-mu2))+(y2*mu2))*delta)+height);
	}
	
	public float[] getNoise(int x1, int x2, double height, double delta, int size) {
		float[] d = new float[size];
		for (int i = 0; i < size; i++) {
			float mu = (float)i/(float)size;
			float in = (float)cosInt(x1, x2, mu, height, delta);
			d[i] = in;
		}
		return d;
	}
}
