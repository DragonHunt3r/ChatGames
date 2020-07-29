package net.steelphoenix.chatgames.generators;

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptException;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.core.util.RandomUtil;

public class DifficultEquationGenerator extends ArithmeticGenerator {
	private static final List<Character> OPERATORS = Arrays.asList('+', '-', '*', '/');
	public DifficultEquationGenerator(ICGPlugin plugin) {
		super(plugin, 2, 11);
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Equation (Hard) (Default)";
	}
	@NotNull
	@Override
	protected final Question getQuestion() throws ScriptException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			builder.append(RandomUtil.randomInt(min, max));

			// Append an operator if it is not the last number
			if (i != 4) {
				builder.append(RandomUtil.pickRandom(OPERATORS));
			}
		}
		return new ArithmeticQuestion(builder.toString());
	}
}