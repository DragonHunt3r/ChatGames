package net.steelphoenix.chatgames.commands.subcommands;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class LeaderboardSubCommand extends SubCommand {
	private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.US);
	public LeaderboardSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.leaderboard", Arrays.asList("leaderboard", "lb", "top"));
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		sender.sendMessage(Util.color(Message.COMMAND_LEADERBOARD_LOADING));
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			int i = 1;
			for (Entry<UUID, Integer> entry : plugin.getLeaderboard().getHighscores(10)) {
				OfflinePlayer player = Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(op -> op.getUniqueId().equals(entry.getKey())).findFirst().orElse(null);
				sender.sendMessage(Util.color(Message.COMMAND_LEADERBOARD_ENTRY.replace("%place%", String.valueOf(i++)).replace("%name%", player == null ? entry.getKey().toString() : player.getName()).replace("%score%", FORMAT.format(entry.getValue()))));
			}

			// No iterations
			if (i == 1) {
				sender.sendMessage(Util.color(Message.COMMAND_LEADERBOARD_EMPTY));
			}
		});
		return true;
	}
}
