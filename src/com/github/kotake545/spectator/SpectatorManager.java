package com.github.kotake545.spectator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

public class SpectatorManager{
	private SimpleCommandMap commandMap;
	private String currentVersion;
	private Object propertyManager;
	public SpectatorManager(){
		try {
			this.commandMap = ((SimpleCommandMap)Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap", new Class[0]).invoke(Bukkit.getServer(), new Object[0]));
			Object obj = Bukkit.getServer().getClass().getDeclaredMethod("getServer", new Class[0]).invoke(Bukkit.getServer(), new Object[0]);
			this.propertyManager = obj.getClass().getDeclaredMethod("getPropertyManager", new Class[0]).invoke(obj, new Object[0]);
			this.currentVersion = this.propertyManager.getClass().getPackage().getName();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendChunk(Player p, int x, int z){
		try {
			Object obj = p.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			List list = (List)obj.getClass().getField("chunkCoordIntPairQueue").get(obj);
			Constructor con = Class.forName(this.currentVersion + ".ChunkCoordIntPair").getConstructor(new Class[] { Integer.TYPE, Integer.TYPE });
			list.add(con.newInstance(new Object[] { Integer.valueOf(x), Integer.valueOf(z) }));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setWidthHeight(Player p, float height, float width, float length){
		try{
			Method handle = p.getClass().getMethod("getHandle", new Class[0]);
			Class<?> c = Class.forName(this.currentVersion + ".Entity");
			Field field1 = c.getDeclaredField("height");
			Field field2 = c.getDeclaredField("width");
			Field field3 = c.getDeclaredField("length");
			field1.setFloat(handle.invoke(p, new Object[0]), height);
			field2.setFloat(handle.invoke(p, new Object[0]), width);
			field3.setFloat(handle.invoke(p, new Object[0]), length);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}