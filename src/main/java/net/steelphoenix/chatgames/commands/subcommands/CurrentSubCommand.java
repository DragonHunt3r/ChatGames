package net.steelphoenix.chatgames.commands.subcommands;

import org.bukkit.command.CommandSender;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class CurrentSubCommand extends SubCommand {
	public CurrentSubCommand(ICGPlugin plugin) {
		super(plugin, "chatgames.admin", "current");
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		IGame current = plugin.getTask().getCurrentGame();

		// No current game
		if (current == null || !current.isActive()) {
			sender.sendMessage(Util.color(Message.COMMAND_CURRENT_NO_GAME));
			return true;
		}

		// Send game info
		sender.sendMessage(Util.color(Message.COMMAND_CURRENT_HEADER));
		sender.sendMessage(Util.color(Message.COMMAND_CURRENT_QUESTION.replace("%question%", Util.uncolor(current.getQuestion().getMessage().replace("%question%", current.getQuestion().getQuestion())))));
		sender.sendMessage(Util.color(Message.COMMAND_CURRENT_ANSWER.replace("%answer%", Util.uncolor(current.getQuestion().getAnswer()))));
		sender.sendMessage(Util.color(Message.COMMAND_CURRENT_TIME.replace("%time%", String.valueOf((((System.currentTimeMillis() - current.getStartTime()) / 10L) / 100D)))));
		return true;
	}
}