package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

import me.killstorm103.Rebug.Main.Config;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import me.killstorm103.Rebug.Utils.User;

public class EventAntiCancelBrandPacket implements PacketListener
{
	private int UserNulled;
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE || e.getPacketType() == PacketType.Play.Client.PLAYER_FLYING || e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || e.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION)
		{
			if (!Config.AntiCancelClientBrandPacket())
				return;
			
			
			Player player = Bukkit.getServer().getPlayer(e.getUser().getUUID());
			User user = Rebug.getUser(player);
			if (user == null)
			{
				UserNulled ++;
				if (UserNulled >= 200)
				{
					PT.KickPlayer(player, PT.RebugsUserWasNullErrorMessage("in " + ChatColor.RESET + getClass().getSimpleName()));
					UserNulled = 0;
				}
				return;
			}
			if (!user.getIsBrandSet())
			{
				user.UnReceivedBrand ++;
				if (user.UnReceivedBrand >= (Config.AntiCancelClientBrandCounter() < 260 ? 260 : Config.AntiCancelClientBrandCounter()))
				{
					PT.KickPlayer(user.getPlayer(), ChatColor.RED + "You were kicked by " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "REBUG " + ChatColor.RED + "for not sending a Client Brand Packet!");
					return;
				}
				if (Rebug.getGetMain().Debug())
					player.sendMessage("UnReceivedBrand= " + user.UnReceivedBrand);
			}
		}
	}
}
