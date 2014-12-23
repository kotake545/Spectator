package com.github.kotake545.spectator.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.kotake545.spectator.Spectator;
import com.github.kotake545.spectator.SpectatorConf;
import com.github.kotake545.spectator.SpectatorUtil;


public class PlayerListener implements Listener {
	private Spectator plugin;
	public PlayerListener(Spectator plugin) {
		this.plugin = plugin;
	}
	@EventHandler()
	public void onPlayerLaunch(BlockCanBuildEvent event) {
		event.setBuildable(true);
	}

	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(SpectatorConf.spectatorSetOnDeath&&event.getEntity() instanceof Player){
			SpectatorUtil.setSpectator((Player)event.getEntity());
		}
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		SpectatorUtil.reloadSpectator(player);

		if(SpectatorConf.spectatorSetOnFirstJoin){
	        if (!event.getPlayer().hasPlayedBefore()){

	        }
		}

		if(SpectatorConf.spectatorSetOnJoin){
			SpectatorUtil.setSpectator(player);
		}
	}

//	@EventHandler()
//	public void onPlayerLaunch(ProjectileLaunchEvent event) {
//		if(SpectatorConf.AllProjectTileCheck){
//			new ThrownPotionTask(event.getEntity()).runTaskTimer(plugin, 0, 1);
//			return;
//		}
//		if(event.getEntity() instanceof ThrownPotion||event.getEntity() instanceof ThrownExpBottle){
//			new ThrownPotionTask(event.getEntity()).runTaskTimer(plugin, 0, 1);
//		}
//	}
//	@EventHandler()
//	public void enderPearlThrown(PlayerTeleportEvent event) {
//	if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
//
//	Player player = event.getPlayer();
//	if(player.hasMetadata("CancellTeleport") ){
//		event.setCancelled(true);
//	}
//	}
}
