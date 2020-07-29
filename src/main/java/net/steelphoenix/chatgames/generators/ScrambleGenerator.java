package net.steelphoenix.chatgames.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.config.IConfig;
import net.steelphoenix.chatgames.config.YAMLConfig;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.RandomUtil;
import net.steelphoenix.core.util.Validate;

public class ScrambleGenerator implements Generator {
	private final IConfig config;
	private final boolean success;
	public ScrambleGenerator(ICGPlugin plugin) {
		Validate.notNull(plugin, "Plugin cannot be null");

		// Config
		File file = new File(plugin.getDataFolder(), "scramble.yml");
		if (!file.exists()) {
			plugin.saveResource("scramble.yml", false);
		}
		this.config = new YAMLConfig(plugin.getLogger(), file);
		this.success = config.load();
	}
	@NotNull
	@Override
	public final Question getNewQuestion() {
		List<String> list = config.getStringList("scrambles");

		// No scrambles defined
		if (list == null || list.isEmpty()) {
			return new ErrorQuestion("No scrambles defined (scramble.yml)");
		}

		return new ScrambleQuestion(RandomUtil.pickRandom(list));
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Scramble (Default)";
	}
	public final boolean hasLoadedSuccessful() {
		return success;
	}
	private class ScrambleQuestion implements Question {
		private final String answer;
		private final String question;
		private ScrambleQuestion(String answer) {
			this.answer = Validate.notNull(answer, "Answer cannot be null");
			this.question = scramble(answer);
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
			return Message.GENERATOR_DECODE;
		}
		@Override
		public final boolean isCorrect(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			return getAnswer().equalsIgnoreCase(input);
		}
		@NotNull
		private final String scramble(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			// Multiple words
			if (input.contains(" ")) {
				String output = "";
				for (String string : input.split(" +")) {
					output = output + " " + shuffle(string);
				}
				return output.trim().replaceAll(" +", " ");
			}

			// One word
			return shuffle(input);
		}
		@NotNull
		private final String shuffle(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			// List of characters
			List<Character> characters = new ArrayList<>();
			for (char c : input.toCharArray()) {
				characters.add(c);
			}

			StringBuilder output = new StringBuilder(input.length());
			while (!characters.isEmpty()) {
				output.append(characters.remove(RandomUtil.randomInt(0, characters.size())));
			}
			return output.toString();
		}
	}
}
