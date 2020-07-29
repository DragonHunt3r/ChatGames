package net.steelphoenix.chatgames.api.game;

import java.util.Collection;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;

public interface IGameTask extends Runnable {
	/**
	 * Get the current running game.
	 * This game may not be active.
	 * @return the last known game or null if no game was ever scheduled
	 * @see IGame#isActive()
	 */
	@Nullable
	public IGame getCurrentGame();
	/**
	 * Get a collectino of all active question generators.
	 * @return a copy of all generators.
	 */
	@NotNull
	public Collection<Generator> getGenerators();
	/**
	 * Add a question generator to this game scheduler.
	 * @param generator The generator to add
	 */
	public void addGenerator(@NotNull Generator generator);
	/**
	 * End the current game and start a new one.
	 */
	public void skip();
	/**
	 * End the current game.
	 */
	public void end();
}
