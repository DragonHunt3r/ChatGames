package net.steelphoenix.chatgames.api.event;

import org.bukkit.event.Event;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.core.util.Validate;

/**
 * Base event for chat game related events.
 * @author SteelPhoenix
 */
public abstract class ChatGameEvent extends Event {
	private final IGame game;
	protected ChatGameEvent(IGame game) {
		this(game, false);
	}
	protected ChatGameEvent(IGame game, boolean async) {
		super(async);
		this.game = Validate.notNull(game, "Game cannot be null");
	}
	/**
	 * Get the new game.
	 * @return the new game.
	 */
	@NotNull
	public final IGame getGame() {
		return game;
	}
}
