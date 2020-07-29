package net.steelphoenix.chatgames.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.core.api.registry.Named;
import net.steelphoenix.core.util.Validate;

/**
 * A question generator that automatically registers itself
 * @see Generator
 */
public abstract class ChatGameGenerator implements Generator, Named {
	private final Plugin plugin;
	protected ChatGameGenerator(Plugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");

		// Register itself
		Plugin cg = Bukkit.getPluginManager().getPlugin("ChatGames");
		Validate.isTrue(cg != null && cg.isEnabled(), () -> new IllegalStateException("ChatGames plugin is not enabled"));
		Validate.isTrue(cg instanceof ICGPlugin, "Plugin is not of type ICGPlugin");
		((ICGPlugin) cg).getTask().addGenerator(this);
	}
	@Override
	public final String getIdentifier() {
		return getName() + " (" + plugin.getName() + ')';
	}
}
