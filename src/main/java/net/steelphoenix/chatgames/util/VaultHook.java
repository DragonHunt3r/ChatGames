package net.steelphoenix.chatgames.util;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.steelphoenix.chatgames.api.ICGPlugin;

public class VaultHook {
	private final Economy economy;
	public VaultHook(ICGPlugin plugin) {
		RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		economy = provider == null ? null : provider.getProvider();
	}
	public final Economy getEconomy() {
		return economy;
	}
	public final boolean isHooked() {
		return economy != null;
	}
}
