package com.github.kotake545.spectator.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.kotake545.spectator.SpectatorUtil;

public class SleaveCommand extends CommandExecuter {
	public static String[] booleanlist = new String[]{"BuriedAliveCheck","opShowMode","spectatorSetOnDeath","spectatorInventoryClear","spectatorReturnInventory","spectatorChatMode","seeSpectatorInvisible","spectatorSetOnJoin","spectatorRemoveOnQuit","spectatorRemoveOnTeleportWorld","spectatorSetOnFirstJoin"};
	@Override
	public boolean onCommand(CommandSender sender, Command command, String paramString, String[] args) {
		if(sender instanceof Player){
			SpectatorUtil.removeSpectator((Player)sender);
			return true;
		}
		return false;
	}
}