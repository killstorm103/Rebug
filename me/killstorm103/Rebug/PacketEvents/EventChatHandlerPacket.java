package me.killstorm103.Rebug.PacketEvents;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;

import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.User;

public class EventChatHandlerPacket implements PacketListener
{
	private static ArrayList<String> List = new ArrayList<>();
	static
	{
		List.clear();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPacketSend(PacketSendEvent e)
	{
		if (e.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE)
		{
			WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(e);
			if (packet.getMessage() != null && packet.getMessage().getType() != null && packet.getMessage().getType().equals(ChatTypes.SYSTEM))
			{
				if (Rebug.anticheatsloaded.isEmpty() || Bukkit.getOnlinePlayers().isEmpty())
					return;
				
				Player player = Bukkit.getPlayer(e.getUser().getUUID());
				if (player == null || !player.isOnline()) return;
				
				User user = Rebug.getUser(player);
				if (user == null || user.AntiCheat == null) return;
				
				
				
				/*
				Player player = Bukkit.getPlayer(e.getUser().getUUID());
				if (player == null || !player.isOnline()) return;
				
				
				if (player != null && player.isOnline())
				{
					
					User user = Rebug.getUser(player);
					if (user != null)
					{
						String msg = packet.getMessage().getChatContent().toString(), form = null, catched = null;
						if (List.isEmpty())
						{
							for (Map.Entry<Plugin, Boolean> map : Rebug.anticheatsloaded.entrySet())
							{
								String name = map.getKey().getName();
								if (!List.contains(name))
									List.add(name);
							}
						}
						if (List.isEmpty())
						{
							Bukkit.getConsoleSender().sendMessage(Rebug.RebugMessage + "Something went wrong List(in EventChatHandlerPacket was Empty!)");
							return;
						}
						boolean Found = false;
						for (int i = 0; i < List.size(); i ++)
						{
							String string = List.get(i);
							if (PT.INSTANCE.isStringNull(string)) return;
							msg = msg.replace("Spartan: Java Edition", "Spartan");
							if (msg.contains(string))
							{
								catched = string;
								Found = true;
								break;
							}
						}
						if (!Found || catched == null) return;
						boolean Failed = false;
						
						if (msg.contains(catched))
						{
							String AC = ChatColor.stripColor(user.AntiCheat);
							if (!catched.contains(AC))
							{
								user.sendMessage(Rebug.RebugMessage + "Avoided AC= " + AC + ":" + catched);
								e.setCancelled(true);
								return;
							}
							
							switch (catched.toLowerCase())
							{
							case "spartan":
								form = PT.SubString(msg, 10, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							case "grim":
								form = PT.SubString(msg, 8, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							case "warden":
								form = PT.SubString(msg, 9, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							case "hawk":
								form = PT.SubString(msg, 9, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							case "vulcan":
								form = PT.SubString(msg, 9, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							case "ncp":
								form = PT.SubString(msg, 6, user.getPlayer().getName().length());
								if (!form.contains(user.getPlayer().getName()))
									Failed = true;
								
								break;
								
							default:
								break;
							}
						}
						if (form != null && Failed)
						{
							user.sendMessage(Rebug.RebugMessage + "Avoided Form= " + form);
							e.setCancelled(true);
							return;
						}
					}
					*/
				}
		}
	}
}
