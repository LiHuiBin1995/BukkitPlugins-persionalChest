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
		Location location = event.getBlockReplacedState().getLocation();
		boolean b = checkPositionRoundChestHolder(player, location);
		if(b==true || player.isOp()==true) {
			new PersionalChestToken().addToken(player, block);	
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
			if(!correct && !player.isOp()) {
				event.setCancelled(true);
				player.sendMessage(PersionalChest.languageSection.getString("persionalChest.permission"));
			}
		}
	}
	
	/**
	 * 判断指定位置周围的箱子的所有者是否和要放置的箱子的所有者相同，如果相同可以放置，否则不能放置
	 * @param player     放置箱子的玩家
	 * @param location   要放置箱子的位置
	 * @return
	 */
	public boolean checkPositionRoundChestHolder(Player player, Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		World world = location.getWorld();
		Block leftBlock =  world.getBlockAt(x-1, y, z);
		Block bottomBlock = world.getBlockAt(x, y, z-1);
		Block rightBlock = world.getBlockAt(x+1, y, z);
		Block topBlock = world.getBlockAt(x, y, z+1);
		PersionalChestToken token = new PersionalChestToken();
		if(token.checkToken(player, leftBlock) == false) {
			return false;
		}else if(token.checkToken(player, bottomBlock) == false) {
			return false;
		}else if(token.checkToken(player, rightBlock) == false) {
			return false;
		}else if(token.checkToken(player, topBlock) == false) {
			return false;
		}
		return true;
	}
	
}
