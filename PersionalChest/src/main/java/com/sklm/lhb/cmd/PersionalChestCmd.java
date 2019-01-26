package com.sklm.lhb.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sklm.lhb.persionalChest.PersionalChest;

public class PersionalChestCmd implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length>=1) {
			if(args[0].equalsIgnoreCase("help")) {
				return help(sender, label, args);
			}else if(args[0].equalsIgnoreCase("reload")) {
				return reload(sender, label, args);
			}
		}
		return true;
	}

	/**
	 * 指令帮助
	 *  /sk_chest help
	 * @param sender
	 * @param label
	 * @param args
	 * @return
	 */
	public boolean help(CommandSender sender, String label, String[] args) {
		if(sender.isOp()) {
			StringBuffer helpInfor = new StringBuffer();
			helpInfor.append("§a§l======【私人背包】======\n");
			helpInfor.append("§a§l◆ /sk_chest help §d§l 指令帮助\n");
			helpInfor.append("§a§l◆ /sk_chest reload §d§l 重载指令");
			sender.sendMessage(helpInfor.toString());
			return true;
		}
		return false;
	}
	
	/**
	 *  重载
	 *   /sk_chest reload
	 * @param sender
	 * @param label
	 * @param args
	 * @return
	 */
	public boolean reload(CommandSender sender, String label, String[] args) {
		if(sender.isOp()) {
			List<String> reloadInfor = new ArrayList<String>();
			reloadInfor.add(0,PersionalChest.languageSection.getString("reload.pre_reload"));
			reloadInfor.add(1,PersionalChest.languageSection.getString("reload.success"));
			reloadInfor.add(2,PersionalChest.languageSection.getString("reload.error"));
			sender.sendMessage(reloadInfor.get(0));
			try {
				PersionalChest.languageSection = PersionalChest.instance.readLanguage();
				sender.sendMessage(reloadInfor.get(1));
				return true;
			} catch (Exception e) {
				sender.sendMessage(reloadInfor.get(2));
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
}
