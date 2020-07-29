package net.steelphoenix.chatgames.generators;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.RandomUtil;
import net.steelphoenix.core.util.Validate;

public class RandomSequenceGenerator implements Generator {
	private static final String UPPER =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = UPPER.toLowerCase();
	private static final String DIGITS = "0123456789";
	private static final String ALPHANUM = UPPER + LOWER + DIGITS;
	private final ICGPlugin plugin;
	public RandomSequenceGenerator(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
	}
	@NotNull
	@Override
	public final Question getNewQuestion() {
		int len = plugin.getConfig().getInt("charsequence-length");

		// Invalid length
		if (len <= 0) {
			return new ErrorQuestion("Sequence length for random sequence question is not positive (" + len + ")");
		}

		return new RandomSequenceQuestion(len);
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Random sequence (Default)";
	}
	private class RandomSequenceQuestion implements Question {
		private final String question;
		private RandomSequenceQuestion(int length) {
			StringBuilder builder = new StringBuilder();
			while (builder.length() < length) {
				builder.append(ALPHANUM.charAt(RandomUtil.randomInt(0, ALPHANUM.length())));
			}
			this.question = builder.toString();
		}
		@NotNull
		@Override
		public final String getAnswer() {
			return question;
		}
		@NotNull
		@Override
		public final String getQuestion() {
			return question;
		}
		@NotNull
		@Override
		public final String getMessage() {
			return Message.GENERATOR_COPY;
		}
		@Override
		public final boolean isCorrect(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			return getAnswer().equals(input);
		}
	}
}
