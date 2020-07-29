package net.steelphoenix.chatgames.commands.subcommands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.SubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;

public class ToggleSubCommand extends SubCommand {
	public ToggleSubCommand(ICGPlugin plugin) {
		super(plugin, null, "toggle");
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		// Too many arguments
		if (args.length > 1) {
			return false;
		}

		// Not a player
		if (!(sender instanceof Player)) {
			sender.sendMessage(Util.color(Message.COMMAND_NOT_A_PLAYER));
			return true;
		}

		UUID uuid = ((Player) sender).getUniqueId();

		// Add or remove from exempt set
		boolean enabled;
		if (plugin.getExemptPlayers().contains(uuid)) {
			plugin.getExemptPlayers().remove(uuid);
			enabled = true;
		}
		else {
			plugin.getExemptPlayers().add(uuid);
			enabled = false;
		}

		sender.sendMessage(Util.color(Message.COMMAND_TOGGLE.replace("%state%", enabled ? Message.COMMAND_ENABLED : Message.COMMAND_DISABLED)));
		return true;
	}
}
