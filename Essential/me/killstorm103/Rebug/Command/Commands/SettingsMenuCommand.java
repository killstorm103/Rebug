package me.killstorm103.Rebug.Command.Commands;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Command.Command;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;

public class SettingsMenuCommand extends Command
{
	@Override
	public @NotNull String getName() {
		return "settings";
	}
	@Override
	public @NotNull String getUsage ()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".usage", "settings | settings <setting name> <setting value>");
	}
	@Override
	public @NotNull String getDescription () 
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".description", "Player and Rebug Settings");
	}
	@Override
	public @NotNull String getPermission()
	{
		return RebugPlugin.getINSTANCE().getConfig().getString("command.commands." + getName().toLowerCase() + ".permission", StartOfPermission + "player_settings");
	}

	@Override
	public String[] SubAliases() {
		return new String[] {"settings"};
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception 
	{
		User user = RebugPlugin.getUser((Player) sender);
		if (user == null)
		{
			sender.sendMessage(PT.RebugsUserWasNullErrorMessage("Settings menu"));
			return;
		}
		if (args.length >= 2)
		{
			if (args[1].equalsIgnoreCase("rebug") || args[1].equalsIgnoreCase("rebugsettings") || args[1].equalsIgnoreCase("rebug settings"))
			{
				if (PT.hasAdminPerms(user.getPlayer())) 
				{
					if (args.length >= 3)
					{
					}
					else
						user.getPlayer().openInventory(ItemsAndMenusUtils.getINSTANCE().getRebugSettings());
				}
				else
					user.sendMessage("You don't have Permission to access that!");
				
				return;
			}
			String Setting = args[1].replace("_", " ").replace("%", " ");
			String type = user.getSettingsList().getOrDefault(Setting, "Unknown");
			if (args.length < 3)
			{
				if (type.equalsIgnoreCase("Unknown")) 
					user.sendMessage("Unknown Setting");
				else
				{
					String text = "";
					switch (type.toLowerCase())
					{
					case "boolean":
						text = "true or false";
						break;
						
					case "integer":
						text = "number (1, 2, 5, 22)";
						break;
						
					case "double":
						text = "number (1.5, .2, .5, 2.2)";
						break;

					default:
						break;
					}
					user.sendMessage("Type<" + type + ">: " + text);
				}
				return;
			}
			String Value = args[2];
			boolean changed_value = false;
			switch (type.toLowerCase())
			{
			case "boolean":
				try
				{
					user.getSettingsBooleans().put(Setting, Boolean.parseBoolean(Value));
					changed_value = true;
				}
				catch (Exception e) {}
				break;
				
			case "integer":
				try
				{
					user.getSettingsIntegers().put(Setting, Integer.parseInt(Value));
					changed_value = true;
				}
				catch (Exception e) {}
				break;
				
			case "double":
				try
				{
					user.getSettingsDoubles().put(Setting, Double.parseDouble(Value));
					changed_value = true;
				}
				catch (Exception e) {}
				break;

			default:
				return;
			}
			user.sendMessage(changed_value ? "Changed " + Setting + "'s value to: " + Value : "Failed to change " + Setting + "'s value to: " + Value);
			return;
		}
		
		user.getPlayer().openInventory(user.getSettingsMenu());
	}

	@Override
	public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		return null;
	}

	@Override
	public boolean HasCustomTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args,
			String alias) 
	{
		return false;
	}

	@Override
	public boolean HideFromCommandsList(CommandSender sender) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (PT.hasPermission(player, getPermission()))
				return false;
				
			return true;
		}
		
		return true;
	}
	@Override
	public @NotNull SenderType getType() {
		return SenderType.Player;
	}
}
