package me.killstorm103.Rebug.PacketEvents;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

public class TestPacket implements PacketListener
{
	@PacketHandler
	public void onPacketReceive (PacketReceiveEvent e)
	{
		User user = e.getUser();
		PacketTypeCommon packtype = e.getPacketType();
	}
	@PacketHandler
	public void onPacketSend (PacketSendEvent e)
	{ 
		User user = e.getUser();
		PacketTypeCommon packettype = e.getPacketType();
	}
}
