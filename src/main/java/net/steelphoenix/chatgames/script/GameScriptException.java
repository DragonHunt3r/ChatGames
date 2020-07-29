package net.steelphoenix.chatgames.script;

public class GameScriptException extends Exception {
	private static final long serialVersionUID = 2179602088726424330L;
	public GameScriptException() {
		super();
	}
	public GameScriptException(String message) {
		super(message);
	}
	public GameScriptException(Throwable cause) {
		super(cause);
	}
	public GameScriptException(String message, Throwable cause) {
		super(message, cause);
	}
}
