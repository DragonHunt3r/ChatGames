package net.steelphoenix.chatgames.commands.subcommands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class VersionSubCommand extends SubCommand {
	public VersionSubCommand(ICGPlugin plugin) {
		super(plugin, null, Arrays.asList("version", "ver", "v"));
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		sender.sendMessage(Util.color(Message.COMMAND_VERSION.replace("%version%", plugin.getDescription().getVersion()).replace("%author%", plugin.getDescription().getAuthors().get(0))));
		return true;
	}
}