package net.steelphoenix.chatgames.script;

import javax.script.Bindings;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.core.util.Validate;

public class JSQuestion implements Question {
	private final Bindings bindings;
	private JSQuestion(Bindings bindings) {
		this.bindings = Validate.notNull(bindings, "JSON cannot be null");
	}
	@NotNull
	@Override
	public final String getAnswer() {
		return (String) bindings.get("answer");
	}
	@NotNull
	@Override
	public final String getQuestion() {
		return (String) bindings.get("question");
	}
	@NotNull
	@Override
	public final String getMessage() {
		return (String) bindings.get("message");
	}
	@Override
	public final boolean isCorrect(@NotNull String input) {
		// Preconditions
		Validate.notNull(input, "Input cannot be null");

		return (boolean) bindings.get("caseSensitive") ? getAnswer().equals(input) : getAnswer().equalsIgnoreCase(input);
	}
	public static final Question of(@NotNull Bindings bindings) throws GameScriptException {
		// Preconditions
		Validate.notNull(bindings, "JSON cannot be null");
		Validate.isTrue(bindings.get("answer") instanceof String, () -> new GameScriptException("Question result did not have an answer member of type String"));
		Validate.isTrue(bindings.get("question") instanceof String, () -> new GameScriptException("Question result did not have a question member of type String"));
		Validate.isTrue(bindings.get("message") instanceof String, () -> new GameScriptException("Question result did not have a message member of type String"));
		Validate.isTrue(bindings.get("caseSensitive") instanceof Boolean, () -> new GameScriptException("Question result did not have a caseSensitive member of type Boolean"));

		return new JSQuestion(bindings);
	}
}
