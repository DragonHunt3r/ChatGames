package net.steelphoenix.chatgames.generators;

import javax.script.ScriptException;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.Validate;

public abstract class ArithmeticGenerator implements Generator {
	protected final ICGPlugin plugin;
	protected final int min;
	protected final int max;
	protected ArithmeticGenerator(ICGPlugin plugin, int min, int max) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
		this.min = min;
		this.max = max;

		Validate.isTrue(max >= min, "Minimum cannot exceed maximum");
	}
	@NotNull
	@Override
	public final Question getNewQuestion() {
		// No script engine present
		if (plugin.getScriptEngine() == null) {
			return new ErrorQuestion("No JavaScript engine found");
		}

		try {
			return getQuestion();
		} catch (ScriptException exception) {
			return new ErrorQuestion(exception.getMessage());
		}
	}
	@NotNull
	protected abstract Question getQuestion() throws ScriptException;
	protected class ArithmeticQuestion implements Question {
		private final String answer;
		private final String question;
		protected ArithmeticQuestion(String question) throws ScriptException {
			this.question = Validate.notNull(question, "Question cannot be null");

			// Get the result
			Object res = plugin.getScriptEngine().eval(question);
			Validate.isTrue(res instanceof Number, () -> new IllegalStateException("Result is NaN"));

			// Get the answer rounding floating point answers up to two decimals
			this.answer = String.valueOf(res instanceof Double ? (Math.round((double) res * 100D) / 100D) : res);
		}
		@NotNull
		@Override
		public final String getAnswer() {
			return answer;
		}
		@NotNull
		@Override
		public final String getQuestion() {
			return question;
		}
		@NotNull
		@Override
		public final String getMessage() {
			return Message.GENERATOR_ARITHMETIC;
		}
		@Override
		public final boolean isCorrect(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			return getAnswer().equalsIgnoreCase(input);
		}
	}
}
