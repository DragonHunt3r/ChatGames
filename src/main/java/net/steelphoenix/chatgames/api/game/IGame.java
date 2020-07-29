package net.steelphoenix.chatgames.api.game;

import net.steelphoenix.annotations.Contract;
import net.steelphoenix.annotations.NotNull;

public interface IGame {
	/**
	 * Get this game's question.
	 * @return this game's question.
	 */
	@NotNull
	public Question getQuestion();
	/**
	 * Get the starting time of this game.
	 * @return the start time as UNIX timestamp.
	 */
	public long getStartTime();
	/**
	 * Check if this game is still active.
	 * @return true if this game is still active (winnable)
	 */
	public boolean isActive();
	/**
	 * Deactivate this game.
	 * Makes it unwinnable.
	 */
	public void setInactive();
	/**
	 * End this game.
	 */
	@Contract("!isActive() -> NOP")
	public void end();
}
