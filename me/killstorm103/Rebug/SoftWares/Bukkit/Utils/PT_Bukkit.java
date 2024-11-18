package me.killstorm103.Rebug.SoftWares.Bukkit.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Shared.Interfaces.PTs;
import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Utils.User;
import net.kyori.adventure.text.Component;

public class PT_Bukkit implements PTs
{
	public static final PT_Bukkit INSTANCE = new PT_Bukkit();

	@Override
	public void DebuggerChatMessage (Object player, PacketReceiveEvent e, String toDebug) 
	{
		Rebug.getINSTANCE().getNMS().DebuggerChatMessage(player, e, toDebug);
	}

	public static final String UnsupportedInVersion = Rebug.RebugMessage
			+ "That isn't supported in this Server Version!";

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

	public static boolean isHotbarInventoryFull(Player player) 
	{
		return player.getInventory().firstEmpty() == -1;
	}
	public static boolean isInventoryFull(Inventory inventory) 
	{
		if (inventory == null) return false;
		
		return inventory.firstEmpty() ==- 1;
	}
	public static ItemStack RepairItem(ItemStack item) 
	{
		ItemStack Stack = new ItemStack(item.getType());
		Stack.setItemMeta(item.getItemMeta());
		return Stack;
	}

	public static String getTextFromComponent(Component component) {
		return LegacyComponentSerializer.legacyAmpersand().serialize(component);
	}

	public static String getServerVersion() {
		return Rebug.getINSTANCE().getServerVersion();
		// return PTNormal.SubString(Bukkit.getServer().getBukkitVersion().trim(), 0,
		// 6).replace("-", "");
	}


	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server."
					+ Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
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
	public void BanPlayer (Object user, String reason) 
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
