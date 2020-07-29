package net.steelphoenix.chatgames.listeners;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.core.util.Validate;

public class PlayerListener implements Listener {
	private static enum Statement {
		CLEANUP ("DELETE FROM chatgames_leaderboard WHERE score = 0"),
		CREATE_EXEMPT ("CREATE TABLE IF NOT EXISTS chatgames_exempt (uuid BINARY(16) PRIMARY KEY)"),
		CREATE_SCORE ("CREATE TABLE IF NOT EXISTS chatgames_leaderboard (uuid BINARY(16) PRIMARY KEY, score INT NOT NULL)"),
		DELETE ("DELETE FROM chatgames_exempt WHERE uuid = ?"),
		INSERT_EXEMPT ("INSERT OR IGNORE INTO chatgames_exempt VALUES (?)"),
		INSERT_SCORE ("INSERT OR IGNORE INTO chatgames_leaderboard VALUES (?, 0)"),
		SELECT ("SELECT * FROM chatgames_exempt WHERE uuid = ?");

		private final String string;
		private Statement(String string) {
			this.string = string;
		}
		@Override
		public String toString() {
			return string;
		}
	}
	private final ICGPlugin plugin;
	public PlayerListener(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");

		// Create table if it does not exist
		try {
			plugin.getDatabase().update(Statement.CREATE_EXEMPT.toString());
			plugin.getDatabase().update(Statement.CREATE_SCORE.toString());
			plugin.getDatabase().update(Statement.CLEANUP.toString());
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not create table for database, this may result in more errors", exception);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public final void onJoin(AsyncPlayerPreLoginEvent event) {
		// Player login is not allowed
		if (event.getLoginResult() != Result.ALLOWED) {
			return;
		}

		try {
			// Insert score
			plugin.getDatabase().update(Statement.INSERT_SCORE.toString(), statement -> statement.setBytes(1, Util.idToBytes(event.getUniqueId())));

			// Get exempt status
			boolean exempt = plugin.getDatabase().query(Statement.SELECT.toString(), statement -> statement.setBytes(1, Util.idToBytes(event.getUniqueId())), result -> {
				return result.next();
			});

			// Player is exempt
			if (exempt) {
				plugin.getExemptPlayers().add(event.getUniqueId());
			}
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not query database", exception);
		}
	}
	@EventHandler
	public final void onQuit(PlayerQuitEvent event) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			boolean exempt = plugin.getExemptPlayers().remove(event.getPlayer().getUniqueId());
			try {
				plugin.getDatabase().update((exempt ? Statement.INSERT_EXEMPT : Statement.DELETE).toString(), statement -> statement.setBytes(1, Util.idToBytes(event.getPlayer().getUniqueId())));
			} catch (SQLException exception) {
				plugin.getLogger().log(Level.SEVERE, "Could not update database", exception);
			}
		});
	}
}
