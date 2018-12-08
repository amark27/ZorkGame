package com.bayviewglen.zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

//this is the horseracing game made compatible with zork
public class HorseRacingZork {
	
	public static final int MAX_NAME_LENGTH = 19;
	public static final int MAX_MONEY_LENGTH = 7;
	public static final int MIN_HORSES = 5;
	public static final int MAX_HORSES = 8;
	public static final int MIN_STEPS = 1;
	public static final int MAX_STEPS = 5;
	public static final int NOTHING = -1;
	public static final int CHOICE_BET = 1;
	public static final int CHOICE_RACE = 2;
	public static final int CHOICE_QUIT = 3;
	public static final int DELAY_IN_MILLISECONDS = 150;
	public static final int LENGTH_OF_RACE = 100;
	public static final int LENGTH_OF_RACE_AND_HEADER = 120;
	public static final int CLEAR_SCREEN = 50;
	public static final int STARTING_WALLET = 1000;
	public static final String NAME_FORMAT = "%-" + MAX_NAME_LENGTH + "s"; // printf format
	public static final String MONEY_FORMAT = "%" + MAX_MONEY_LENGTH + "s"; // printf format
	public static final int NUM_PLAYERS = 1;
	
	public static Scanner keyboard = new Scanner(System.in);
	
	/**
	 *plays horseracing
	 * 
	 * @param String name the name of the player
	 * @return boolean   true if player wins
	 */
	public static boolean play(String name) {	
		System.out.println("Welcome to Horse Racing!\n\nPress enter to continue");
		keyboard.nextLine();
		
		Scanner horseData = null;
		Scanner players = null;
		try {
			horseData = new Scanner(new File("data/horses.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // try catch
		
		// lengths of arrays are assigned here to reduce file scanners and return statements because I don't like returning array pointers
		String[] horses = new String[Integer.parseInt(horseData.nextLine())]; // first line of horseData file
		String[] horsesInRace = null; // modified later
		String[] playerNames = {name};
		
		getHorses(horses, horseData); // gets all horses from file

		int[] playerHorses = new int[NUM_PLAYERS];
		
		boolean newRace = true;
		while (true) { // break statement exits the while loop in the else statement			
			if (newRace) {
				horsesInRace = new String[ThreadLocalRandom.current().nextInt(MIN_HORSES, MAX_HORSES + 1)]; // chooses between 5 to 8 for the number of horses in the race
				initializeRace(horses, horsesInRace, playerHorses);
				newRace = false;
			} // if else
						
			displayTable(playerNames, playerHorses, horsesInRace);
			
			int choice = getChoice();
			if (choice == CHOICE_BET) {
				getHorseBet(playerHorses, horsesInRace); // getBet method returns player number
			} else if (choice == CHOICE_RACE) {
				int winners[] = race(horsesInRace);
				for (int i = 0; i < playerHorses.length; i++) {
					for (int j = 1; j <= winners[0]; j++) {
						if (playerHorses[i] == winners[j]) {
							return true;
						} // if else
					} // for j
					return false;
				} // for i
				newRace = true;
			} else {
				return false;
			} // if else
		} // while
	} // main method
	
	public static void getHorses(String[] horses, Scanner file) {
		for (int i = 0; i < horses.length; i++) {
			horses[i] = file.nextLine();
			if (horses[i].length() > MAX_NAME_LENGTH) {
				horses[i] = horses[i].substring(0, MAX_NAME_LENGTH);
			} // if else
		} // for i
	} // getHorses method
	
	public static void initializeRace(String[] horses, String[] horsesInRace, int[] playerHorses) {
		for (int i = 0; i < horsesInRace.length; i++) {
			String horse;
			do { // gets a random horse and checks if it is already in the race, if it is then get another random horse
				horse = horses[ThreadLocalRandom.current().nextInt(0, horses.length)]; // selects a random horse
			} while (alreadyInRace(horse, horsesInRace, i)); // do while
			horsesInRace[i] = horse;
		} // for i
		for (int j = 0; j < playerHorses.length; j++) {
			playerHorses[j] = NOTHING; // resets playerHorses
		} // for j
	} // initializeRace method
	
	public static void displayTable(String[] playerNames, int[] playerHorses, String[] horsesInRace) {
		System.out.println("#|Player Name        |Horse                 |"
					   + "\n-|-------------------|----------------------|");
		for (int i = 0; i < playerNames.length; i++) {
			if (playerHorses[i] != NOTHING) {
				System.out.printf((i + 1) + "|" + NAME_FORMAT  
						+ "|" + (playerHorses[i] + 1) + ". " + NAME_FORMAT + "|\n", 
						playerNames[i], horsesInRace[playerHorses[i]]);
			} else {
				System.out.printf((i + 1) + "|" + NAME_FORMAT  
						+ "|                      |\n", playerNames[i]);
			} // if else
			System.out.println("-|-------------------|----------------------|");
		} // for i
	} // displayTable method
	
	public static boolean alreadyInRace(String horse, String[] horsesInRace, int horsesSoFar) { // checks if horse is still in a race
		for (int i = 0; i < horsesSoFar; i++){
			if (horsesInRace[i].equalsIgnoreCase(horse)){
				return true;
			} // if else
		} // for i
		return false;
	} // alreadyInRace method
	
	public static int getChoice() {
		while (true) {
			System.out.print("Bet (1), Start Race (2), Quit (3): ");
			String choice = keyboard.nextLine();
			if (choice.equals("" + CHOICE_BET)) {
				return CHOICE_BET;
			} else if (choice.equals("" + CHOICE_RACE)) {
				return CHOICE_RACE;
			} else if (choice.equals("" + CHOICE_QUIT)) {
				return CHOICE_QUIT;
			} else {
				System.out.println("Invalid Choice!");
			} // if else
		} // while
	} // getChoice method
	
	
	public static void getHorseBet(int[] playerHorses, String[] horsesInRace) {
		System.out.print("Please bet for a horse (enter 0 for a list of horses): ");
		boolean valid = false; // as a horse number has not been entered, validBet is false
		while (!valid) { // while the horse number is not valid
			try { // making sure that an integer is entered
				int horseIndex = Integer.parseInt(keyboard.nextLine()) - 1;
				if (horseIndex >= 0 && horseIndex < horsesInRace.length) { // valid bet
					playerHorses[0] = horseIndex;
					valid = true;
				} else if (horseIndex == NOTHING) { // displays the horses
					System.out.printf("\n#|Horse Name         |\n-|-------------------|\n");
					for (int i = 0; i < horsesInRace.length; i++) {
						System.out.printf(i + 1 + "|" + NAME_FORMAT + "|", horsesInRace[i]);
						System.out.println("\n-|-------------------|");
					} // for i
				} else { // over the max horse number
					System.out.print("Please enter a valid horse number: ");
				} // if else
			} catch (Exception ex) {
				System.out.print("Please enter a valid horse number: ");
			} // catch
		} // while
		System.out.println();
	} // getHorseBet method
	
	public static int[] race(String[] horsesInRace) {
		boolean isWinner = false;
		int[] winners = new int[horsesInRace.length + 1]; // first integer stores the length of the used array
		String[] distance = new String[horsesInRace.length];
		for (int h = 0; h < horsesInRace.length; h++) {
			distance[h] = "" + (h + 1); // horse number
		} // for h
		while (!isWinner) {
			for (int i = 1; i <= CLEAR_SCREEN; i++) { // clear screen
				System.out.println();
			} // for i
			for (int j = 1; j <= LENGTH_OF_RACE_AND_HEADER; j++) { // top bar
				System.out.print("-");
			} // for j
			for (int k = 0; k < horsesInRace.length; k++) { // iterates through each horse
				System.out.printf("\n" + NAME_FORMAT, horsesInRace[k]);
				if (distance[k].length() >= LENGTH_OF_RACE) {
					distance[k] = distance[k].substring(distance[k].length() - LENGTH_OF_RACE, distance[k].length());
					winners[winners[0] + 1] = k; // adds horse to winners
					winners[0]++; // number of winners
					isWinner = true;
					System.out.println("|" + distance[k]); // in case distance is over 100
				} else {
					System.out.println("|" + distance[k]); // print before increasing distance
					int movement = ThreadLocalRandom.current().nextInt(MIN_STEPS, MAX_STEPS + 1);
					for (int n = 1; n <= movement; n++) { // adding spaces (moving the horses)
						distance[k] = " " + distance[k];
					} // for n
				} // if else
				for (int m = 1; m <= LENGTH_OF_RACE_AND_HEADER; m++) { // bar under each horse
					System.out.print("-");
				} // for m
			} // for k
			try {
				Thread.sleep(DELAY_IN_MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // try catch
		} // while
		System.out.println("\n\nPress enter to continue");
		keyboard.nextLine();
		return winners;
	} // race method
	
} // HorseRacingZork