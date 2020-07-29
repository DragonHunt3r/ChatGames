# ChatGames
Spigot ChatGames plugin: https://www.spigotmc.org/resources/64888/

## API

### Events
* AsyncChatGameWinEvent - When a chat game is won
* ChatGameExpireEvent - When the answer time runs out
* ChatGameStartEvent - When a new chat game is started

### Custom games
Create a class extending ChatGameGenerator, and instantiate it; it will be automatically registered. See the documentation for Question.

## JavaScript questions
A script must return a JSON body containing
* answer (String) (See Question#getAnswer())
* question (String) (See Question#getQuestion())
* message (String) (See Question#getMessage())
* caseSentitive (boolean) (If a player's answer must also match cases)