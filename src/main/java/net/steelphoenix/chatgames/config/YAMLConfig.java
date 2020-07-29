package net.steelphoenix.chatgames.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.core.util.Validate;

public class YAMLConfig extends YamlConfiguration implements IConfig {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private final Logger logger;
	private final File file;
	private final Charset encoding;
	public YAMLConfig(Logger logger, File file) {
		this(logger, file, UTF8);
	}
	public YAMLConfig(Logger logger, File file, Charset encoding) {
		this.logger = Validate.notNull(logger, "Logger cannot be null");
		this.file = Validate.notNull(file, "File cannot be null");
		this.encoding = Validate.notNull(encoding, "Encoding cannot be null");
	}
	@Override
	public final boolean load() {
		// Clear set values
		map.clear();

		try (InputStream is = new FileInputStream(file); Reader r = new InputStreamReader(is, encoding)){
			load(r);
			return true;
		} catch (InvalidConfigurationException | IOException exception) {
			logger.log(Level.SEVERE, "Could not load configuration from " + file.getName(), exception);
			return false;
		}
	}
	@Override
	public final boolean reload() {
		return load();
	}
	@Override
	public final boolean save() {
		try {
			save(file, encoding);
			return true;
		} catch (IOException exception) {
			logger.log(Level.SEVERE, "Could not save configuration to " + file.getName(), exception);
			return false;
		}
	}
	@Override
	public final void save(@NotNull File file) throws IOException {
		save(file, UTF8);
	}
	/**
	 * Mimics {@link #save(File)}, but lets you define the character encoding.
	 * @param file File to save to.
	 * @param encoding Encoding to use.
	 * @throws IOException Thrown when the given file cannot be written to for any reason.
	 * @throws IllegalArgumentException Thrown when file or charset is null.
	 */
	public final void save(@NotNull File file, @NotNull Charset encoding) throws IOException {
		Validate.notNull(file, "File cannot be null");
		Validate.notNull(encoding, "Encoding cannot be null");

		Files.createParentDirs(file);

		String data = saveToString();

		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), encoding)) {
			writer.write(data);
		}
	}
}
