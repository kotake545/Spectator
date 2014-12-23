package com.github.kotake545.spectator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.kotake545.spectator.cmd.SJoinCommand;
import com.github.kotake545.spectator.cmd.SleaveCommand;
import com.github.kotake545.spectator.cmd.SpectatorCommand;
import com.github.kotake545.spectator.listener.PlayerListener;
import com.github.kotake545.spectator.listener.SpectatorListener;

public class Spectator extends JavaPlugin {
	public static FileConfiguration config;
    public static Spectator instance;
    public static Collection<Player> spectators;
	public static YamlConfiguration spectator;
	private static SpectatorManager spectatorManager;

    @Override
    public void onEnable() {
        instance = this;
        File configFile = new File(this.getDataFolder(), "config.yml");
        if ( !configFile.exists() ) {
        	this.saveDefaultConfig();
        }
        spectators = new ArrayList<Player>();
        spectator=getConfiguration("spectators");
  		List<String> players = new ArrayList<String>();
		Set<String> playerKeys = spectator.getConfigurationSection("").getKeys(false);
		for(String p : playerKeys){
			players.add(p);
		}
        for(Player onlinepayer:Bukkit.getOnlinePlayers()){
        	if(players.contains(onlinepayer.getName())){
        		SpectatorUtil.setSpectator(onlinepayer);
        	}
        }
        SpectatorConf.reloadConfig();

		getCommand("spectator").setExecutor(new SpectatorCommand());
		getCommand("spectatorjoin").setExecutor(new SJoinCommand());
		getCommand("spectatorleave").setExecutor(new SleaveCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new SpectatorListener(this), this);
    }
    private YamlConfiguration getConfiguration(String configName) {
        File file = new File(
                Spectator.instance.getDataFolder() +
                File.separator + configName + ".yml");

        if ( !file.exists() ) {
            YamlConfiguration conf = new YamlConfiguration();
            try {
                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    public static File getFile(String yml){
    	File file;
		file = new File(Spectator.instance.getDataFolder() + File.separator +yml +".yml");
		if (!file.exists()) {
			YamlConfiguration conf = new YamlConfiguration();
			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
    }
    public static SpectatorManager getSpectatorManager(){
    	if (spectatorManager == null) {
    		spectatorManager = new SpectatorManager();
    		}
    	return spectatorManager;
    	}
}
