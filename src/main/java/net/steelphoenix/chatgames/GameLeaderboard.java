package net.steelphoenix.chatgames;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.Leaderboard;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.core.util.Validate;

public class GameLeaderboard implements Leaderboard {
	private static enum Statement {
		DELETE ("UPDATE chatgames_leaderboard SET score = 0 WHERE uuid = ?"),
		GET_ALL ("SELECT * FROM chatgames_leaderboard ORDER BY score DESC LIMIT ?"),
		GET_ID ("SELECT score FROM chatgames_leaderboard WHERE uuid = ?"),
		INCREMENT ("UPDATE chatgames_leaderboard SET score = score + 1 WHERE uuid = ?"),
		TRUNCATE ("UPDATE chatgames_leaderboard SET score = 0");

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
	public GameLeaderboard(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
	}
	@Override
	public final void increment(@NotNull UUID id) {
		// Preconditions
		Validate.notNull(id, "ID cannot be null");

		try {
			plugin.getDatabase().update(Statement.INCREMENT.toString(), statement -> statement.setBytes(1, Util.idToBytes(id)));
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not increment score for " + id, exception);
		}
	}
	@Override
	public final void reset() {
		try {
			plugin.getDatabase().update(Statement.TRUNCATE.toString());
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not reset leaderboard", exception);
		}
	}
	@Override
	public final void reset(@NotNull UUID id) {
		// Preconditions
		Validate.notNull(id, "UUID cannot be null");

		// Remove from database
		try {
			plugin.getDatabase().update(Statement.DELETE.toString(), statement -> statement.setBytes(1, Util.idToBytes(id)));
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not reset leaderboard score for " + id, exception);
		}
	}
	@NotNull
	@Override
	public final List<Entry<UUID, Integer>> getHighscores(int size) {
		// Preconditions
		Validate.isTrue(size >= 0, "Size cannot be negative");

		// Get top x entries from database
		try {
			return plugin.getDatabase().query(Statement.GET_ALL.toString(), statement -> statement.setInt(1, size), result -> {
				List<Entry<UUID, Integer>> list = new ArrayList<>(size);
				while (result.next()) {
					list.add(new LeaderboardEntry(Util.idFromBytes(result.getBytes("uuid")), result.getInt("score")));
				}
				return list;
			});
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not load leaderboard", exception);
			return new LinkedList<>();
		}
	}
	@Override
	public final int get(@NotNull UUID id) {
		// Preconditions
		Validate.notNull(id, "UUID cannot be null");

		try {
			return plugin.getDatabase().query(Statement.GET_ID.toString(), statement -> statement.setBytes(1, Util.idToBytes(id)), result -> result.next() ? result.getInt("score") : 0);
		} catch (SQLException exception) {
			plugin.getLogger().log(Level.SEVERE, "Could not get leaderboard score for " + id + ", using 0", exception);
			return 0;
		}
	}
	private class LeaderboardEntry implements Entry<UUID, Integer> {
		private final UUID uuid;
		private final int val;
		private LeaderboardEntry(UUID uuid, int val) {
			this.uuid = Validate.notNull(uuid, "UUID cannot be null");
			this.val = val;
		}
		@Override
		public final UUID getKey() {
			return uuid;
		}
		@Override
		public final Integer getValue() {
			return val;
		}
		@Override
		public final Integer setValue(Integer value) {
			throw new UnsupportedOperationException();
		}
	}
}
