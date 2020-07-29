package net.steelphoenix.chatgames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.scheduler.BukkitTask;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.event.ChatGameStartEvent;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.chatgames.api.game.IGameTask;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.generators.ErrorQuestion;
import net.steelphoenix.core.util.RandomUtil;
import net.steelphoenix.core.util.Validate;

public class GameTask implements IGameTask {
	private final ICGPlugin plugin;
	private final List<Generator> generators = new ArrayList<>();
	private IGame game = null;
	private BukkitTask task = null;
	public GameTask(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
	}
	@Override
	public final void run() {
		ChatGameStartEvent event = new ChatGameStartEvent(new Game(plugin, getNextQuestion()));
		plugin.getServer().getPluginManager().callEvent(event);
		this.game = event.isCancelled() ? null : event.getGame();
	}
	@Nullable
	@Override
	public final IGame getCurrentGame() {
		return game;
	}
	@NotNull
	@Override
	public final Collection<Generator> getGenerators() {
		return new ArrayList<>(generators);
	}
	@Override
	public final void addGenerator(@NotNull Generator generator) {
		// Preconditions
		Validate.notNull(generator, "Generator cannot be null");
		Validate.isTrue(!generators.contains(generator), () -> new IllegalStateException("Generator already registered"));

		generators.add(generator);
	}
	@Override
	public final synchronized void skip() {
		// End the current task
		end();

		// Reschedule
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 60L, getGameDelay());
	}
	@Override
	public final synchronized void end() {
		// End the current game if available
		if (game != null) {
			game.end();
		}

		// Cancel the game task
		if (task != null) {
			task.cancel();
		}
	}
	public final void clearGenerators() {
		generators.clear();
	}
	@NotNull
	private final Question getNextQuestion() {
		// No generators
		if (generators.isEmpty()) {
			return new ErrorQuestion("No Question Generator found.");
		}

		return generators.get(RandomUtil.randomInt(0, generators.size())).getNewQuestion();
	}
	private final long getGameDelay() {
		long delay = plugin.getConfiguration().getLong("game-delay") * 20L;
		if (delay <= 0) {
			delay = 6000L;
		}
		return delay;
	}
}