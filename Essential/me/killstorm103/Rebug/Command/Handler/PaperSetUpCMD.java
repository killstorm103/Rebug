package me.killstorm103.Rebug.Command.Handler;

import java.util.logging.Level;

import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.QuickConfig;

public class PaperSetUpCMD 
{
	public static void setup (boolean main, int num, long sleep)
	{
		if (main)
		{
			try
			{
			    RebugPlugin.getINSTANCE().getLifecycleManager().registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, Paper ->
				{
					Paper.registrar().register(QuickConfig.MainLabel(), new me.killstorm103.Rebug.Command.Handler.Paper.PaperShortCutBasic());
				});
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
			RebugPlugin.getINSTANCE().getLifecycleManager().registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, Paper ->
			{
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
											if (PT.isStringNull(sub))
												RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + "'s sub was Null");
											else
												Paper.registrar().register(sub, new me.killstorm103.Rebug.Command.Handler.Paper.PaperShortCutBasic());
										} 
										catch (Exception e) 
										{
											e.printStackTrace();
											RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + " Errored with sub: " + sub + (PT.isStringNull(sub) ? "" : " length: " + sub.length()));
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
			});
			break;
			
		case 2:
			RebugPlugin.getINSTANCE().getLifecycleManager().registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, Paper ->
			{
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
											if (PT.isStringNull(sub))
												RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + "'s sub was Null");
											else
											{
												Paper.registrar().register(sub, new me.killstorm103.Rebug.Command.Handler.Paper.PaperShortCutBasic());
											}
										} 
										catch (Exception e) 
										{
											e.printStackTrace();
											RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + " Errored with sub: " + sub + (PT.isStringNull(sub) ? "" : " length: " + sub.length()));
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
			});
			break;
			
		case 3:
			RebugPlugin.getINSTANCE().getLifecycleManager().registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, Paper ->
			{
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
								if (PT.isStringNull(sub))
									RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + "'s sub was Null");
								else
								{
									Paper.registrar().register(sub, new me.killstorm103.Rebug.Command.Handler.Paper.PaperShortCutBasic());
								}
							}
							catch (Exception e) 
							{
								e.printStackTrace();
								RebugPlugin.getINSTANCE().Log(Level.SEVERE, cmds.getName() + " Errored with sub: " + sub + (PT.isStringNull(sub) ? "" : " length: " + sub.length()));
							}
						}
					}
				}
			});
			break;

		default:
			break;
		}
	}
}
