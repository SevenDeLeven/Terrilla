package com.sevendeleven.terrilla.main;

import java.util.HashMap;
import java.util.Map;

import com.sevendeleven.terrilla.util.ID;
import com.sevendeleven.terrilla.world.Tile;

public class Register {
	
	private static Map<ID, Tile> tileMap = new HashMap<>();
	private static Map<Integer, Tile> tileMapByID = new HashMap<>();
	private static Map<String, Tile> tileMapByName = new HashMap<>();
	
	public static void registerTile(ID id, Tile tile) {
		System.out.println("Registering tile " + id.getUniqueName() + " with numeric id " + id.getId());
		tileMap.put(id, tile);
		tileMapByID.put(id.getId(), tile);
		tileMapByName.put(id.getUniqueName(), tile);
	}
	
	public static Tile getTile(ID id) {
		return tileMap.get(id);
	}
	
	public static Tile getTile(Integer id) {
		return tileMapByID.get(id);
	}
	
	public static Tile getTile(String name) {
		return tileMapByName.get(name);
	}
	
}
