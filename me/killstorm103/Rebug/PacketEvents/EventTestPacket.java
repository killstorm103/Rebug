package me.killstorm103.Rebug.PacketEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;

public class EventTestPacket implements PacketListener
{
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public void onPacketReceive(PacketReceiveEvent e) 
	{
		if (e.getPacketType() != null && e.getPacketName() != null && e.getPacketName().length() > 0)
		{
			if (e.getPacketId() != 6 && !e.getPacketName().equalsIgnoreCase("PLAYER_FLYING") && !e.getPacketName().equalsIgnoreCase("PLAYER_ROTATION") && !e.getPacketName().equalsIgnoreCase("PLAYER_POSITION") && !e.getPacketName().equalsIgnoreCase("PLAYER_POSITION") && !e.getPacketName().equalsIgnoreCase("KEEP_ALIVE"))
			{
				Player player = Bukkit.getPlayer(e.getUser().getUUID());
				//player.sendMessage("PacketName= " + e.getPacketName() + " PacketID= " + e.getPacketId());
			}
		}
		if (e.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS)
		{
			WrapperPlayClientResourcePackStatus rs = new WrapperPlayClientResourcePackStatus(e);
			Player player = Bukkit.getServer().getPlayer(e.getUser().getUUID());
			player.sendMessage("Hash= " + rs.getHash() + " Result= " + rs.getResult());
		}
		if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)
		{
			WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(e);
			//Player player = Bukkit.getPlayer(e.getUser().getUUID());
		//	player.sendMessage("blockpos= "+ blockPlacement.getBlockPosition() + " Cursor= " + blockPlacement.getCursorPosition() + " face= " + blockPlacement.getFace() + " inside= " + blockPlacement.getInsideBlock());
		}
		if (e.getPacketType() == PacketType.Play.Client.UPDATE_SIGN)
		{
			WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(e);
		}
	}
}
