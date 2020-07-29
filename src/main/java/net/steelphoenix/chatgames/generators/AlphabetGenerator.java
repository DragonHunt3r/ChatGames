package net.steelphoenix.chatgames.generators;

import org.apache.commons.lang.Validate;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.RandomUtil;

public class AlphabetGenerator implements Generator {
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	private static final String[] SUFFIXES = new String[] {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
	@NotNull
	@Override
	public final Question getNewQuestion() {
		return new AlphabetQuestion();
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Alphabet (Default)";
	}
	private class AlphabetQuestion implements Question {
		private final int index = RandomUtil.randomInt(0, ALPHABET.length());
		@NotNull
		@Override
		public final String getAnswer() {
			return Character.toString(ALPHABET.charAt(index));
		}
		@NotNull
		@Override
		public final String getQuestion() {
			return getOrdinal(index + 1);
		}
		@NotNull
		@Override
		public final String getMessage() {
			return Message.GENERATOR_ALPHABET;
		}
		@Override
		public final boolean isCorrect(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			return getAnswer().equalsIgnoreCase(input);
		}
		// TODO: Translation config
		@NotNull
		private final String getOrdinal(int index) {
			switch (index % 100) {
			case 11:
			case 12:
			case 13:
				return index + "th";
			default:
				return index + SUFFIXES[index % 10];
			}
		}
	}
}
