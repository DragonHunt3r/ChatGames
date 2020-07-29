package net.steelphoenix.chatgames.commands.subcommands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class SkipSubCommand extends SubCommand {
	public SkipSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.admin", Arrays.asList("next", "skip"));
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		sender.sendMessage(Util.color(Message.COMMAND_SKIP));
		plugin.getTask().skip();
		return true;
	}
}
