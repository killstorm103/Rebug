package me.killstorm103.Rebug.Shared.Interfaces;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public interface PTs
{
	public void KickPlayer (Object player, String reason);
	public void BanPlayer (Object player, String reason);
	public void sendMessage (Object user, String text);
	public void sendMessage (Object play, String message, boolean UseRebugMessage);
	public void DebuggerChatMessage (Object user, PacketReceiveEvent e, String toDebug);
}
