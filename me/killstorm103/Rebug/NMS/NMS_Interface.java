package me.killstorm103.Rebug.NMS;

import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;

import me.killstorm103.Rebug.Utils.User;

public interface NMS_Interface 
{
	public void ExploitSendPacket(Object attacker, Object target, String exploit, boolean CrashesPlayer);

	public void SendPacket(Object player, Object object);

	public void SendChatPacket(Object player, String text, byte type);

	public void PlayAntiCheatSelectedSound(Object player);

	public void SetUpMenu (User user, String menu);

	public void SetUpMenu (String menu);

	public ItemStack getMadeItems(String item, User user);

	public ItemStack getMadeItems(User user, String menuName, String itemName);

	public ItemStack getMadeItems(String menuName, String itemName);

	public ItemStack getMadeItems(String ItemName);

	public void DebuggerChatMessage(Object player, PacketReceiveEvent e, String toDebug);
	
	public void sendTitle (Object player, String mainTitle, String SubTitle, int fadeIn, int stay, int fadeOut);
	
	public void PlaySound (Object user, String sound, float volume, float pitch);
}
