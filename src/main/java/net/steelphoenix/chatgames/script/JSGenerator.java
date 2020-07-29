package net.steelphoenix.chatgames.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.generators.ErrorQuestion;
import net.steelphoenix.core.api.function.exception.ExceptionSupplier;
import net.steelphoenix.core.util.IOUtil;
import net.steelphoenix.core.util.Validate;

public class JSGenerator implements Generator {
	private final ICGPlugin plugin;
	private final MethodInvoker invoker;
	private JSGenerator(ICGPlugin plugin, MethodInvoker invoker) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
		this.invoker = Validate.notNull(invoker, "Function invoker cannot be null");
	}
	@NotNull
	@Override
	public final Question getNewQuestion() {
		try {
			return JSQuestion.of(invoker.get());
		} catch (GameScriptException exception) {
			// If there is a throwable cause, we log this
			if (exception.getCause() != null) {
				plugin.getLogger().log(Level.WARNING, "An error occurred trying to use " + getIdentifier(), exception.getCause());
			}

			return new ErrorQuestion(exception.getMessage());
		}
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return invoker.getFile().getName();
	}
	public static final Generator of(@NotNull ICGPlugin plugin, @NotNull File file) throws IOException {
		// Preconditions
		Validate.notNull(plugin, "Plugin cannot be null");
		Validate.notNull(file, "File cannot be null");

		MethodInvoker invoker = new MethodInvoker(plugin.getScriptEngine(), file);
		return new JSGenerator(plugin, invoker);
	}
	private static class MethodInvoker implements ExceptionSupplier<Bindings, GameScriptException> {
		private final ScriptEngine engine;
		private final File file;
		private final String content;
		private MethodInvoker(ScriptEngine engine, File file) throws IOException {
			this.engine = Validate.notNull(engine, "Engine cannot be null");
			this.file = Validate.notNull(file, "File cannot be null");

			// File reading
			try (Reader reader = new FileReader(file)) {
				this.content = IOUtil.toString(reader);
			}
		}
		@NotNull
		public final File getFile() {
			return file;
		}
		@NotNull
		@Override
		public final Bindings get() throws GameScriptException {
			try {
				Object result = engine.eval(content);
				if (!(result instanceof Bindings)) {
					throw new GameScriptException("The script in " + file.getName() + " did not return a mapping of key/value pairs");
				}
				return (Bindings) result;
			} catch (ScriptException exception) {
				throw new GameScriptException("Could not execute the script in " + file.getName(), exception);
			}
		}
	}
}
