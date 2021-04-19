package nashi.deathmessage;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mainCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if (sender instanceof Player) {
			boolean p = false;
			boolean k = false;
			Player player = (Player) sender;
			UUID uid = player.getUniqueId();
			if (args.length == 0) {
				player.sendMessage("=======自定義殺人訊息=======");
				player.sendMessage("手持武器時的殺人訊息\n/dm item <自定義殺人訊息>");
				player.sendMessage("空手時的殺人訊息\n/dm air <自定義殺人訊息>");
				player.sendMessage("%player% 代表被擊殺的玩家ID");
				player.sendMessage("%killer% 代表擊殺方的ID");
				player.sendMessage("如果訊息放空則是恢復預設");
				player.sendMessage("=======自定義殺人訊息=======");
				return true;
			}
			if (player.hasPermission("deathmessage.use")) {
				if (args[0].equalsIgnoreCase("item")) {
					try {
						if (args[1] != null) {
							String str = args[1];
							for (int i = 2; i < args.length; i++) {
								str += " " + args[i];
							}
							if (str.contains("%player%")) {
								p = true;
							}
							if (str.contains("%killer%")) {
								k = true;
							}
							if (p || player.hasPermission("deathmessage.noplayer")) {
								if (k || player.hasPermission("deathmessage.nokiller")) {
									Main.getPlugin().getConfig().set(uid + ".item", str);
									Main.dm_withitem.put(player, str);
									player.sendMessage(ChatColor.GREEN + "武器殺人訊息已更改");
								} else {
									player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%killer%\"(代表自己的ID)");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%player%\"(代表被擊殺者的ID)");
							}
						} else {
							Main.getPlugin().getConfig().set(uid + ".item", null);
							Main.dm_withitem.remove(player);
							player.sendMessage(ChatColor.RED + "武器殺人訊息已重設");
						}
					} catch (Exception e) {
						Main.getPlugin().getConfig().set(uid + ".item", null);
						Main.dm_withitem.remove(player);
						player.sendMessage(ChatColor.RED + "武器殺人訊息已重設");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("air")) {
					try {
						if (args[1] != null) {
							String str = args[1];
							for (int i = 2; i < args.length; i++) {
								str += " " + args[i];
							}
							if (str.contains("%player%")) {
								p = true;
							}
							if (str.contains("%killer%")) {
								k = true;
							}
							if (p || player.hasPermission("deathmessage.noplayer")) {
								if (k || player.hasPermission("deathmessage.nokiller")) {
									Main.getPlugin().getConfig().set(uid + ".air", str);
									Main.dm.put(player, str);
									player.sendMessage(ChatColor.GREEN + "空手殺人訊息已更改");
								} else {
									player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%killer%\"(代表自己的ID)");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "你的殺人訊息必須要有\"%player%\"(代表被擊殺者的ID)");
							}
						} else {
							Main.getPlugin().getConfig().set(uid + ".air", null);
							Main.dm.remove(player);
							player.sendMessage(ChatColor.RED + "空手殺人訊息已重設");
						}
					} catch (Exception e) {
						Main.getPlugin().getConfig().set(uid + ".air", null);
						Main.dm.remove(player);
						player.sendMessage(ChatColor.RED + "空手殺人訊息已重設");
					}
				}
				return true;
			} else {
				Main.getPlugin().getConfig().set(uid + ".item", null);
				Main.dm_withitem.remove(player);
				Main.getPlugin().getConfig().set(uid + ".air", null);
				Main.dm.remove(player);
				player.sendMessage(ChatColor.RED + "因為沒有權限,所以殺人訊息已重設");
				return true;
			}
		}
		return true;
	}

}
