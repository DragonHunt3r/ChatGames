package net.steelphoenix.chatgames.api.event;

import org.bukkit.event.HandlerList;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.game.IGame;

/**
 * Event for when a game ends by timeout.
 * @author SteelPhoenix
 */
public class ChatGameExpireEvent extends ChatGameEvent {
	private static final HandlerList HANDLERS = new HandlerList();
	public ChatGameExpireEvent(IGame game) {
		super(game);
	}
	@NotNull
	@Override
	public final HandlerList getHandlers() {
		return HANDLERS;
	}
	@NotNull
	public static final HandlerList getHandlerList() {
		return HANDLERS;
	}
}
