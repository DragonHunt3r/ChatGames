package net.steelphoenix.chatgames.config;

import org.bukkit.configuration.Configuration;

import net.steelphoenix.chatgames.api.Reloadable;

public interface IConfig extends Configuration, Reloadable {
	public boolean load();
	public boolean save();
}
