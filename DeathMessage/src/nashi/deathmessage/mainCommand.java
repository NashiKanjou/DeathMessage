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
				player.sendMessage("=======�۩w�q���H�T��=======");
				player.sendMessage("����Z���ɪ����H�T��\n/dm item <�۩w�q���H�T��>");
				player.sendMessage("�Ť�ɪ����H�T��\n/dm air <�۩w�q���H�T��>");
				player.sendMessage("%player% �N��Q���������aID");
				player.sendMessage("%killer% �N�������誺ID");
				player.sendMessage("�p�G�T����ūh�O��_�w�]");
				player.sendMessage("=======�۩w�q���H�T��=======");
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
									player.sendMessage(ChatColor.GREEN + "�Z�����H�T���w���");
								} else {
									player.sendMessage(ChatColor.DARK_RED + "�A�����H�T�������n��\"%killer%\"(�N��ۤv��ID)");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "�A�����H�T�������n��\"%player%\"(�N��Q�����̪�ID)");
							}
						} else {
							Main.getPlugin().getConfig().set(uid + ".item", null);
							Main.dm_withitem.remove(player);
							player.sendMessage(ChatColor.RED + "�Z�����H�T���w���]");
						}
					} catch (Exception e) {
						Main.getPlugin().getConfig().set(uid + ".item", null);
						Main.dm_withitem.remove(player);
						player.sendMessage(ChatColor.RED + "�Z�����H�T���w���]");
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
									player.sendMessage(ChatColor.GREEN + "�Ť���H�T���w���");
								} else {
									player.sendMessage(ChatColor.DARK_RED + "�A�����H�T�������n��\"%killer%\"(�N��ۤv��ID)");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "�A�����H�T�������n��\"%player%\"(�N��Q�����̪�ID)");
							}
						} else {
							Main.getPlugin().getConfig().set(uid + ".air", null);
							Main.dm.remove(player);
							player.sendMessage(ChatColor.RED + "�Ť���H�T���w���]");
						}
					} catch (Exception e) {
						Main.getPlugin().getConfig().set(uid + ".air", null);
						Main.dm.remove(player);
						player.sendMessage(ChatColor.RED + "�Ť���H�T���w���]");
					}
				}
				return true;
			} else {
				Main.getPlugin().getConfig().set(uid + ".item", null);
				Main.dm_withitem.remove(player);
				Main.getPlugin().getConfig().set(uid + ".air", null);
				Main.dm.remove(player);
				player.sendMessage(ChatColor.RED + "�]���S���v��,�ҥH���H�T���w���]");
				return true;
			}
		}
		return true;
	}

}
