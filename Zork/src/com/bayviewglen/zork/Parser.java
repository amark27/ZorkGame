package com.bayviewglen.zork;

/*
 * Author:  Michael Kolling
 * Version: 1.0
 * Date:    July 1999
 * 
 * This class is part of Zork. Zork is a simple, text based adventure game.
 *
 * This parser reads user input and tries to interpret it as a "Zork"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a three word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class Parser {
	private CommandWords commands; // holds all valid command words

	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Parser() {
		commands = new CommandWords();
	}

	/**
	 *reads the user's input and creates a command
	 * 
	 * @param 
	 * @return Command   the command that the user inputs 
	 */
	public Command getCommand() {
		String inputLine = ""; // will hold the full input line
		String[] words = new String[6];

		System.out.print("> "); // print prompt

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			inputLine = reader.readLine();
		} catch (java.io.IOException exc) {
			System.out.println("There was an error during reading: " + exc.getMessage());
		}

		StringTokenizer tokenizer = new StringTokenizer(inputLine);

		int count = 0;
		while (tokenizer.hasMoreTokens() && count< words.length){
			while (true){
				if (tokenizer.hasMoreTokens()){
					String word = tokenizer.nextToken();
					if (!notABadWord(word).equalsIgnoreCase("badWord")){
						words[count] = word;
						break;
					}
				} else {
					break;
				}
			}
			count++;
		}
		

		// note: we just ignore the rest of the input line.

		// Now check whether this word is known. If so, create a command
		// with it. If not, create a "nil" command (for unknown command).

		
		if (words[0] == null){
			return new Command("", words[1], words[2], words[3], words[4], words[5]);
		} else {
			return new Command(words[0], words[1], words[2], words[3], words[4], words[5]);
		}

	}

	/**
	 * Print out a list of valid command words.
	 */
	public void showCommands() {
		commands.showAll();
	}
	
	/**
	 *checks whether there are filler words
	 * 
	 * @param String word  the word to be checked
	 * @return String   "badWord"  if the word was a filler word, word otherwise
	 */
	private String notABadWord(String word){
		if (word.equalsIgnoreCase("with")){
		return "badWord";
		} else if (word.equalsIgnoreCase("the")){
		return "badWord";
		} else if (word.equalsIgnoreCase("to")){
		return "badWord";
		} else if (word.equalsIgnoreCase("and")){
		return "badWord";
		} else if (word.equalsIgnoreCase("an")){
		return "badWord";
		} else if (word.equalsIgnoreCase("in")){
		return "badWord";
		} else if (word.equalsIgnoreCase("a")){
		return "badWord";
		} else if (word.equalsIgnoreCase("but")){
		return "badWord";
		} else if (word.equalsIgnoreCase("at")){
			return "badWord";
		} else if (word.equalsIgnoreCase("my")){
			return "badWord";
		} else if (word.equalsIgnoreCase("of")){
			return "badWord";
		} else if (word.equalsIgnoreCase("towards")){
			return "badWord";
		} else if (word.equalsIgnoreCase("toward")){
			return "badWord";
		} else {
		return word;
		}
	}
}
