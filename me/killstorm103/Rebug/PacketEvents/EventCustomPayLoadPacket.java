package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class EventCustomPayLoadPacket implements PacketListener
{
	@PacketHandler 
	public void onPacketReceive (PacketReceiveEvent e)
	{
		if (e.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE)
		{
			Player player = Rebug.getGetMain().getServer().getPlayer(e.getUser().getUUID());
			WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
			if (player != null)
			{
				me.killstorm103.Rebug.Utils.User user = Rebug.getUser(player);
				String magic = null;
				if (packet.getChannelName().equalsIgnoreCase("REGISTER"))
				{
					if (user == null)
			    	{
						PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("when reading CustomPayLoad:Register - rejoin!"));
			    		return;
			    	}
					try
					{
						magic = new String(packet.getData(), "UTF-8").substring(0);
						user.setRegister(magic);
						if (Config.TellClientRegisters())
							user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + magic);
						
						Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "register " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + magic);
					}
					catch (Exception ee) 
					{
						ee.printStackTrace();
					}
				}
				if (packet.getChannelName().equalsIgnoreCase("MC|Brand"))
				{
			    	if (user == null)
			    	{
			    		PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("when reading CustomPayLoad:MC|Brand - rejoin!"));
			    		return;
			    	}
			    	if (Config.getAllowedToOverRideClientBrand())
			    		user.setBrandSet(false);
			    	
			    	if (user.getIsBrandSet())
			    	{
			    		if (Config.getCantOverrideClientBrand().equalsIgnoreCase("kick"))
			    			PT.KickPlayer(user.getPlayer(), "Rebug's already collected your client brand you shound't be sending another client brand Packet!");
			    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("ban"))
			    			PT.BanPlayer(user.getPlayer(), " Rebug's already collected your client brand you shound't be sending another client brand Packet!");
			    		else if (Config.getCantOverrideClientBrand().equalsIgnoreCase("warn"))
			    			user.getPlayer().sendMessage("Rebug's already collected your client brand you shound't be sending another client brand Packet!");
			    		
			    		return;
			    	}
			    	try
			    	{
			    		magic = new String(packet.getData(), "UTF-8").substring(1);
						if (!user.getIsBrandSet())
			    		{
							user.setBrand(magic);
							if (Config.TellClientBrandOnJoin())
								user.getPlayer().sendMessage(Rebug.RebugMessage + ChatColor.RED + "Your " + ChatColor.AQUA + "client " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + magic + " " + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
						
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + ChatColor.RED + user.getPlayer().getName() + "'s " + ChatColor.AQUA + "client brand " + ChatColor.RED + "is" + ChatColor.GRAY + ": " + magic + " " + PT.getPlayerVersion(user.getProtocol()) + " (" + PT.getPlayerVersion(user.getPlayer()) + ")");
							user.UnReceivedBrand = 0;
							user.setBrandSet(true);
			    		}
			    	}
			    	catch (Exception ee) 
			    	{
			    		if (user.getBrand() == null || !user.getIsBrandSet() || user.getBrand().length() <= 0)
			    		{
			    			if (user.getBrand() != null && user.getBrand().length() <= 0)
			    				user.getPlayer().sendMessage("Your client's brand was Null, user.getBrand().length() <= 0!");
			    			
			    			if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
			    				user.getPlayer().sendMessage("Rebug failed to load your client brand this may cause issues!");
			    			
			    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
			    				PT.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
			    			
			    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
			    				PT.BanPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
			    			
			    			return;
			    		}
			    		
			    		ee.printStackTrace();
					}
			    	
			    	if (user.getBrand() == null || !user.getIsBrandSet() || user.getBrand().length() <= 0)
			    	{
			    		if (user.getBrand() != null && user.getBrand().length() <= 0)
		    				user.getPlayer().sendMessage("Your client's brand was Null, user.getBrand().length() <= 0!");
			    		
			    		if (Config.getClientInfoSetting().equalsIgnoreCase("warn"))
		    				user.getPlayer().sendMessage("Rebug failed to load your client brand this may cause issues!");
		    			
		    			else if (Config.getClientInfoSetting().equalsIgnoreCase("kick")) 
		    				PT.KickPlayer(user.getPlayer(), "Rebug kicked you due to failing to load your client brand!");
		    			
		    			else if (Config.getClientInfoSetting().equalsIgnoreCase("ban"))
		    				PT.BanPlayer(user.getPlayer(), "Rebug banned you due to failing to load your client brand!");
			    	}
				}
			}
			else
				Bukkit.getConsoleSender().sendMessage("Player was null in " + getClass().getSimpleName());
		}
	}
}
