package net.steelphoenix.chatgames.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.commands.subcommands.CurrentSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.LeaderboardSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.ListSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.ReloadSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.ResetSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.SkipSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.ToggleSubCommand;
import net.steelphoenix.chatgames.commands.subcommands.VersionSubCommand;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.core.util.Validate;

public class ChatGamesCommand implements CommandExecutor, TabCompleter {
	private final List<SubCommand> list;
	public ChatGamesCommand(ICGPlugin plugin) {
		Validate.notNull(plugin, "Plugin cannot be null");

		// Subcommands
		this.list = Arrays.asList(new CurrentSubCommand(plugin), new LeaderboardSubCommand(plugin), new ListSubCommand(plugin), new ReloadSubCommand(plugin), new ResetSubCommand(plugin), new SkipSubCommand(plugin), new ToggleSubCommand(plugin), new VersionSubCommand(plugin));

		// Executor registering
		PluginCommand command = ((JavaPlugin) plugin).getCommand("chatgames");
		command.setUsage(getUsage());
		command.setExecutor(this);
		command.setTabCompleter(this);
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Find the correct subcommand
		SubCommand command = args.length < 1 ? null : list.stream().filter(sub -> sub.getAliases().contains(args[0].toLowerCase())).findFirst().orElse(null);

		// Execute the subcommand if present
		if (command != null && command.onCommand(sender, cmd, label, args)) {
			return true;
		}

		sender.sendMessage(Util.color("&c" + cmd.getUsage().replace("<command>" , label)));
		return true;
	}
	@NotNull
	@Override
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		List<String> suggestions = new LinkedList<>();

		// No subcommand defined
		if (args.length < 1) {
			return suggestions;
		}

		// Find the correct subcommand
		if (args.length == 1) {

			// Add all sub command aliases to the list of suggestions
			list.stream().filter(sub -> sub.isAuthorized(sender)).map(SubCommand::getAliases).forEach(list -> suggestions.addAll(list));

			// Filter out all suggestions not matching the sender's input
			return suggestions.stream().filter(string -> string.toLowerCase().startsWith(args[0].toLowerCase())).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
		}

		// Get completions for the entered subcommand
		SubCommand command = list.stream().filter(sub -> sub.getAliases().contains(args[0].toLowerCase())).findFirst().orElse(null);

		return command == null ? suggestions : command.onTabComplete(sender, cmd, label, args);
	}
	@NotNull
	private final String getUsage() {
		return "/<command> <" + String.join("|", list.stream().map(sub -> sub.getUsage()).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList())) + ">";
	}
}
