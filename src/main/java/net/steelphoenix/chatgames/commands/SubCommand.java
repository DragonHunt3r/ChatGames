package net.steelphoenix.chatgames.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.Validate;

public abstract class SubCommand implements CommandExecutor, TabCompleter {
	protected final ICGPlugin plugin;
	private final String permission;
	private final List<String> aliases;
	protected SubCommand(ICGPlugin plugin, String permission, String alias) {
		this(plugin, permission, Arrays.asList(alias));
	}
	protected SubCommand(ICGPlugin plugin, String permission, List<String> aliases) {
		Validate.isTrue(aliases != null && !aliases.isEmpty(), "Aliases cannot be null");
		Validate.noneNull(aliases, "Alias cannot be null");

		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
		this.permission = permission;
		this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
	}
	@Nullable
	public final String getPermission() {
		return permission;
	}
	@NotNull
	public final List<String> getAliases() {
		return aliases;
	}
	@NotNull
	public String getUsage() {
		return aliases.get(0);
	}
	public final boolean isAuthorized(@NotNull CommandSender sender) {
		// Preconditions
		Validate.notNull(sender, "Sender cannot be null");

		return permission == null || permission.isEmpty() || !(sender instanceof Player) || sender.hasPermission(permission);
	}
	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// No permission
		if (!isAuthorized(sender)) {
			sender.sendMessage(Util.color(Message.COMMAND_NO_PERMISSION));
			return true;
		}

		return onCommand(sender, label, args);
	}
	@NotNull
	@Override
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		List<String> list = isAuthorized(sender) ? onTabComplete(sender, label, args) : null;
		return list == null ? new LinkedList<>() : list;
	}
	public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
	@Nullable
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		return null;
	}
}
