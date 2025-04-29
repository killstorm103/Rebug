package me.killstorm103.Rebug.Events;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTimeUpdate;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.Scheduler;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;
import me.killstorm103.Rebug.Utils.User;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

@SuppressWarnings("deprecation")
public class EventPacket implements PacketListener
{
	private final DecimalFormat former = new DecimalFormat("#.###");
	private void onHandleCancel (ProtocolPacketEvent e, User user)
	{
		e.setCancelled(true);
		if (e instanceof PacketSendEvent)
			user.setPreReceivePacket(user.getPreReceivePacket() - 1);
		else
			user.setPreSendPacket(user.getPreSendPacket() - 1);
	}
	
	@Override
	public void onPacketSend (PacketSendEvent e) 
	{
		if (e.getUser() == null || e.getUser().getUUID() == null) return;
		
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;

		User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
		if (user == null) return;
		
		user.setPreReceivePacket(user.getPreReceivePacket() + 1);
		
		if (e.getPacketType() == PacketType.Play.Server.CLOSE_WINDOW && user.getSettingsBooleans().getOrDefault("Anti-Force Close Menu", false))
			onHandleCancel(e, user);
			
		if (e.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) 
		{
			WrapperPlayServerEntityEffect packet = new WrapperPlayServerEntityEffect (e);
			if (packet.getEntityId() != user.getPlayer().getEntityId() || user.getSettingsBooleans().getOrDefault("Potions", true))
				return;
			
			String effect = packet.getPotionType().getName().getKey();
			if (PT.isStringNull(effect)) return;
			
			if (effect.equalsIgnoreCase("night_vision") && user.getSettingsBooleans().getOrDefault("Night Vision", false) || effect.equalsIgnoreCase("resistance") && user.getSettingsBooleans().getOrDefault("Damage Resistance", false) || effect.equalsIgnoreCase("fire_resistance") && user.getSettingsBooleans().getOrDefault("Fire Resistance", false))
				return;
			
			for (PotionEffect effectType : user.getPlayer().getActivePotionEffects()) 
			{
				if (effectType != null && !PT.isStringNull(effectType.getType().getName()))
				{
					if (effectType.getType().getName().equalsIgnoreCase("NIGHT_VISION") && user.getSettingsBooleans().getOrDefault("Night Vision", false) || effectType.getType().getName().equalsIgnoreCase("DAMAGE_RESISTANCE") && user.getSettingsBooleans().getOrDefault("Damage Resistance", false) || effectType.getType().getName().equalsIgnoreCase("FIRE_RESISTANCE") && user.getSettingsBooleans().getOrDefault("Fire Resistance", false)) {}
					else
						user.getPlayer().removePotionEffect(PotionEffectType.getByName(effectType.getType().getName()));
				}
			}
			onHandleCancel(e, user);
		}
		if (e.getPacketType() == PacketType.Play.Server.TIME_UPDATE)
		{
			WrapperPlayServerTimeUpdate packet = new WrapperPlayServerTimeUpdate(e);
			if (user.getSettingsBooleans().getOrDefault("Time Adjuster", false))
				packet.setTimeOfDay(user.getSettingsIntegers().getOrDefault("World Time", 1000));
		}
		if (e.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) 
		{
			if (user.getSettingsBooleans().getOrDefault("S08 Alerts", false) && (System.currentTimeMillis() + 20000) > user.getJoined()) 
			{
				//user.setS08Started(System.currentTimeMillis());
				if (user.task != null)
				{
					user.task.cancel();
					user.taskLater.cancel();
					//user.setS08Ticks(0);
					//user.setS08Time(user.getS08Started());
				}
				user.setS08Started(System.currentTimeMillis());
				user.setS08Ticks(0);
		//		user.setS08Time(user.getS08Started());
				user.task = Scheduler.runTimer(new Runnable()
				{
					@Override
					public void run() 
					{
						user.setS08Time((System.currentTimeMillis() - user.getS08Started()) / 1000);
						user.setS08Ticks(user.getS08Ticks() + 1);
						if (!ServerVersionUtil.isChatTypeGameInfoSupported())
							PT.sendTitle(user, ChatColor.RED + "Server Position (S08)", ChatColor.WHITE.toString()  + user.getS08Time() + " seconds - " + user.getS08Ticks() + " Ticks", 1, 20, 1);
						else
							PT.sendChat(user, ChatColor.RED + "Server Position (S08) " + ChatColor.GRAY + "- " + ChatColor.WHITE.toString() + user.getS08Time() + " seconds - " + user.getS08Ticks() + " Ticks", ChatTypes.GAME_INFO);
						
						if (!user.getSettingsBooleans().getOrDefault("S08 Alerts", false))
						{
							user.task.cancel();
							return;
						}
					}
				}, 0, 1);
				
				user.taskLater = Scheduler.runLater(new Runnable() {
					
					@Override
					public void run() 
					{
						user.task.cancel();
						user.setS08Ticks(0);
						user.setS08Time(user.getS08Started());
						user.taskLater.cancel();
					}
				}, FileUtils.getPlayerSettingsConfig().getLong("S08 Alerts.ticks", 100));
			}
		}
		if (e.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE)
		{
			if (!RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Per Player Alerts", false)) return;
			
			String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(new WrapperPlayServerSystemChatMessage(e).getMessage().asComponent()));
			message = ChatColor.stripColor(message).toLowerCase();
			CancelForPerPlayer(e, user, message);
		}
		if (e.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE)
		{
			if (!RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Per Player Alerts", false)) return;
			
			WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(e);
			if (packet.getMessage().getType().equals(ChatTypes.CHAT) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.chat") || packet.getMessage().getType().equals(ChatTypes.SYSTEM) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.system") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND_OUTGOING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command_outgoing") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND_INCOMING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command_incoming") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command") || packet.getMessage().getType().equals(ChatTypes.SAY_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.say_command") || packet.getMessage().getType().equals(ChatTypes.RAW) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.raw") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND_OUTGOING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command_outgoing") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND_INCOMING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command_incoming") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command") || packet.getMessage().getType().equals(ChatTypes.GAME_INFO) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.game_info") || packet.getMessage().getType().equals(ChatTypes.EMOTE_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.emote_command")) 
			{
				String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(packet.getMessage().getChatContent()));
				message = ChatColor.stripColor(message).toLowerCase();
				CancelForPerPlayer(e, user, message);
			}
		}
	}
	private void CancelForPerPlayer (PacketSendEvent e, @NotNull User user, @NotNull String message)
	{
		if (PT.isStringNull(message) || user == null) return;
		
		if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.past-chat-type"))
			RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "made it past chat type check, Packet: " + e.getPacketName().toUpperCase());
		
		if (RebugsAntiCheatSwitcherPlugin.AntiCheats.isEmpty() && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false)) 
		{
			RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "AntiCheats was Empty, trying to add ACs!");
			ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
		}
		if (RebugsAntiCheatSwitcherPlugin.AntiCheats.isEmpty())
		{
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false))
				RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "AntiCheats was still Empty!");

			return;
		}
		for (String outgoing : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.replace-message-text"))
		{
			message = message.replace(outgoing.toLowerCase(), "");
		}
		String[] Alert_Related = PT.SplitString(message);
		int min = FileUtils.getLoadedAntiCheatsFile().getInt("minimum-message-length");
		min = min < 1 ? 1 : min;
		if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-length"))
			RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "raw length: " + message.length() + " casted length: " + Alert_Related.length);
			
		if (Alert_Related.length < min) return;
		
		if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message"))
			RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "per player alerts message: " + message);
		
		if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.casted-alert-message"))
		{
			RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "Casted Alert Message (0/first): " + Alert_Related[0]);
			if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug-to-players"))
				user.sendMessage("Casted Alert Message (0/first): " + Alert_Related[0]);
		}
		
		int Amount = Alert_Related.length;
		String connedAntiCheat = user.getAntiCheat().toLowerCase();
		boolean Cancelled = false, Detected_Alert_MSG = false;
		try
		{
			for (Map.Entry<String, String> map : RebugsAntiCheatSwitcherPlugin.AntiCheats.entrySet()) 
			{
				String alert = map.getValue().toLowerCase(), alert_message = ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile()
				.getString("loaded-anticheats." + alert.toLowerCase() + ".alert-message"));
				alert_message = ChatColor.stripColor(alert_message);
				alert_message = alert_message.trim();
				alert = alert.trim();
				
				if (Alert_Related[0].equalsIgnoreCase(alert_message)) 
				{
					Detected_Alert_MSG = true;
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.contains-alert-message"))
						RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "Contains AntiCheat Alert Message: " + alert_message);
						
					boolean landed = false, setbacked = false, punishedd = false;
					int username = 1;
					for (String scan : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.alerts"))
					{
						String[] toText = PT.SplitString(scan);
						int found = toText.length, finding = 0;
						for (int checking = 1; checking < Amount; checking ++)
						{
							for (int scanning = 0; scanning < toText.length; scanning ++)
							{
								for (Player p : Bukkit.getOnlinePlayers())
								{
									if (Alert_Related[checking].equalsIgnoreCase(toText[scanning].replace("%rebug_user%", p.getName()).replace("%rebug_player%", p.getName())))
										finding ++;
									
									if (finding == found)
									{
										landed = true;
										break;
									}
								}
							}
						}
						if (landed)
						{
							username = FileUtils.getLoadedAntiCheatsFile().getInt("alerts-to-scan-for.alerts-user-numbers." + scan.toLowerCase().replace(" ", "-").replace("%", ""), -Integer.MAX_VALUE);
							if (username ==- Integer.MAX_VALUE)
							{
								username = 1;
								if (RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false))
									RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "AC: " + alert + " Failed to get Username Number");
							}
							break;
						}
					}
					if (landed)
					{
						if (FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.setback").size() > 0)
						{
							for (String scan : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.setback"))
							{
								for (int checking = 1; checking < Amount; checking ++)
								{
									if (Alert_Related[checking].equalsIgnoreCase(scan))
									{
										setbacked = true;
										break;
									}
								}
							}
						}
						if (FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.punished").size() > 0)
						{
							for (String scan : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.punished"))
							{
								for (int checking = 1; checking < Amount; checking ++)
								{
									if (Alert_Related[checking].equalsIgnoreCase(scan))
									{
										punishedd = true;
										break;
									}
								}
							}
						}
						if (!Alert_Related[username].equalsIgnoreCase(user.getName()))
						{
							Cancelled = true;
							RebugsAntiCheatSwitcherPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Flag (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
							user.setPreReceivePacket(user.getPreReceivePacket() - 1);
							e.setCancelled(Cancelled);
							break;
						}
						if (Alert_Related[username].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || punishedd && !user.getSettingsBooleans().getOrDefault("Punishes", true) || setbacked && !user.getSettingsBooleans().getOrDefault("Setbacks", true) || !punishedd && !setbacked && !user.getSettingsBooleans().getOrDefault("Flags", true))) 
						{
							Cancelled = true;
							RebugsAntiCheatSwitcherPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Flag - AC isn't the same as the User's tho or user setting (show: flags or setbacks or punished) was off! (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
							user.setPreReceivePacket(user.getPreReceivePacket() - 1);
							e.setCancelled(Cancelled);
							break;
						}
						break;
					}
				}
			}
			if (Alert_Related.length >= min)
			{
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.identifier", false))
					RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "identifier: " + e.getPacketName());
				
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-alert-message-detected"))
					RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "was Alert Detected: " + (Detected_Alert_MSG ? "Yes" : "No") + " Catched MSG: " + Alert_Related[0]);
					
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-message-cancelled"))
				{
					RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "was MSG Cancelled: " + (Cancelled ? "Yes" : "No"));
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-cancelled-debug-player"))
						RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "AC: " + user.getAntiCheat() + " NumberID: " + user.getNumberIDs() + " Selected: " + user.getSelectedAntiCheats());
					
				}
			}
		}
		catch (Exception exception) 
		{
			exception.printStackTrace();
		}
	}
	@Override
	public void onPacketReceive (PacketReceiveEvent e)
	{
		if (e.getUser() == null || e.getUser().getUUID() == null) return;
		
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;

		User user = RebugsAntiCheatSwitcherPlugin.getUser(player);
		if (user == null) return;
		user.setPreSendPacket(user.getPreSendPacket() + 1);
		final double distX = user.getPlayer().getLocation().getX() - user.getLastTickPosX(),
				distY = user.getPlayer().getLocation().getY() - user.getLastTickPosY(),
				distZ = user.getPlayer().getLocation().getZ() - user.getLastTickPosZ(),
				bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0, bpsY = Math.sqrt(distY * distY) * 20.0;
		
		if (e.getPacketType() == PacketType.Play.Client.ANIMATION)
			user.setPreCPSLeft(user.getPreCPSLeft() + 1);
		
		if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
		{
			WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
			String data = new String (((WrapperPlayClientPluginMessage) packet).getData()).replaceAll("\\P{Print}", "");
			Component Chat = Component.text(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + e.getPacketName());
			final String StartHover = e.getPacketName() + " | Client\n\n";
			if (packet.getChannelName().equalsIgnoreCase("register") || packet.getChannelName().equalsIgnoreCase("minecraft:register") || packet.getChannelName().equalsIgnoreCase("minecraft|register"))
			{
				user.setRegister(data);
				if (user.getSettingsBooleans().getOrDefault("Register Packet", false))
				{
					Chat = Chat.hoverEvent(Component.text(StartHover + "Channel: " + ((WrapperPlayClientPluginMessage) packet).getChannelName() + "\n\nData: " + "\nBytes: " + ((WrapperPlayClientPluginMessage) packet).getData() + "\nString: " + 
						    data + "\n\nClick me to Suggest Data as a Command")).clickEvent(ClickEvent.suggestCommand("" + data));
					PT.sendChat(user, e.getPacketName(), ChatTypes.CHAT, Chat);
				}
				return;
			}
			if (packet.getChannelName().equalsIgnoreCase("mc|brand") || packet.getChannelName().equalsIgnoreCase("mc:brand") || packet.getChannelName().equalsIgnoreCase("minecraft|brand") || packet.getChannelName().equalsIgnoreCase("minecraft:brand"))
			{
				if (!user.isSetBrand())
				{
					user.setClientBrand(data);
					if (user.getSettingsBooleans().getOrDefault("ClientBrand Packet", false))
					{
						Chat = Chat.hoverEvent(Component.text(StartHover + "Channel: " + ((WrapperPlayClientPluginMessage) packet).getChannelName() + "\n\nData: " + "\nBytes: " + ((WrapperPlayClientPluginMessage) packet).getData() + "\nString: " + 
							    data + "\n\nClick me to Suggest Data as a Command")).clickEvent(ClickEvent.suggestCommand("" + data));
						PT.sendChat(user, e.getPacketName(), ChatTypes.CHAT, Chat);
					}
					user.setSetBrand(true);
				}
				return;
			}
			if (user.getSettingsBooleans().getOrDefault("Other CustomPayLoad", false))
			{
				Chat = Chat.hoverEvent(Component.text(StartHover + "Channel: " + ((WrapperPlayClientPluginMessage) packet).getChannelName() + "\n\nData: " + "\nBytes: " + ((WrapperPlayClientPluginMessage) packet).getData() + "\nString: " + 
					    data + "\n\nClick me to Suggest Data as a Command")).clickEvent(ClickEvent.suggestCommand("" + data));
				PT.sendChat(user, e.getPacketName(), ChatTypes.CHAT, Chat);
				return;
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY)
		{
			WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(e);
			if (packet.getAction() != InteractAction.ATTACK || !user.getSettingsBooleans().getOrDefault("Hitbox Debugger", false) && !user.getSettingsBooleans().getOrDefault("RayTrace Debugger", false) && !user.getSettingsBooleans().getOrDefault("Reach Debugger", false)) return;
			
			final int entityId = packet.getEntityId();
			Scheduler.run(new Runnable()
			{
				@Override
				public void run()
				{
					Entity entity = null;
					for (Entity ee : user.getPlayer().getWorld().getEntities())
					{
						if (ee.getEntityId() == entityId)
						{
							entity = ee;
							break;
						}
					}
					if (entity == null) return;

					if (entity instanceof Player)
					{
						// TODO Send Particles to show the players HitBox and RayTrace!
						if (user.getSettingsBooleans().getOrDefault("Hitbox Debugger", false))
						{
							// Flame Particles
						}
						if (user.getSettingsBooleans().getOrDefault("RayTrace Debugger", false))
						{
							// TODO Make
							Particle<?> Part = new Particle<ParticleData> (ParticleTypes.ENCHANTED_HIT);
							PacketEvents.getAPI().getPlayerManager().getUser(user.getPlayer()).sendPacket(
							new WrapperPlayServerParticle(Part, true, new Vector3d(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ()), new Vector3f(1, 1, 1), 1, 100, true));
		                }
					}
					if (user.getSettingsBooleans().getOrDefault("Reach Debugger", false))
						PT.sendChat(user.getPlayer(), RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Reach Debugger", ChatTypes.CHAT, 
						Component.text(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + "Reach Debugger").hoverEvent(Component.text("You:\nX: " + user.getPlayer().getLocation().getX() + "\nY: " + user.getPlayer().getLocation().getY() + "\nZ: " + user.getPlayer().getLocation().getZ() + "\n\nTarget:\nX: " + entity.getLocation().getX() + "\nY: " + entity.getLocation().getY() + "\nZ: " + entity.getLocation().getZ() + "\n\nDistance: " + user.getPlayer().getLocation().distance(entity.getLocation()) + "\n\nYour Name: " + user.getName() + "\nYour EntityID: " + user.getPlayer().getEntityId() + "\nTarget Name: " + entity.getName() + "\nTarget Type: " + entity.getType().name() + "\nTarget EntityID: " + entity.getEntityId())));
					
				}
			});
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING)
		{
			user.setLastTickPosX(user.getPlayer().getLocation().getX());
			user.setLastTickPosY(user.getPlayer().getLocation().getY());
			user.setLastTickPosZ(user.getPlayer().getLocation().getZ());
			user.setOnGround(new WrapperPlayClientPlayerFlying(e).isOnGround());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) 
		{
			WrapperPlayClientPlayerPosition packet = new WrapperPlayClientPlayerPosition(e);
			user.setOnGround(packet.isOnGround());
			if (!user.isMoving())
			{
				user.setBPSY(former.format(0));
				user.setBPSXZ(former.format(0));
			}
			else
			{
				user.setBPSXZ(former.format(bpsXZ));
				user.setBPSY(former.format(bpsY));
			}
			user.setTimer_balance(e.getTimestamp());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
		{
			WrapperPlayClientPlayerPositionAndRotation packet = new WrapperPlayClientPlayerPositionAndRotation(e);
			user.setOnGround(packet.isOnGround());
			if (!user.isMoving())
			{
				user.setBPSY(former.format(0));
				user.setBPSXZ(former.format(0));
			}
			else
			{
				user.setBPSXZ(former.format(bpsXZ));
				user.setBPSY(former.format(bpsY));
			}
			user.setTimer_balance(e.getTimestamp());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) 
			user.setOnGround(new WrapperPlayClientPlayerRotation(e).isOnGround());
		
		if (e.getPacketType() == PacketType.Play.Client.ENTITY_ACTION)
		{
			WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e);
			if (packet.getAction() == Action.STOP_SPRINTING || packet.getAction() == Action.START_SPRINTING) 
			{
				boolean start = packet.getAction() == Action.START_SPRINTING;
				user.setSprinting(start);
			}
			if (packet.getAction() == Action.STOP_SNEAKING || packet.getAction() == Action.START_SNEAKING) {
				boolean start = packet.getAction() == Action.START_SNEAKING;
				user.setSneaking(start);
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE)
		{
			Scheduler.run(new Runnable() 
			{
				@Override
				public void run() 
				{
					if (ServerVersionUtil.isServerNewerThan1_8())
						user.setBlocking(PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isNewerThan(ClientVersion.V_1_8) && PT.isBlockingPossibleCheck(user.getPlayer().getInventory().getItemInMainHand()) || PT.isBlockingPossibleCheck(new ItemStack[] {user.getPlayer().getInventory().getItemInOffHand(), user.getPlayer().getInventory().getItemInMainHand()}) && user.getPlayer().isBlocking());
					else
						user.setBlocking(PT.isBlockingPossibleCheck(user.getPlayer().getItemInHand()) && user.getPlayer().isBlocking());
				}
			});
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING)
			user.setBlocking(false);
		
		if (e.getPacketType() == PacketType.Play.Client.USE_ITEM)
		{
			user.setPreCPSRight(user.getPreCPSRight() + 1);
			WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(e);
			if (ServerVersionUtil.isServerNewerThan1_8())
			{
				if (PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isNewerThan(ClientVersion.V_1_8) && PT.isBlockingPossibleCheck(user.getPlayer().getInventory().getItemInMainHand()) || PT.isBlockingPossibleCheck(packet.getHand() == InteractionHand.OFF_HAND ? user.getPlayer().getInventory().getItemInOffHand() : user.getPlayer().getInventory().getItemInMainHand()))
					user.setBlocking(true);
			}
			else if (PT.isBlockingPossibleCheck(packet.getHand() == InteractionHand.OFF_HAND ? user.getPlayer().getInventory().getItemInOffHand() : user.getPlayer().getItemInHand()))
				user.setBlocking(true);
		}
		
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			user.setPreCPSRight(user.getPreCPSRight() + 1);
			WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(e);
			if (ServerVersionUtil.isServerNewerThan1_8())
			{
				if (PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isNewerThan(ClientVersion.V_1_8) && PT.isBlockingPossibleCheck(user.getPlayer().getInventory().getItemInMainHand()) || PT.isBlockingPossibleCheck(packet.getHand() == InteractionHand.OFF_HAND ? user.getPlayer().getInventory().getItemInOffHand() : user.getPlayer().getInventory().getItemInMainHand()))
					user.setBlocking(true);
			}
			else if (PacketEvents.getAPI().getPlayerManager().getClientVersion(player).isNewerThan(ClientVersion.V_1_8) && PT.isBlockingPossibleCheck(user.getPlayer().getInventory().getItemInMainHand()) || PT.isBlockingPossibleCheck(packet.getHand() == InteractionHand.OFF_HAND ? user.getPlayer().getInventory().getItemInOffHand() : user.getPlayer().getItemInHand()))
				user.setBlocking(true);
		}
	}
}
