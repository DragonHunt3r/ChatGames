package net.steelphoenix.chatgames.generators;

import net.steelphoenix.chatgames.api.game.Question;

public class ErrorQuestion implements Question {
	private final String message;
	public ErrorQuestion(String message) {
		this.message = message;
	}
	@Override
	public final String getAnswer() {
		return "";
	}
	@Override
	public final String getQuestion() {
		return message;
	}
	@Override
	public final String getMessage() {
		return "&4Error! &c%question%.";
	}
	@Override
	public final boolean isCorrect(String input) {
		return false;
	}
}
