package me.killstorm103.Rebug.SoftWares.Spigot.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Shared.Interfaces.PTs;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;
import net.kyori.adventure.text.Component;

public class PT_Spigot implements PTs
{
	
	public static final PT_Spigot INSTANCE = new PT_Spigot();

	@Override
	public void DebuggerChatMessage (Object player, PacketReceiveEvent e, String toDebug) 
	{
		Rebug.getINSTANCE().getNMS().DebuggerChatMessage(player, e, toDebug);
	}

	public static List<String> getPlayerNames () {
		List<String> t = new ArrayList<>();
		t.clear();
		Player[] Players = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getOnlinePlayers().toArray(Players);
		for (int i = 0; i < Players.length; i++) {
			t.add(Players[i].getName());
		}
		return t;
	}
	@Override
	public void sendMessage (Object play, String message, boolean UseRebugMessage)
	{
		if (play != null && play instanceof Player) 
		{
			((Player) play).sendMessage((UseRebugMessage ? Rebug.RebugMessage : "") + message);
			return;
		}
		if (play != null && play instanceof User) 
		{
			if (UseRebugMessage)
				((User) play).sendMessage(message);
			else
				((User) play).getPlayer().sendMessage(message);
			
			return;
		}
		Rebug.getINSTANCE().Log(Level.SEVERE, "Unknown Object in sendMessage (PTNormal) (object: " + play + ") (msg: " + message + ")");
	}
	@Override
	public void sendMessage (Object play, String message) 
	{
		sendMessage(play, message, true);
	}
	public void SendTitle (Player player, String title, String subtitle)
	{
	//	PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayClientMessage);
	}
	
	// TODO Rework//fix
	public void SendPacketUnBlock(Player player) {
		PacketEvents.getAPI().getPlayerManager().sendPacket(player,
				new WrapperPlayClientPluginMessage("NWS|Debugger Switch", Unpooled.buffer().array()));
	}

	
	// TODO Recode This!
	
	public static Player getPlayerFromHumanEntity(LivingEntity entity) {
		return (Player) entity;
	}

	public static String getTextFromComponent(Component component) {
		return LegacyComponentSerializer.legacyAmpersand().serialize(component);
	}

	public static void Log(CommandSender sender, String tolog) {
		sender.sendMessage(tolog);
	}

	public static void LogToConsole(String tolog) {
		Rebug.getINSTANCE().getServer().getConsoleSender().sendMessage(tolog);
	}
	// Inventory Size can be: 9, 18, 27, 36, 45, 54

	public static Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		Inventory inv = Bukkit.createInventory(holder, size, title);
		inv.clear();

		return inv;
	}

	public static Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
		Inventory inv = Bukkit.createInventory(holder, type, title);
		inv.clear();

		return inv;
	}

	public final Location getSpawn() {
		final Location loc = new Location(Bukkit.getWorld("world"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posX"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posY"),
				Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posZ")),
				locWithRots = new Location(Bukkit.getWorld("world"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posX"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posY"),
						Rebug.getINSTANCE().getConfig().getDouble("world-spawn.posZ"),
						(float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Yaw"),
						(float) Rebug.getINSTANCE().getConfig().getDouble("world-spawn.Pitch"));

		return Rebug.getINSTANCE().getConfig().getBoolean("world-spawn.set-rots") ? locWithRots : loc;
	}

	public static String RebugsUserWasNullErrorMessage(String AddOn) {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Rebug's " + ChatColor.RED
				+ "\"User\" was somehow null, " + AddOn;
	}

	public static void RunTaskCommand(CommandSender sender, String command) {
		Bukkit.getServer().getScheduler().runTask(Rebug.getINSTANCE(), new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender == null ? Bukkit.getServer().getConsoleSender() : sender, command);
			}
		});
	}
	@Override
	public void KickPlayer (Object user, String reason)
	{
		if (user == null) return;
		
		Player player = null;
		if (user instanceof Player)
			player = (Player) user;
		
		if (user instanceof me.killstorm103.Rebug.Shared.Utils.User)
			player = ((me.killstorm103.Rebug.Shared.Utils.User) user).getPlayer();
		
		if (player == null || !player.isOnline())
			return;

		if (!Rebug.KickList.contains(player.getUniqueId()))
			Rebug.KickList.add(player.getUniqueId());
		
		String name = player.getName();

		Bukkit.getScheduler().runTask(Rebug.getINSTANCE(), new Runnable() {

			@Override
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"kick " + name + " " + reason);
			}
		});
	}
	@Override
	public void BanPlayer(Object user, String reason) 
	{
		if (user == null) return;
		Player player = null;
		
		if (user instanceof Player)
			player = (Player) user;
		
		if (user instanceof me.killstorm103.Rebug.Shared.Utils.User)
			player = ((me.killstorm103.Rebug.Shared.Utils.User) user).getPlayer();
		
		if (player == null || !player.isOnline())
			return;
		
		String name = player.getName();
		Bukkit.getScheduler().runTask(Rebug.getINSTANCE(), new Runnable() 
		{
			@Override
			public void run() 
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"ban " + name + " " + reason);
			}
		});
	}
}
