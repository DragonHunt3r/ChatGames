package net.steelphoenix.chatgames;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.annotations.Nullable;
import net.steelphoenix.chatgames.api.ChatGameGenerator;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.Leaderboard;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.IGameTask;
import net.steelphoenix.chatgames.commands.ChatGamesCommand;
import net.steelphoenix.chatgames.config.IConfig;
import net.steelphoenix.chatgames.config.YAMLConfig;
import net.steelphoenix.chatgames.generators.AlphabetGenerator;
import net.steelphoenix.chatgames.generators.DifficultEquationGenerator;
import net.steelphoenix.chatgames.generators.EquationGenerator;
import net.steelphoenix.chatgames.generators.RandomSequenceGenerator;
import net.steelphoenix.chatgames.generators.ScrambleGenerator;
import net.steelphoenix.chatgames.generators.TriviaGenerator;
import net.steelphoenix.chatgames.listeners.ChatListener;
import net.steelphoenix.chatgames.listeners.PlayerListener;
import net.steelphoenix.chatgames.listeners.WinListener;
import net.steelphoenix.chatgames.script.JSManager;
import net.steelphoenix.chatgames.util.SQLiteDatabase;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.api.database.IDatabase;
import net.steelphoenix.core.util.Validate;

public class ChatGames extends JavaPlugin implements ICGPlugin {
	private final Set<UUID> exempt =  new HashSet<>();
	private final ScriptEngineManager manager = new ScriptEngineManager(null);
	private IConfig config;
	private IGameTask task;
	private IDatabase db;
	private Leaderboard leaderboard;
	private ScriptEngine engine;
	private JSManager scripts;
	@Override
	public final void onEnable() {
		// Datafolder
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		// Config
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			this.saveResource("config.yml", false);
		}
		config = new YAMLConfig(getLogger(), file);

		// SQLite database file
		File data = new File(getDataFolder(), "data.db");
		if (!data.exists()) {
			try {
				data.createNewFile();
			} catch (IOException exception) {
				getLogger().log(Level.SEVERE, "Could not create database file", exception);
				return;
			}
		}
		try {
			db = new SQLiteDatabase(data);
		} catch (ClassNotFoundException exception) {
			throw new AssertionError("SQLite driver not found", exception);
		}

		// Leaderboard
		leaderboard = new GameLeaderboard(this);

		// Scripts
		scripts = new JSManager(this);

		// Game task
		task = new GameTask(this);

		// Listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new WinListener(this), this);

		// Command
		new ChatGamesCommand(this);

		// Reload
		reload();
	}
	@Override
	public final void onDisable() {
		// Nothing
	}
	@NotNull
	@Override
	public final IConfig getConfiguration() {
		return config;
	}
	@NotNull
	@Override
	public final IGameTask getTask() {
		return task;
	}
	@NotNull
	@Override
	public final IDatabase getDatabase() {
		return db;
	}
	@NotNull
	@Override
	public final Leaderboard getLeaderboard() {
		return leaderboard;
	}
	@Nullable
	@Override
	public final ScriptEngine getScriptEngine() {
		return engine;
	}
	@NotNull
	@Override
	public final Set<UUID> getExemptPlayers() {
		return exempt;
	}
	@Override
	public final boolean reload() {
		boolean success = true;

		// Reload config
		success &= config.reload();

		// Messages
		success &= Util.loadAnnotatedConfig(this, Message.class);

		// JS engine
		String engineName = getConfiguration().getString("js-engine", "JavaScript");
		engine = manager.getEngineByName(engineName);
		if (engine == null) {
			getLogger().log(Level.WARNING, "No JavaScript engine found for " + engineName);
			success &= false;
		}

		// Scripts
		success &= scripts.reload();

		// Generators
		// Get all 3rd party generators
		List<Generator> gens = task.getGenerators().stream().filter(ChatGameGenerator.class::isInstance).collect(Collectors.toList());
		// Clear all generators
		((GameTask) task).clearGenerators();
		// Alphabet questions
		if (config.getBoolean("enable-alphabet")) {
			task.addGenerator(new AlphabetGenerator());
		}
		// Copy questions
		if (config.getBoolean("enable-copy")) {
			task.addGenerator(new RandomSequenceGenerator(this));
		}
		// Maths questions
		if (config.getBoolean("enable-maths")) {
			task.addGenerator(new EquationGenerator(this));
		}
		// Maths questions (difficult)
		if (config.getBoolean("enable-maths-difficult")) {
			task.addGenerator(new DifficultEquationGenerator(this));
		}
		// Unscramble questions
		if (config.getBoolean("enable-scramble")) {
			ScrambleGenerator gen = new ScrambleGenerator(this);
			success &= gen.hasLoadedSuccessful();
			task.addGenerator(gen);
		}
		// Trivia questions
		if (config.getBoolean("enable-trivia")) {
			TriviaGenerator gen  =new TriviaGenerator(this);
			success &= gen.hasLoadedSuccessful();
			task.addGenerator(gen);
		}
		scripts.getGenerators().forEach(task::addGenerator);
		gens.forEach(task::addGenerator);

		task.skip();
		return success;
	}
	// Utility method to color and send a message to all non-exempt online players
	public final void broadcast(@NotNull String message) {
		// Preconditions
		Validate.notNull(message, "Message cannot be null");

		getServer().getOnlinePlayers().stream().filter(player -> !getExemptPlayers().contains(player.getUniqueId())).forEach(player -> player.sendMessage(Util.color(message)));
	}
}