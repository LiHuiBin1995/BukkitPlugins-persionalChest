package com.sklm.lhb.chestHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sklm.lhb.persionalChest.PersionalChest;
public class ChestActionHandler implements Listener {
	
	@EventHandler
	public void playerPlaceChest(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		PersionalChestToken token = new PersionalChestToken();
		Location location = event.getBlockReplacedState().getLocation();
		boolean b = token.checkPositionRoundChestHolder(player, location);
		if(b==true || player.isOp()==true) {
			token.addToken(player, block);	
		}else {
			player.sendMessage(PersionalChest.languageSection.getString("persionalChest.createDoubleChestError"));
			event.setBuild(false);
			return ;
		}
		
	}
	
	@EventHandler
	public void playerBreakChest(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		PersionalChestToken token = new PersionalChestToken();
		boolean correct = token.removeToken(player, block);
		if(!correct && !player.isOp()) {
			event.setCancelled(true);
			player.sendMessage(PersionalChest.languageSection.getString("persionalChest.permission"));
		}
	}
	
	@EventHandler
	public void playerOpenChest(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			PersionalChestToken token = new PersionalChestToken();
			boolean correct = token.checkToken(player, block);
			boolean hasPermission = token.hasPermissionOfDoubleChest(player, block);
			if((!correct && !player.isOp()) || hasPermission==false) {
				if(!player.isOp()) {
					event.setCancelled(true);
					player.sendMessage(PersionalChest.languageSection.getString("persionalChest.permission"));
				}
			}
		}
	}
	
	
	
}
