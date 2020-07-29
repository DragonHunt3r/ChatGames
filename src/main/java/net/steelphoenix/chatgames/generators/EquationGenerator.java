package net.steelphoenix.chatgames.generators;

import javax.script.ScriptException;

import net.steelphoenix.annotations.NotNull;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.game.Question;
import net.steelphoenix.core.util.RandomUtil;

public class EquationGenerator extends ArithmeticGenerator {
	public EquationGenerator(ICGPlugin plugin) {
		super(plugin, 10, 251);
	}
	@NotNull
	@Override
	public final String getIdentifier() {
		return "Equation (Default)";
	}
	@NotNull
	@Override
	protected final Question getQuestion() throws ScriptException {
		int n1 = RandomUtil.randomInt(min, max);
		int n2 = RandomUtil.randomInt(min, max);
		String string = n1 + (RandomUtil.randomBoolean() ? "+" : "-") + n2;
		return new ArithmeticQuestion(string);
	}
}
