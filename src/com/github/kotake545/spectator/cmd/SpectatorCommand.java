package com.github.kotake545.spectator.cmd;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.kotake545.spectator.SpectatorConf;
import com.github.kotake545.spectator.SpectatorUtil;

public class SpectatorCommand extends CommandExecuter {
	public static String[] booleanlist = new String[]{"BuriedAliveCheck","opShowMode","spectatorSetOnDeath","spectatorInventoryClear","spectatorReturnInventory","spectatorChatMode","seeSpectatorInvisible","spectatorSetOnJoin","spectatorRemoveOnQuit","spectatorRemoveOnTeleportWorld","spectatorSetOnFirstJoin"};
	@Override
	public boolean onCommand(CommandSender sender, Command command, String paramString, String[] args) {
		if(args.length!=1&&args.length!=2&&args.length!=3){
			return unknown(sender);
		}
		if(args.length==2&&(args[0].equals("remove")||args[0].equals("set"))){
			if(Bukkit.getPlayerExact(args[1])!=null&&Bukkit.getPlayerExact(args[1]).isOnline()){
				if(args[0].equals("set")){
					sender.sendMessage(SpectatorConf.format+"setSpectator["+args[1]+"]");
					SpectatorUtil.setSpectator(Bukkit.getPlayerExact(args[1]));
				}else{
					sender.sendMessage(SpectatorConf.format+"removeSpectator["+args[1]+"]");
					SpectatorUtil.removeSpectator(Bukkit.getPlayerExact(args[1]));
				}
			}else{
				String jp = SpectatorConf.format+ChatColor.RED+"["+args[1]+"]が見つかりません。";
				String other = SpectatorConf.format+ChatColor.RED+"["+args[1]+"] can not be found.";
				if(sender instanceof Player){
					SpectatorUtil.sendMessage((Player) sender,jp, other);
				}else{
					sender.sendMessage(other);
				}
			}
			return true;
		}
		if(args.length==1&&(args[0].equals("removeall")||args[0].equals("setall"))){
			if(args[0].equals("setall")){
				String jp = SpectatorConf.format+ChatColor.AQUA+"OnlinePlayer全員を観戦者に設定しました。";
				String other = SpectatorConf.format+ChatColor.AQUA+"All online player become spectator.";
				if(sender instanceof Player){
					SpectatorUtil.sendMessage((Player) sender,jp, other);
				}else{
					sender.sendMessage(other);
				}
				for(Player p:Bukkit.getOnlinePlayers()){
					SpectatorUtil.setSpectator(p);
				}
			}else{
				String jp = SpectatorConf.format+ChatColor.AQUA+"OnlinePlayer全員を観戦者から離脱させました。";
				String other = SpectatorConf.format+ChatColor.AQUA+"All online player quit spectator.";
				if(sender instanceof Player){
					SpectatorUtil.sendMessage((Player) sender,jp, other);
				}else{
					sender.sendMessage(other);
				}
				for(Player p:Bukkit.getOnlinePlayers()){
					SpectatorUtil.removeSpectator(p);
				}
			}
			return true;
		}
		if(args.length==1&&(args[0].equals("config"))){
			return confighelp(sender);
		}

		if(args.length==3&&(args[0].equals("config"))&&Arrays.asList(booleanlist).contains(args[1])){
			if(Boolean.valueOf(args[2]) instanceof Boolean&&!args[2].equals("help")){
				SpectatorConf.setConfig(args[1],Boolean.valueOf(args[2]));
				sender.sendMessage(SpectatorConf.format+ChatColor.GREEN+"SaveConfig:"+ChatColor.AQUA+args[1]+" = "+Boolean.valueOf(args[2]));
				return true;
			}else{
				sender.sendMessage(SpectatorConf.format+"/spectator config "+args[1]+ChatColor.RED+" [true or false]");
				sender.sendMessage(SpectatorConf.format+ChatColor.GOLD+"-------[Description]-------");
				returnDescription(sender,args[1]);
			}
			return true;
		}else{
			return confighelp(sender);
		}
	}
	private void returnDescription(CommandSender sender,String list){
		String format = ChatColor.RED+list+ChatColor.AQUA+" - "+ChatColor.GOLD;
		String jp = "説明文作成途中";
		String other = "During description created";
		if(list.equals("BuriedAliveCheck")){
			jp = "生き埋めや、奈落に落ちた時、自動的に初期スポーンへテレポートする設定";
			other = "Teleport fell (fell into the abyss or became buried alive) that time to respawn";
		}
		if(list.equals("opShowMode")){
			jp = "管理者が観戦者、観戦者チャットを見えるようにするか否か";
			other = "Admin is I can see the audience.";
		}
		if(list.equals("spectatorSetOnDeath")){
			jp = "死亡時自動的に観戦者にするか";
			other = "When player died, automatically becomes the spectator.";
		}
		if(list.equals("spectatorInventoryClear")){
			jp = "観戦者移行時にアイテムを観戦用のものに変えるか";

		}
		if(list.equals("spectatorReturnInventoryh")){
			jp = "観戦者離脱時に元々のインベントリを復元する。";

		}
		if(list.equals("spectatorChatMode")){
			jp = "観戦者チャットモード";

		}
		if(list.equals("seeSpectatorInvisible")){
			jp = "観戦者同士で見えるようにするか否か";

		}
		if(list.equals("spectatorSetOnJoin")){
			jp = "サーバー接続時に観戦者に設定する。";

		}
		if(list.equals("spectatorRemoveOnQuit")){
			jp = "サーバー離脱時に観戦者から抜ける";

		}
		if(list.equals("spectatorRemoveOnTeleportWorld")){
			jp = "ワールド移動時に観戦者から抜ける";

		}
		if(sender instanceof Player){
			SpectatorUtil.sendMessage((Player) sender,format+jp, format+other);
		}else{
			sender.sendMessage(format+other);
		}
	}

	private Boolean unknown(CommandSender sender){
		sender.sendMessage(SpectatorConf.format+"/spectator set [playername]");
		sender.sendMessage(SpectatorConf.format+"/spectator remove [playername]");
		sender.sendMessage(SpectatorConf.format+"/spectator setall");
		sender.sendMessage(SpectatorConf.format+"/spectator removeall");
		sender.sendMessage(SpectatorConf.format+"/spectator config [list] [true/false]");
		sender.sendMessage(SpectatorConf.format+ChatColor.GOLD+"┗[ /spectator config -> Help");
		return true;
	}
	private Boolean confighelp(CommandSender sender){
		sender.sendMessage(SpectatorConf.format+"/spectator config "+ChatColor.GOLD+"[list]"+ChatColor.RESET+" [true/false]");
		sender.sendMessage(SpectatorConf.format+ChatColor.GOLD+"-------[list]-------");
		sender.sendMessage(booleanlist);
		sender.sendMessage(ChatColor.GOLD+"┗[ /spectator config [list] help -> Description");
		return true;
	}
}