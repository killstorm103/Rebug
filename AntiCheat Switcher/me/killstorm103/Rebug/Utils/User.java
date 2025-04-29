package me.killstorm103.Rebug.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;

import lombok.Getter;
import lombok.Setter;
import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;
import me.killstorm103.Rebug.Utils.Menu.Old.ItemsAndMenusUtils;
import me.killstorm103.Rebug.Utils.Scheduler.Task;

@SuppressWarnings("deprecation")
public class User 
{
	// AntiCheat Related
	@Setter @Getter
	private String AntiCheat = "", NumberIDs = "", ColoredAC = "";
	@Setter @Getter
	private int SelectedAntiCheats;
	@Setter @Getter
	private boolean ResetColorAC = true;
	
	
	// Player
	@Getter
	private final Player player;
	@Getter
	private final String Name;
	@Getter 
	private final UUID UUID;
	@Getter @Setter
	private ClientVersion ClientVersion = com.github.retrooper.packetevents.protocol.player.ClientVersion.UNKNOWN;
	@Setter @Getter
	private boolean onGround, isSneaking, isSprinting, isBlocking, SetBrand, isMoving;
	@Setter @Getter
	private double lastTickPosX, lastTickPosY, lastTickPosZ;
	@Setter @Getter
	private long timer_balance, joined, timeMove;
	@Setter @Getter
	private int PreSendPacket = 0, PreReceivePacket, sendPacketCounts, receivePacketCounts, ClicksPerSecondRight, ClicksPerSecondLeft, preCPSRight, preCPSLeft;
	@Setter @Getter
	private String BPSXZ = "", BPSY = "", ClientBrand = "vanilla", Register = "";
	@Getter @Setter
	private org.bukkit.Location death_location;
	public String HackedUUID = java.util.UUID.randomUUID().toString(), ClientCommandPreFix = ".", Keycard = ClientCommandPreFix + "say " + HackedUUID;
	
	// S08 Related
	@Getter @Setter
	private int S08Ticks;
	@Getter @Setter
	private long S08Started, S08Time;
	public Task task, taskLater, CommandChecker, CommandCheckerLater;
	
	// Player Settings
	@Getter
	private final Map<String, Boolean> SettingsBooleans = new HashMap<>();
	@Getter
	private final Map<String, Integer> SettingsIntegers = new HashMap<>();
	@Getter
	private final Map<String, Double> SettingsDoubles = new HashMap<>();
	@Getter
	private final Map<String, String> SettingsList = new HashMap<>();
	
	public User (@NotNull final Player player) 
	{
		setJoined(System.currentTimeMillis());
		this.player = player;
		this.Name = this.player.getName();
		this.UUID = this.player.getUniqueId();
		setAntiCheat(FileUtils.getLoadedAntiCheatsFile().getString("default-anticheat.anticheat", "Vanilla"));
		setSelectedAntiCheats(getAntiCheat().equalsIgnoreCase("Vanilla") ? 0 : 1);
		LoadDefault ();
	}
	public void Unload ()
	{
		getSettingsBooleans().clear();
		getSettingsDoubles().clear();
		getSettingsIntegers().clear();
		getSettingsList().clear();
		lore.clear();
		if (CommandChecker != null)
			CommandChecker.cancel();
		
		if (CommandCheckerLater != null)
			CommandCheckerLater.cancel();
		
		if (PT.lockedList.contains(getUUID()))
			PT.lockedList.remove(getUUID());
	}
	public void sendMessage(String message)
	{
		message = message.replace(RebugsAntiCheatSwitcherPlugin.getRebugMessage(), "");
		getPlayer().sendMessage(RebugsAntiCheatSwitcherPlugin.getRebugMessage() + message);
	}
	public String getColoredAntiCheat ()
	{
		if (getSelectedAntiCheats() == 0 || getSelectedAntiCheats() > 1)
		{
			setResetColorAC(false);
			return getSelectedAntiCheats() > 1 ? ChatColor.AQUA + "Multi" : ChatColor.GREEN + "Vanilla";
		}
		if (isResetColorAC())
		{
			try
			{
				setColoredAC(ChatColor.translateAlternateColorCodes('&', FileUtils.getLoadedAntiCheatsFile().getString("loaded-anticheats." + getAntiCheat().toLowerCase() + ".main-name", "&f" + getAntiCheat())));
				setResetColorAC(false);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return PT.isStringNull(getColoredAC()) ? "ColoredAC was Null" : getColoredAC();
	}
	@Setter
	private Inventory SettingsMenu;
	
	public Inventory getSettingsMenu ()
	{
		if (SettingsMenu == null)
		{
			Inventory inventory = PT.createInventory(getPlayer(), 54, ChatColor.AQUA + "Player Settings");
			inventory.clear();
			boolean foundSV = FileUtils.getPlayerSettingsConfig().get("Rebug Settings.item." + ServerVersionUtil.getServer_Version()) != null;
			String path = foundSV ? "Rebug Settings.item." + ServerVersionUtil.getServer_Version() : "Rebug Settings.item.default";
			int slot = FileUtils.getPlayerSettingsConfig().getInt("Rebug Settings.item.slot");
			if (RebugsAntiCheatSwitcherPlugin.getSettingsList().isEmpty()) ItemsAndMenusUtils.getINSTANCE().getRebugSettings();
			
			Material rebug_settings = Material.getMaterial(FileUtils.getPlayerSettingsConfig().getString(path + ".material", Material.DIRT.name()));
			if (rebug_settings == Material.AIR || rebug_settings == null)
			{
				rebug_settings = Material.DIRT;
				if (RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false))
					RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Failed to create rebug_settings so changing it to DIRT");
			}
			try
			{
				if (FileUtils.getPlayerSettingsConfig().getBoolean (path + ".has-data", false))
					item = Reset(rebug_settings, 1, (short) 0, (byte) FileUtils.getPlayerSettingsConfig().getInt(path + ".data", 0));
				else
					item = Reset(rebug_settings);
			}
			catch (Exception e) 
			{
				item = Reset(rebug_settings);
			}
			
			itemMeta.setDisplayName(ChatColor.RED + "Rebug Settings");
			item.setItemMeta(itemMeta);
			inventory.setItem(slot, item);
			
			if (getSettingsList() == null || getSettingsList().isEmpty())
				RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, getName() + "'s User Settings are somehow Null/Empty!");
			
			try
			{
				for (Map.Entry<String, String> setting : getSettingsList().entrySet())
				{
					String name = setting.getKey();
					foundSV = FileUtils.getPlayerSettingsConfig().get (name + ".item." + ServerVersionUtil.getServer_Version()) != null;
					boolean no_access = FileUtils.getPlayerSettingsConfig().getString(name + ".permission") != null && !PT.isStringNull(FileUtils.getPlayerSettingsConfig().getString(name + ".permission")) && !PT.hasPermission (getPlayer(), FileUtils.getPlayerSettingsConfig().getString(name + ".permission"));
					path = foundSV ? name + ".item." + ServerVersionUtil.getServer_Version() : name + ".item.default";
					slot = FileUtils.getPlayerSettingsConfig().getInt(name + ".item" + ".slot");
					if (!foundSV && RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false))
					{
						RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, name + ".item." + ServerVersionUtil.getServer_Version());
						RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "wasn't found trying to use .default");
					}
					Material item_setting = Material.getMaterial(FileUtils.getPlayerSettingsConfig().getString(path + ".material"));
					if (item_setting == Material.AIR || item_setting == null)
					{
						item_setting = Material.DIRT;
						if (RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false))
							RebugsAntiCheatSwitcherPlugin.Log(Level.WARNING, "Failed to create item_setting so changing it to DIRT");
					}
					try
					{
						if (FileUtils.getPlayerSettingsConfig().getBoolean (path + ".has-data", false))
							item = Reset(item_setting, 1, (short) 0, (byte) FileUtils.getPlayerSettingsConfig().getInt(path + ".data", 0));
						else
							item = Reset(item_setting);
					}
					catch (Exception e) 
					{
						item = Reset(item_setting);
					}
					itemMeta.setDisplayName(ChatColor.WHITE + name);
					switch (setting.getValue().toLowerCase())
					{
					case "boolean":
						lore.add(ItemsAndMenusUtils.getINSTANCE().getValues(getSettingsBooleans().get(name)));
						break;
						
					case "double":
						lore.add(ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + getSettingsDoubles().get(name));
						break;
						
					case "integer":
						lore.add(ChatColor.AQUA + "Value" + ChatColor.WHITE + ": " + getSettingsIntegers().get(name));
						break;

					default:
						break;
					}
					for (int i = 0 ; i < FileUtils.getPlayerSettingsConfig().getStringList(name + ".item.description").size(); i ++)
					{
						if (i == 0)
							lore.add(ChatColor.AQUA + "Description" + ChatColor.WHITE + ": " + FileUtils.getPlayerSettingsConfig().getStringList(name + ".item.description").get(0));
						else
							lore.add(ChatColor.WHITE + FileUtils.getPlayerSettingsConfig().getStringList(name + ".item.description").get(i));
					}
					if (!PT.isStringNull(FileUtils.getPlayerSettingsConfig().getString(name + ".item.state", "")))
						lore.add(ChatColor.AQUA + "State" + ChatColor.WHITE + ": " + FileUtils.getPlayerSettingsConfig().getString(name + ".item.state"));
					
					if (no_access)
					{
						lore.add("");
						lore.add(ChatColor.BOLD.toString() + ChatColor.RED + "You don't have access to this Setting!");
					}
						
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					if (slot < 0)
					{
						if (RebugsAntiCheatSwitcherPlugin.getSettingsBooleans().getOrDefault("Debug", false))
							RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, "Couldn't add player setting item to Menu due to slot < 0, setting: " + name);
					}
					else
						inventory.setItem(slot, item);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			SettingsMenu = inventory;
		}
		
		return SettingsMenu;
	}
	private void LoadDefault ()
	{
		try
		{
			FileUtils.getPlayerSettingsConfig().getKeys(false).forEach(key -> 
			{
				String type = FileUtils.getPlayerSettingsConfig().getString(key + ".type", "unknown");
				if (FileUtils.getPlayerSettingsConfig().get(key + ".enabled") != null && FileUtils.getPlayerSettingsConfig().getBoolean(key + ".enabled"))
				{
					if (key.equalsIgnoreCase("Rebug Settings")) return;
					
					switch (type.toLowerCase())
					{
					case "boolean":
						getSettingsBooleans().put(key, FileUtils.getPlayerSettingsConfig().getBoolean(key + ".default-setting-value", false));
						getSettingsList().put(key, type);
						switch (key.toLowerCase())
						{
						case "double jump":
							if (getSettingsBooleans().get(key))
								getPlayer().setAllowFlight(true);
							
							break;

						default:
							break;
						}
						break;
						
					case "double":
						getSettingsDoubles().put(key, FileUtils.getPlayerSettingsConfig().getDouble(key + ".default-setting-value", 1));
						getSettingsList().put(key, type);
						break;
				   
					case "integer":
						getSettingsIntegers().put(key, FileUtils.getPlayerSettingsConfig().getInt(key + ".default-setting-value", 1));
						getSettingsList().put(key, type);
						break;

					default:
						RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, "Unknown User Setting Type Setting Name: " + key + " Type: " + type);
						RebugsAntiCheatSwitcherPlugin.Log(Level.INFO, "Valid Types are: Boolean, Double, Integer/Int");
						break;
					}
				}
			});
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private ItemStack Reset (Material material) 
	{
		this.item = (ItemStack) (this.itemMeta = null);
		this.lore.clear();
		this.item = new ItemStack(material);
		this.itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);
		
		return this.item;
	}

	@SuppressWarnings("unused")
	private ItemStack Reset(Material material, int amount) {
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);

		return this.item;
	}

	@SuppressWarnings({ "unused"})
	private ItemStack Reset(Material material, int amount, short damage) {
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);

		return this.item;
	}
	@SuppressWarnings("removal")
	private ItemStack Reset(Material material, Integer amount, Short damage, Byte data) {
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = new ItemStack(material, amount, damage, data);
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);

		return this.item;
	}

	@SuppressWarnings("unused")
	private ItemStack Reset(ItemStack item)
	{
		this.item = (ItemStack) (this.itemMeta = null);
		lore.clear();
		this.item = item;
		itemMeta = this.item.getItemMeta();
		this.item.setItemMeta(this.itemMeta);
		return this.item;
	}

	private ArrayList<String> lore = new ArrayList<>();
	private ItemMeta itemMeta = null;
	private ItemStack item = null;
}
