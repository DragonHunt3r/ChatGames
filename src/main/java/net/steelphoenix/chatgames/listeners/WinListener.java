package net.steelphoenix.chatgames.listeners;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.event.AsyncChatGameWinEvent;
import net.steelphoenix.chatgames.util.VaultHook;
import net.steelphoenix.core.util.Validate;

public class WinListener implements Listener {
	private final ICGPlugin plugin;
	private VaultHook vault = null;
	public WinListener(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");

		// Hook vault
		// We assume plugins do not change through reloads so only once will the plugin attempt to hook Vault
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		this.vault = vault == null || !vault.isEnabled() ? null : new VaultHook(plugin);
		if (this.vault == null || !this.vault.isHooked()) {
			plugin.getLogger().log(Level.INFO, "Vault Economy not found");
		}
	}
	@EventHandler
	public final void onWin(AsyncChatGameWinEvent event) {
		// Default reward listener is not enabled
		if (!plugin.getConfiguration().getBoolean("reward-default.enabled")) {
			return;
		}

		// Schedule reward synchronously
		plugin.getServer().getScheduler().runTask(plugin, () -> {
			// Check if Vault is enabled for economy rewards
			if (vault != null && vault.isHooked()) {
				// Deposit money if amount to give is positive
				double amount = plugin.getConfiguration().getDouble("reward-default.money");
				if (amount > 0D) {
					vault.getEconomy().depositPlayer(event.getPlayer(), amount);
				}
			}

			// Execute commands if any are defined
			List<String> cmds = plugin.getConfiguration().getStringList("reward-default.commands");
			if (cmds != null) {
				cmds.forEach(command -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", event.getPlayer().getName())));
			}
		});
	}
}
