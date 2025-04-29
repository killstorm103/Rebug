package me.killstorm103.Rebug.Command.Handler;

import java.util.logging.Level;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Command.Handler.Bukkit.ShortCutBasic;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;

public class BukkitSetUpCMD 
{
	public static void setup (boolean main, int num, long sleep)
	{
		if (main)
		{
			try
			{
				RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(QuickConfig.MainLabel()).setExecutor(RebugsAntiCheatSwitcherPlugin.getINSTANCE());
				RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(QuickConfig.MainLabel()).setTabCompleter(RebugsAntiCheatSwitcherPlugin.getINSTANCE());
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return;
		}
		switch (num)
		{
		case 1:
			for (me.killstorm103.Rebug.Command.Command cmds : RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommands())
			{
				new Thread()
				{
					@Override
					public void run ()
					{
						try
						{
							if (sleep > 0)
								sleep (sleep);
							
							if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
								RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName());
								
							if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
							{
								if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
									RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
								
								String sub = "";
								for (int i = 0; i < cmds.SubAliases().length; i ++)
								{
									sub = cmds.SubAliases()[i];
									if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
										RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
									
									try
									{
										RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
										RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
									} 
									catch (Exception e) 
									{
										e.printStackTrace();
									}
								}
							}
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
					
				}.start();
			}
			break;
			
		case 2:
			new Thread ()
			{
				@Override
				public void run ()
				{
					try
					{
						if (sleep > 0)
							sleep (sleep);
						
						for (me.killstorm103.Rebug.Command.Command cmds : RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommands())
						{
							if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
								RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName());
								
							if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
							{
								if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
									RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
								
								String sub = "";
								for (int i = 0; i < cmds.SubAliases().length; i ++)
								{
									sub = cmds.SubAliases()[i];
									if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
										RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
									
									try
									{
										RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
										RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
									} 
									catch (Exception e) 
									{
										e.printStackTrace();
									}
								}
							}
						}
					}
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			}.start();
			break;
			
		case 3:
			for (me.killstorm103.Rebug.Command.Command cmds : RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommands())
			{
				if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
					RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName());
					
				if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
				{
					if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
						RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
					
					String sub = "";
					for (int i = 0; i < cmds.SubAliases().length; i ++)
					{
						sub = cmds.SubAliases()[i].trim();
						if (RebugsAntiCheatSwitcherPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
							RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
						
						try
						{
							RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
							RebugsAntiCheatSwitcherPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
			break;

		default:
			break;
		}
	}
}
