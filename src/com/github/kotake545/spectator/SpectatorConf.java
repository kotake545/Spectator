package com.github.kotake545.spectator;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;


public class SpectatorConf {
	public static Boolean opShowSpectator = false;
	public static Boolean spectatorSetOnDeath = true;
	public static Boolean spectatorInventoryClear = true;
	public static Boolean spectatorReturnInventory = false;
	public static Boolean spectatorChatMode = true;
	public static Boolean seeSpectatorInvisible = false;
	public static Boolean spectatorRemoveOnQuit = true;
	public static Boolean spectatorRemoveOnTeleportWorld = true;
	public static Boolean spectatorSetOnJoin = false;
	public static String spectatorChat = "&3[Spectator]&7<&4<player>&7>&7 <msg>";
	public static String format = "§3[Spectator]§r";
	public static String TeleporterName = ChatColor.AQUA+"SpectatorCompass";
	public static boolean spectatorSetOnFirstJoin = false;
	//public static String TeleporterGui = ChatColor.AQUA+"SpectatorCompass";
    /**
     * config.ymlの読み出し処理。
     * @throws IOException
     * @return 成功したかどうか
     */
    public static void reloadConfig() {
        File configFile = new File(Spectator.instance.getDataFolder(), "config.yml");
        if ( !configFile.exists() ) {
        	//読み込み失敗
        	Spectator.instance.saveDefaultConfig();
        	return;
        }
        // config取得
        Spectator.instance.reloadConfig();
        FileConfiguration config = Spectator.instance.getConfig();
    	opShowSpectator = config.getBoolean("opShowMode");
    	spectatorSetOnDeath = config.getBoolean("spectatorSetOnDeath");
    	spectatorInventoryClear = config.getBoolean("spectatorInventoryClear");
    	spectatorReturnInventory = config.getBoolean("spectatorReturnInventory");
    	spectatorChatMode = config.getBoolean("spectatorChatMode");
    	seeSpectatorInvisible = config.getBoolean("seeSpectatorInvisible");
    	spectatorRemoveOnQuit = config.getBoolean("spectatorRemoveOnQuit");
    	spectatorRemoveOnTeleportWorld = config.getBoolean("spectatorRemoveOnTeleportWorld");
    	spectatorChat = config.getString("spectatorChat");
    	spectatorSetOnJoin = config.getBoolean("spectatorSetOnJoin");
    	spectatorSetOnFirstJoin = config.getBoolean("spectatorSetOnFirstJoin");
    	Spectator.config = config;
    }
    public static void setConfig(String setting,Boolean b) {
    	Spectator.config.set(String.format("%s",setting),b);
		try {
			Spectator.config.save(Spectator.getFile("config"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		reloadConfig();
    }
}
