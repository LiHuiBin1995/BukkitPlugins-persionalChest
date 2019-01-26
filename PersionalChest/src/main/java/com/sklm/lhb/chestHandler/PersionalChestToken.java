package com.sklm.lhb.chestHandler;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sklm.lhb.persionalChest.PersionalChest;

public class PersionalChestToken {

	/**
	 * 添加物品口令
	 * @param player    口令所有者
	 * @param block     要添加的口令的方块
	 */
	@SuppressWarnings("unchecked")
	public void addToken(Player player, Block block) {
		if(block.getState().getType()==Material.CHEST) {
			List<MetadataValue> metaList = player.getMetadata("persionalChest");
			String persionalChestMsg = null;
			if(metaList.size() == 1) {
				persionalChestMsg = metaList.get(0).asString();
				JSONParser parse = new JSONParser();
				try {		
					JSONArray jsonArray = (JSONArray) parse.parse(persionalChestMsg);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("holder", player.getName());
					jsonObject.put("world", block.getLocation().getWorld().getName());
					jsonObject.put("x", block.getLocation().getBlockX());
					jsonObject.put("y", block.getLocation().getBlockY());
					jsonObject.put("z", block.getLocation().getBlockZ());
					jsonArray.add(jsonObject);
					player.removeMetadata("persionalChest", PersionalChest.instance);
					player.setMetadata("persionalChest", new FixedMetadataValue(PersionalChest.instance, jsonArray.toJSONString()));	
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else if(metaList.size() == 0) {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("holder", player.getName());
				jsonObject.put("world", block.getLocation().getWorld().getName());
				jsonObject.put("x", block.getLocation().getBlockX());
				jsonObject.put("y", block.getLocation().getBlockY());
				jsonObject.put("z", block.getLocation().getBlockZ());
				jsonArray.add(jsonObject);
				player.removeMetadata("persionalChest", PersionalChest.instance);
				player.setMetadata("persionalChest", new FixedMetadataValue(PersionalChest.instance, jsonArray.toJSONString()));
			}
		}
	}
	
	/**
	 * 敲碎物品时移除口令，如果该玩家没有权限敲碎该物品，则口令不会移除
	 * @param player    敲碎物品的玩家
	 * @param block     敲碎的物品
	 * @param event     敲碎物品检测事件
	 * @return 如果口令移除成功返回true,否则返回false
	 */
	@SuppressWarnings("unchecked")
	public boolean removeToken(Player player, Block block) {
		boolean flag = true;
		if(block.getState().getType() == Material.CHEST) {
			Chest persionalChest = (Chest) block.getState();
			ItemStack[] itemAll = persionalChest.getBlockInventory().getContents();
			for (ItemStack itemStack : itemAll) {
				if(itemStack != null && itemStack.getType() != Material.AIR) {
					flag = false;
				}
			}
			List<MetadataValue> metaList = player.getMetadata("persionalChest");
			if(metaList != null && metaList.size() == 1 && !metaList.get(0).asString().equals("[]") && flag==true) {
				JSONParser parse = new JSONParser();
				try {
					JSONArray jsonArray = (JSONArray) parse.parse(metaList.get(0).asString());
					JSONObject blockJson = new JSONObject();
					blockJson.put("holder", player.getName());
					blockJson.put("world", block.getLocation().getWorld().getName());
					blockJson.put("x", block.getLocation().getBlockX());
					blockJson.put("y", block.getLocation().getBlockY());
					blockJson.put("z", block.getLocation().getBlockZ());
					if(jsonArray.toJSONString().contains(blockJson.toJSONString())) {
						String jsonStr = jsonArray.toJSONString().replace(blockJson.toJSONString(), "").trim();
						if(jsonStr.contains("},,{")) {
							jsonStr = jsonStr.replace("},,{", "},{");
						}else if(jsonStr.contains("[,{")) {
							jsonStr = jsonStr.replace("[,{", "[{");
						}else if(jsonStr.contains("},]")) {
							jsonStr = jsonStr.replace("},]", "}]");
						}
						player.removeMetadata("persionalChest", PersionalChest.instance);
						player.setMetadata("persionalChest", new FixedMetadataValue(PersionalChest.instance, jsonStr));
					}else {
						flag = false;
					}
				} catch (ParseException e) {
					flag = false;
					e.printStackTrace();
				}
			}else {
				flag = false;
				player.sendMessage(PersionalChest.languageSection.getString("persionalChest.hasItem"));
			}
		}
		return flag;
	}
	
	/**
	 * 检测口令是否正确，如果正确可以操作方块，否则无法操作方块
	 * @param player    要操作方块的玩家
	 * @param block     要操作的方块
	 * @return  如果口令正确返回true,否则返回false
	 */
	@SuppressWarnings("unchecked")
	public boolean checkToken(Player player, Block block) {
		boolean flag = true;
		if(block.getState().getType() == Material.CHEST) {
			List<MetadataValue> metaList = player.getMetadata("persionalChest");
			if(metaList !=null && metaList.size()==1 && !metaList.get(0).asString().equals("[]")) {
				JSONParser parse = new JSONParser();
				try {
					JSONArray jsonArray = (JSONArray) parse.parse(metaList.get(0).asString());
					JSONObject blockJson = new JSONObject();
					blockJson.put("holder", player.getName());
					blockJson.put("world", block.getLocation().getWorld().getName());
					blockJson.put("x", block.getLocation().getBlockX());
					blockJson.put("y", block.getLocation().getBlockY());
					blockJson.put("z", block.getLocation().getBlockZ());
					if(!jsonArray.toJSONString().contains(blockJson.toJSONString())) {
						flag = false;
					}
				} catch (ParseException e) {
					flag = false;
					e.printStackTrace();
				}
			}else {
				flag = false;
			}
		}
		return flag;
	}
}
