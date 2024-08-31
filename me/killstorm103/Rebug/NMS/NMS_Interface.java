package me.killstorm103.Rebug.NMS;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;

import me.killstorm103.Rebug.Utils.User;

public interface NMS_Interface 
{
	public void ExploitSendPacket (Player attacker, Player target, String exploit);
	public void CrashSendPacket (Player attacker, Player target, String mode, String spawncrashmode);
	public void SendPacketUnBlock (Player player);
	public void SendPacket (Player player, Object object);
	public void SendChatPacket (Player player, String text, byte type);
	public void PlayAntiCheatSelectedSound (Player player);
	public void SetUpMenu (User user, String menu);
	public void SetUpMenu (String menu);
	public ItemStack getMadeItems (String item, User user);
	public ItemStack getMadeItems(User user, String menuName, String itemName);
	public ItemStack getMadeItems (String menuName, String itemName);
	public ItemStack getMadeItems (String ItemName);
	public void DebuggerChatMessage (Player player, PacketReceiveEvent e, String toDebug);
}
