package com.github.kotake545.spectator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

public class SpectatorManager{
	@SuppressWarnings("unused")
	private SimpleCommandMap commandMap;
	private String Version;
	private Object propertyManager;
	public SpectatorManager(){
		try {
			this.commandMap = ((SimpleCommandMap)Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap", new Class[0]).invoke(Bukkit.getServer(), new Object[0]));
			Object obj = Bukkit.getServer().getClass().getDeclaredMethod("getServer", new Class[0]).invoke(Bukkit.getServer(), new Object[0]);
			this.propertyManager = obj.getClass().getDeclaredMethod("getPropertyManager", new Class[0]).invoke(obj, new Object[0]);
			this.Version = this.propertyManager.getClass().getPackage().getName();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 更新用。
	 * @param p
	 * @param x
	 * @param z
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendChunk(Player p, int x, int z){
		try {
			Object obj = p.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			List list = (List)obj.getClass().getField("chunkCoordIntPairQueue").get(obj);
			Constructor con = Class.forName(this.Version + ".ChunkCoordIntPair").getConstructor(new Class[] { Integer.TYPE, Integer.TYPE });
			list.add(con.newInstance(new Object[] { Integer.valueOf(x), Integer.valueOf(z) }));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 判定変更。デフォルト:0.0F, 0.6F, 1.8F
	 * @param p
	 * @param height
	 * @param width
	 * @param length
	 */
	public void setHeight(Player p, float height, float width, float length){
		try{
			Method handle = p.getClass().getMethod("getHandle", new Class[0]);
			Class<?> c = Class.forName(this.Version + ".Entity");
			Field heightfield = c.getDeclaredField("height");
			Field widthfield = c.getDeclaredField("width");
			Field lengthfield = c.getDeclaredField("length");
			heightfield.setFloat(handle.invoke(p, new Object[0]), height);
			widthfield.setFloat(handle.invoke(p, new Object[0]), width);
			lengthfield.setFloat(handle.invoke(p, new Object[0]), length);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}