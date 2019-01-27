package com.sklm.lhb.chestHandler;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.json.simple.JSONArray;
import com.sklm.lhb.json.JsonFileUtil;
import com.sklm.lhb.persionalChest.PersionalChest;

public class PlayerActionHandler implements Listener {

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		JSONArray jsonArray = JsonFileUtil.readJsonToArray(PersionalChest.getPath()+"player\\", player.getName(),PersionalChest.CHARACTERSET);
		if(jsonArray != null) {
			player.setMetadata("persionalChest", new FixedMetadataValue(PersionalChest.instance, jsonArray.toJSONString()));
		}
	}
	
	@EventHandler
	public void playerQueryGame(PlayerQuitEvent event) {	
		Player player = event.getPlayer();
		List<MetadataValue> metaList = player.getMetadata("persionalChest");
		if(metaList.size() == 1) {
			JsonFileUtil.createJsonFile(metaList.get(0).asString(), PersionalChest.getPath()+"player\\", player.getName(),PersionalChest.CHARACTERSET);
		}	
	}
	
	
}
