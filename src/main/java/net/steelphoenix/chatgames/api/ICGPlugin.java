package net.steelphoenix.chatgames.api;

import java.util.Set;
import java.util.UUID;

import javax.script.ScriptEngine;

import org.bukkit.plugin.Plugin;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.game.IGameTask;
import net.steelphoenix.chatgames.config.IConfig;
import net.steelphoenix.core.api.database.IDatabase;

public interface ICGPlugin extends Plugin, Reloadable {
	/**
	 * Get the plugin's configuration.
	 * @return the plugin's config.yml as IConfig
	 */
	@NotNull
	public IConfig getConfiguration();
	/**
	 * Get the running game timer task.
	 * @return the running game task.
	 */
	@NotNull
	public IGameTask getTask();
	/**
	 * Get the plugin's SQLite database.
	 * @return the database.
	 */
	@NotNull
	public IDatabase getDatabase();
	/**
	 * Get the game leaderboard.
	 * @return the game leaderboard.
	 */
	@NotNull
	public Leaderboard getLeaderboard();
	/**
	 * Get the plugin's JS engine.
	 * "JavaScript" by default.
	 * @return the script engine or null.
	 */
	@Nullable
	public ScriptEngine getScriptEngine();
	/**
	 * Get a set of players that are exempt from getting chatgame messages.
	 * @return get the set of exempt players.
	 */
	@NotNull
	public Set<UUID> getExemptPlayers();
}
