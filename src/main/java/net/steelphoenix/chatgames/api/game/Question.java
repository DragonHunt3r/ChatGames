package net.steelphoenix.chatgames.api.game;

import net.steelphoenix.annotations.NotNull;

public interface Question {
	/**
	 * Get this question's answer.
	 * @return this question's answer.
	 */
	@NotNull
	public String getAnswer();
	/**
	 * Get the current question.
	 * @return the question asked in chat.
	 */
	@NotNull
	public String getQuestion();
	/**
	 * Get the message to be broadcasted holding this question.
	 * @return the message sent when the game is activated with %question% being the question from {@link #getQuestion()}
	 */
	@NotNull
	public String getMessage();
	/**
	 * Check if a given input is correct.
	 * @param input Input to compare to the answer.
	 * @return true if the input is a correct answer on this question.
	 */
	public boolean isCorrect(@NotNull String input);
}
