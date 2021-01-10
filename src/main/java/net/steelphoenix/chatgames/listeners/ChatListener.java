package net.steelphoenix.chatgames.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.steelphoenix.chatgames.ChatGames;
import net.steelphoenix.chatgames.api.ICGPlugin;
import net.steelphoenix.chatgames.api.event.AsyncChatGameWinEvent;
import net.steelphoenix.chatgames.api.game.IGame;
import net.steelphoenix.chatgames.util.Util;
import net.steelphoenix.chatgames.util.messaging.Message;
import net.steelphoenix.core.util.Validate;

public class ChatListener implements Listener {
	private final ICGPlugin plugin;
	public ChatListener(ICGPlugin plugin) {
		this.plugin = Validate.notNull(plugin, "Plugin cannot be null");
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public final void onChat(AsyncPlayerChatEvent event) {
		IGame game = plugin.getTask().getCurrentGame();

		// If there is no game currently
		if (game == null || !game.isActive()) {
			return;
		}

		Player player = event.getPlayer();
		String message = event.getMessage();
		long answertime = System.currentTimeMillis() - game.getStartTime();
		String time = String.valueOf((answertime / 10L) / 100D);

		// If the answer is incorrect
		if (!game.getQuestion().isCorrect(message)) {
			return;
		}

		game.setInactive();

		// Increment the player's score
		plugin.getLeaderboard().increment(player.getUniqueId());

		// Schedule the messages for the next tick, so the player's chat message comes before the win broadcast
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {

		    // Broadcast the win
		    ((ChatGames) plugin).broadcast(Message.ANNOUNCEMENT_WIN.replace("%player%", player.getName()));
		    ((ChatGames) plugin).broadcast(Message.ANNOUNCEMENT_TIME.replace("%time%", time));

		    // Event calling
		    AsyncChatGameWinEvent gameEvent = new AsyncChatGameWinEvent(game, player, message, answertime);
		    plugin.getServer().getPluginManager().callEvent(gameEvent);

		    // Send the winning player a message if defined
		    if (gameEvent.getWinMessage() != null) {
		    	player.sendMessage(Util.color(gameEvent.getWinMessage()));
		    }
	    }, 1L);
	}
}
