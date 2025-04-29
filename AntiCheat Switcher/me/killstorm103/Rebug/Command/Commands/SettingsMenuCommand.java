package me.killstorm103.Rebug.Command.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;

@SuppressWarnings("deprecation")
public class SettingsMenuCommand extends Command
{
	@Override
	public @NotNull String getName ()
	{
		return "settings";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "settings | settings <setting name> <setting value>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "Player and Rebug Settings");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "player_settings");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"settings"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		User user = RebugsAntiCheatSwitcherPlugin.getUser((Player) sender);
		if (user == null)
		{
			sender.sendMessage(PT.RebugsUserWasNullErrorMessage("Settings menu"));
			return;
		}
		if (args.length >= 2)
		{
			int number = command.equalsIgnoreCase(QuickConfig.MainLabel()) ? 1 : 0, Failed = 0;
			String Setting = args[number + 1].replace("_", " ").replace("%", " "), type = null, Value = null, ReturnValue = null;
			double value = 0, max = 1, min = 0;
			if (args[number].equalsIgnoreCase("rebug"))
			{
				if (PT.hasAdminPerms(user.getPlayer())) 
				{
					if (args.length >= 3)
					{
						type = RebugsAntiCheatSwitcherPlugin.getSettingsList().getOrDefault(Setting, "Unknown");
						if (args.length < number + 3 && !type.equalsIgnoreCase("action"))
						{
							user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type + " and it's Value is Missing!");
							return;
						}
						Value = type.equalsIgnoreCase("action") ? "" : args[number + 2];
						try
						{
							switch (type.toLowerCase())
							{
							case "boolean":
								if (Value.equalsIgnoreCase("true") || Value.equalsIgnoreCase("false"))
								{
									RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().put(Setting, Boolean.parseBoolean(Value));
									ReturnValue = RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().get(Setting) + "";
								}
								else 
									Failed = 1;
								break;
								
							case "integer":
								value = Integer.parseInt(Value);
								max = FileUtils.getMenusConfig().getInt("Rebug Settings." + Setting + ".max-value", 5);
								min = FileUtils.getMenusConfig().getInt("Rebug Settings." + Setting + ".min-value", 1);
								if (value > max)
								{
									user.sendMessage(Setting + "'s Max Value is: " + max);
									value = max;
								}
								else if (value < min)
								{
									user.sendMessage(Setting + "'s Min Value is: " + min);
									value = min;
								}
								RebugsAntiCheatSwitcherPlugin.getSettingsIntegers().put(Setting, (int) value);
								ReturnValue = RebugsAntiCheatSwitcherPlugin.getSettingsIntegers().get(Setting) + "";
								break;
								
							case "double":
								value = Double.parseDouble(Value);
								max = FileUtils.getMenusConfig().getDouble("Rebug Settings." + Setting + ".max-value", 5);
								min = FileUtils.getMenusConfig().getDouble("Rebug Settings." + Setting + ".min-value", 1);
								if (value > max)
								{
									user.sendMessage(Setting + "'s Max Value is: " + max);
									value = max;
								}
								else if (value < min)
								{
									user.sendMessage(Setting + "'s Min Value is: " + min);
									value = min;
								}
								RebugsAntiCheatSwitcherPlugin.getSettingsDoubles().put(Setting, value);
								ReturnValue = RebugsAntiCheatSwitcherPlugin.getSettingsDoubles().get(Setting) + "";
								break;
								
							case "action":
								Failed = 2;
								switch (Setting.toLowerCase()) 
								{
								case "op/deop":
									user.getPlayer().setOp(!user.getPlayer().isOp());
									user.sendMessage("you are " + (user.getPlayer().isOp() ? "now " + ChatColor.GREEN + "Opped" : "no longer " + ChatColor.DARK_RED + "Op"));
									break;
									
								case "reload config":
									RebugsAntiCheatSwitcherPlugin.getINSTANCE().Reload_Configs(user.getPlayer());
									break;

								default:
									Failed = 3;
									break;
								}
								break;

							default:
								user.sendMessage(Setting + " is Unknown Setting!");
								return;
							}
						}
						catch (Exception e) 
						{
							user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type);
							return;
						}
						if (Failed > 1)
						{
							user.sendMessage(Failed == 3 ? Setting + " is a Unknown Action!" : "Executod Action: " + Setting);
							return;
						}
						if (Failed == 1)
						{
							user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type);
							return;
						}
						user.sendMessage("Changed " + Setting + "'s value to: " + ReturnValue);
						ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(ItemsAndMenusUtils.getINSTANCE().getRebugSettings(), FileUtils.getMenusConfig().getInt("Rebug Settings." + Setting + ".item.slot", -Integer.MAX_VALUE), 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + (type.equalsIgnoreCase("boolean") ? ItemsAndMenusUtils.getINSTANCE().getValues(Boolean.parseBoolean(ReturnValue)) : ReturnValue));
						return;
					}
					else
					{
						if (!FileUtils.getPlayerSettingsConfig().getBoolean("Rebug Settings.enabled", false))
						{
							user.sendMessage("Rebug Settings Menu is Disabled!");
							return;
						}
						user.getPlayer().openInventory(ItemsAndMenusUtils.getINSTANCE().getRebugSettings());
					}
				}
				else
					user.sendMessage("You don't have Permission to access that!");
				
				return;
			}
			type = user.getSettingsList().getOrDefault(Setting, "Unknown");
			if (args.length < number + 3)
			{
				user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type + " and it's Value is Missing!");
				return;
			}
			if (!PT.isStringNull(FileUtils.getPlayerSettingsConfig().getString(Setting + ".permission", "")) && !PT.hasPermission (user, FileUtils.getPlayerSettingsConfig().getString(Setting + ".permission")))
			{
				user.sendMessage("You don't have permission to use the Setting: " + Setting + "!");
				return;
			}
			Value = args[number + 2];
			try
			{
				switch (type.toLowerCase())
				{
				case "boolean":
					if (Value.equalsIgnoreCase("true") || Value.equalsIgnoreCase("false"))
					{
						user.getSettingsBooleans().put(Setting, Boolean.parseBoolean(Value));
						ReturnValue = user.getSettingsBooleans().get(Setting) + "";
					}
					else 
						Failed = 1;
					break;
					
				case "integer":
					value = Integer.parseInt(Value);
					max = FileUtils.getPlayerSettingsConfig().getInt(Setting + ".max-value", 5);
					min = FileUtils.getPlayerSettingsConfig().getInt(Setting + ".min-value", 1);
					if (value > max)
					{
						user.sendMessage(Setting + "'s Max Value is: " + max);
						value = max;
					}
					else if (value < min)
					{
						user.sendMessage(Setting + "'s Min Value is: " + min);
						value = min;
					}
					user.getSettingsIntegers().put(Setting, (int) value);
					ReturnValue = user.getSettingsIntegers().get(Setting) + "";
					break;
					
				case "double":
					value = Double.parseDouble(Value);
					max = FileUtils.getPlayerSettingsConfig().getDouble(Setting + ".max-value", 5);
					min = FileUtils.getPlayerSettingsConfig().getDouble(Setting + ".min-value", 1);
					if (value > max)
					{
						user.sendMessage(Setting + "'s Max Value is: " + max);
						value = max;
					}
					else if (value < min)
					{
						user.sendMessage(Setting + "'s Min Value is: " + min);
						value = min;
					}
					user.getSettingsDoubles().put(Setting, value);
					ReturnValue = user.getSettingsDoubles().get(Setting) + "";
					break;

				default:
					user.sendMessage(Setting + " is Unknown Setting!");
					return;
				}
			}
			catch (Exception e) 
			{
				user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type);
				return;
			}
			if (Failed == 1)
			{
				user.sendMessage("Failed to change " + Setting + "'s Value " + Setting + "'s Type is: " + type);
				return;
			}
			user.sendMessage("Changed " + Setting + "'s value to: " + ReturnValue);
			ItemsAndMenusUtils.getINSTANCE().UpdateMenuValueChangeLore(user.getSettingsMenu(), FileUtils.getPlayerSettingsConfig().getInt(Setting + ".item.slot", -Integer.MAX_VALUE), 0, ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + (type.equalsIgnoreCase("boolean") ? ItemsAndMenusUtils.getINSTANCE().getValues(Boolean.parseBoolean(ReturnValue)) : ReturnValue));
			return;
		}
		user.getPlayer().openInventory(user.getSettingsMenu());
	}

	@Override
	public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		ArrayList<String> list = new ArrayList<>();
		int number = alias.equalsIgnoreCase(QuickConfig.MainLabel()) ? 1 : 0;;
		if (args.length < number + 2)
		{
			list.add("user");
			list.add("rebug");
			return StringUtil.copyPartialMatches(args[args.length - 1], list, new ArrayList<>());
		}
		if (args.length >= 2 && args.length < (2 + number + 1))
		{
			if (args[number].equalsIgnoreCase("rebug"))
			{
				FileUtils.getMenusConfig().getConfigurationSection("Rebug Settings").getKeys(false).forEach(key -> 
				{
					if (key.equalsIgnoreCase("menu")) return;
					
					list.add(key.replace(" ", "_"));
				});
				return StringUtil.copyPartialMatches(args[args.length - 1], list, new ArrayList<>());
			}
			else if (args[number].equalsIgnoreCase("user"))
			{
				User user = RebugsAntiCheatSwitcherPlugin.getUser((Player) sender);
				if (user == null) return null;
				
				for (Map.Entry<String, String> map : user.getSettingsList().entrySet())
				{
					list.add(map.getKey().replace(" ", "_"));
				}
				return StringUtil.copyPartialMatches(args[args.length - 1], list, new ArrayList<>());
			}
		}
		return null; 
	}
	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args, String alias)
	{
		return args.length > 0;
	}
	@Override
	public @NotNull SenderType getType() {
		return SenderType.Player;
	}
}
