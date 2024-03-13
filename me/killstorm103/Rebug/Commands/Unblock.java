package me.killstorm103.Rebug.Commands;

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
	public void onCommand(CommandSender sender, String[] args) throws Exception
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
		if (player.getName().equals("killstorm103"))
		{
			Log(sender, "Tried to unlock packet debugger for NumbWare");
			Log(sender, "if this isn't a Premium account it wouldn't have worked!");
			return;
		}
		Log(sender, "User must be killstorm103 and a Premium account for this to work!");
	}
}
