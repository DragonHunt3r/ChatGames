package net.steelphoenix.chatgames.commands.subcommands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class ResetSubCommand extends SubCommand {
	public ResetSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.admin.reset", "reset");
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 2) {
			return false;
		}

		// Reset for a specific player
		if (args.length == 2) {
			OfflinePlayer player = Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(p -> p.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);

			// No such player
			if (player == null) {
				sender.sendMessage(Util.color(Message.COMMAND_NO_PLAYER.replace("%player%", args[1])));
				return true;
			}

			sender.sendMessage(Util.color(Message.COMMAND_RESET_PLAYER.replace("%player%", player.getName())));
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> plugin.getLeaderboard().reset(player.getUniqueId()));
			return true;
		}

		// Reset all scores
		sender.sendMessage(Util.color(Message.COMMAND_RESET_ALL));
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> plugin.getLeaderboard().reset());
		return true;
	}
	@Nullable
	@Override
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		return args.length == 2 ? plugin.getServer().getOnlinePlayers().stream().map(player -> player.getName()).filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList()) : null;
	}
	@NotNull
	@Override
	public final String getUsage() {
		return super.getUsage() + " [player]";
	}
}
