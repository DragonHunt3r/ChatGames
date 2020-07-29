package net.steelphoenix.chatgames.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Generator;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.chatgames.config.IConfig;
import net.steelphoenix.chatgames.config.YAMLConfig;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.RandomUtil;
import net.steelphoenix.core.util.Validate;

public class TriviaGenerator implements Generator {
	private final IConfig config;
	private final boolean success;
	private final List<TriviaEntry> list = new ArrayList<>();
	public TriviaGenerator(ICGPlugin plugin) {
		Validate.notNull(plugin, "Plugin cannot be null");

		// Config
		File file = new File(plugin.getDataFolder(), "trivia.yml");
		if (!file.exists()) {
			plugin.saveResource("trivia.yml", false);
		}
		this.config = new YAMLConfig(plugin.getLogger(), file);

		// Config could not load properly
		if (!config.load()) {
			success = false;
			return;
		}

		ConfigurationSection section = config.getConfigurationSection("questions");

		// No section
		if (section == null) {
			success = true;
			return;
		}

		boolean success = true;
		for (String key : section.getKeys(false)) {
			ConfigurationSection sub = section.getConfigurationSection(key);

			// Not a section
			if (sub == null) {
				plugin.getLogger().log(Level.WARNING, "questions." + key + " is not a section (trivia.yml)");
				success &= false;
				continue;
			}

			// Get the question
			String question = sub.getString("question");

			// No question
			if (question == null) {
				plugin.getLogger().log(Level.WARNING, "questions." + key + " does not have a question (trivia.yml)");
				success &= false;
				continue;
			}

			// Get the answers
			List<String> answers = sub.getStringList("answer");

			// No answers
			if (answers == null || answers.isEmpty()) {
				plugin.getLogger().log(Level.WARNING, "questions." + key + " does not have answers (trivia.yml)");
				success &= false;
				continue;
			}

			// Add the entry
			list.add(new TriviaEntry(question, answers));
		}
		this.success = success;
	}
	@NotNull
	@Override
	public final Question getNewQuestion() {
		// No questions defined
		if (list.isEmpty()) {
			return new ErrorQuestion("No trivia questions defined");
		}

		return new TriviaQuestion(RandomUtil.pickRandom(list));
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Trivia (Default)";
	}
	public final boolean hasLoadedSuccessful() {
		return success;
	}
	private class TriviaQuestion implements Question {
		private final TriviaEntry entry;
		private TriviaQuestion(TriviaEntry entry) {
			this.entry = Validate.notNull(entry, "Entry cannot be null");
		}
		@NotNull
		@Override
		public final String getAnswer() {
			return String.join(", ", entry.getAnswers());
		}
		@NotNull
		@Override
		public final String getQuestion() {
			return entry.getQuestion();
		}
		@NotNull
		@Override
		public final String getMessage() {
			return Message.GENERATOR_TRIVIA;
		}
		@NotNull
		@Override
		public final boolean isCorrect(@NotNull String input) {
			// Preconditions
			Validate.notNull(input, "Input cannot be null");

			return entry.getAnswers().stream().anyMatch(string -> string.equalsIgnoreCase(input));
		}
	}
	private class TriviaEntry {
		private final String question;
		private final List<String> answers;
		private TriviaEntry(String question, List<String> answers) {
			this.question = Validate.notNull(question, "Question cannot be null");
			this.answers = Validate.notNull(answers, "Answers cannot be null");
		}
		@NotNull
		private final String getQuestion() {
			return question;
		}
		@NotNull
		private final List<String> getAnswers() {
			return answers;
		}
	}
}
