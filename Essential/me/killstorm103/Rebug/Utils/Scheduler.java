package me.killstorm103.Rebug.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.killstorm103.Rebug.RebugPlugin;

public final class Scheduler
{
	public static void cancelTasks (Plugin plugin)
	{
		if (ServerVersionUtil.isFolia()) 
			Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
		else
			Bukkit.getScheduler().cancelTasks(plugin);
	}
	public static void run (Runnable runnable)
	{
		if (ServerVersionUtil.isFolia())
			Bukkit.getGlobalRegionScheduler().execute(RebugPlugin.getINSTANCE(), runnable);
		else
			Bukkit.getScheduler().runTask(RebugPlugin.getINSTANCE(), runnable);
	}
	public static void runAsynchronously (Runnable runnable)
	{
		if (ServerVersionUtil.isFolia())
			Bukkit.getGlobalRegionScheduler().execute(RebugPlugin.getINSTANCE(), runnable);
		else
			Bukkit.getScheduler().runTaskAsynchronously(RebugPlugin.getINSTANCE(), runnable);
	}
	public static Task runLater(Runnable runnable, long delayTicks) {
		if (ServerVersionUtil.isFolia())
			return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(RebugPlugin.getINSTANCE(), t -> runnable.run(), delayTicks));
		else
			return new Task(Bukkit.getScheduler().runTaskLater(RebugPlugin.getINSTANCE(), runnable, delayTicks));
	}

	public static Task runTimer(Runnable runnable, long delayTicks, long periodTicks) {
		if (ServerVersionUtil.isFolia())
			return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(RebugPlugin.getINSTANCE(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
		else
			return new Task(Bukkit.getScheduler().runTaskTimer(RebugPlugin.getINSTANCE(), runnable, delayTicks, periodTicks));
	}
	public static Task runTimerAsynchronously(Runnable runnable, long delayTicks, long periodTicks)
	{
		if (ServerVersionUtil.isFolia())
			return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(RebugPlugin.getINSTANCE(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
		else
			return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(RebugPlugin.getINSTANCE(), runnable, delayTicks, periodTicks));
	}
	public static class Task
	{
		private Object foliaTask;
		private BukkitTask bukkitTask;
		private boolean running;

		Task (Object foliaTask)
		{
			this.running = true;
			this.foliaTask = foliaTask;
		}
		Task (BukkitTask bukkitTask) 
		{
			this.running = true;
			this.bukkitTask = bukkitTask;
		}
		public boolean isRunning ()
		{
			return (foliaTask != null || bukkitTask != null) && running;
		}
		public void cancel () 
		{
			if (!isRunning()) return;
			
			if (foliaTask != null)
				((ScheduledTask) foliaTask).cancel();
			else
				bukkitTask.cancel();
			
			running = false;
		}
	}
}
