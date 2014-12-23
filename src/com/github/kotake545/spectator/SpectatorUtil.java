package com.github.kotake545.spectator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectatorUtil {
	/**
	 * 観戦者の読み込み
	 */
	@SuppressWarnings("deprecation")
	public static void reloadSpectator(Player player){
		if(SpectatorConf.seeSpectatorInvisible){
			return;
		}
		for(Player spectator:Spectator.spectators){
			 if (spectator != player) {
				 player.hidePlayer(spectator);
				 if(spectator.isOp()&&SpectatorConf.opShowSpectator){
					 player.showPlayer(spectator);
					 continue;
				 }
			}
		}

	}
	public static void teleporter(Player spectator){
		Inventory gui = Bukkit.getServer().createInventory(spectator, 27,"SpectatorTeleporter");
		for(Player player:Bukkit.getOnlinePlayers()){
			if(SpectatorUtil.checkSpectator(player)||player==spectator){
				continue;
			}
			ItemStack playerhead = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta meta = (SkullMeta)playerhead.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName(player.getName());
			playerhead.setItemMeta(meta);
			gui.addItem(new ItemStack[] { playerhead });
		}
		spectator.openInventory(gui);
	}
	/**
	 * playerを観戦者にする
	 * @param player
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void setSpectator(Player player){
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,9999*20,0));
		player.setAllowFlight(true);
		player.setFlying(true);
		Spectator.getSpectatorManager().setWidthHeight(player, 0.0F, 0.0F, 0.0F);
		for(Player other:Bukkit.getOnlinePlayers()){
			 if (other != player) {
				 other.hidePlayer(player);
				 if(other.isOp()&&SpectatorConf.opShowSpectator){
					 other.showPlayer(player);
					 continue;
				 }
			}
		}
        PlayerInventory inventory = player.getInventory();
		List<List<ItemStack>> inv = new ArrayList<List<ItemStack>>();
		if(Spectator.spectator.get(player.getName())!=null){
			inv = (List<List<ItemStack>>) Spectator.spectator.get(player.getName());
			}else if(SpectatorConf.spectatorReturnInventory){
				//inv.add(inventory.getContents());
				List<ItemStack> contents = new ArrayList<ItemStack>();
				ItemStack[] i = player.getInventory().getContents();
				int pos = 0;
				for (ItemStack stack : i) {
					contents.add(pos, stack);
					pos++;
					}
				inv.add(contents);
				int pos2 = 0;
				List<ItemStack> armorcontents = new ArrayList<ItemStack>();
				ItemStack[] a = player.getInventory().getArmorContents();
				for (ItemStack stack : a) {
					armorcontents.add(pos2, stack);
					pos2++;
					}
				inv.add(armorcontents);
				}else{
					inv = null;
				}
		if(SpectatorConf.spectatorInventoryClear){
			inventory.clear();
			inventory.setHelmet(null);
			inventory.setChestplate(null);
			inventory.setLeggings(null);
			inventory.setBoots(null);
			inventory.setItem(0,setName(new ItemStack(Material.COMPASS),SpectatorConf.TeleporterName));
			player.updateInventory();
		}
		if(Spectator.spectators.contains(player)){//既に登録されていた場合終わる。
			String jp = SpectatorConf.format+ChatColor.AQUA+"あなたは観戦者です。";
			String other = SpectatorConf.format+ChatColor.AQUA+"You are spectators.";
			SpectatorUtil.sendMessage(player, jp, other);
			return;
		}
		Spectator.spectators.add(player);
		Spectator.spectator.set(String.format("%s",player.getName()),inv);
		try {
			Spectator.spectator.save(Spectator.getFile("spectators"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jp = SpectatorConf.format+ChatColor.AQUA+"観戦者になりました。";
		String other = SpectatorConf.format+ChatColor.AQUA+"You became spectators.";
		SpectatorUtil.sendMessage(player, jp, other);
		return;
	}
	/**
	 * playerを観客者から除外する。
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void removeSpectator(Player player){
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		if(player.getGameMode()!=GameMode.CREATIVE){
			 player.setAllowFlight(false);
				player.setFlying(false);
		}
		Spectator.getSpectatorManager().setWidthHeight(player, 0.0F, 0.6F, 1.8F);
		for(Player other:Bukkit.getOnlinePlayers()){
			 if (other != player) {
				 other.showPlayer(player);
			}
		}
		if(!Spectator.spectators.contains(player)){//登録されていない場合何もせず終わる。
			return;
		}
        PlayerInventory inventory = player.getInventory();
		if (SpectatorConf.spectatorInventoryClear&&inventory != null && inventory.contains(Material.COMPASS)){
			inventory.clear();
			inventory.setHelmet(null);
			inventory.setChestplate(null);
			inventory.setLeggings(null);
			inventory.setBoots(null);
			player.updateInventory();
		}
		//インベントリー修復がONの場合復元する
		if(SpectatorConf.spectatorReturnInventory){
			if(Spectator.spectator.get(String.format("%s",player.getName()))!=null){
				List<List<ItemStack>> inv = new ArrayList<List<ItemStack>>();
				inv = (List<List<ItemStack>>) Spectator.spectator.get(player.getName());
				int pos = 0;
				for(ItemStack a :inv.get(0)){
					 player.getInventory().setItem(pos,a);
					 pos++;
				}
				player.getInventory().setHelmet(inv.get(1).get(3));
				player.getInventory().setChestplate(inv.get(1).get(2));
				player.getInventory().setLeggings(inv.get(1).get(1));
				player.getInventory().setBoots(inv.get(1).get(0));
				player.updateInventory();
			}
		}
		Spectator.spectators.remove(player);
		Spectator.spectator.getConfigurationSection("").set(player.getName(), null);
		try {
			Spectator.spectator.save(Spectator.getFile("spectators"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jp = SpectatorConf.format+ChatColor.GREEN+"観戦者から離脱しました。";
		String other = SpectatorConf.format+ChatColor.GREEN+"You quit spectators.";
		SpectatorUtil.sendMessage(player, jp, other);
		return;
	}

	public static void sendMessage(Player player,String jp,String other){
		if(getPlayerLanguage((Player) player).matches("ja_JP")){
			player.sendMessage(jp);
		}else{
			player.sendMessage(other);
		}
	}

	public static Boolean spectatorcheckByConfig(Player player){
  		List<String> players = new ArrayList<String>();
		Set<String> playerKeys = Spectator.spectator.getConfigurationSection("").getKeys(false);
		for(String p : playerKeys){
			players.add(p);
		}
		if(players.contains(player.getName())){
			return true;
		}
		return false;
	}
	public static Boolean checkSpectator(Player player){
		if(Spectator.spectators.contains(player)||spectatorcheckByConfig(player)){
			return true;
		}
		return false;
	}

	  public static ItemStack setName(ItemStack is, String name)
	  {
	    return setName(is, name, null);
	  }
	  public static ItemStack setName(ItemStack is, String name, List<String> descripList)
	  {
	    ItemMeta m = is.getItemMeta();
	    m.setDisplayName(name);
	    if ((descripList != null) && (descripList.size() > 0)) {
	      m.setLore(descripList);
	    }
	    is.setItemMeta(m);
	    return is;
	  }
		public static String getPlayerLanguage(Player p){
			try {
				Object o = getEntityPlayer(p);
				String s = (String) getValue(o, "locale");

				return s;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		public static Object getValue(Object instance, String fieldName) throws Exception {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
			//eturn Language.getLanguage(fieldName);
		}
	    public static Object getEntityPlayer(Player p) throws Exception{
	        Method getHandle = p.getClass().getMethod("getHandle");
	        return getHandle.invoke(p);
	    }

	    @SuppressWarnings("unused")
		private static Method getMethod(String name, Class<?> clazz) {
	        for (Method m : clazz.getDeclaredMethods()) {
	            if (m.getName().equals(name)) return m;
	        }
	        return null;
	    }
}
