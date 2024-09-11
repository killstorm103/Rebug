package me.killstorm103.Rebug.PacketEvents;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSpectate;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.PTNormal;
import me.killstorm103.Rebug.Utils.User;

public class EventPackets implements PacketListener
{
	private final DecimalFormat former = new DecimalFormat("#.###");
	
	@SuppressWarnings("unused")
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		Player player = Bukkit.getPlayer(e.getUser().getUUID());
		if (player == null || !player.isOnline())
			return;
		
		if (Config.Experimental_Features("AntiCrasher"))
		{
			if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
			{
				WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
				float pitch = packet.getPitch(), yaw = packet.getYaw();
				if (Float.isNaN(pitch)|| Float.isInfinite(pitch) || Math.abs(pitch) > 500)
				{
					e.setCancelled(true);
					PTNormal.KickPlayer(player, ChatColor.DARK_RED + "Rebug | AntiCrasher | illegal Pitch | " + pitch);
				}
			}
			if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION)
			{
				WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
				float pitch = packet.getPitch(), yaw = packet.getYaw();
				if (Float.isNaN(pitch) || Float.isInfinite(pitch) || Math.abs(pitch) > 500)
				{
					e.setCancelled(true);
					PTNormal.KickPlayer(player, ChatColor.DARK_RED + "Rebug | AntiCrasher | illegal Pitch | " + pitch);
				}
			}
		}
		User user = Rebug.getUser(player);
		if (user == null) 
		{
			if (Rebug.PacketDebuggerPlayers.containsKey(player.getUniqueId()))
			{
				FileConfiguration config = Rebug.getINSTANCE().getPlayerConfigFile(player);
				if (config != null)
				{
					final String Section = "Packet Debugger Settings";
					
					
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING && config.getConfigurationSection(Section).getBoolean("Flying"))
					{
						WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(e);
						PTNormal.INSTANCE.DebuggerChatMessage (player, e, "Ground= " + packet.isOnGround());
					}
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION && config.getConfigurationSection(Section).getBoolean("Position"))
					{
						WrapperPlayClientPlayerPosition packet = new WrapperPlayClientPlayerPosition(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "X= " + packet.getLocation().getX() + "\nY= " + packet.getLocation().getY() + "\nZ= " + packet.getLocation().getX() + "\nGround= " + packet.isOnGround());
					}
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION && config.getConfigurationSection(Section).getBoolean("PositionLook"))
					{
						WrapperPlayClientPlayerPositionAndRotation packet = new WrapperPlayClientPlayerPositionAndRotation(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "X= " + packet.getLocation().getX() + "\nY= " + packet.getLocation().getY() + "\nZ= " + packet.getLocation().getX() + "\nYaw= " + packet.getLocation().getYaw() + "\nPitch= " + packet.getLocation().getPitch() + "\nGround= " + packet.isOnGround());
					}
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION &&  config.getConfigurationSection(Section).getBoolean("Look"))
					{
						WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
						PTNormal.INSTANCE.DebuggerChatMessage (player, e, "Yaw= " + packet.getLocation().getYaw() + "\nPitch= " + packet.getLocation().getPitch() + "\nGround= " + packet.isOnGround());
					}
					
					if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE && config.getConfigurationSection(Section).getBoolean("Custom PayLoad"))
					{
						WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
						try
						{
							PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Channel= " + packet.getChannelName() + "\nData= " + new String(packet.getData(), "UTF-8").replace("", ""));
						}
						catch (Exception g) 
						{
							g.printStackTrace();
						}
					}
					
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES && config.getConfigurationSection(Section).getBoolean("Abilities"))
					{
						WrapperPlayClientPlayerAbilities packet = new WrapperPlayClientPlayerAbilities(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Invulnerable= " + packet.isInGodMode().get() + "\nFlying= " + packet.isFlying() + "\nAllowedFlight= " + packet.isFlightAllowed().get() 
						+ "\nCreativeMode= " + packet.isInCreativeMode().get() + "\nFlySpeed= " + packet.getFlySpeed().get() + "\nWalkSpeed= " + packet.getWalkSpeed().get());
					}
					if (e.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS && config.getConfigurationSection(Section).getBoolean("Client Settings"))
					{
						WrapperPlayClientSettings packet = new WrapperPlayClientSettings(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Locale= " + packet.getLocale() + "\nViewDistance= " + packet.getViewDistance() + "\nChatVisibility= " + packet.getVisibility().name() + 
						"\nChatColors= " + packet.isChatColorable() + "\nVersion= " + packet.getClientVersion().getProtocolVersion() + 
					    "\nSkinParts= " + packet.getVisibleSkinSection().getMask() + "\nHand= " + packet.getMainHand().name() + "\nServerListingAllowed= " + packet.isServerListingAllowed());
					}
					if (e.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && config.getConfigurationSection(Section).getBoolean("Client Status"))
					{
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Action= " + new WrapperPlayClientClientStatus(e).getAction().name());
					}
					
					if (e.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW && config.getConfigurationSection(Section).getBoolean("Close Window"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "ID= " + new WrapperPlayClientCloseWindow(e).getWindowId());
					
					if (e.getPacketType() == PacketType.Play.Client.CLICK_WINDOW && config.getConfigurationSection(Section).getBoolean("Click Window"))
					{
						WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "ID= " + packet.getWindowId() + "\nSlot= " + packet.getSlot() + "\nButton= " + packet.getButton() + "\nClickType= " + packet.getWindowClickType().name() + "\nItem= " + packet.getCarriedItemStack() + "\nMode= " + packet.getActionNumber().get());
					}
					if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE && config.getConfigurationSection(Section).getBoolean("Keep Alive"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "ID= " + new WrapperPlayClientKeepAlive(e).getId());
					
					if (e.getPacketType() == PacketType.Play.Client.SPECTATE && config.getConfigurationSection(Section).getBoolean("Spectate"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "UUID= " + new WrapperPlayClientSpectate(e).getTargetUUID());
					
					if (e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE && config.getConfigurationSection(Section).getBoolean("Steer Vehicle"))
					{
						WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Forwards= " + packet.getForward() + "\nSideWards= " + packet.getSideways() + "\nFlags= " + packet.getFlags());
					}
					if (e.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION && config.getConfigurationSection(Section).getBoolean("Transaction"))
					{
						WrapperPlayClientWindowConfirmation packet = new WrapperPlayClientWindowConfirmation(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Window= " + packet.getWindowId() + "\nActionID= " + packet.getActionId() + "\nAccepted= " + packet.isAccepted());
					}
					if (e.getPacketType() == PacketType.Play.Client.ANIMATION && config.getConfigurationSection(Section).getBoolean("Arm Animation"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "N/A");
					
					if (e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && config.getConfigurationSection(Section).getBoolean("Held Item Slot"))
					{
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Slot= " + new WrapperPlayClientHeldItemChange(e).getSlot());
					}
					
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && config.getConfigurationSection(Section).getBoolean("Block Place"))
					{
						WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "posX= " + packet.getBlockPosition().getX() + "\nposY= " + packet.getBlockPosition().getY() + "\nposZ= " + packet.getBlockPosition().getZ() + "\nCursorPosX= " + packet.getCursorPosition().getX() + "\nCursorPosY= " + packet.getCursorPosition().getY() + "\nCursorPosZ= " + packet.getCursorPosition().getZ() + "\nFace= " + packet.getFace().name() + "\nItem= " + packet.getItemStack() + "\nHand= " + packet.getHand().name() + "\nInsideBlock= " + packet.getInsideBlock() + "\nSequence= " + packet.getSequence());
					}
					if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && config.getConfigurationSection(Section).getBoolean("Digging"))
					{
						WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(e);
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Action= " + packet.getAction().name() + "\nBlockFace= " + packet.getBlockFace() + "\nX= " + packet.getBlockPosition().getX() + "\nY= " + packet.getBlockPosition().getY() + "\nZ= " + packet.getBlockPosition().getZ() + "\nSequence= " + packet.getSequence());
					}
					
					if (e.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && config.getConfigurationSection(Section).getBoolean("Entity Action"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Action= " + new WrapperPlayClientEntityAction(e).getAction());
					
					if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE && config.getConfigurationSection(Section).getBoolean("Tab Complete"))
						PTNormal.INSTANCE.DebuggerChatMessage(player, e, "Text= " + new WrapperPlayClientTabComplete(e).getText());
					
				}
			}
			if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
			{
				WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
				if (packet.getChannelName().equalsIgnoreCase("ACT|Debugger") && !Rebug.getINSTANCE().isAllowedToDebugPackets.contains(player.getUniqueId()))
					Rebug.getINSTANCE().isAllowedToDebugPackets.add(player.getUniqueId());
				
				if (packet.getChannelName().equalsIgnoreCase("MC|Brand") && !Rebug.getINSTANCE().ClientBranded.containsKey(player.getUniqueId()))
				{
					try 
					{
						String brand = new String(packet.getData(), "UTF-8").substring(1);
						if (Config.TellClientBrandOnJoin())
							player.sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + PTNormal.getPlayerVersion(PTNormal.getPlayerVersion(player)) + " (" + PTNormal.getPlayerVersion(player) + ")");
					
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + player.getName() + "'s " + ChatColor.AQUA + "client brand " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + brand + " " + PTNormal.getPlayerVersion(PTNormal.getPlayerVersion(player)) + " (" + PTNormal.getPlayerVersion(player) + ")");
						Rebug.getINSTANCE().ClientBranded.put(player.getUniqueId(), brand);
					} 
					catch (UnsupportedEncodingException e1) {}
				}
				if (packet.getChannelName().equalsIgnoreCase("Register") && !Rebug.getINSTANCE().ClientRegistered.containsKey(player.getUniqueId()))
				{
					try
					{
						String register = new String(packet.getData(), "UTF-8").substring(0);
						if (Config.TellClientRegisters())
							player.sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + register);
						
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + player.getName() + "'s " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + register);
						Rebug.getINSTANCE().ClientRegistered.put(player.getUniqueId(), register);
					}
					catch (Exception ee) 
					{
						ee.printStackTrace();
					}
				}
			}
			return;
		}
		else if (Rebug.getINSTANCE().isAllowedToDebugPackets.contains(user.getPlayer().getUniqueId()))
		{
			user.AllowedToDebug = Rebug.hasAdminPerms(player);
			Rebug.getINSTANCE().isAllowedToDebugPackets.remove(user.getPlayer().getUniqueId());
		}
		else if (Rebug.getINSTANCE().ClientBranded.containsKey(user.getPlayer().getUniqueId()))
		{
			user.UnReceivedBrand = 0;
			user.BrandSetCount = 1;
			user.setBrand(Rebug.getINSTANCE().ClientBranded.get(user.getPlayer().getUniqueId()));
			Rebug.getINSTANCE().ClientBranded.remove(user.getPlayer().getUniqueId());
		}
		else if (Rebug.getINSTANCE().ClientRegistered.containsKey(user.getPlayer().getUniqueId()))
		{
			user.setRegister(Rebug.getINSTANCE().ClientRegistered.get(user.getPlayer().getUniqueId()));
			Rebug.getINSTANCE().ClientRegistered.remove(user.getPlayer().getUniqueId());
		}
		BPlayerBoard board = Netherboard.instance().getBoard(user.getPlayer());
		ItemStack item = null;
		user.preSend ++;
		final double
		distX = user.getLocation().getX() - user.lastTickPosX,
		distY = user.getLocation().getY() - user.lastTickPosY,
		distZ = user.getLocation().getZ() - user.lastTickPosZ,
		bpsXZ = Math.sqrt(distX * distX + distZ * distZ) * 20.0,
		bpsY = Math.sqrt(distY * distY) * 20.0;  
		
		if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE && user.isPacketDebuggerEnabled() && user.TabCompletePacket)
			user.DebuggerChatMessage(e, "Text= " + new WrapperPlayClientTabComplete(e).getText());
		
		if (e.getPacketType() == PacketType.Play.Client.ANIMATION)
		{
			user.preCPS ++;
			if (user.isPacketDebuggerEnabled() && user.ArmAnimationPacket)
				user.DebuggerChatMessage(e, "N/A");
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES && user.isPacketDebuggerEnabled() && user.AbilitiesPacket)
		{
			WrapperPlayClientPlayerAbilities packet = new WrapperPlayClientPlayerAbilities(e);
			user.DebuggerChatMessage(e, "Invulnerable= " + packet.isInGodMode().get() + "\nFlying= " + packet.isFlying() + "\nAllowedFlight= " + packet.isFlightAllowed().get() 
			+ "\nCreativeMode= " + packet.isInCreativeMode().get() + "\nFlySpeed= " + packet.getFlySpeed().get() + "\nWalkSpeed= " + packet.getWalkSpeed().get());
		}
		if (e.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS && user.isPacketDebuggerEnabled() && user.SettingsPacket)
		{
			WrapperPlayClientSettings packet = new WrapperPlayClientSettings(e);
			user.DebuggerChatMessage(e, "Locale= " + packet.getLocale() + "\nViewDistance= " + packet.getViewDistance() + "\nChatVisibility= " + packet.getVisibility().name() + 
			"\nChatColors= " + packet.isChatColorable() + "\nVersion= " + packet.getClientVersion().getProtocolVersion() + 
		    "\nSkinParts= " + packet.getVisibleSkinSection().getMask() + "\nHand= " + packet.getMainHand().name() + "\nServerListingAllowed= " + packet.isServerListingAllowed());
		}
		if (e.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && user.isPacketDebuggerEnabled() && user.StatusPacket)
		{
			user.DebuggerChatMessage(e, "Action= " + new WrapperPlayClientClientStatus(e).getAction().name());
		}
		
		if (e.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW && user.isPacketDebuggerEnabled() && user.CloseWindowPacket)
			user.DebuggerChatMessage(e, "ID= " + new WrapperPlayClientCloseWindow(e).getWindowId());
		
		if (e.getPacketType() == PacketType.Play.Client.CLICK_WINDOW && user.isPacketDebuggerEnabled() && user.ClickWindowPacket)
		{
			WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(e);
			user.DebuggerChatMessage(e, "ID= " + packet.getWindowId() + "\nSlot= " + packet.getSlot() + "\nButton= " + packet.getButton() + "\nClickType= " + packet.getWindowClickType().name() + "\nItem= " + packet.getCarriedItemStack() + "\nMode= " + packet.getActionNumber().get());
		}
		if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE && user.isPacketDebuggerEnabled() && user.KeepAlivePacket)
			user.DebuggerChatMessage(e, "ID= " + new WrapperPlayClientKeepAlive(e).getId());
		
		if (e.getPacketType() == PacketType.Play.Client.SPECTATE && user.isPacketDebuggerEnabled() && user.SpectatePacket)
		{
			user.DebuggerChatMessage(e, "UUID= " + new WrapperPlayClientSpectate(e).getTargetUUID());
		}
		if (e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE && user.isPacketDebuggerEnabled() && user.SteerVehiclePacket)
		{
			WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(e);
			user.DebuggerChatMessage(e, "Forwards= " + packet.getForward() + "\nSideWards= " + packet.getSideways() + "\nFlags= " + packet.getFlags());
		}
		if (e.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION && user.isPacketDebuggerEnabled() && user.TransactionPacket)
		{
			WrapperPlayClientWindowConfirmation packet = new WrapperPlayClientWindowConfirmation(e);
			user.DebuggerChatMessage(e, "Window= " + packet.getWindowId() + "\nActionID= " + packet.getActionId() + "\nAccepted= " + packet.isAccepted());
		}
		
		if (e.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS)
		{
			WrapperPlayClientResourcePackStatus rs = new WrapperPlayClientResourcePackStatus(e);
			user.sendMessage("Resource Pact Status: Hash= " + rs.getHash() + " Result= " + rs.getResult());
		}
		if (e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE)
		{
			user.getPlayer().updateInventory();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 3);
			if (user.isPacketDebuggerEnabled() && user.HeldItemSlotPacket)
				user.DebuggerChatMessage(e, "Slot= " + new WrapperPlayClientHeldItemChange(e).getSlot());
		}
		
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			item = user.getPlayer().getItemInHand();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 3);
			if (PTNormal.isHoldingSword(item))
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Blocking " + ChatColor.GREEN + Rebug.getINSTANCE().getTickMark(), 3);
			
			if (user.isPacketDebuggerEnabled() && user.BlockPlacePacket)
			{
				WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(e);
				user.DebuggerChatMessage(e, "posX= " + packet.getBlockPosition().getX() + "\nposY= " + packet.getBlockPosition().getY() + "\nposZ= " + packet.getBlockPosition().getZ() + "\nCursorPosX= " + packet.getCursorPosition().getX() + "\nCursorPosY= " + packet.getCursorPosition().getY() + "\nCursorPosZ= " + packet.getCursorPosition().getZ() + "\nFace= " + packet.getFace().name() + "\nItem= " + packet.getItemStack() + "\nHand= " + packet.getHand().name() + "\nInsideBlock= " + packet.getInsideBlock() + "\nSequence= " + packet.getSequence());
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING)
		{
			WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(e);
			if (packet.getAction() == DiggingAction.RELEASE_USE_ITEM || packet.getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND)
			{
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Blocking " + ChatColor.RED + ChatColor.BOLD.toString() + "X", 3);
			}
			if (user.isPacketDebuggerEnabled() && user.DiggingPacket)
				user.DebuggerChatMessage(e, "Action= " + packet.getAction().name() + "\nBlockFace= " + packet.getBlockFace() + "\nX= " + packet.getBlockPosition().getX() + "\nY= " + packet.getBlockPosition().getY() + "\nZ= " + packet.getBlockPosition().getZ() + "\nSequence= " + packet.getSequence());
		}
		if (e.getPacketType() == PacketType.Play.Client.ENTITY_ACTION)
		{
			WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(e);
			if (packet.getAction() == Action.STOP_SPRINTING || packet.getAction() == Action.START_SPRINTING)
			{
				boolean start = packet.getAction() == Action.START_SPRINTING;
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Sprinting " + (start ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 2);
			}
			if (packet.getAction() == Action.STOP_SNEAKING || packet.getAction() == Action.START_SNEAKING)
			{
				boolean start = packet.getAction() == Action.START_SNEAKING;
				Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "Sneaking " + (start ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 1);
			}
			if (user.isPacketDebuggerEnabled() && user.EntityActionPacket)
				user.DebuggerChatMessage(e, "Action= " + packet.getAction());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING)
		{
			WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(e);
			user.lastTickPosX = user.getLocation().getX();
			user.lastTickPosY = user.getLocation().getY();
			user.lastTickPosZ = user.getLocation().getZ();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 0);
			if (user.isPacketDebuggerEnabled() && user.FlyingPacket)
				user.DebuggerChatMessage (e, "Ground= " + packet.isOnGround());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION)
		{
			WrapperPlayClientPlayerPosition packet = new WrapperPlayClientPlayerPosition(e);
			if (board != null)
			{
				board.set(ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + former.format(bpsY), 6);
				board.set(ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + former.format(bpsXZ), 7);
			}
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "TB " + (e.getTimestamp() - user.timer_balance) + "ms", 4);
			
			user.timer_balance = e.getTimestamp();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 0);
			if (user.isPacketDebuggerEnabled() && user.PositionPacket)
				user.DebuggerChatMessage (e, "X= " + packet.getLocation().getX() + "\nY= " + packet.getLocation().getY() + "\nZ= " + packet.getLocation().getX() + "\nGround= " + packet.isOnGround());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
		{
			WrapperPlayClientPlayerPositionAndRotation packet = new WrapperPlayClientPlayerPositionAndRotation(e);
			user.yaw = packet.getLocation().getYaw();
			user.pitch = packet.getLocation().getPitch();
			if (board != null)
			{
				board.set(ChatColor.DARK_RED + "BPS (Y) " + ChatColor.WHITE + former.format(bpsY), 6);
				board.set(ChatColor.DARK_RED + "BPS (XZ) " + ChatColor.WHITE + former.format(bpsXZ), 7);
			}
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "TB " + (e.getTimestamp() - user.timer_balance) + "ms", 4);
			
			user.timer_balance = e.getTimestamp();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 0);
			if (user.isPacketDebuggerEnabled() && user.PositionLookPacket)
				user.DebuggerChatMessage (e, "X= " + packet.getLocation().getX() + "\nY= " + packet.getLocation().getY() + "\nZ= " + packet.getLocation().getX() + "\nYaw= " + packet.getLocation().getYaw() + "\nPitch= " + packet.getLocation().getPitch() + "\nGround= " + packet.isOnGround());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION)
		{
			WrapperPlayClientPlayerRotation packet = new WrapperPlayClientPlayerRotation(e);
			user.yaw = packet.getLocation().getYaw();
			user.pitch = packet.getLocation().getPitch();
			Rebug.getINSTANCE().UpdateScoreBoard(user, ChatColor.DARK_RED + "OnGround " + (packet.isOnGround() ? ChatColor.GREEN + Rebug.getINSTANCE().getTickMark() : ChatColor.RED + ChatColor.BOLD.toString() + "X"), 0);
			if (user.isPacketDebuggerEnabled() && user.LookPacket)
				user.DebuggerChatMessage (e, "Yaw= " + packet.getLocation().getYaw() + "\nPitch= " + packet.getLocation().getPitch() + "\nGround= " + packet.isOnGround());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
		{
			WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
			if (user.isPacketDebuggerEnabled() && user.CustomPayLoadPacket)
			{
				try
				{
					user.DebuggerChatMessage(e, "Channel= " + packet.getChannelName() + "\nData= " + new String(packet.getData(), "UTF-8").replace("", ""));
				}
				catch (Exception g) 
				{
					g.printStackTrace();
				}
			}
				
			if (packet.getChannelName().equalsIgnoreCase("ACT|Debugger"))
				user.AllowedToDebug = false;
			
			if (packet.getChannelName().equalsIgnoreCase("Register"))
			{
				try
				{
					user.setRegister(new String(packet.getData(), "UTF-8").substring(0));
					if (Config.TellClientRegisters())
						user.sendMessage (ChatColor.RED + "Your " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getRegister());
					
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
		    			PTNormal.KickPlayer(user.getPlayer(), "Rebug's already collected your client brand you shound't be sending another client brand Packet!");
		    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("ban"))
		    			PTNormal.BanPlayer(user.getPlayer(), " Rebug's already collected your client brand you shound't be sending another client brand Packet!");
		    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("warn"))
		    			user.sendMessage("Already collected your client brand you shound't be sending another client brand Packet!");
		    		
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
			    				user.sendMessage("Failed to load your client brand this may cause issues!");
			    			
			    			if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
			    			{
			    				PTNormal.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
			    				return;
			    			}
			    			if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
			    			{
			    				PTNormal.BanPlayer(user.getPlayer(), "Rebug banned you due to failing to load your client brand!");
			    				return;
			    			}
				    	}
						if (user.BrandSetCount < 1)
						{
							Bukkit.getScheduler().runTaskLater(Rebug.getINSTANCE(), new Runnable()
							{
								
								@Override
								public void run() {
									if (Config.TellClientBrandOnJoin())
										user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getBrand() + " " + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
								
									Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "client brand " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + user.getBrand() + " " + PTNormal.getPlayerVersion(user.getProtocol()) + " (" + PTNormal.getPlayerVersion(user.getPlayer()) + ")");
								}
							}, 11);
						}
						user.BrandSetCount ++;
						if (user.BrandSetCount > Integer.MAX_VALUE)
						{
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "User " + user.getPlayer().getName() + " Set their client brand more than  Integer.MAX_VALUE  Resetting counter to 1");
							user.BrandSetCount = 1;
						}
						user.UnReceivedBrand = 0;
		    		}
		    	}
		    	catch (Exception ee) 
		    	{
		    		if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
	    				user.sendMessage("Failed to load your client brand this may cause issues!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
	    				PTNormal.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
	    			
	    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
	    				PTNormal.BanPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
	    			
	    			if (Rebug.debug)
	    				ee.printStackTrace();
				}
			}
		}
		if (user.BrandSetCount > 0 || !Config.AntiCancelClientBrandPacket()) return;
		
		
		user.UnReceivedBrand ++;
		if (user.UnReceivedBrand >= (Config.AntiCancelClientBrandCounter() < 200 ? 200 : Config.AntiCancelClientBrandCounter()))
		{
			PTNormal.KickPlayer(user.getPlayer(), ChatColor.RED + "You were kicked by " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "REBUG " + ChatColor.RED + "for not sending a Client Brand Packet!");
			return;
		}
		Rebug.Debug(user.getPlayer(), "UnReceivedBrand= " + user.UnReceivedBrand);
	}
	@SuppressWarnings ({ "deprecation"})
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
		if (e.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK)
		{
			user.S08Pos = 0;
			if (user.joined < System.currentTimeMillis())
			{
				if (user.ShowS08Alert)
				{
					new BukkitRunnable () 
					{
		                @Override
		                public void run()
		                {
		                	if (user.S08Pos > 100 || !user.ShowS08Alert)
		                	{
		                		cancel();
		                		return;
		                	}
		                	
		                	Rebug.getINSTANCE().getNMS().SendChatPacket(user.getPlayer(), "{\"text\":\"" + ChatColor.AQUA + "Server Position (S08) Receiving : Ticks: " 
				                    + user.S08Pos + "\"}", (byte) 2);
							user.S08Pos ++;
		                }
		            }.runTaskTimer(Rebug.getINSTANCE(), 0, 1);
				}
			}
			/*
			 * 
			 * IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Hello " + ChatColor.GOLD + " you" + "\"}");

		    PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);

		PT.SendPacket(user.getPlayer(), title);
		PT.SendPacket(user.getPlayer(), length);
			 */
		}
		if (e.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE)
		{
			WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(e);
			if (packet.getMessage().getType().equals(ChatTypes.CHAT) || packet.getMessage().getType().equals(ChatTypes.SYSTEM))
			{
				String message = ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(packet.getMessage().getChatContent()));
				message = ChatColor.stripColor(message);
				message = message.trim();
				
				
				if (!Rebug.PrivatePerPlayerAlerts) return;
				
				if (Rebug.anticheats.isEmpty() && Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory"))
				{
					Bukkit.getConsoleSender().sendMessage("Rebug.anticheats was Empty, trying to add ACs!");
					user.getPlayer().closeInventory();
					user.getPlayer().openInventory(ItemsAndMenusUtils.INSTANCE.getAntiCheats());
					user.getPlayer().closeInventory();
				}
				if (Rebug.anticheats.isEmpty())
				{
					if (Rebug.getINSTANCE().getLoadedAntiCheatsFile().getBoolean("loaded-anticheats.create-inventory"))
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Rebug AntiCheats was still Empty!");
					
					return;
				}
				
				message = message.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(">>", "").replace("<<", "").replace(">", "")
			    .replace("<", "").replace("/", "").replace("//", "").replace("Â", "").replace("ÂÂ", "").replace("ÂÂÂ", "").replace("(", "").replace(")", "").replace("┬", "").replace("º", "").replace("┬º", "").replace("*", "").replace("**", "").replace("^", "");
				
				String[] Alert_Related = StringUtils.split(message);
				
				if (Alert_Related.length < 4) return;
				
				String connedAntiCheat = user.AntiCheat.toLowerCase();
				
				for (Map.Entry<Plugin, String> map : Rebug.anticheats.entrySet())
				{
					String alert = map.getValue().toLowerCase(), alert_message = ChatColor.translateAlternateColorCodes('&', Rebug.getINSTANCE().getLoadedAntiCheatsFile().getString("loaded-anticheats." + alert.toLowerCase() + ".alert-message"));
					alert_message = ChatColor.stripColor(alert_message);
					alert_message = alert_message.trim();
					alert = alert.trim();
					
					if (Alert_Related[0].equalsIgnoreCase(alert_message))
					{
						// Flags
						if (Alert_Related[2].equalsIgnoreCase("failed") || Alert_Related[2].equalsIgnoreCase("flagged"))
						{
							if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Flag (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || !user.ShowFlags))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Flag - AC isn't the same as the User's tho or ShowFlags was off! (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							break;
						}
						if (Alert_Related[2].equalsIgnoreCase("was") && Alert_Related[3].equalsIgnoreCase("flagged"))
						{
							if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
							{
								Rebug.Debug(user, "Skipped MSG Line= Not User - was Flagged - (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || !user.ShowFlags))
							{
								Rebug.Debug(user, "Skipped MSG Line= is User - was Flagged - AC isn't the same as the User's tho or ShowFlags was off! (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							break;
						}
						if (Alert_Related.length > 4 && Alert_Related[2].equalsIgnoreCase("might") && Alert_Related[3].equalsIgnoreCase("be") && Alert_Related[4].equalsIgnoreCase("using"))
						{
							if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
							{
								Rebug.Debug(user, "Skipped MSG Line= Not User - was Flagged - (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || !user.ShowFlags))
							{
								Rebug.Debug(user, "Skipped MSG Line= is User - was Flagged - AC isn't the same as the User's tho or ShowFlags was off! (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							break;
						}
						
						// Punished
						if (Alert_Related[2].equalsIgnoreCase("was") && Alert_Related[3].equalsIgnoreCase("punished"))
						{
							if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Punished (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || !user.ShowPunishes))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Punished - AC isn't the same as the User's tho or ShowPunishes was off! (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							break;
						}
						
						// Setback
						if (Alert_Related[2].equalsIgnoreCase("was") && Alert_Related[3].equalsIgnoreCase("setback"))
						{
							if (!Alert_Related[1].equalsIgnoreCase(user.getName()))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Not User - Punished (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							if (Alert_Related[1].equalsIgnoreCase(user.getName()) && (!connedAntiCheat.contains(alert) || !user.ShowSetbacks))
							{
								Rebug.Debug(user.getPlayer(), "Skipped MSG Line= Is User - Punished - AC isn't the same as the User's tho or ShowPunishes was off! (P-AC " + user.AntiCheat + " A-AC " + alert + ")");
								user.preReceive --;
								e.setCancelled(true);
								break;
							}
							break;
						}
						break;
					}
				}
			}
		}
	}
}
