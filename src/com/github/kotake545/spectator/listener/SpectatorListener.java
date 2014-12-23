package com.github.kotake545.spectator.listener;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.github.kotake545.spectator.Spectator;
import com.github.kotake545.spectator.SpectatorConf;
import com.github.kotake545.spectator.SpectatorUtil;

public class SpectatorListener implements Listener {
	private Spectator plugin;
	public SpectatorListener(Spectator plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onSpectatorInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			if(event.getMaterial() == Material.COMPASS&&event.getItem().getItemMeta().getDisplayName().equals(SpectatorConf.TeleporterName)){
				SpectatorUtil.teleporter(player);
			}
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		 if ((event.getCurrentItem() != null) && (event.getCurrentItem().getType() == Material.SKULL_ITEM) && (event.getCurrentItem().getDurability() == 3)){
			 ItemStack playerhead = event.getCurrentItem();
			 SkullMeta meta = (SkullMeta)playerhead.getItemMeta();
			 Player skullOwner = this.plugin.getServer().getPlayer(meta.getOwner());
			 event.getWhoClicked().closeInventory();
			 if ((skullOwner != null) && (skullOwner.isOnline()) && (!skullOwner.getAllowFlight())){
				 event.getWhoClicked().teleport(skullOwner);
				 ((Player)event.getWhoClicked()).sendMessage(SpectatorConf.format + "Teleported you to "  + skullOwner.getName());
			 }else if (skullOwner == null){
				 //offline
			 }
			 event.setCancelled(true);
		 }
	}

	@EventHandler
	public void onSpectatorRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			SpectatorUtil.setSpectator(player);
		}
	}

	@EventHandler
	public void onSpectatorQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			if(SpectatorConf.spectatorRemoveOnQuit){
				SpectatorUtil.removeSpectator(player);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSpectatorChat(PlayerChatEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			String message = replace(player.getDisplayName(), event.getMessage());
			Collection<Player> sendPlayers = new ArrayList<Player>();
			if(SpectatorConf.spectatorChatMode){
				sendPlayers = Spectator.spectators;
			}else{
				for(Player a:Bukkit.getOnlinePlayers()){
					sendPlayers.add(a);
				}
			}
			for(Player send:sendPlayers){
				send.sendMessage(message);
			}
			event.setCancelled(true);
		}
	}
	public static String replace(String name,String message){
		String msg = SpectatorConf.spectatorChat;
		msg = msg.replaceAll("&", "§");
		msg = msg.replaceAll("<player>", name);
		msg = msg.replaceAll("<msg>", message);
		return msg;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSpectartorDamage(final EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			final Player player = (Player)event.getEntity();
			if(SpectatorUtil.checkSpectator(player)){
		        new BukkitRunnable() {
		            public void run() {
		            	player.setFireTicks(0);
		            }
		        }.runTaskLater(Spectator.instance, 1L);
				event.setCancelled(true);
			}
		}
	}
    @EventHandler
    public void onSpectartorInventory(InventoryClickEvent event){
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player)event.getWhoClicked();
			if(SpectatorUtil.checkSpectator(player)){
				event.setCancelled(true);
			}
		}
    }
    @EventHandler
    public void onSpectartorBed(PlayerBedEnterEvent event){
		Player player = (Player)event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onSpectartorPickupItem(PlayerPickupItemEvent event){
		Player player = (Player)event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onSpectartorPortal(PlayerPortalEvent event){
		Player player = (Player)event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onSpectartorExpChange(PlayerExpChangeEvent event){
		Player player = (Player)event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
            ((ExperienceOrb)player.getWorld().spawn(player.getLocation(), ExperienceOrb.class)).setExperience( event.getAmount() );
            event.setAmount(0);
            player.teleport(player.getWorld().getSpawnLocation());
		}
    }
    @EventHandler
    public void onSpectartorFoodcChange(FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(SpectatorUtil.checkSpectator(player)){
				event.setCancelled(true);
			}
		}
    }
    @EventHandler
    public void onSpectartorBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onSpectartorDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
    	if ((event.getTarget() != null) && ((event.getTarget() instanceof Player)) && SpectatorUtil.checkSpectator(((Player)event.getTarget()))) {
    		event.setCancelled(true);
    	}
    }
    @EventHandler
    public void onSpectartorBlockDamage(BlockDamageEvent event){
		Player player = event.getPlayer();
		if(SpectatorUtil.checkSpectator(player)){
			event.setCancelled(true);
		}
    }
    @EventHandler
    public void onSpectartorEnter(VehicleEnterEvent event){
    	if(event.getEntered() instanceof Player){
        	Player player = (Player)event.getEntered();
        			if (SpectatorUtil.checkSpectator(player)) {
        				event.setCancelled(true);
        			}
    	}
    }
    @EventHandler
    public void onSpectartorVehicleDamage(VehicleDamageEvent event){
    	if(event.getAttacker() instanceof Player){
        	Player player = (Player)event.getAttacker();
        			if (SpectatorUtil.checkSpectator(player)) {
        				event.setCancelled(true);
        			}
    	}
    }

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamageEvent(EntityDamageByEntityEvent event){
        final Entity entityDamager = event.getDamager();
        Entity entityDamaged = event.getEntity();
		if(entityDamager instanceof Player){//HidePlayerが加害者。
			if(SpectatorUtil.checkSpectator((Player)entityDamager)){
				event.setCancelled(true);
			}
		}
		if(entityDamaged instanceof Player){//HidePlayerが被害者
			Player damaged = (Player) entityDamaged;
			if(SpectatorUtil.checkSpectator((Player)entityDamaged)){
				if(entityDamager instanceof Projectile){
					final Projectile projecttile = (Projectile) entityDamager;
                    Vector velocity = projecttile.getVelocity();
                    damaged.teleport(damaged.getLocation().add(0,2,0));
                    Projectile next = projecttile.getShooter().launchProjectile(projecttile.getClass());
                    next.setVelocity(velocity);
                    next.setBounce(false);
                    next.setShooter(projecttile.getShooter());
                    projecttile.remove();
//    				if(projecttile instanceof EnderPearl){
//    					projecttile.getShooter().setMetadata("CancellTeleport", new FixedMetadataValue(plugin, true));
//    					//1tick後に削除
//    			        new BukkitRunnable() {
//    			            public void run() {
//    			            	projecttile.getShooter().removeMetadata("CancellTeleport", plugin);
//    			            }
//    			        }.runTaskLater(plugin, 1);
//    				}
				}
				event.setCancelled(true);
			}
		}
	}
}
