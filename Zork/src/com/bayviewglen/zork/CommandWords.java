package com.bayviewglen.zork;
/*
 * Author:  Michael Kolling.
 * Version: 1.0
 * Date:    July 1999
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * This class is part of the "Zork" game.
 */

public class CommandWords {
	// a constant array that holds all valid command words
	private static final String validCommands[] = { "go", "up", "down", "walk", "move", "display", "digup", "dig", "north", "south", "east", "west", "throw", "quit", "help", "eat", "swim", "collect" , "play", "drop", "release", "letgo", "grab", "lift", "take", "show", "view", "look", "fly", "pick", "save", "use"};
	/**
	 * Constructor - initialize the command words.
	 */
	public CommandWords() {
		// nothing to do at the moment...
	}

	/**
	 * Check whether a given String is a valid command word. Return true if it
	 * is, false if it isn't.
	 **/
	public boolean isCommand(String aString) {
		for (int i = 0; i < validCommands.length; i++) {
			if (aString.trim() != null && validCommands[i].equalsIgnoreCase(aString.trim()))
				return true;
		}
		// if we get here, the string was not found in the commands
		return false;
	}

	/*
	 * Print all valid commands to System.out.
	 */
	public static void showAll() {
		for (int i = 0; i < validCommands.length; i++) {
			System.out.print(validCommands[i] + "  ");
		}
		System.out.println();
	}

}
