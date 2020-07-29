package net.steelphoenix.chatgames.api;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.steelphoenix.annotations.Contract;
import net.steelphoenix.annotations.NotNull;

/**
 * A Leaderboard should always be accessed asynchronously.
 */
public interface Leaderboard {
	/**
	 * Add 1 to the target's current score.
	 * @param id the target's identifier.
	 */
	public void increment(@NotNull UUID id);
	/**
	 * Reset all scores.
	 */
	public void reset();
	/**
	 * Reset the target's score.
	 * @param id the target's identifier.
	 */
	public void reset(@NotNull UUID id);
	/**
	 * Get a list with the highest scores.
	 * @param size the amount of scores to grab if available.
	 * @return a List with the highest scores in descending order.
	 */
	@Contract("size >= 0")
	@NotNull
	public List<Entry<UUID, Integer>> getHighscores(int size);
	/**
	 * Get the target's score.
	 * @param id the target's identifier.
	 * @return the target's score.
	 */
	public int get(@NotNull UUID id);
}
