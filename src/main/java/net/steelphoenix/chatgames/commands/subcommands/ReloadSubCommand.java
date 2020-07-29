package net.steelphoenix.chatgames.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class ReloadSubCommand extends SubCommand {
	public ReloadSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.admin.reload", "reload");
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		sender.sendMessage(Util.color(plugin.reload() ? Message.COMMAND_RELOAD_SUCCESS : Message.COMMAND_RELOAD_FAIL));
		return true;
	}
}