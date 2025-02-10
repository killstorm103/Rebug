package me.killstorm103.Rebug.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import me.killstorm103.Rebug.RebugPlugin;

public class FileUtils 
{
	public static void createCustomConfig(String con)
	{
		if (PT.isStringNull(con))
			return;

		switch (con.toLowerCase()) 
		{
		case "server":
			ServerFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "Server.yml");
			if (!ServerFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				ServerFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("Server.yml", false);
			}
			ServerConfig = YamlConfiguration.loadConfiguration(ServerFile);
			break;

		case "config":
			ConfigFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "config.yml");
			if (!ConfigFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				ConfigFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("config.yml", false);
			}
			YamlConfiguration.loadConfiguration(ConfigFile);
			break;

		case "player settings":
			PlayerSettingsFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "Player Settings.yml");
			if (!PlayerSettingsFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				PlayerSettingsFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("Player Settings.yml", false);
			}

			PlayerSettingsConfig = YamlConfiguration.loadConfiguration(PlayerSettingsFile);
			break;

		case "loaded items":
			LoadedItemsConfigFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "Items.yml");
			if (!LoadedItemsConfigFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				LoadedItemsConfigFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("Items.yml", false);
			}
			ItemsConfig = YamlConfiguration.loadConfiguration(LoadedItemsConfigFile);
			break;
			
		case "menus":
			MenusConfigFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "Menus.yml");
			if (!MenusConfigFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				MenusConfigFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("Menus.yml", false);
			}
			MenusConfig = YamlConfiguration.loadConfiguration(MenusConfigFile);
			break;

		case "loaded anticheats":
			LoadedAntiCheatsConfigFile = new File(RebugPlugin.getINSTANCE().getDataFolder(), "Loaded AntiCheats.yml");
			if (!LoadedAntiCheatsConfigFile.exists()) 
			{
				RebugPlugin.getINSTANCE().Log(Level.INFO, "Generating " + con + ".yml file!");
				LoadedAntiCheatsConfigFile.getParentFile().mkdirs();
				RebugPlugin.getINSTANCE().saveResource("Loaded AntiCheats.yml", false);
			}
			LoadedAntiCheatsFile = YamlConfiguration.loadConfiguration(LoadedAntiCheatsConfigFile);
			break;

		default:
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Unknown Config File!");
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "FileUtils:createCustomConfig");
			break;
		}
	}
	public static void savePlayer(@NotNull Object object) 
	{
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Player in FileUtils:savePlayer was Somehow Null!");
			return;
		}
		YamlConfiguration playerConfig = new YamlConfiguration();
		User user = RebugPlugin.getUser(player);
		if (user == null)
			return;
		
		playerConfig.set("Player Name", player.getName());
		playerConfig.createSection("Player Settings");
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat", user.getAntiCheat());
		playerConfig.getConfigurationSection("Player Settings").set("Amount Of Selected AntiCheats", user.getSelectedAntiCheats());
		playerConfig.getConfigurationSection("Player Settings").set("AntiCheat Number IDs", user.getNumberIDs());
		for (Map.Entry<String, String> list : user.getSettingsList().entrySet())
		{
			switch (list.getValue().toLowerCase())
			{
			case "boolean":
				for (Map.Entry<String, Boolean> setting : user.getSettingsBooleans().entrySet())
					playerConfig.getConfigurationSection("Player Settings").set(setting.getKey(), setting.getValue());
						
				break;
				
			case "double":
				for (Map.Entry<String, Double> setting : user.getSettingsDoubles().entrySet())
					playerConfig.getConfigurationSection("Player Settings").set(setting.getKey(), setting.getValue());
				
				break;
				
			case "integer":
				for (Map.Entry<String, Integer> setting : user.getSettingsIntegers().entrySet())
					playerConfig.getConfigurationSection("Player Settings").set(setting.getKey(), setting.getValue());
				
				break;

			default:
				break;
			}
		}
		
		user.getSettingsBooleans().clear();
		user.getSettingsDoubles().clear();
		user.getSettingsIntegers().clear();
		user.getSettingsList().clear();
		save(player, playerConfig);
	}
	@Getter
	public static FileConfiguration LoadedAntiCheatsFile, ItemsConfig, PlayerSettingsConfig, ServerConfig, MenusConfig;
	public static File folder, LoadedAntiCheatsConfigFile, LoadedItemsConfigFile, ConfigFile, PlayerSettingsFile, ServerFile, MenusConfigFile;
	public static void restorePlayer (@NotNull Object object)
	{
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Player in FileUtils:restorePlayer was Somehow Null!");
			return;
		}
		if (Bukkit.getPlayerExact(player.getName()) == null)
			return;

		FileConfiguration playerConfig = getPlayerConfig(player);
		if (playerConfig == null)
			return;

		User user = RebugPlugin.getUser(player);
		if (user == null)
			return;

		// Player Settings
		for (Map.Entry<String, String> setting : user.getSettingsList().entrySet())
		{
			String name = setting.getKey();
			switch (setting.getValue().toLowerCase())
			{
			case "boolean":
				boolean isEnabled = playerConfig.getConfigurationSection("Player Settings").getBoolean(setting.getKey());
				if (!PT.isStringNull(FileUtils.getPlayerSettingsConfig().getString(name + ".permission", "")) && !PT.hasPermission (user, FileUtils.getPlayerSettingsConfig().getString(name + ".permission")))
				{
					if (isEnabled)
						user.sendMessage("Disabled User Setting: " + name + " because you don't have Permission to use it!");
						
					isEnabled = false;
				}
				user.getSettingsBooleans().put(name, isEnabled);
				if (!isEnabled) break;
				
				switch (name.toLowerCase())
				{
				case "double jump":
					user.getPlayer().setAllowFlight(true);
					break;

				default:
					break;
				}
				break;
				
			case "double":
				user.getSettingsDoubles().put(name, playerConfig.getConfigurationSection("Player Settings").getDouble(setting.getKey()));
				break;
			
			case "integer":	
				user.getSettingsIntegers().put(name, playerConfig.getConfigurationSection("Player Settings").getInt(setting.getKey()));
				break;

			default:
				break;
			}
		}
		user.setNumberIDs("");
		user.setSelectedAntiCheats(playerConfig.getConfigurationSection("Player Settings").getInt("Amount Of Selected AntiCheats"));
		String def = getLoadedAntiCheatsFile().getString("default-anticheat.anticheat", "Vanilla");
		if (user.getSelectedAntiCheats() < 2) 
		{
			user.setAntiCheat(PT.hasAdminPerms(user) || getLoadedAntiCheatsFile().getBoolean("default-anticheat.bypass-permission-enabled", false) && user.hasPermission(getLoadedAntiCheatsFile().getString("default-anticheat.permission", "me.killstorm103.rebug.user.bypass_force_default_anticheat"))
			? playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat", "Vanilla")
			: getLoadedAntiCheatsFile().getBoolean("default-anticheat.force-anticheat", false) ? user.getAntiCheat()
							: playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat", "Vanilla"));
			if (!user.getAntiCheat().equalsIgnoreCase("Vanilla"))
			{
				Plugin plugin = Bukkit.getPluginManager().getPlugin(user.getAntiCheat());
				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + user.getAntiCheat().toLowerCase()) == null
				|| !getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + user.getAntiCheat().toLowerCase() + ".enabled")
				|| plugin == null || !plugin.isEnabled()) 
				{
					user.sendMessage("The AntiCheat " + "(" + user.getAntiCheat() + ")" + " you had Selected was Disabled or No Longer on the Server!");
					user.sendMessage("So We Changed your AntiCheat to: " + (PT.isStringNull(def) ? "Vanilla" : def));
					user.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
				}
			}
		}
		else
		{
			user.setNumberIDs(playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat Number IDs"));
			user.setAntiCheat(playerConfig.getConfigurationSection("Player Settings").getString("AntiCheat"));
			String[] check = PT.SplitString(user.getAntiCheat());
			for (int i = 0; i < check.length; i ++)
			{
				if (PT.isStringNull(check[i]))
				{
					user.setNumberIDs("");
					user.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
					user.sendMessage("a AC String was Null somehow so chaged you to: " + (PT.isStringNull(def) ? "Vanilla" : def));
					break;
				}
				if (check[i].equalsIgnoreCase("Vanilla"))
				{
					user.setNumberIDs("");
					user.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
					user.sendMessage("Your AntiCheat was somehow Mulit while being Vanilla! so fixed that!");
					break;
				}
				Plugin plugin = Bukkit.getPluginManager().getPlugin(check[i]);
				if (getLoadedAntiCheatsFile().get("loaded-anticheats." + check[i].toLowerCase()) == null
						|| !getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + check[i].toLowerCase() + ".enabled")
						|| plugin == null || !plugin.isEnabled())
				{
					user.setAntiCheat(PT.isStringNull(def) ? "Vanilla" : def);
					user.setNumberIDs("");
					user.sendMessage("The AntiCheat " + "(" + check[i] + ")" +  " you had Selected was Disabled or No Longer on the Server!");
					user.sendMessage("So We Changed your AntiCheat to: " + (PT.isStringNull(def) ? "Vanilla" : def));
					break;
				}
			}
		}
		user.setSelectedAntiCheats(user.getAntiCheat().equalsIgnoreCase("Vanilla") || user.getAntiCheat().equalsIgnoreCase(def) ? 0 : user.getSelectedAntiCheats());
		user.setResetColorAC(true);
	}
	public static void initFolder()
	{
		folder = new File(RebugPlugin.getINSTANCE().getDataFolder(), "player-data");
		if (!folder.exists())
		{
			RebugPlugin.getINSTANCE().Log(Level.INFO, "Creating player-data folder!");
			folder.mkdirs();
		}
	}

	private static File getPlayerFile(@NotNull Player player) {
		return new File(folder, player.getUniqueId().toString() + ".yml");
	}
	public static File getConfigFile() {
		return ConfigFile;
	}

	public static FileConfiguration getPlayerConfig(@NotNull Player p)
	{
		File playerFile = getPlayerFile(p);
		if (!playerFile.exists())
			return null;

		YamlConfiguration playerConfig = new YamlConfiguration();
		try {
			playerConfig.load(playerFile);
			return playerConfig;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	private static void save(@NotNull Player p, FileConfiguration config) 
	{
		File playerFile = getPlayerFile(p);
		try {
			config.save(playerFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
