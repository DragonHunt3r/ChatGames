package net.steelphoenix.chatgames;

import java.text.NumberFormat;
import java.util.Locale;

import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.event.ChatGameExpireEvent;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.Validate;

public class Game implements IGame {
	private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.US);
	private final ICGPlugin plugin;
	private final Question question;
	private final long start = System.currentTimeMillis();
	private boolean active = true;
	public Game(ICGPlugin plugin, Question question) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
		this.question = Validate.notNull(question, "Question cannot be null");

		// If enough players are online
		if (checkPlayerRequirement()) {
			((ChatGames) plugin).broadcast(question.getMessage().replace("%question%", question.getQuestion()));
			plugin.getServer().getScheduler().runTaskLater(plugin, this::end, getAnswerTime());
		}
	}
	@Override
	public final Question getQuestion() {
		return question;
	}
	@Override
	public final boolean isActive() {
		return active;
	}
	@Override
	public final synchronized void setInactive() {
		this.active = false;
	}
	@Override
	public final long getStartTime() {
		return start;
	}
	@Override
	public final synchronized void end() {
		// Game has already ended
		if (!isActive()) {
			return;
		}

		setInactive();
		((ChatGames) plugin).broadcast(Message.ANNOUNCEMENT_GAMEOVER);
		((ChatGames) plugin).broadcast(Message.ANNOUNCEMENT_ANSWER.replace("%answer%", question.getAnswer()));
		plugin.getServer().getPluginManager().callEvent(new ChatGameExpireEvent(this));
	}
	private final boolean checkPlayerRequirement() {
		int required = plugin.getConfiguration().getInt("required-players");
		if (plugin.getServer().getOnlinePlayers().size() < required) {
			((ChatGames) plugin).broadcast(Message.ANNOUNCEMENT_NO_PLAYERS.replace("%online%", FORMAT.format(plugin.getServer().getOnlinePlayers().size())).replace("%required%", FORMAT.format(required)));
			setInactive();
			return false;
		}
		return true;
	}
	private final long getAnswerTime() {
		long delay = plugin.getConfiguration().getLong("answer-time") * 20L;
		if (delay <= 0) {
			delay = 2400L;
		}
		return delay;
	}
}
