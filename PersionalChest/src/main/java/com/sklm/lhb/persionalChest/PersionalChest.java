package com.sklm.lhb.persionalChest;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.sklm.lhb.chestHandler.ChestActionHandler;
import com.sklm.lhb.chestHandler.PlayerActionHandler;
import com.sklm.lhb.cmd.PersionalChestCmd;
import com.sklm.lhb.json.JsonFileUtil;

public class PersionalChest extends JavaPlugin {

	private static final String PATH = System.getProperty("user.dir")+"\\plugins\\persionalChest\\";
	public static PersionalChest instance = null;
	public static ConfigurationSection languageSection = null;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new ChestActionHandler(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerActionHandler(), this);
		this.getCommand("sk_chest").setExecutor(new PersionalChestCmd());
		try {
			languageSection = readLanguage();
		} catch (Exception e) {
			System.err.println("语言配置文件读取失败");
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		@SuppressWarnings("unchecked")
		Collection<Player> onlinePlayer = (Collection<Player>) Bukkit.getOnlinePlayers();
		for (Player player : onlinePlayer) {
			List<MetadataValue> metaList = player.getMetadata("persionalChest");
			if(metaList.size() == 1) {
				JsonFileUtil.createJsonFile(metaList.get(0).asString(), PersionalChest.getPath()+"player\\", player.getName());
			}
		}
	}
	
	public static String getPath() {
		return PersionalChest.PATH;
	}
	
	/**
	 * 读取语言配置文件
	 * @return
	 */
	public ConfigurationSection readLanguage() throws Exception {
		File languageFile = new File(PersionalChest.getPath()+"language.yml");
		if(!languageFile.exists()) {
			PersionalChest.instance.saveResource("language.yml", false);
		}
		
		YamlConfiguration yaml = new YamlConfiguration();
		yaml.load(languageFile);
		return yaml.getConfigurationSection("root");
	}
}
