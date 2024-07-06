package me.killstorm103.Rebug.PacketEvents;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class EventPackets implements PacketListener
{
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;
			
		User user = Rebug.getUser(player);
		if (user == null) return;
		ItemStack item = null;
		user.preSend ++;
		if (e.getPacketType() == PacketType.Play.Client.ANIMATION)
			user.preCPS ++;
		
		if (e.getPacketType() == PacketType.Play.Client.USE_ITEM)
		{
			WrapperPlayClientUseItem clientUseItem = new WrapperPlayClientUseItem(e);
			user.sendMessage("hand= " + clientUseItem.getHand().name());
		}
		
		if (e.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS)
		{
			WrapperPlayClientResourcePackStatus rs = new WrapperPlayClientResourcePackStatus(e);
			user.sendMessage("Resource Pact Status: Hash= " + rs.getHash() + " Result= " + rs.getResult());
		}
		if (e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE)
		{
			user.getPlayer().updateInventory();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 3, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X");
		}
		
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			item = user.getPlayer().getItemInHand();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 3, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X");
			if (PT.isHoldingSword(item))
				Rebug.getINSTANCE().UpdateScoreBoard(user, 3, ChatColor.DARK_RED + "Blocking " + ChatColor.GREEN + Rebug.GetMain().getTickMark());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING)
		{
			WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(e);
			if (packet.getAction() == DiggingAction.RELEASE_USE_ITEM || packet.getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND)
				Rebug.getINSTANCE().UpdateScoreBoard(user, 3, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X");
		}
		if (e.getPacketType() == PacketType.Play.Client.ENTITY_ACTION)
		{
			WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e);
			if (packet.getAction() == Action.STOP_SPRINTING || packet.getAction() == Action.START_SPRINTING)
			{
				boolean start = packet.getAction() == Action.START_SPRINTING;
				Rebug.getINSTANCE().UpdateScoreBoard(user, 2, ChatColor.DARK_RED + "Sprinting " + (start ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
			}
			if (packet.getAction() == Action.STOP_SNEAKING || packet.getAction() == Action.START_SNEAKING)
			{
				boolean start = packet.getAction() == Action.START_SNEAKING;
				Rebug.getINSTANCE().UpdateScoreBoard(user, 1, ChatColor.DARK_RED + "Sneaking " + (start ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING)
		{
			WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(e);
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 0, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION)
		{
			WrapperPlayClientPlayerPosition packet = new WrapperPlayClientPlayerPosition(e);
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 0, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
		{
			WrapperPlayClientPlayerPositionAndRotation packet = new WrapperPlayClientPlayerPositionAndRotation(e);
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 0, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION)
		{
			WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
			Rebug.getINSTANCE().UpdateScoreBoard(user, 0, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.GetMain().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"));
		}
		if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
		{
			WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
			if (packet.getChannelName().equalsIgnoreCase("Register"))
			{
				try
				{
					user.setRegister(new String(packet.getData(), "UTF-8").substring(0));
					if (Config.TellClientRegisters())
						user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getRegister());
					
					Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getRegister());
				}
				catch (Exception ee) 
				{
					ee.printStackTrace();
				}
			}
			if (packet.getChannelName().equalsIgnoreCase("MC|Brand"))
			{
		    	if (Config.getAllowedToOverRideClientBrand())
		    		user.BrandSetCount = 0;
		    	
		    	if (user.BrandSetCount > 0)
		    	{
		    		if (Config.getCantOverrideClientBrand().equalsIgnoreCase("kick"))
		    			PT.KickPlayer(user.getPlayer(), "Rebug's already collected your client brand you shound't be sending another client brand Packet!");
		    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("ban"))
		    			PT.BanPlayer(user.getPlayer(), " Rebug's already collected your client brand you shound't be sending another client brand Packet!");
		    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("warn"))
		    			user.getPlayer().sendMessage(Rebug.RebugMessage + "Already collected your client brand you shound't be sending another client brand Packet!");
		    		
		    		return;
		    	}
		    	try
		    	{
					if (user.BrandSetCount < 1)
		    		{
						user.setBrand(new String(packet.getData(), "UTF-8").substring(1));
						if (user.getBrand() == null || user.getBrand().length() <= 0)
				    	{
				    		if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
			    				user.getPlayer().sendMessage(Rebug.RebugMessage + "Failed to load your client brand this may cause issues!");
			    			
			    			if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
			    			{
			    				PT.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
			    				return;
			    			}
			    			if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
			    			{
			    				PT.BanPlayer(user.getPlayer(), "Rebug banned you due to failing to load your client brand!");
			    				return;
			    			}
				    	}
						if (user.BrandSetCount < 1)
						{
							Bukkit.getScheduler().runTaskLater(Rebug.GetMain(), new Runnable()
							{
								
								@Override
								public void run() {
									if (Config.TellClientBrandOnJoin())
										user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getBrand() + " " + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
								
									Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "client brand " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getBrand() + " " + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
								}
							}, 11);
						}
						user.BrandSetCount ++;
						if (user.BrandSetCount > Integer.MAX_VALUE)
						{
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "User " + user.getPlayer().getName() + " Set their client brand more than " + Integer.MAX_VALUE + " Resetting counter to 1");
							user.BrandSetCount = 1;
						}
						user.UnReceivedBrand = 0;
		    		}
		    	}
		    	catch (Exception ee) 
		    	{
		    		if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
	    				user.getPlayer().sendMessage(Rebug.RebugMessage + "Failed to load your client brand this may cause issues!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
	    				PT.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
	    				PT.BanPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
	    			
	    			if (Rebug.debug)
	    				ee.printStackTrace();
				}
			}
		}
		if (user.BrandSetCount > 0 || !Config.AntiCancelClientBrandPacket()) return;
		
		
		user.UnReceivedBrand ++;
		if (user.UnReceivedBrand >= (Config.AntiCancelClientBrandCounter() < 200 ? 200 : Config.AntiCancelClientBrandCounter()))
		{
			PT.KickPlayer(user.getPlayer(), ChatColor.RED + "You were kicked by " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "REBUG " + ChatColor.RED + "for not sending a Client Brand Packet!");
			return;
		}
		Rebug.Debug(user.getPlayer(), "UnReceivedBrand= " + user.UnReceivedBrand);
	}
	@SuppressWarnings("deprecation")
	@Override 
	public void onPacketSend (PacketSendEvent e)
	{
		if (Bukkit.getOnlinePlayers().isEmpty()) return;
		
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline()) return;
		
		User user = Rebug.getUser(player);
		if (user == null) return;
		user.preReceive ++;
		
		if (e.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT)
		{
			if (user.PotionEffects) return;
			
			for (PotionEffect effect : user.getPlayer().getActivePotionEffects())
			{
				if (effect != null)
				{
					if (effect.getType() == PotionEffectType.DAMAGE_RESISTANCE && user.Damage_Resistance || effect.getType() == PotionEffectType.FIRE_RESISTANCE && user.Fire_Resistance) return;
					
					user.getPlayer().removePotionEffect(PotionEffectType.getById(effect.getType().getId()));
				}
			}
			user.preReceive --;
			e.setCancelled(true);
		}
		if (e.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE)
		{
			WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(e);
			if (packet.getMessage().getType().equals(ChatTypes.CHAT) || packet.getMessage().getType().equals(ChatTypes.SYSTEM))
			{
				String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(packet.getMessage().getChatContent()));
				message = ChatColor.stripColor(message);
				message = message.trim();
				String[] inChat = StringUtils.split(message);
				// TODO: Test
				/*
				if (inChat.length > 0 && Bukkit.getOnlinePlayers().size() > 1)
				{
					for (Player p : Bukkit.getOnlinePlayers())
					{
						User used = Rebug.getUser(p);
						if (used != null && used.LastSentMessage != null)
						{
							if (message.toLowerCase().contains(user.getName().toLowerCase()) && !user.AllowAT)
								e.setCancelled(true);
							
							used.LastSentMessage = null;
						}
					}
				}
				*/
				if (!Rebug.PrivatePerPlayerAlerts) return;
				
				if (Rebug.anticheats.isEmpty())
				{
					Bukkit.getConsoleSender().sendMessage("Rebug.anticheats was Empty, trying to add ACs!");
					if (user.getPlayer().getOpenInventory() != null)
						user.getPlayer().closeInventory();
					
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getAntiCheats());
					user.getPlayer().closeInventory();
				}
				if (Rebug.anticheats.isEmpty())
				{
					Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Rebug AntiCheats was still Empty!");
					return;
				}
				
				message = message.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(">>", "").replace("<<", "").replace(">", "")
			    .replace("<", "").replace("/", "").replace("//", "").replace("Â", "").replace("ÂÂ", "").replace("ÂÂÂ", "").replace("(", "").replace(")", "");
				
				String[] Alert_Related = StringUtils.split(message);
				
				if (Alert_Related.length < 4) return;
				
				for (Map.Entry<Plugin, String> map : Rebug.anticheats.entrySet())
				{
					String alert = map.getValue(), alert_message = ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + alert.toLowerCase() + ".alert-message"));
					alert_message = ChatColor.stripColor(alert_message);
					alert_message = alert_message.trim();
					alert = alert.trim();
					
					// Flags
					if (Alert_Related[0].equalsIgnoreCase(alert_message) && Alert_Related[2].equalsIgnoreCase("failed"))
					{
						if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
						{
							Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Flag");
							user.preReceive --;
							e.setCancelled(true);
						}
						if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!user.AntiCheat.equalsIgnoreCase(alert) || !user.ShowFlags))
						{
							Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Flag - AC isn't the same as the User's tho or ShowFlags was off!");
							user.preReceive --;
							e.setCancelled(true);
						}
						break;
					}
					// Punished
					if (Alert_Related[0].equalsIgnoreCase(alert_message) && Alert_Related[2].equalsIgnoreCase("was") && Alert_Related[3].equalsIgnoreCase("punished"))
					{
						if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
						{
							Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Punished");
							user.preReceive --;
							e.setCancelled(true);
						}
						if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!user.AntiCheat.equalsIgnoreCase(alert) || !user.ShowFlags))
						{
							Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Punished - AC isn't the same as the User's tho or ShowFlags was off!");
							user.preReceive --;
							e.setCancelled(true);
						}
						break;
					}
				}
			}
		}
	}
}
