package net.steelphoenix.chatgames.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.game.IGame;

/**
 * Event for when a game starts.
 * @author SteelPhoenix
 */
public class ChatGameStartEvent extends ChatGameEvent implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancel = false;
	public ChatGameStartEvent(IGame game) {
		super(game);
	}
	@Override
	public final boolean isCancelled() {
		return cancel;
	}
	@Override
	public final void setCancelled(boolean cancel) {
		this.cancel = cancel;
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
