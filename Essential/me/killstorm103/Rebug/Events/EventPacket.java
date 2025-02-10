package me.killstorm103.Rebug.Events;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTimeUpdate;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.killstorm103.Rebug.RebugPlugin;
import me.killstorm103.Rebug.Utils.FileUtils;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.Scheduler;
import me.killstorm103.Rebug.Utils.ServerVersionUtil;
import me.killstorm103.Rebug.Utils.User;

@SuppressWarnings("deprecation")
public class EventPacket implements PacketListener
{
	private final DecimalFormat former = new DecimalFormat("#.###");

	@Override
	public void onPacketSend (PacketSendEvent e) 
	{
		if (e.getUser() == null || e.getUser().getUUID() == null) return;
		
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;

		User user = RebugPlugin.getUser(player);
		if (user == null) return;
		
		user.setPreReceive(user.getPreReceive() + 1);
		
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
			e.setCancelled(true);
			user.setPreReceive(user.getPreReceive() - 1);
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
						if (ServerVersionUtil.isServerNewerOrEquals1_19())
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
			WrapperPlayServerSystemChatMessage packet = new WrapperPlayServerSystemChatMessage(e);
			try
			{
				if (!RebugPlugin.getSettingsBooleans().getOrDefault("Per Player Alerts", false))
					return;
				
				String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(packet.getMessage().asComponent()));
				message = ChatColor.stripColor(message).toLowerCase();
				
				if (PT.isStringNull(message)) return;
				
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.past-chat-type"))
					RebugPlugin.getINSTANCE().Log(Level.INFO, "made it past chat type check");
				
				if (RebugPlugin.End_Game_AntiCheats.isEmpty() && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false)) 
				{
					RebugPlugin.getINSTANCE().Log(Level.INFO, "Rebug.anticheats was Empty, trying to add ACs!");
					ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
				}
				if (RebugPlugin.End_Game_AntiCheats.isEmpty())
				{
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false))
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "Rebug AntiCheats was still Empty!");

					return;
				}
				
				try
				{
					for (String outgoing : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.replace-message-text"))
					{
						message = message.replace(outgoing.toLowerCase(), "");
					}
				}
				catch (Exception ee) 
				{
					ee.printStackTrace();
				}
				
				String[] Alert_Related = PT.SplitString(message);
				int min = FileUtils.getLoadedAntiCheatsFile().getInt("minimum-message-length");
				min = min < 1 ? 1 : min;
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-length"))
					RebugPlugin.getINSTANCE().Log(Level.INFO, "raw length: " + message.length() + " casted length: " + Alert_Related.length);
					
				if (Alert_Related.length < min)
					return;
				
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message"))
					RebugPlugin.getINSTANCE().Log(Level.INFO, "per player alerts message: " + message);
				
				if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.casted-alert-message"))
				{
					RebugPlugin.getINSTANCE().Log(Level.INFO, "Casted Alert Message (0/first): " + Alert_Related[0]);
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug-to-players"))
						user.sendMessage("Casted Alert Message (0/first): " + Alert_Related[0]);
				}
				
				int Amount = Alert_Related.length;
				String connedAntiCheat = user.getAntiCheat().toLowerCase();
				boolean Cancelled = false, Detected_Alert_MSG = false;
				
				for (Map.Entry<String, String> map : RebugPlugin.End_Game_AntiCheats.entrySet()) 
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
							RebugPlugin.getINSTANCE().Log(Level.INFO, "Contains AntiCheat Alert Message: " + alert_message);
							
						boolean landed = false, setbacked = false, punishedd = false;
						int username = 1;
						String[] toText = null;
						for (String scan : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.alerts"))
						{
							toText = PT.SplitString(scan);
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
								try
								{
									username = FileUtils.getLoadedAntiCheatsFile().getInt("alerts-to-scan-for.alerts-user-numbers." + scan.toLowerCase().replace(" ", "-").replace("%", ""));
								}
								catch (Exception f) 
								{
									username = 1;
									if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
										f.printStackTrace();
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
								RebugPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Flag (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
								user.setPreReceive(user.getPreReceive() - 1);
								e.setCancelled(Cancelled);
								break;
							}
							if (Alert_Related[username].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || punishedd && !user.getSettingsBooleans().getOrDefault("Punishes", true) || setbacked && !user.getSettingsBooleans().getOrDefault("Setbacks", true) || !punishedd && !setbacked && !user.getSettingsBooleans().getOrDefault("Flags", true))) 
							{
								Cancelled = true;
								RebugPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Flag - AC isn't the same as the User's tho or user setting (show: flags or setbacks or punished) was off! (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
								user.setPreReceive(user.getPreReceive() - 1);
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
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "identifier: " + e.getPacketName());
						
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-alert-message-detected", false))
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "was Alert Detected: " + (Detected_Alert_MSG ? "Yes" : "No") + " Catched MSG: " + Alert_Related[0]);
						
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-message-cancelled", false))
					{
						RebugPlugin.getINSTANCE().Log(Level.WARNING, "was MSG Cancelled: " + (Cancelled ? "Yes" : "No"));
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-cancelled-debug-player", false))
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "AC: " + user.getAntiCheat() + " NumberID: " + user.getNumberIDs() + " Selected: " + user.getSelectedAntiCheats());
						
					}
				}
			}
			catch (Exception f) 
			{
				f.printStackTrace();
			}
		}
		if (e.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE)
		{
			WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(e);
			try
			{
				if (!RebugPlugin.getSettingsBooleans().getOrDefault("Per Player Alerts", false))
					return;
				
				if (packet.getMessage().getType().equals(ChatTypes.CHAT) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.chat") || packet.getMessage().getType().equals(ChatTypes.SYSTEM) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.system") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND_OUTGOING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command_outgoing") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND_INCOMING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command_incoming") || packet.getMessage().getType().equals(ChatTypes.TEAM_MSG_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.team_msg_command") || packet.getMessage().getType().equals(ChatTypes.SAY_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.say_command") || packet.getMessage().getType().equals(ChatTypes.RAW) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.raw") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND_OUTGOING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command_outgoing") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND_INCOMING) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command_incoming") || packet.getMessage().getType().equals(ChatTypes.MSG_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.msg_command") || packet.getMessage().getType().equals(ChatTypes.GAME_INFO) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.game_info") || packet.getMessage().getType().equals(ChatTypes.EMOTE_COMMAND) && FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.check-chat-types.emote_command")) 
				{
					String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(packet.getMessage().getChatContent()));
					message = ChatColor.stripColor(message).toLowerCase();
					
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.past-chat-type"))
						RebugPlugin.getINSTANCE().Log(Level.INFO, "made it past chat type check");
					
					if (RebugPlugin.End_Game_AntiCheats.isEmpty() && FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false)) 
					{
						RebugPlugin.getINSTANCE().Log(Level.INFO, "Rebug.anticheats was Empty, trying to add ACs!");
						ItemsAndMenusUtils.getINSTANCE().getAntiCheats();
					}
					if (RebugPlugin.End_Game_AntiCheats.isEmpty())
					{
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory", false))
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "Rebug AntiCheats was still Empty!");

						return;
					}
					
					try
					{
						for (String outgoing : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.replace-message-text"))
						{
							message = message.replace(outgoing.toLowerCase(), "");
						}
					}
					catch (Exception ee) 
					{
						ee.printStackTrace();
					}
					
					String[] Alert_Related = PT.SplitString(message);
					int min = FileUtils.getLoadedAntiCheatsFile().getInt("minimum-message-length");
					min = min < 1 ? 1 : min;
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-length"))
						RebugPlugin.getINSTANCE().Log(Level.INFO, "raw length: " + message.length() + " casted length: " + Alert_Related.length);
						
					if (Alert_Related.length < min)
						return;
					
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message"))
						RebugPlugin.getINSTANCE().Log(Level.INFO, "per player alerts message: " + message);
					
					if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.casted-alert-message"))
					{
						RebugPlugin.getINSTANCE().Log(Level.INFO, "Casted Alert Message (0/first): " + Alert_Related[0]);
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug-to-players"))
							user.sendMessage("Casted Alert Message (0/first): " + Alert_Related[0]);
					}
					
					int Amount = Alert_Related.length;
					String connedAntiCheat = user.getAntiCheat().toLowerCase();
					boolean Cancelled = false, Detected_Alert_MSG = false;
					
					for (Map.Entry<String, String> map : RebugPlugin.End_Game_AntiCheats.entrySet()) 
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
								RebugPlugin.getINSTANCE().Log(Level.INFO, "Contains AntiCheat Alert Message: " + alert_message);
								
							boolean landed = false, setbacked = false, punishedd = false;
							int username = 1;
							String[] toText = null;
							for (String scan : FileUtils.getLoadedAntiCheatsFile().getStringList("alerts-to-scan-for.alerts"))
							{
								toText = PT.SplitString(scan);
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
									try
									{
										username = FileUtils.getLoadedAntiCheatsFile().getInt("alerts-to-scan-for.alerts-user-numbers." + scan.toLowerCase().replace(" ", "-").replace("%", ""));
									}
									catch (Exception f) 
									{
										username = 1;
										if (RebugPlugin.getSettingsBooleans().getOrDefault("Debug", false))
											f.printStackTrace();
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
									RebugPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Flag (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
									user.setPreReceive(user.getPreReceive() - 1);
									e.setCancelled(Cancelled);
									break;
								}
								if (Alert_Related[username].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || punishedd && !user.getSettingsBooleans().getOrDefault("Punishes", true) || setbacked && !user.getSettingsBooleans().getOrDefault("Setbacks", true) || !punishedd && !setbacked && !user.getSettingsBooleans().getOrDefault("Flags", true))) 
								{
									Cancelled = true;
									RebugPlugin.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Flag - AC isn't the same as the User's tho or user setting (show: flags or setbacks or punished) was off! (P-AC " + user.getAntiCheat() + " A-AC " + alert + ")");
									user.setPreReceive(user.getPreReceive() - 1);
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
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "identifier: " + e.getPacketName());
						
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-alert-message-detected"))
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "was Alert Detected: " + (Detected_Alert_MSG ? "Yes" : "No") + " Catched MSG: " + Alert_Related[0]);
							
						if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.was-message-cancelled"))
						{
							RebugPlugin.getINSTANCE().Log(Level.WARNING, "was MSG Cancelled: " + (Cancelled ? "Yes" : "No"));
							if (FileUtils.getLoadedAntiCheatsFile().getBoolean("alerts-to-scan-for.debug.message-cancelled-debug-player"))
								RebugPlugin.getINSTANCE().Log(Level.WARNING, "AC: " + user.getAntiCheat() + " NumberID: " + user.getNumberIDs() + " Selected: " + user.getSelectedAntiCheats());
							
						}
					}
				}
			}
			catch (Exception f) 
			{
				f.printStackTrace();
			}
		}
	}
	@Override
	public void onPacketReceive (PacketReceiveEvent e)
	{
		if (e.getUser() == null || e.getUser().getUUID() == null) return;
		
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;

		User user = RebugPlugin.getUser(player);
		if (user == null) return;
		user.setPreSend(user.getPreSend() + 1);
		final double distX = user.getPlayer().getLocation().getX() - user.getLastTickPosX(),
				distY = user.getPlayer().getLocation().getY() - user.getLastTickPosY(),
				distZ = user.getPlayer().getLocation().getZ() - user.getLastTickPosZ(),
				bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0, bpsY = Math.sqrt(distY * distY) * 20.0;
		
		if (e.getPacketType() == PacketType.Play.Client.ANIMATION)
			user.setPreCPSLeft(user.getPreCPSLeft() + 1);
		
		if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
		{
			WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
			if (packet.getChannelName().equalsIgnoreCase("register") || packet.getChannelName().equalsIgnoreCase("minecraft:register") || packet.getChannelName().equalsIgnoreCase("minecraft|register"))
			{
				try 
				{
					user.setRegister(new String(packet.getData(), "UTF-8").replace("", ""));
					if (user.getSettingsBooleans().getOrDefault("Register Packet", false))
						user.sendMessage("Your Register is" + ChatColor.GRAY + ": " + user.getRegister());
				}
				catch (UnsupportedEncodingException e1) 
				{
					if (user.getSettingsBooleans().getOrDefault("Register Packet", false))
						user.sendMessage("Failed to read your Register Packet!");
				}
				return;
			}
			if (packet.getChannelName().equalsIgnoreCase("mc|brand") || packet.getChannelName().equalsIgnoreCase("mc:brand") || packet.getChannelName().equalsIgnoreCase("minecraft|brand") || packet.getChannelName().equalsIgnoreCase("minecraft:brand"))
			{
				if (!user.isSetBrand())
				{
					try
					{
						user.setClientBrand(new String(packet.getData(), "UTF-8").substring(1));
						if (user.getSettingsBooleans().getOrDefault("ClientBrand Packet", false))
							user.sendMessage("Your ClientBrand is" + ChatColor.GRAY + ": " + user.getClientBrand());
					}
					catch (Exception e1) 
					{
						user.setClientBrand("vanilla");
						if (user.getSettingsBooleans().getOrDefault("ClientBrand Packet", false))
							user.sendMessage("Failed to get your client brand assuming your using vanilla!");
					}
					user.setSetBrand(true);
				}
				return;
			}
			if (user.getSettingsBooleans().getOrDefault("Other CustomPayLoad", false))
			{
				try
				{
					user.sendMessage("Channel: " + packet.getChannelName() + " Data: " + new String(packet.getData(), "UTF-8").replace("", ""));
				}
				catch (Exception eee) 
				{
					user.sendMessage("Failed to read your Other CustomPayLoad Packet!");
				}
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY)
		{
			WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(e);
			if (packet.getAction() != InteractAction.ATTACK || !user.getSettingsBooleans().getOrDefault("Hitbox Debugger", false) && !user.getSettingsBooleans().getOrDefault("RayTrace Debugger", false) && !user.getSettingsBooleans().getOrDefault("Reach Debugger", false)) return;
			
			int entityId = packet.getEntityId();
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
					if (entity != null && user.getSettingsBooleans().getOrDefault("Reach Debugger", false))
						user.sendMessage("distance: " + user.getPlayer().getLocation().distance(entity.getLocation()) + " from: " + entity.getName());
				}
			});
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING)
		{
			WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(e);
			user.setLastTickPosX(user.getPlayer().getLocation().getX());
			user.setLastTickPosY(user.getPlayer().getLocation().getY());
			user.setLastTickPosZ(user.getPlayer().getLocation().getZ());
			user.setOnGround(packet.isOnGround());
			PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark())}, new int[] {0}));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) 
		{
			WrapperPlayClientPlayerPosition packet = new WrapperPlayClientPlayerPosition(e);
			user.setOnGround(packet.isOnGround());
			user.setBPSY(former.format(bpsY));
			user.setBPSXZ(former.format(bpsXZ));
			user.setTimer_balance(e.getTimestamp());
			PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark()), ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + user.getBPSY(), ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + user.getBPSXZ(), ChatColor.DARK_RED + "TB " + (e.getTimestamp() - user.getTimer_balance()) + "ms"}, new int[] {0, 6, 6, 4}));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
			WrapperPlayClientPlayerPositionAndRotation packet = new WrapperPlayClientPlayerPositionAndRotation(e);
			user.setOnGround(packet.isOnGround());
			user.setBPSXZ(former.format(bpsXZ));
			user.setBPSY(former.format(bpsY));
			user.setTimer_balance(e.getTimestamp());
			PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + user.getBPSY(), ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + user.getBPSXZ(), ChatColor.DARK_RED + "TB " + (e.getTimestamp() - user.getTimer_balance()) + "ms", ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + PT.getTickMark(): PT.getXMark())}, new int[] {6, 6, 4, 0}));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {
			WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
			user.setOnGround(packet.isOnGround());
			PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark())}, new int[] {0}));
		}
		if (e.getPacketType() == PacketType.Play.Client.ENTITY_ACTION)
		{
			WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e);
			if (packet.getAction() == Action.STOP_SPRINTING || packet.getAction() == Action.START_SPRINTING) 
			{
				boolean start = packet.getAction() == Action.START_SPRINTING;
				user.setSprinting(start);
				PT.UpdateScoreBoard(user, new String[] {ChatColor.DARK_RED + "Sprinting " + (start ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark())}, new int[] {2}, new String[] {"sprinting"});
			}
			if (packet.getAction() == Action.STOP_SNEAKING || packet.getAction() == Action.START_SNEAKING) {
				boolean start = packet.getAction() == Action.START_SNEAKING;
				user.setSneaking(start);
			}
			if (packet.getAction() == Action.STOP_SNEAKING || packet.getAction() == Action.START_SNEAKING || packet.getAction() == Action.STOP_SPRINTING || packet.getAction() == Action.START_SPRINTING)
				PT.CallEvent(new EventUpdateScore(user.getPlayer(), new String[] {ChatColor.DARK_RED + "Sprinting " + (user.isSprinting() ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark()), ChatColor.DARK_RED + "Sneaking " + (user.isSneaking() ? ChatColor.GREEN + PT.getTickMark() : PT.getXMark())}, new int[] {2, 1}));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING)
		{
			WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(e);
			if (!ServerVersionUtil.isServerNewerThan1_8() && user.isBlocking() && (packet.getAction() == DiggingAction.RELEASE_USE_ITEM || packet.getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND || packet.getAction() == DiggingAction.DROP_ITEM || packet.getAction() == DiggingAction.DROP_ITEM_STACK)) 
			{
				user.setBlocking(false);
				PT.UpdateScoreBoard(user, new String[] {ChatColor.DARK_RED + "Blocking " + PT.getXMark()}, new int[] {3}, new String[] {"blocking"});
			}
		}
	}
}
