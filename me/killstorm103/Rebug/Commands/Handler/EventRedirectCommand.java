package me.killstorm103.Rebug.Commands.Handler;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;

import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;

public class EventRedirectCommand implements PacketListener
{
	private String AddItOn;
	
	@Override
	public void onPacketReceive (PacketReceiveEvent e) 
	{
		if (e.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE)
		{
			WrapperPlayClientChatMessage packet = new WrapperPlayClientChatMessage(e);
			this.AddItOn = null;
			String message = packet.getMessage();
			Player player = Bukkit.getServer().getPlayer(e.getUser().getUUID());
			if (message == null || message.length() < 1)
				return;
			
			String Aliase = null;
			
			for (Command cmd : Rebug.getGetMain().getCommands())
			{
				if (cmd.SubAliases() != null && cmd.SubAliases().length > 0)
				{
					for (int i = 0; i < cmd.SubAliases().length; i ++)
					{
						Aliase = cmd.SubAliases()[i];
						if (message.toLowerCase().startsWith(Aliase))
						{
							if (Rebug.getGetMain().Debug())
							{
								player.sendMessage("msg= " + message + " msg len= " + message.length() + " sub= " + Aliase + " sub len= " + Aliase.length());
							}
							if (message.length() > Aliase.length())
							{
								if (RunCheck(player, message, Aliase, PT.SubString(message, Aliase.length(), message.length())))
								{
									e.setCancelled(true);
									return;
								}
							}
							PT.RunTaskCommand (cmd.HasToBeConsole() ? Bukkit.getServer().getConsoleSender() : player, "rebug " + cmd.getName() + (this.AddItOn != null ? " " + this.AddItOn : ""));
							e.setCancelled(true);
							break;
						}
					}
				}
			}
		}
	}
	public boolean RunCheck (Player sender, String message, String Aliase, String SubString)
	{
		if (message.toLowerCase().startsWith("/client") || message.toLowerCase().startsWith("/damage"))
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getName().equals(PT.SubString(message, Aliase.length() + 1, message.length())))
				{
					this.AddItOn = p.getName();
					break;
				}
			}
			if (this.AddItOn == null)
			{
				sender.sendMessage(Rebug.RebugMessage + " Unknown Player!");
				return true;
			}
		}
		return false;
	}
}
