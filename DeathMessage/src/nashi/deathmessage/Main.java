package nashi.deathmessage;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.Zrips.CMI.Modules.RawMessages.RawMessage;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatModifier;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.PlayerList;

public class Main extends JavaPlugin implements Listener {
	public static HashMap<Player, String> dm = new HashMap<Player, String>();
	public static HashMap<Player, String> dm_withitem = new HashMap<Player, String>();
	private static Plugin plugin;

	public void onEnable() {
		plugin = this;
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("dm").setExecutor(new mainCommand());
	}

	public void onDisable() {
		this.saveConfig();
	}

	@EventHandler
	public void login(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!player.hasPermission("deathmessage.use")) {
			return;
		}
		boolean p = false;
		boolean k = false;
		UUID uid = player.getUniqueId();
		try {
			String air = plugin.getConfig().getString(uid + ".air");
			if (air != null) {
				if (air.contains("%player%")) {
					p = true;
				}
				if (air.contains("%killer%")) {
					k = true;
				}
				if (p || player.hasPermission("deathmessage.noplayer")) {
					if (k || player.hasPermission("deathmessage.nokiller")) {
						Main.dm.put(player, air);
					} else {
						player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%killer%\"(代表自己的ID)");
						return;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%player%\"(代表被擊殺者的ID)");
					return;
				}

			}
		} catch (Exception e1) {
		}

		try {
			String item = plugin.getConfig().getString(uid + ".item");
			if (item != null) {
				if (item.contains("%player%")) {
					p = true;
				}
				if (item.contains("%killer%")) {
					k = true;
				}
				if (p || player.hasPermission("deathmessage.noplayer")) {
					if (k || player.hasPermission("deathmessage.nokiller")) {
						Main.dm_withitem.put(player, item);
					} else {
						player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%killer%\"(代表自己的ID)");
						return;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%player%\"(代表被擊殺者的ID)");
					return;
				}
			}
		} catch (Exception e1) {
		}

	}

	public static boolean a(Player p, Player killer) {
		ItemStack a = killer.getInventory().getItemInMainHand();
		if (a != null && !a.getType().equals(Material.AIR)) {
			if (!dm_withitem.containsKey(killer)) {
				return false;
			}
			/*
			 * net.minecraft.server.v1_16_R1.ItemStack b = CraftItemStack.asNMSCopy(a);
			 * 
			 * NBTTagCompound c = new NBTTagCompound();
			 * 
			 * b.save(c);
			 * 
			 * ChatComponentText d = null;
			 * 
			 * d = new ChatComponentText(ChatColor .translateAlternateColorCodes('&',
			 * dm_withitem.get(killer).replaceAll("%killer%", killer.getDisplayName())
			 * .replaceAll("%player%", p.getDisplayName())) .replace("%item%",
			 * b.getName().getString()));
			 * 
			 * ChatModifier g = d.getChatModifier(); //(IChatBaseComponent) new
			 * ChatComponentText(c.toString()) // g.setChatHoverable(new
			 * ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ITEM, (IChatBaseComponent)
			 * new ChatComponentText(c.toString())));
			 * 
			 * d.setChatModifier(g); ChatComponentText[] m = new ChatComponentText[1]; m[0]
			 * = d;
			 * 
			 * @SuppressWarnings("deprecation") PlayerList h =
			 * MinecraftServer.getServer().getPlayerList();
			 */
			RawMessage rm= new RawMessage();
			rm.clear();
			rm.addText(String.valueOf(ChatColor
					.translateAlternateColorCodes('&',
							dm_withitem.get(killer).replaceAll("%killer%", killer.getDisplayName())
									.replaceAll("%player%", p.getDisplayName()))
					.replace("%item%", a.getItemMeta().getDisplayName())));
			rm.addItem(a);

			for (Player i : Bukkit.getOnlinePlayers()) {
				rm.show(i);
				// h.getPlayer(i.getName()).sendMessage(m);
			}
			return true;
		} else {
			if (!dm.containsKey(killer)) {
				return false;
			}
			net.minecraft.server.v1_16_R3.ItemStack b = CraftItemStack.asNMSCopy(a);

			NBTTagCompound c = new NBTTagCompound();

			b.save(c);

			ChatComponentText d = null;

			d = new ChatComponentText(ChatColor.translateAlternateColorCodes('&', dm.get(killer)
					.replaceAll("%killer%", killer.getDisplayName()).replaceAll("%player%", p.getDisplayName())));

			ChatModifier g = d.getChatModifier();

			d.setChatModifier(g);

			@SuppressWarnings("deprecation")
			PlayerList h = MinecraftServer.getServer().getPlayerList();
			ChatComponentText[] m = new ChatComponentText[1];
			m[0] = d;
			for (Player i : Bukkit.getOnlinePlayers()) {
				h.getPlayer(i.getName()).sendMessage(i.getUniqueId(), m);
			}
			return true;
		}
	}

	@EventHandler
	public void left(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		dm.remove(player);
		dm_withitem.remove(player);
	}

	@EventHandler
	public void death(PlayerDeathEvent e) {
		Player player = e.getEntity();
		LivingEntity le = player.getKiller();
		if (le == null) {
			return;
		}
		if (le instanceof Player) {
			Player killer = (Player) le;
			if (!dm.containsKey(killer) && !dm_withitem.containsKey(killer)) {
				return;
			}
			if (a(player, killer)) {
				e.setDeathMessage(null);
			}
		}
	}

	public static Plugin getPlugin() {
		return plugin;
	}

}
