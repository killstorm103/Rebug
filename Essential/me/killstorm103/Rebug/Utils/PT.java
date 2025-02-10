package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Events.EventUpdateScore;
import me.killstorm103.Rebug.Utils.API.ApiProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

@SuppressWarnings("deprecation")
public class PT 
{
	public static void run_server_command(String cmd) 
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
	}
	public static List<UUID> lockedList = new ArrayList<>();

	public static void checkPlayer (User user, boolean sudo) 
	{
		if (user == null || lockedList.contains(user.getUUID()) || !user.getSettingsBooleans().getOrDefault("Client Command Checker", false) && !sudo)
			return;

		lockedList.add(user.getUUID());
		int Timer = 60;
		final String button = FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.button", "████████████"), 
		commandName = FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.command", "tellraw");
		if (FileUtils.getPlayerSettingsConfig().getBoolean("Client Command Checker.sendOnce", true))
		{
			try
			{
				user.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.strings.top", "&e------&aplease click the button&e------")));
				FileUtils.getPlayerSettingsConfig().getConfigurationSection("Client Command Checker.component.messages").getKeys(false).forEach(key -> 
				{
					final String text = ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.messages." + key + ".text", "%rebug% %button%").replace("%rebug%", RebugPlugin.getRebugMessage()).replace("%button%", button).replace("%user%", user.getName()).replace("%player%", user.getName())),
					hover = ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.messages." + key + ".hover", "%time% left").replace("%time%", Timer + " " + (Timer > 1 ? "seconds" : "second")).replace("%user%", user.getName()).replace("%player%", user.getName()));

					sendChat(user.getPlayer(), "", ChatTypes.CHAT, Component.text(text).clickEvent(ClickEvent.runCommand(commandName + " " + user.getName())).hoverEvent(HoverEvent.showText(Component.text(hover))));
				});
				user.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.strings.bottom", "&e------&aplease click the button&e------")));
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
		else
		{
			user.CommandChecker = Scheduler.runTimer(new Runnable()
			{
				final User user1 = user;
				int secondLeft = Timer;
				@Override
				public void run() 
				{
					if (!user1.getSettingsBooleans().getOrDefault("Client Command Checker", false) && !sudo) 
					{
						user.CommandChecker.cancel();
						lockedList.remove(user1.getUUID());
					}
					
					this.secondLeft--;
					if (lockedList.contains(user1.getUUID())) 
					{
						if (user1.getPlayer().isOnline()) 
						{
							try
							{
								user1.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.strings.top", "&e------&aplease click the button&e------")).replace("%user%", user1.getName()).replace("%player%", user1.getName()));;
								FileUtils.getPlayerSettingsConfig().getConfigurationSection("Client Command Checker.component.messages").getKeys(false).forEach(key -> 
								{
									final String text = ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.messages." + key + ".text", "%rebug% %button%").replace("%rebug%", RebugPlugin.getRebugMessage()).replace("%button%", button).replace("%user%", user.getName()).replace("%player%", user.getName())),
									hover = ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.component.messages." + key + ".hover", "%time% left").replace("%time%", secondLeft + " " + (secondLeft > 1 ? "seconds" : "second")).replace("%user%", user.getName()).replace("%player%", user.getName()));

									sendChat(user1.getPlayer(), "", ChatTypes.CHAT, Component.text(text).clickEvent(ClickEvent.runCommand(commandName + " " + user.getName())).hoverEvent(HoverEvent.showText(Component.text(hover))));
								});
								user1.sendMessage(ChatColor.translateAlternateColorCodes('&', FileUtils.getPlayerSettingsConfig().getString("Client Command Checker.strings.bottom", "&e------&aplease click the button&e------")).replace("%user%", user1.getName()).replace("%player%", user1.getName()));
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						} 
						else
						{
							user.CommandChecker.cancel();
							lockedList.remove(user1.getUUID());
						}
					}
					else 
					{
						user.CommandChecker.cancel();
					}
				}
			}, 0, 20L);
		}
		user.CommandCheckerLater = Scheduler.runLater(new Runnable() 
		{
			final User user1 = user;
			@Override
			public void run() 
			{
				if (!user1.getSettingsBooleans().getOrDefault("Client Command Checker", false) && !sudo) 
				{
					user.CommandCheckerLater.cancel();
					lockedList.remove(user1.getUUID());
				}
				
				Player player = Bukkit.getPlayer(user1.getUUID());
				if (player == null || !player.isOnline()) {
					user.CommandCheckerLater.cancel();
					lockedList.remove(player != null ? player.getUniqueId() : user1.getUUID());
				}
				if (!lockedList.contains(player != null ? player.getUniqueId() : user1.getUUID())) 
				{
					user.CommandCheckerLater.cancel();
					return;
				}
				if (lockedList.contains(player != null ? player.getUniqueId() : user1.getUUID()))
				{
					user1.sendMessage("You Failed the Client Command Checker!");
					user1.sendMessage("To Disable this check use /settings");
					user.CommandCheckerLater.cancel();
					lockedList.remove(player != null ? player.getUniqueId() : user1.getUUID());
				}
			}
		}, 20 * Timer);
	}
	
	public static void CallEvent (@NotNull Event e)
	{
		boolean fixed = false;
		if (!fixed) return;
		
		Bukkit.getPluginManager().callEvent (e);
	}
	public static String[] SplitString (String str)
	{
		return str.split(" ");
	}
	public static boolean isStringNull (String str)
	{
		return str == null || str.length() < 1;
	}
	public static void sendTitle (@NotNull Object object, String MainTitle, String SubTile, int FadeInTicks, int StayTicks, int FadeOutTicks)
	{
		if (object == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "sendTitle object is @NotNull but is null!");
			return;
		}
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null) return;
	
		com.github.retrooper.packetevents.protocol.player.User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
		if (user == null)
		{
			player.sendMessage(RebugPlugin.getRebugMessage() + "PacketEvent's User was null[PT:sendTitle]");
			return;
		}
		
		MainTitle = MainTitle == null ? "" : MainTitle;
		SubTile = SubTile == null ? "" : SubTile;
		user.sendTitle(MainTitle, SubTile, FadeInTicks, StayTicks, FadeOutTicks);
	}
	public static void sendChat (@NotNull Object object, String message, ChatType type, Component custom_component)
	{
		if (object == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "sendChat object is @NotNull but is null!");
			return;
		}
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null) return;

		com.github.retrooper.packetevents.protocol.player.User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
		if (user == null)
		{
			player.sendMessage(RebugPlugin.getRebugMessage() + "PacketEvent's User was null[PT:sendChat]");
			return;
		}
		
		user.sendMessage(custom_component != null ? custom_component : Component.text(message), type);
	}
	public static void sendChat (@NotNull Object object, String message, ChatType type)
	{
		sendChat(object, message, type, null);
	}
	public static String ReverseString (String name) 
	{
		name = name.trim();
		StringBuilder reversedNameBuilder = new StringBuilder(), subNameBuilder = new StringBuilder();
		for (int i = 0; i < name.length(); i++)
		{
			char currentChar = name.charAt(i);
			if (currentChar != ' ' && currentChar != '-')
				subNameBuilder.append(currentChar);

			else 
			{
				reversedNameBuilder.insert(0, currentChar + subNameBuilder.toString());
				subNameBuilder.setLength(0);
			}
		}
		return reversedNameBuilder.insert(0, subNameBuilder.toString()).toString();
	}
	public static String RebugsUserWasNullErrorMessage(String AddOn) {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Rebug's " + ChatColor.RED
				+ "\"User\" was somehow null, " + AddOn;
	}
	public static Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		Inventory inv = Bukkit.createInventory(holder, size, title);
		inv.clear();
		return inv;
	}
	public static int getPing (@NotNull Object player) 
	{
		if (player == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "getPing's player is @NotNull but is Null!");
			return -1;
		}
		return PacketEvents.getAPI().getPlayerManager().getPing(player);
	}
	public static boolean isStringNull (String... s) 
	{
		if (s == null || s.length < 1)
			return true;

		for (int i = 0; i < s.length; i++)
		{
			if (s[i] == null || s[i].length() < 1)
				return true;
		}
		return false;
	}
	public static boolean isServerNewerOrEquals (@NotNull ServerVersion server)
	{
		return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(server);
	}
	public static boolean isServerOlderThan (@NotNull ServerVersion server)
	{
		return PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(server);
	}
	public static String getVersion (@NotNull Player user)
	{
		try
		{
			return PacketEvents.getAPI().getPlayerManager().getUser(user).getClientVersion().getReleaseName();
		}
		catch (Exception e) 
		{
			return ClientVersion.UNKNOWN.name();
		}
	}
	public static int getProtocolVersion (@NotNull Player user)
	{
		try
		{
			return PacketEvents.getAPI().getPlayerManager().getUser(user).getClientVersion().getProtocolVersion();
		}
		catch (Exception e) 
		{
			return ClientVersion.UNKNOWN.getProtocolVersion();
		}
	}
	public static void KickPlayer (@NotNull Object user, String reasonn)
	{
		Player player = null;
		if (user instanceof Player)
			player = (Player) user;
		
		if (user instanceof User)
			player = ((User) user).getPlayer();
		
		if (player == null || !player.isOnline())
			return;
		
		String name = player.getName(), reason = isStringNull(reasonn) ? "No Reason Given!" : reasonn;

		Scheduler.run(new Runnable() {
			
			@Override
			public void run() 
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + name + " " + reason);
			}
		});
	}
	public void sendMessage (Object play, String message, boolean UseRebugMessage)
	{
		if (play instanceof Player) 
		{
			((Player) play).sendMessage((UseRebugMessage ? RebugPlugin.getRebugMessage() : "") + message);
			return;
		}
		if (play instanceof User) 
		{
			if (UseRebugMessage)
				((User) play).sendMessage(message);
			else
				((User) play).getPlayer().sendMessage(message);
			
			return;
		}
		RebugPlugin.getINSTANCE().Log(Level.SEVERE, "Unknown Object in sendMessage (PTNormal) (object: " + play + ") (msg: " + message + ")");
	}
	@SuppressWarnings("unused")
	public static boolean isNumber_Float(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Float.parseFloat(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Double(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Integer(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	public static boolean isNumber_Short(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Short.parseShort(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	public static boolean hasAdminPerms (@NotNull Object object)
	{
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null) return false;
		
		if (player.isOp()) return true;
		
		try
		{
			for (Map.Entry<String, Map<String, Boolean>> ranks : RebugPlugin.StaffRanks.entrySet())
			{
				for (Map.Entry<String, Boolean> staff : ranks.getValue().entrySet())
				{
					if (player.hasPermission(staff.getKey()))
						return true;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	public static boolean hasPermission (@NotNull Object object, @NotNull String perm)
	{
		if (object == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "hasPerms object is @NotNull but is null!");
			return false;
		}
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
		
		if (player == null) return false;
		
		if (player.isOp() || !isStringNull(perm) && player.hasPermission(perm)) return true;
		
		try
		{
			for (Map.Entry<String, Map<String, Boolean>> ranks : RebugPlugin.StaffRanks.entrySet())
			{
				for (Map.Entry<String, Boolean> staff : ranks.getValue().entrySet())
				{
					if (player.hasPermission(staff.getKey()))
						return true;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	public static void PlayAntiCheatSelectedSound (Object object)
	{
		Player player = null;
		if (object instanceof Player)
			player = (Player) object;
		
		if (object instanceof User)
			player = ((User) object).getPlayer();
			
		if (player == null) return;
		
		final boolean found = FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats.use-selected-sound." + ServerVersionUtil.getServer_Version()) != null;
		final String sound = FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.use-selected-sound." + (found ? ServerVersionUtil.getServer_Version() : "default"));
		if (RebugPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
		
		if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
			RebugPlugin.getINSTANCE().Log(Level.WARNING, "Using default AntiCheat Selected Sound!");
		
		try
		{
			player.playSound(player.getLocation(), sound, 1F, 1F);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void UpdateAntiCheat (User user, String[] ItemName, ItemStack item, CommandSender Sudo, boolean Multi) 
	{
		try
		{
			if (RebugPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
			
			if (user == null) 
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugPlugin.getRebugMessage() + "User was null!");

				return;
			}
			int amount = user.getSelectedAntiCheats();
			String oldAC = amount > 1 ? user.getAntiCheat().replace(" ", ", ").trim() : user.getAntiCheat();
			if (Multi) 
			{
				int size = ItemName.length - 1; // -2
				if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.enabled", false)) 
				{
					if (Sudo == null) 
					{
						user.sendMessage("Sorry but Multiple AntiCheats is Disabled!");
						return;
					} 
					else
						Sudo.sendMessage(RebugPlugin.getRebugMessage() + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You making "
								+ user.getName()
								+ " Select Multiple AntiCheats while it's Disabled in the Loaded AntiCheats.yml config!");
				}
				final int maxACs = FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats.multiple-anticheat.max-anticheats", 2);
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.multiple-anticheat.limiter-enabled", false) && maxACs > 1 && size > maxACs) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Sorry but your trying to Choose more AntiCheats than allowed!");
					else
						user.sendMessage("Sorry but your trying to Choose more AntiCheats than allowed!");

					return;
				}

				String ID_Name = "", Carded = "", Kickables = "", AntiCheatName = "";
				for (int i = 1; i < ItemName.length; i++) 
				{
					if (isStringNull(ItemName[i])) 
					{
						if (Sudo != null)
							Sudo.sendMessage(
									RebugPlugin.getRebugMessage() + "Failed to change user's AntiCheat, the given anticheat name string ["
											+ i + "] was null!");
						else
							user.sendMessage("Failed to change AntiCheats due to the given anticheat name string [" + i
									+ "] was null!");

						return;
					}
					if (ItemName[i].equalsIgnoreCase("Vanilla")) 
					{
						if (Sudo == null)
							user.sendMessage("Dumbass you can't Select Vanilla when trying to Select Multiple AntiCheats!");
						else
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Dumbass you can't Choose Vanilla for Multiple AntiCheats!");

						return;
					}
					if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + ItemName[i].toLowerCase()) == null) 
					{
						if (Sudo != null)
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + "AntiCheat " + "\"" + ItemName[i] + "\" was not found!");
						else
							user.sendMessage("AntiCheat " + "\"" + ItemName[i] + "\" was not found!");

						return;
					}
					AntiCheatName = TheChatColor
							.stripColor(TheChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile()
									.getString("loaded-anticheats." + ItemName[i].toLowerCase() + ".main-name"), true, ItemName[i]));
					
					if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".enabled", false)) 
					{
						if (Sudo != null)
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY
									+ "You are making " + user.getName() + " Select " + AntiCheatName
									+ " which is a Disabled AntiCheat");

						else
						{
							user.sendMessage("AntiCheat: " + AntiCheatName + " is Disabled!");
							return;
						}
					}
					if (Sudo == null && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".permission-to-use", false) && !hasPermission(user.getPlayer(), FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats.permission.to.use.anticheat", "me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase()).replace("%anticheat%", AntiCheatName.toLowerCase()).replace("%ac%", AntiCheatName.toLowerCase()))) 
					{
						user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
						user.sendMessage("You need the permission:");
						user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + AntiCheatName.toLowerCase());
						user.getPlayer().closeInventory();
						return;
					}

					if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".is-multi-enabled", false)) 
					{
						if (Sudo != null)
							Sudo.sendMessage(
									RebugPlugin.getRebugMessage() + "Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");
						else
							user.sendMessage("Sorry but multi AntiCheat is not Enabled for " + AntiCheatName + "!");

						return;
					}

					String m_id = AntiCheatName.toLowerCase();
					if (ID_Name.toLowerCase().contains(m_id))
					{
						if (Sudo == null)
							user.sendMessage("Failed to Add Muti AC " + AntiCheatName
									+ " Due to ID already containing this AntiCheat!");
						else
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Failed to Add Muti AC " + AntiCheatName
									+ " Due to ID already containing this AntiCheat!");

						return;
					}
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + AntiCheatName.toLowerCase() + ".requires-reconnect", false))
						Kickables += AntiCheatName + (i < ItemName.length - 1 ? " " : "");

					ID_Name += AntiCheatName + (i < ItemName.length - 1 ? " " : "");
				}
				if (isStringNull(ID_Name)) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Failed to set Multi AC due to ID being null!");
					else
						user.sendMessage("Failed to set Multi AC due to ID being null!");

					return;
				}
				Carded = ID_Name.replace(" ", "_").toLowerCase();
				if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats.multiple-anticheats." + Carded) == null) 
				{
					if (size == 2)
						Carded = ReverseString(Carded.replace("_", " ")).replace(" ", "_");

					else 
					{
						Carded = Carded.replace("_", " ");
						for (String AC : FileUtils.getLoadedAntiCheatsFile().getConfigurationSection("loaded-anticheats.multiple-anticheats").getKeys(true)) 
						{
							if (isSameStringButDiffOrder(Carded, AC.replace("_", " "))) 
							{
								Carded = AC;
								break;
							}
						}
					}
					Carded = Carded.replace(" ", "_").toLowerCase();
					if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats.multiple-anticheats." + Carded) == null) 
					{
						if (Sudo != null)
						{
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + Carded + " was not Found in the Loaded AntiCheats.yml file!");
							Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Method: " + (size == 2 ? "ReverseString" : "isSameStringButDiffOrder"));
						}
						else
						{
							user.sendMessage(Carded + " was not Found in the Loaded AntiCheats.yml file! tell a Owner or Admin!");
							user.sendMessage("Method: " + (size == 2 ? "ReverseString" : "isSameStringButDiffOrder"));
						}
						return;
					}
					if (isSameStringButDiffOrder(user.getAntiCheat(), ID_Name))
					{
						PlayAntiCheatSelectedSound(user.getPlayer());
						return;
					}
				}
				user.setAntiCheat(ID_Name);
				user.setSelectedAntiCheats(size);
				user.setNumberIDs(FileUtils.getLoadedAntiCheatsFile().getInt("loaded-anticheats.multiple-anticheats." + Carded) + "");
				UpdateUserPerms(user.getPlayer(), user.getNumberIDs());
				user.setResetColorAC(true);
				if (Sudo != null) 
				{
					Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Successfully Changed " + user.getName() + "'s AntiCheats to: "
							+ user.getAntiCheat());
					user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin")
							+ " Manually Set Your AntiCheats to: " + user.getAntiCheat() + "!");
				}
				else
					user.sendMessage("You selected: " + user.getAntiCheat());

				
				CallEvent(new EventUpdateScore(user, new String[] {ChatColor.DARK_RED + "AC " + ChatColor.AQUA + "Multi"}, new int[] {10}));
				PlayAntiCheatSelectedSound(user.getPlayer());
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean ("log.server.anticheat-switches", false))
					RebugPlugin.getINSTANCE().Log(Level.INFO, "" + FileUtils.getLoadedAntiCheatsFile().getString("log.server.message").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", user.getAntiCheat()).replace("%anticheat%", user.getAntiCheat()).replace("%oldac%", oldAC).replace("%oldanticheat%", oldAC));
					
				if (!isStringNull(Kickables))
				{
					String[] Split = SplitString(Kickables);
					if (Split.length > 1)
					{
						Kickables = "";
						for (int i = 0; i < Split.length; i++) 
						{
							Kickables += TheChatColor
									.stripColor(TheChatColor.translateAlternateColorCodes('&',
											FileUtils.getLoadedAntiCheatsFile().getString(
													"loaded-anticheats." + Split[i].toLowerCase() + ".main-name"), true, Split[i]))
									+ (i < Split.length - 1 ? " & " : "");
						}
					}
					else
						Kickables = TheChatColor
								.stripColor(TheChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile()
										.getString("loaded-anticheats." + Split[0].toLowerCase() + ".main-name"), true, Split[0]));

					KickPlayer(user.getPlayer(), RebugPlugin.getRebugMessage() + "Reconnect required in order to make sure "
							+ Kickables + ChatColor.DARK_GRAY + " work properly!");
					return;
				}
				return;
			}

			if (isStringNull(ItemName[0])) 
			{
				if (Sudo != null)
					Sudo.sendMessage(
							RebugPlugin.getRebugMessage() + "Failed to change user's AntiCheat, the given anticheat name string was null!");

				user.sendMessage("Failed to change AntiCheat due to the given AntiCheat name string was null!");
				return;
			}

			if (user.getAntiCheat().equalsIgnoreCase(ItemName[0])) 
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Unable to change " + user.getName() + "'s AntiCheat due to "
							+ user.getName() + " already having this AntiCheat Selected!");
				
				PlayAntiCheatSelectedSound(user.getPlayer());
				return;
			}
			if (!ItemName[0].equalsIgnoreCase("vanilla") && FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + ItemName[0].toLowerCase()) == null)
			{
				if (Sudo != null)
					Sudo.sendMessage(RebugPlugin.getRebugMessage() + "AntiCheat " + "\"" + ItemName[0] + "\" was not found!");
				else
					user.sendMessage("AntiCheat " + "\"" + ItemName[0] + "\" was not found!");

				return;
			}
			if (ItemName[0].equalsIgnoreCase("Vanilla"))
			{
				if (Sudo == null && !user.hasPermission("me.killstorm103.rebug.user.select_vanilla") && !hasAdminPerms(user)) 
				{
					user.sendMessage("You don't have permission to use Vanilla!");
					return;
				}
				if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.vanilla.enabled")) 
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugPlugin.getRebugMessage() + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY + "You are making " + user.getName() + " Select Vanilla as their AntiCheat which is Disabled!");
					else 
					{
						user.sendMessage("Vanilla is Disabled!");
						return;
					}
				}
			}
			if (!ItemName[0].equalsIgnoreCase("Vanilla"))
			{
				if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".enabled"))
				{
					if (Sudo != null)
						Sudo.sendMessage(RebugPlugin.getRebugMessage() + ChatColor.YELLOW + "Warning " + ChatColor.DARK_GRAY
								+ "You are making " + user.getName() + "'s Select "
								+ TheChatColor.stripColor(TheChatColor.translateAlternateColorCodes('&',
										FileUtils.getLoadedAntiCheatsFile().getString(
												"loaded-anticheats." + ItemName[0].toLowerCase() + ".main-name"), true, ItemName[0]))
								+ " which is a Disabled AntiCheat");

					else 
					{
						user.sendMessage("AntiCheat: " + ItemName[0] + " is Disabled!");
						return;
					}
				}
				if (Sudo == null
						&& FileUtils.getLoadedAntiCheatsFile()
								.getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".permission-to-use")
						&& !user.hasPermission(
								"me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase())) {
					user.sendMessage("Sorry but permission-to-use is Enabled for this AntiCheat!");
					user.sendMessage("You need the permission:");
					user.sendMessage("me.killstorm103.rebug.user.select_anticheat_" + ItemName[0].toLowerCase());
					return;
				}
				if (Sudo == null && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + ItemName[0].toLowerCase() + ".requires-combining", false))
				{
					if (!FileUtils.getLoadedAntiCheatsFile().getBoolean("combining-anticheats.bypass-permission-enabled", false) || !hasPermission(user.getPlayer(), RebugPlugin.getINSTANCE().getConfig().getString("combining-anticheats.permission", "me.killstorm103.rebug.user.bypass_anticheatcombine")))
					{
						user.sendMessage("Sorry but you have to combine this AntiCheat With another AntiCheat!");
						user.sendMessage("do /ac <anticheat> <anticheat> (....)");
						return;
					}
				}
			}
			user.setAntiCheat(ItemName[0].equalsIgnoreCase("vanilla") ? "Vanilla" :
				ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + ItemName[0].toLowerCase() + ".main-name"))));
			user.setSelectedAntiCheats(user.getAntiCheat().equalsIgnoreCase("Vanilla") ? 0 : 1);
			user.setNumberIDs("");
			UpdateUserPerms(user.getPlayer(), user.getAntiCheat());
			user.setResetColorAC(true);
			if (user.getSelectedAntiCheats() > 0 && FileUtils.getLoadedAntiCheatsFile().getBoolean ("loaded-anticheats." + user.getAntiCheat().toLowerCase() + ".api", false))
			{
				try
				{
					ApiProvider.onApi (user.getPlayer(), user.getAntiCheat());
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			String NewAC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.getAntiCheat()).toLowerCase();
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name", false))
				NewAC = NewAC.replace(NewAC, ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);

			CallEvent(new EventUpdateScore(user, new String[] {ChatColor.DARK_RED + "AC " + NewAC}, new int[] {10}));
			String AC = ChatColor.stripColor(NewAC);
			if (Sudo != null) {
				Sudo.sendMessage(RebugPlugin.getRebugMessage() + "Successfully Changed " + user.getName() + "'s AntiCheat to: "
						+ ChatColor.stripColor(user.getColoredAntiCheat()));
				user.sendMessage((Sudo instanceof Player ? ((Player) Sudo).getName() : "a Owner or Admin")
						+ " Manually Set Your AntiCheat to: " + ChatColor.stripColor(user.getColoredAntiCheat()) + "!");
			} 
			else 
			{
				if (item != null)
					user.sendMessage("You selected: " + AC
							+ (AC.equalsIgnoreCase("vanilla") || AC.equalsIgnoreCase("nocheatplus")
									|| AC.equalsIgnoreCase("NCP")
											? ""
											: " " + ChatColor.stripColor(PT
													.SubString(item.getItemMeta().getLore().get(3), 10,
															item.getItemMeta().getLore().get(3).length())
													.replace(" ", ""))));
				else
					user.sendMessage("You selected: " + AC);
			}
			PlayAntiCheatSelectedSound(user.getPlayer());
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean ("log.server.anticheat-switches", false))
				RebugPlugin.getINSTANCE().Log(Level.INFO, "" + FileUtils.getLoadedAntiCheatsFile().getString("log.server.message").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", user.getAntiCheat()).replace("%anticheat%", user.getAntiCheat()).replace("%oldac%", oldAC).replace("%oldanticheat%", oldAC));
			
			user.getPlayer().closeInventory();
			
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + user.getAntiCheat().toLowerCase() + ".requires-reconnect", false))
				KickPlayer(user.getPlayer(), RebugPlugin.getRebugMessage() + "Reconnect required in order to make sure " + user.getColoredAntiCheat() + ChatColor.DARK_GRAY + " works properly!");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void UpdateUserPerms(@NotNull Player user, String itemName) 
	{
		if (isStringNull(itemName) || user == null)
			return;

		Scheduler.run(new Runnable() 
		{
			@Override
			public void run() 
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), RebugPlugin.getINSTANCE().getConfig().getString("user.update-perms-command").replace("%player%", user.getName()).replace("%user%", user.getName()).replace("%ac%", itemName).replace("%anticheat%", itemName));
			}
		});
		Scheduler.run(new Runnable() {
			
			@Override
			public void run() 
			{
				if (RebugPlugin.getINSTANCE().getConfig().getBoolean("user.use-ranks")) 
				{
					int uses = 0;
					for (String section : FileUtils.getServerConfig().getConfigurationSection("ranks").getKeys(true))
					{
						String perm = FileUtils.getServerConfig().getString("ranks." + section + ".permission", "");
						if (user.hasPermission(perm))
						{
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), RebugPlugin.getINSTANCE().getConfig().getString("user.staff-group-command").replace("%user%", user.getName())
							.replace("%player%", user.getName()).replace("%rank%", section.toLowerCase()));
							uses ++;

							if (uses >= RebugPlugin.getINSTANCE().getConfig().getInt("user.ranks", 1))
								break;
						}
					}
				}
			}
		});
	}
	private static final String TickMark = "✔️";

	public static final String getTickMark() 
	{
		final String New = TickMark.substring(0, 1);
		return New;
	}
	public static final String getXMark ()
	{
		try
		{
			return ChatColor.translateAlternateColorCodes('&', RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.marks.x"));
		}
		catch (Exception e) {}
		
		return RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.marks.x");
	}
	public static boolean isSameStringButDiffOrder(String str1, String str2)
	{
		String[] words1 = str1.split(" "), words2 = str2.split(" ");
		Arrays.sort(words1);
		Arrays.sort(words2);
		return Arrays.equals(words1, words2);
	}
	public static String SubString(String sub, int begin, int max) {
		String ss = sub.substring(begin, Math.min(sub.length(), max));

		return ss;
	}
	public static void addScoreBoard (@NotNull Object object)
	{
		if (object == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "addScoreBoard: object is @NotNull but is Null");
			return;
		}
		final int UsePlugin = RebugPlugin.getINSTANCE().getConfig().getInt("scoreboard.plugin", 0);
		if (!RebugPlugin.getINSTANCE().getConfig().getBoolean("scoreboard.enabled", false) || UsePlugin < 1) return;
		
		User user = null;
		if (object instanceof Player)
			user = RebugPlugin.getUser((Player) object);
		
		if (object instanceof User)
			user = (User) object;
			
		if (user == null)
		{
			RebugPlugin.getINSTANCE().Log(Level.SEVERE, "User was Null on addScoreBoard!");
			return;
		}
		Object board = null;
		final String Title = ChatColor.translateAlternateColorCodes('&', QuickConfig.ScoreboardTitle());
		String AC = user.getColoredAntiCheat(), strip = ChatColor.stripColor(AC).toLowerCase();
		if (FileUtils.getLoadedAntiCheatsFile().get("loaded-anticheats." + strip + ".has-short-name") != null && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + strip + ".has-short-name"))
			AC = AC.replace(AC, ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + strip + ".short-name")) + ChatColor.RESET);
		
		try
		{
			switch (UsePlugin)
			{
			case 1:
				if (Netherboard.instance().hasBoard(user.getPlayer())) break;
				
				board = Netherboard.instance().createBoard(user.getPlayer(), Title);
				int start = 0;
				for (String string : RebugPlugin.getINSTANCE().getConfig().getStringList("scoreboard.custom.list"))
				{
					string = string.replace("%user_onground%", user.getPlayer().isOnGround() ? getTickMark() : getXMark()).replace("%user_anticheat%", AC).replace("%user_client%", getVersion(user.getPlayer())).
					replace("%user_cps%", user.getClicksPerSecondLeft() + " : " + user.getClicksPerSecondRight()).replace("%user_timer_balance%", user.getTimer_balance() + "").replace("%user_isblocking%", user.getPlayer().isBlocking() ? getTickMark() : getXMark())
					.replace("%user_bpsxz%", "0").replace("%user_bpsy%", "0").replace("%user_pps%", user.getSendPacketCounts() + "/in " + user.getReceivePacketCounts() + "/out").replace("%user_issprinting%", user.getPlayer().isSprinting() ? getTickMark() : getXMark())
					.replace("%user_issneaking%", user.getPlayer().isSneaking() ? getTickMark() : getXMark());
					((BPlayerBoard) board).set(ChatColor.translateAlternateColorCodes('&', string), start ++);
				}
				user.getPlayer().setScoreboard(((BPlayerBoard) board).getScoreboard());
				break;
				
			default:
				break;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void UpdateScoreBoard (User user, String[] text, int[] score, String[] WhatItIs)
	{
		if (!RebugPlugin.getINSTANCE().getConfig().getBoolean("scoreboard.enabled", false) || user == null || !user.getPlayer().isOnline())
			return;
		
		final int UsePlugin = RebugPlugin.getINSTANCE().getConfig().getInt("scoreboard.plugin", 0);
		for (int o = 0; o < WhatItIs.length; o ++)
		{
			if (RebugPlugin.getINSTANCE().getConfig().get ("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase()) == null || !RebugPlugin.getINSTANCE().getConfig().getBoolean("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase() + ".enabled"))
				return;
			
			
			Object board = null;
			if (RebugPlugin.getINSTANCE().getConfig().get ("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase() + ".replace") != null && RebugPlugin.getINSTANCE().getConfig().getBoolean("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase() + ".replace") && RebugPlugin.getINSTANCE().getConfig().get("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase() + ".text") != null && !PT.isStringNull(RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.custom.slots-that-change." + WhatItIs[o].toLowerCase() + ".text")))
			{
				String AC = user.getColoredAntiCheat(), striped = ChatColor.stripColor(user.getAntiCheat()).toLowerCase();
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats." + striped + ".has-short-name"))
					AC = AC.replace(AC, ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + striped + ".short-name")) + ChatColor.RESET);
				
				for (int i = 0; i < text.length; i ++)
				{
					text[i] = RebugPlugin.getINSTANCE().getConfig().getString("scoreboard.custom.slots-that-change." + WhatItIs[i].toLowerCase() + ".text").replace("%user_onground%", user.isOnGround() ? getTickMark() : getXMark()).
					replace("%user_issneaking%", user.isSneaking() ? getTickMark() : getXMark())
					.replace("%user_issprinting%", user.isSprinting() ? getTickMark() : getXMark()).replace("%user_isblocking%", user.isBlocking() ? getTickMark() : getXMark()).replace("%user_timer_balance%", user.getTimer_balance() + "").
					replace("%user_pps%", ChatColor.WHITE.toString() + user.getSendPacketCounts() + "/in " + user.getReceivePacketCounts() + "/out")
					.replace("%user_bpsy%", user.getBPSY()).replace("%user_bpsxz%", user.getBPSXZ()).
				    replace("%user_cps%", ChatColor.WHITE.toString() + user.getClicksPerSecondLeft() + " : " + user.getClicksPerSecondRight()).replace("%user_client%", getVersion(user.getPlayer())).replace("%user_anticheat%", AC);
					text[i] = ChatColor.translateAlternateColorCodes('&', text[i]);
					score[i] = RebugPlugin.getINSTANCE().getConfig().getInt("scoreboard.custom.slots-that-change." + WhatItIs[i].toLowerCase() + ".slot");
					
					switch (UsePlugin)
					{
					case 1:
						board = Netherboard.instance().getBoard(user.getPlayer());
						if (board == null)
							return;

						if (!PT.isStringNull(((BPlayerBoard) board).get(score[i])))
							((BPlayerBoard) board).remove(score[i]);

						((BPlayerBoard) board).set(text[i], score[i]);
						break;
						
					default:
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unsupported Board! (UpdateScoreBoard)");
						break;
					}
				}
			}
		}
	}
	public static void DeleteScoreBoard (Player player)
	{
		if (!RebugPlugin.getINSTANCE().getConfig().getBoolean("scoreboard.enabled", false) || player == null) return;
		
		Object board = null;
		final int UsePlugin = RebugPlugin.getINSTANCE().getConfig().getInt("scoreboard.plugin", 0);
		
		switch (UsePlugin)
		{
		case 1:
			board = Netherboard.instance().getBoard(player);
			if (board != null) 
			{
				((BPlayerBoard) board).clear();
				((BPlayerBoard) board).delete();
			}
			break;
			
		default:
			RebugPlugin.getINSTANCE().Log(Level.WARNING, "Unsupported Board! (DeleteScoreBoard)");
			break;
		}
	}
}
