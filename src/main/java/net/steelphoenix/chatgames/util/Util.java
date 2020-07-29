package net.steelphoenix.chatgames.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.config.IConfig;
import net.steelphoenix.chatgames.config.YAMLConfig;
import net.steelphoenix.chatgames.util.messaging.annotations.ConfigBase;
import net.steelphoenix.chatgames.util.messaging.annotations.ConfigValue;
import net.steelphoenix.core.util.Validate;

@net.steelphoenix.annotations.Util
public class Util {
	private Util() {
		// Nothing
	}
	private static final char COLOR_CHAR = '\u00A7';
	private static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
	private static final Pattern STRIP_PATTERN = Pattern.compile("(?i)[" + COLOR_CHAR + "&][0-9A-FK-ORX]");
	@NotNull
	public static final String color(@NotNull String input) {
		// Preconditions
		Validate.notNull(input, "Input cannot be null");

		char[] buf = input.toCharArray();
		for (int i = 0; i < buf.length - 1; i++) {
			if (buf[i] == '&' && COLOR_CODES.indexOf(buf[i + 1]) != -1) {
				buf[i] = ChatColor.COLOR_CHAR;
				buf[i + 1] = Character.toUpperCase(buf[i + 1]);
			}
		}
		return new String(buf);
	}
	@NotNull
	public static final String uncolor(@NotNull String input) {
		// Preconditions
		Validate.notNull(input, "Input cannot be null");

		return STRIP_PATTERN.matcher(input).replaceAll("");
	}
	@NotNull
	public static final byte[] idToBytes(@NotNull UUID uuid) {
		// Preconditions
		Validate.notNull(uuid, "UUID cannot be null");

		ByteBuffer buf = ByteBuffer.wrap(new byte[16]);
		buf.putLong(uuid.getMostSignificantBits());
		buf.putLong(uuid.getLeastSignificantBits());
		return buf.array();
	}
	@NotNull
	public static final UUID idFromBytes(@NotNull byte[] bytes) {
		// Preconditions
		Validate.notNull(bytes, "Bytes cannot be null");

		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return new UUID(buf.getLong(), buf.getLong());
	}
	public static final boolean loadAnnotatedConfig(@NotNull ICGPlugin plugin, @NotNull Class<?> clazz) {
		// Preconditions
		Validate.notNull(plugin, "Plugin cannot be null");
		Validate.notNull(clazz, "Class cannot be null");

		// Get the base annotation
		ConfigBase base = clazz.getAnnotation(ConfigBase.class);
		Validate.notNull(base, "No @ConfigBase annotation found in " + clazz.getName());

		boolean success = true;
		// Config
		File file = new File(plugin.getDataFolder(), base.file());
		if (!file.exists()) {
			plugin.saveResource(base.file(), false);
		}
		IConfig config = new YAMLConfig(plugin.getLogger(), file);

		// If the config does not load properly we do not return so at least default values are set
		if (!config.load()) {
			success &= false;
		}

		// Iterate over each field
		for (Field field : clazz.getDeclaredFields()) {
			// Not static
			if (!Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			// Not a String
			if (field.getType() != String.class) {
				continue;
			}

			ConfigValue val = field.getAnnotation(ConfigValue.class);

			// No such annotation
			if (val == null) {
				continue;
			}

			boolean access = field.isAccessible();
			field.setAccessible(true);
			String path = base.path().isEmpty() ? val.path() : base.path() + "." + val.path();

			// Set to default if not set
			if (!config.isSet(path)) {
				config.set(path, val.def());
			}

			// Set the value
			try {
				field.set(null, config.getString(path));
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				plugin.getLogger().log(Level.WARNING, "Could not set field " + field.getName() + " in " + clazz.getName(), exception);
				success &= false;
			}

			field.setAccessible(access);
		}

		// Push all missing defaults back to file if loading was successful
		if (success) {
			success &= config.save();
		}

		return success;
	}
}
