package net.steelphoenix.chatgames.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class ListSubCommand extends SubCommand {
	public ListSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.admin", "list");
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		sender.sendMessage(Util.color(Message.COMMAND_LIST_HEADER));
		plugin.getTask().getGenerators().forEach(generator -> sender.sendMessage(Util.color(Message.COMMAND_LIST_ENTRY.replace("%entry%", generator.getIdentifier()))));
		return true;
	}
}
