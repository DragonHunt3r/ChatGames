package net.steelphoenix.chatgames.script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.Reloadable;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.core.util.Validate;

public class JSManager implements Reloadable {
	private final ICGPlugin plugin;
	private final File dir;
	private final List<Generator> generators = new LinkedList<>();
	public JSManager(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");

		// Directory
		this.dir = new File(plugin.getDataFolder(), "scripts");
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
	@Override
	public final boolean reload() {
		generators.clear();

		// No script engine
		if (plugin.getScriptEngine() == null) {
			return true;
		}

		return getFiles().stream().map(this::load).allMatch(res -> res);
	}
	@NotNull
	public final List<Generator> getGenerators() {
		return generators;
	}
	private final boolean load(File file) {
		// Try to load the generator
		try {
			generators.add(JSGenerator.of(plugin, file));
			return true;
		} catch (IOException exception) {
			plugin.getLogger().log(Level.WARNING, "Could not load script " + file.getName(), exception);
			return false;
		}
	}
	@NotNull
	private final List<File> getFiles() {
		return Arrays.stream(dir.listFiles()).filter(File::isFile).filter(file -> file.getName().toLowerCase().endsWith(".js")).collect(Collectors.toList());
	}
}
