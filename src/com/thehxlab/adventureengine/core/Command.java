package com.thehxlab.adventureengine.core;


//For laters Define a Command interface that all commands (like "take," "move," "examine") implement.
//This helps decouple command parsing from game logic.

public interface Command {
	void execute(String[] args);
}
