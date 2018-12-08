package com.bayviewglen.zork;

/**
 * Class Command - Part of the "Zork" game.
 * 
 * author: Michael Kolling version: 1.0 date: July 1999
 *
 * This class holds information about a command that was issued by the user. A
 * command currently consists of two strings: a command word and a second word
 * (for example, if the command was "take map", then the two strings obviously
 * are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid command
 * words. If the user entered an invalid command (a word that is not known) then
 * the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 *
 * The second word is not checked at the moment. It can be anything. If this
 * game is extended to deal with items, then the second part of the command
 * should probably be changed to be an item rather than a String.
 */

public class Command {
	private String commandWord;
	private String secondWord;
	private String thirdWord;
	private String fourthWord;
	private String fifthWord;
	private String sixthWord;
	
	/**
	 * Create a command object. First and second word must be supplied, but
	 * either one (or both) can be null. The command word should be null to
	 * indicate that this was a command that is not recognized by this game.
	 */
	public Command(String firstWord, String secondWord, String thirdWord, String fourthWord, String fifthWord, String sixthWord) {
		commandWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
		this.fourthWord = fourthWord;
		this.fifthWord = fifthWord;
		this.sixthWord = sixthWord;

		// make it all lowercase and without space
		if (this.commandWord != null) {
			this.commandWord = this.commandWord.toLowerCase().trim();
		}
		if (this.secondWord != null) {
			this.secondWord = this.secondWord.toLowerCase().trim();
		}
		if (this.thirdWord != null) {
			this.thirdWord = this.thirdWord.toLowerCase().trim();
		}
		if (this.fourthWord != null) {
			this.fourthWord = this.fourthWord.toLowerCase().trim();
		}
		if (this.fifthWord != null) {
			this.fifthWord = this.fifthWord.toLowerCase().trim();
		}
		
		// account for double words, combine them
		fixDoubleWords("pick", "up");
		fixDoubleWords("helicopter", "key");
		fixDoubleWords("hydrochloric", "acid");
		fixDoubleWords("horse", "racing");
		fixDoubleWords("dr.", "rick");
		fixDoubleWords("doctor", "rick");
		fixDoubleWords("dr", "rick");
		fixDoubleWords("let", "go");
		fixDoubleWords("metal", "detector");
		fixDoubleWords("dig", "up");
	}


	/**
	 *combines two consecutive words in a command if they are in the command and removes punctuation
	 * 
	 * @param String a  the first word to watch out for and combine
	 * @param String b  the second word to watch out for and combine
	 * @return nothing
	 */
	private void fixDoubleWords(String a, String b) {
		for (int i = 1; i < 6; i++) {

			if (getWord(i) != null && getWord(i+1) != null && getWord(i).equals(a) && getWord(i + 1).equals(b)) {
				setWord(i, getWord(i) + getWord(i + 1));
				for (int j = i + 1; j < 6; j++) {
					setWord(j, getWord(j + 1));
				}
				setWord(6, null);
				for (int m = 0; m < getWord(i).length(); m++) {
					if (getWord(i).substring(m, m + 1).equals(".") || getWord(i).substring(m, m + 1).equals(",")
							|| getWord(i).substring(m, m + 1).equals(":")
							|| getWord(i).substring(m, m + 1).equals(";")) {
						setWord(i, getWord(i).substring(0, m) + getWord(i).substring(m + 1));
					}
				}
				return;
			}

		}
	}

	/**
	 * Return the command word (the first word) of this command. If the command
	 * was not understood, the result is null.
	 */
	public String getCommandWord() {
		return commandWord;
	}

	/**
	 * Return the second word of this command. Returns null if there was no
	 * second word.
	 */
	public String getSecondWord() {
		return secondWord;
	}

	/**
	 * Return the third word of this command. Returns null if there was no
	 * second word.
	 */
	public String getThirdWord() {
		return thirdWord;
	}

	/**
	 *returns word given an index of the command
	 * 
	 * @param int num  the index of the command
	 * @return String  the word at that index of the command, returns null if no word there
	 */
	public String getWord(int num) {
		if (num == 1) {
			return commandWord;
		} else if (num == 2) {
			return secondWord;
		} else if (num == 3) {
			return thirdWord;
		} else if (num == 4) {
			return fourthWord;
		} else if (num == 5) {
			return fifthWord;
		} else if (num == 6) {
			return sixthWord;
		} else {
			return null;
		}
	}

	/**
	 * Return true if this command was not understood.
	 */
	public boolean isUnknown() {
		return (commandWord == null);
	}

	/**
	 *returns whether there is a word in a given index of the command
	 * 
	 * @param int num  the index of the command
	 * @return boolean true if there is a word at that index, false otherwise
	 */
	public boolean hasWord(int i) {
		if (i == 2) {
			return (secondWord != null);
		} else if (i == 3) {
			return (thirdWord != null);
		} else if (i == 4) {
			return (fourthWord != null);
		}else if (i == 5) {
			return (fifthWord != null);
		}else if (i == 6) {
			return (sixthWord != null);
		}
		return false;

	}

	/**
	 *changes the word at a given index of the command
	 * 
	 * @param int num  the index of the command
	 * @param String word  the word to change the current word with
	 * @return nothing
	 */
	private void setWord(int i, String word) {
		if (i == 1) {
			commandWord = word;
		} else if (i == 2) {
			secondWord = word;
		} else if (i == 3) {
			thirdWord = word;
		} else if (i == 4) {
			fourthWord = word;
		}else if (i == 5) {
			fifthWord = word;
		}else if (i == 6) {
			sixthWord = word;
		}
	}

}
