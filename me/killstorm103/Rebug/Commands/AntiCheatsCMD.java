package me.killstorm103.Rebug.Commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;

public class AntiCheatsCMD extends Command
{
	@Override
	public String getName() {
		return "anticheatslist";
	}

	@Override
	public String getSyntax() {
		return "anticheatslist | anticheatslist <reset/reload>";
	}

	@Override
	public String getDescription() {
		return "tries to get a list of anticheats on the server!";
	}

	@Override
	public String getPermission() {
		return StartOfPermission() + "anticheatslist";
	}

	@Override
	public String[] SubAliases()
	{
		String[] s = {"/anticheatslist"};
		return s;
	}

	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (args.length >= 2 && (args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("reload") || args[1].equalsIgnoreCase("update")))
			Rebug.anticheats.clear();
		
		if (Rebug.anticheats.isEmpty())
		{
			boolean Found = false, FoundCMD = false;
			sender.sendMessage(Rebug.RebugMessage + "Trying to load AntiCheats list!");
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
			{
				if (plugin.isEnabled())
				{
					if (!Rebug.anticheats.containsKey(plugin.getName()))
					{
						String name = plugin.getName().toLowerCase(), description = null, cmd = null, whole_command_key = null, whole_command_obj = null, version = "Unknown Version", author = "Unknown", checker;
						Found = (name.startsWith("anticheat") || name.startsWith("anti-cheat") || name.startsWith("ac") || name.endsWith("anticheat") || name.endsWith("anti-cheat") || name.endsWith("ac"));
						if (plugin.getDescription() != null)
						{
							if (plugin.getDescription().getAuthors() != null && plugin.getDescription().getAuthors().size() > 0)
							{
								int size = plugin.getDescription().getAuthors().size();
								if (size > 1)
								{
									author = "[";
									for (int i = 0; i < plugin.getDescription().getAuthors().size(); i ++)
									{
										author += plugin.getDescription().getAuthors().get(i) + ", ";
									}
									author += "]";
									author = author.replace(", ]", "]").replace("[[", "[").replace("]]", "]");
								}
								else
									author = plugin.getDescription().getAuthors().get(0);
							}
							description = plugin.getDescription().getDescription() != null ? plugin.getDescription().getDescription().toLowerCase() : description;
							version = plugin.getDescription().getVersion() != null && plugin.getDescription().getVersion().length() > 0 ? plugin.getDescription().getVersion() : version;
						}
						// Value patches
						if (!Found)
						{
							checker = author.toLowerCase();
							Found = (name.startsWith("horizon") && checker.contains("mrcraftgoo"));
						}
						
						if (!Found && description != null && description.length() >= 1)
							Found = (description.matches("anticheat") || description.matches("anti-cheat") || description.matches("ac") || description.contains("anticheat") || description.contains("anti-cheat"));
						
						if (!Found && plugin.getDescription() != null && plugin.getDescription().getCommands() != null && plugin.getDescription().getCommands().size() > 0)
						{
							// key = command name, value = {info about the command}
							for (Map.Entry<String, Map<String, Object>> map : plugin.getDescription().getCommands().entrySet())
							{
								cmd = map.getKey().toLowerCase();
								if (cmd != null && cmd.length() > 0 && !name.equals("rebug"))
									FoundCMD = (cmd.startsWith("alerts") || cmd.startsWith("notify") || cmd.startsWith("ac") || cmd.startsWith("anticheat") || cmd.startsWith("anti-cheat") || cmd.startsWith("anti_cheat"));
								
								if (!FoundCMD)
								{
									for (Map.Entry<String, Object> hole : map.getValue().entrySet())
									{
										whole_command_key = hole.getKey().toLowerCase();
										if (whole_command_key.matches("permission") || whole_command_key.matches("permissions"))
										{
											whole_command_obj = hole.getValue().toString();
											FoundCMD = (whole_command_obj.matches("anticheat.checks.bypass") || whole_command_obj.matches("anticheat.checks.bypass.*") || whole_command_obj.matches("anticheat.bypass") || whole_command_obj.matches("ac.checks.bypass") || whole_command_obj.matches("ac.checks.bypass.*") || whole_command_obj.matches("ac.bypass"));
										}
										if (FoundCMD)
										{
											Rebug.anticheats.put(plugin.getName(), version + " By " + author);
											FoundCMD = false;
										}
									}
								}
								else
								{
									Rebug.anticheats.put(plugin.getName(), version + " By " + author);
									FoundCMD = false;
								}
							}
						}
						
						if (Found && !FoundCMD)
						{
							Rebug.anticheats.put(plugin.getName(), version + " By " + author);
							Found = false;
						}
					}
				}
			}
		}
		if (Rebug.anticheats.isEmpty())
		{
			sender.sendMessage(Rebug.RebugMessage + "No AntiCheats Found!");
			return;
		}
		sender.sendMessage(Rebug.RebugMessage + "AntiCheat's:");
		for (Map.Entry<String, String> map : Rebug.anticheats.entrySet())
		{
			String acs = map.getKey() + " " + map.getValue();
			acs = acs.length() >= 256 ? acs.replace(" " + map.getValue(), "info was to long!") : acs;
			acs = acs.length() >= 256 ? acs.replace("info was to long!", "") : acs;
			sender.sendMessage(Rebug.RebugMessage + acs);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return false;
	}

	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public boolean hasCommandCoolDown() {
		return false;
	}
	
}
