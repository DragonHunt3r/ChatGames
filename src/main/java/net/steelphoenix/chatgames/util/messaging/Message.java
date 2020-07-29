/*******************************************************************************
 * Copyright (C) 2020 Gideon Jasper Timo Bot ("Dr4g0nHunt3r")
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.steelphoenix.chatgames.util.messaging;

import net.steelphoenix.chatgames.util.messaging.annotations.ConfigBase;
import net.steelphoenix.chatgames.util.messaging.annotations.ConfigValue;

@ConfigBase(file = "messages.yml", path = "message")
public class Message {
	private Message() {
		// Nothing
	}
	@ConfigValue(path = "announcement.answer", def = "&a&lGames &8&l>> &7No one won the game! The answer was: &e%answer%")
	public static String ANNOUNCEMENT_ANSWER;
	@ConfigValue(path = "announcement.game-over", def = "&a&lGames &8&l>> &7The Chat Games have ended.")
	public static String ANNOUNCEMENT_GAMEOVER;
	@ConfigValue(path = "announcement.no-players", def = "&a&lGames &8&l>> &7Not enough players are currently online &e(%online%/%required%)&7.")
	public static String ANNOUNCEMENT_NO_PLAYERS;
	@ConfigValue(path = "announcement.win-time", def = "&a&lGames &8&l>> &7Time: &e%time% seconds")
	public static String ANNOUNCEMENT_TIME;
	@ConfigValue(path = "announcement.win", def = "&a&lGames &8&l>> &e%player% &7has won the game!")
	public static String ANNOUNCEMENT_WIN;
	@ConfigValue(path = "command.current.answer", def = "&fAnswer: &7&o'%answer%&7&o'")
	public static String COMMAND_CURRENT_ANSWER;
	@ConfigValue(path = "command.current.header", def = "&eCurrent question:")
	public static String COMMAND_CURRENT_HEADER;
	@ConfigValue(path = "command.current.no-game", def = "&6No game is currently active.")
	public static String COMMAND_CURRENT_NO_GAME;
	@ConfigValue(path = "command.current.question", def = "&fQuestion: &7&o'%question%&7&o'")
	public static String COMMAND_CURRENT_QUESTION;
	@ConfigValue(path = "command.current.time", def = "&fStarted: &7&o%time% seconds ago")
	public static String COMMAND_CURRENT_TIME;
	@ConfigValue(path = "command.generic.disabled", def = "disabled")
	public static String COMMAND_DISABLED;
	@ConfigValue(path = "command.generic.enabled", def = "enabled")
	public static String COMMAND_ENABLED;
	@ConfigValue(path = "command.leaderboard.empty", def = "&cNo scores yet.")
	public static String COMMAND_LEADERBOARD_EMPTY;
	@ConfigValue(path = "command.leaderboard.entry", def = "&3%place%&8>> &b%name% - %score%")
	public static String COMMAND_LEADERBOARD_ENTRY;
	@ConfigValue(path = "command.leaderboard.loading", def = "&eLoading leaderboard...")
	public static String COMMAND_LEADERBOARD_LOADING;
	@ConfigValue(path = "command.list.entry", def = "&f - &7&o%entry%")
	public static String COMMAND_LIST_ENTRY;
	@ConfigValue(path = "command.list.header", def = "&eLoaded question generators:")
	public static String COMMAND_LIST_HEADER;
	@ConfigValue(path = "command.generic.no-permission", def = "&cNo permission.")
	public static String COMMAND_NO_PERMISSION;
	@ConfigValue(path = "command.generic.not-a-player", def = "&cYou must be a player to do this.")
	public static String COMMAND_NOT_A_PLAYER;
	@ConfigValue(path = "command.reload.fail", def = "&cAn error occurred while reloading. Check console.")
	public static String COMMAND_RELOAD_FAIL;
	@ConfigValue(path = "command.reload.success", def = "&7Reloaded successfully.")
	public static String COMMAND_RELOAD_SUCCESS;
	@ConfigValue(path = "command.reset.all", def = "&7Resetting leaderboard...")
	public static String COMMAND_RESET_ALL;
	@ConfigValue(path = "command.reset.player", def = "&7Resetting leaderboard score for &e%player%&7...")
	public static String COMMAND_RESET_PLAYER;
	@ConfigValue(path = "command.skip", def = "&7Skipped to the next question.")
	public static String COMMAND_SKIP;
	@ConfigValue(path = "command.toggle", def = "&7Chat games &e%state%&7.")
	public static String COMMAND_TOGGLE;
	@ConfigValue(path = "command.generic.unknown-player", def = "&7%player% &cis not a known player.")
	public static String COMMAND_NO_PLAYER;
	@ConfigValue(path = "command.version", def = "&a&lGames &8&l>> &7ChatGames version &e%version% &7by &e%author%&7.")
	public static String COMMAND_VERSION;
	@ConfigValue(path = "game.alphabet", def = "&a&lGames &8&l>> &7Give the &e%question% &7letter in the alphabet to get rewards!")
	public static String GENERATOR_ALPHABET;
	@ConfigValue(path = "game.arithmetic", def = "&a&lGames &8&l>> &7Solve &e%question% &7to get rewards!")
	public static String GENERATOR_ARITHMETIC;
	@ConfigValue(path = "game.copy", def = "&a&lGames &8&l>> &7Copy &e%question% &7to get rewards!")
	public static String GENERATOR_COPY;
	@ConfigValue(path = "game.decode", def = "&a&lGames &8&l>> &7Decode &e%question% &7to get rewards!")
	public static String GENERATOR_DECODE;
	@ConfigValue(path = "game.trivia", def = "&a&lGames &8&l>> &7%question%")
	public static String GENERATOR_TRIVIA;
	@ConfigValue(path = "win", def = "&a&lGames &8&l>> &7You won!")
	public static String WIN_MESSAGE;
}
