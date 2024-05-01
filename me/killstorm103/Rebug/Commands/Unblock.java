package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Main.Rebug;
import me.killstorm103.Rebug.Utils.PT;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class Unblock extends Command
{

	@Override
	public String getName() {
		return "unblock";
	}

	@Override
	public String getSyntax() {
		return "unblock <player>";
	}

	@Override
	public String getDescription() {
		return "unblocks the packetdebugger for numbware";
	}
	@Override
	public String getPermission() {
		return StartOfPermission() + "unblock";
	}
	
	@Override
	public void onCommand(CommandSender sender, String command, String[] args) throws Exception
	{
		if (args.length == 1)
		{
			Log(sender, getSyntax());
			return;
		}
		Player player = null;
		player = Rebug.getGetMain().getServer().getPlayer(args[1]);
		if (player == null)
		{
			Log(sender, "Player not found!");
			return;
		}
		ByteBuf buf = Unpooled.buffer(256);
		buf.setByte(0, (byte)0);
		buf.writerIndex(1);
		PacketPlayOutCustomPayload p = new PacketPlayOutCustomPayload("NWS|Debugger Switch",
	    new PacketDataSerializer(buf));
		PT.SendPacket(player, p);
		Log(sender, Rebug.RebugMessage + "Tried to unlock packet debugger for NumbWare");
		Log(sender, Rebug.RebugMessage + "if this didn't work it's cause your not using Developer Edition of NumbWare!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String[] args) 
	{
		if (args.length > 2 && args.length == 3)
		{
			List<String> PlayerNames = new ArrayList<String>();
			Player[] Players = new Player[Bukkit.getOnlinePlayers().size()];
			Bukkit.getOnlinePlayers().toArray(Players);
			for (int i = 0; i < Players.length; i ++)
			{
				PlayerNames.add(Players[i].getName());
			}
			return PlayerNames;
		}
		
		return null;
	}

	@Override
	public boolean HasCustomTabComplete() {
		return false;
	}

	@Override
	public boolean HideFromCommandsList() {
		return false;
	}
	@Override
	public boolean HasToBeConsole() {
		return false;
	}

	@Override
	public String[] SubAliases() {
		return null;
	}

}
