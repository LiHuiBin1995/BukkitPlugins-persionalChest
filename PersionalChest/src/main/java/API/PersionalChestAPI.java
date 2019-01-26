package API;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sklm.lhb.chestHandler.PersionalChestToken;
import com.sklm.lhb.persionalChest.PersionalChest;

public class PersionalChestAPI {

	/**
	 * 是否具有操作物品的权限
	 * @param player     玩家
	 * @param block      要操作的物品(此物品应该是被放在指定世界的指定位置)
	 * @return   如果有操作该物品的权限则返回true，否则返回false
	 */
	public static boolean isTokenPass(Player player, Block block) {
		PersionalChestToken token = new PersionalChestToken();
		return token.checkToken(player, block);
	}
	
	/**
	 * 添加玩家操作block物品的权限
	 * @param player   玩家
	 * @param block    要操作的物品(此物品应该是被放在指定世界的指定位置)
	 */
	public static void addToken(Player player, Block block) {
		PersionalChestToken token = new PersionalChestToken();
		token.addToken(player, block);
	}
	
	/**
	 * 移除玩家操作block物品的权限
	 * @param player      玩家
	 * @param block       要操作的物品(此物品应该是被放在指定世界的指定位置)
	 * @return  如果该物品的权限移除成功则返回true，否则返回false
	 */
	@SuppressWarnings("unchecked")
	public static boolean removeToken(Player player, Block block) {
		boolean flag = true;
		List<MetadataValue> metaList = player.getMetadata("persionalChest");
		if(metaList != null && metaList.size() == 1 && !metaList.get(0).asString().equals("[]")) {
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
		}
		return flag;
	}
}
