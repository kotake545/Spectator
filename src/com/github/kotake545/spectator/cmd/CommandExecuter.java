package com.github.kotake545.spectator.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandExecuter implements CommandExecutor {
	@Override
	abstract public boolean onCommand(CommandSender sender, Command command, String paramString,String[] args);

}