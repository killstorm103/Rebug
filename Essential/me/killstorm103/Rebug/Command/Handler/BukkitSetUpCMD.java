package me.killstorm103.Rebug.Command.Handler;

import java.util.logging.Level;
import me.killstorm103.Rebug.RebugPlugin;
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
				RebugPlugin.getINSTANCE().getCommand(QuickConfig.MainLabel()).setExecutor(RebugPlugin.getINSTANCE());
				RebugPlugin.getINSTANCE().getCommand(QuickConfig.MainLabel()).setTabCompleter(RebugPlugin.getINSTANCE());
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
			for (me.killstorm103.Rebug.Command.Command cmds : RebugPlugin.getINSTANCE().getCommands())
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
							
							if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
								RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName());
								
							if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
							{
								if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
									RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
								
								String sub = "";
								for (int i = 0; i < cmds.SubAliases().length; i ++)
								{
									sub = cmds.SubAliases()[i];
									if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
										RebugPlugin.getINSTANCE().Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
									
									try
									{
										RebugPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
										RebugPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
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
						
						for (me.killstorm103.Rebug.Command.Command cmds : RebugPlugin.getINSTANCE().getCommands())
						{
							if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
								RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName());
								
							if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
							{
								if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
									RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
								
								String sub = "";
								for (int i = 0; i < cmds.SubAliases().length; i ++)
								{
									sub = cmds.SubAliases()[i];
									if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
										RebugPlugin.getINSTANCE().Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
									
									try
									{
										RebugPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
										RebugPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
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
			for (me.killstorm103.Rebug.Command.Command cmds : RebugPlugin.getINSTANCE().getCommands())
			{
				if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
					RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName());
					
				if (cmds != null && !PT.isStringNull(cmds.SubAliases()) && RebugPlugin.getINSTANCE().getConfig().getBoolean("command.commands." + cmds.getName().toLowerCase() + ".shortcut-enabled", false))
				{
					if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Command: " + cmds.getName() + " Made it past Null Check");
					
					String sub = "";
					for (int i = 0; i < cmds.SubAliases().length; i ++)
					{
						sub = cmds.SubAliases()[i].trim();
						if (RebugPlugin.getINSTANCE().getConfig().getBoolean("command.settings.debug.onEnable-sub-commands", false))
							RebugPlugin.getINSTANCE().Log(Level.WARNING, cmds.getName() + "'s SubCMD: " + sub);
						
						try
						{
							RebugPlugin.getINSTANCE().getCommand(sub.toString()).setExecutor(new ShortCutBasic());
							RebugPlugin.getINSTANCE().getCommand(sub.toString()).setTabCompleter(new ShortCutBasic());
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
