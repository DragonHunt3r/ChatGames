package net.steelphoenix.chatgames.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.steelphoenix.annotations.Contract;
import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.Validate;

/**
 * Event for when a game ends by a player sending the correct answer.
 * This event is called asynchronous.
 * @author SteelPhoenix
 */
public class AsyncChatGameWinEvent extends ChatGameEvent {
	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private final String answer;
	private final long answerTime;
	private String win;
	public AsyncChatGameWinEvent(IGame game, Player player, String answer, long answerTime) {
		super(game, true);
		this.player = Validate.notNull(player, "Player cannot be null");
		this.answer = Validate.notNull(answer, "Answer cannot be null");
		this.answerTime = answerTime;
		this.win = Message.WIN_MESSAGE;

		Validate.isTrue(answerTime >= 0L, "Answer time cannot be negative");
	}
	/**
	 * Get the winning player.
	 * @return the winning player.
	 */
	@NotNull
	public final Player getPlayer() {
		return player;
	}
	/**
	 * Get the game's question.
	 * @return the Question object related to this event.
	 */
	@NotNull
	public final Question getQuestion() {
		return getGame().getQuestion();
	}
	/**
	 * Get the user input.
	 * @return the answer the player wrote.
	 */
	@NotNull
	public final String getAnswer() {
		return answer;
	}
	/**
	 * Get the message sent to the winning player or null if no message is defined.
	 * @return win message to be sent to the winning player.
	 */
	@Nullable
	public final String getWinMessage() {
		return win;
	}
	/**
	 * Set the message the winning player receives
	 * @param win new message
	 */
	public final void setWinMessage(@Nullable String win) {
		this.win = win;
	}
	/**
	 * Get the answer time in milliseconds.
	 * @return time it took to answer in milliseconds.
	 */
	@Contract("val >= 0L")
	public final long getAnswerTime() {
		return answerTime;
	}
	// Note that these methods are not marked final so the (legacy) ChatGameWinEvent can override it with a different handler list
	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	@NotNull
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
