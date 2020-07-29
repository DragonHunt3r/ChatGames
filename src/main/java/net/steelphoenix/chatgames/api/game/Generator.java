package net.steelphoenix.chatgames.api.game;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.core.api.registry.Identifiable;

public interface Generator extends Identifiable<String> {
	/**
	 * Called when this generator gets to ask a question.
	 * @return new Question object
	 */
	@NotNull
	public Question getNewQuestion();
}
