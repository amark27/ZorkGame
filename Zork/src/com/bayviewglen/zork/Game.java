package com.bayviewglen.zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initializes all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */

public class Game implements java.io.Serializable {
	private Parser parser;
	private Room currentRoom;
	private Room lastRoom = new Room(" ");
	private Room tempLastRoom = new Room(" ");
	private Drrick drrick = new Drrick();
	private final int TARGET_HELICOPTER_KEYS = 6;
	// This is a MASTER object that contains all of the rooms and is easily
	// accessible.
	// The key will be the name of the room -> no spaces (Use all caps and
	// underscore -> Great Room would have a key of GREAT_ROOM
	// In a hashmap keys are case sensitive.
	// masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the
	// Great Room (assuming you have one).
	private HashMap<String, Room> masterRoomMap;
	private Scanner keyboard = new Scanner(System.in);
	private Player player = new Player();

	/**
	 *Occupies the masterRoomMap with data from the room data file
	 *
	 * @param String fileName  name of the data file from which to get information       
	 * @return nothing
	 * @throws FileNotFoundException

 */
	private void initRooms(String fileName) throws Exception {
		masterRoomMap = new HashMap<String, Room>();
		Scanner roomScanner;
		try {
			HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();
			roomScanner = new Scanner(new File(fileName));
			while (roomScanner.hasNext()) {
				Room room = new Room();
				// Read the Name
				String roomName = roomScanner.nextLine();
				room.setRoomName(roomName.split(":")[1].trim());
				// Read the Description
				String roomDescription = roomScanner.nextLine();
				room.setDescription(roomDescription.split(":")[1].replaceAll("<br>", "\n").trim());

				// Read the Exits
				String roomExits = roomScanner.nextLine();
				// An array of strings in the format E-RoomName

				String[] rooms = new String[0];
				try { // allows for rooms to have no exits
					String[] tempRooms = roomExits.split(":")[1].split(",");
					rooms = tempRooms;
				} catch (IndexOutOfBoundsException e) {
				}

				// read the room's inventory of objects from the data file
				String inventoryObjects = roomScanner.nextLine();

				String[] inventoryObjectsArray = new String[0];
				try { // allows for rooms to have no objects
					inventoryObjectsArray = inventoryObjects.split(":")[1].split(",");
				} catch (IndexOutOfBoundsException e) {
				}
				// add the inventory objects to the room

				for (int i = 0; i < inventoryObjectsArray.length; i++) {
					if (inventoryObjectsArray[i].trim().length() != 0) {
						room.addObjectToRoomInventory(inventoryObjectsArray[i].trim());
					}
				}

				// read the room's inventory of characters from the data file
				String inventoryCharacters = roomScanner.nextLine();

				String[] inventoryCharactersArray = new String[0];
				try { // allows for rooms to have no exits
					inventoryCharactersArray = inventoryCharacters.split(":")[1].split(",");
				} catch (IndexOutOfBoundsException e) {
				}
				// add the inventory characters to the room

				for (int i = 0; i < inventoryCharactersArray.length; i++) {
					if (!(inventoryCharactersArray[i].trim().length() == 0)) {
						room.addCharacterToRoomInventory(inventoryCharactersArray[i].trim());
					}
				}

				HashMap<String, String> temp = new HashMap<String, String>();

				for (int i = 0; i < rooms.length; i++) {// go through the exits
														// of the room
					String s = rooms[i];
					if (!(s.trim().length() == 0)) {
						temp.put(s.split("-")[0].trim(), s.split("-")[1]);
					}

				}

				// for (String s : rooms){//go through the exits of the room
				// temp.put(s.split("-")[0].trim(), s.split("-")[1]);
				// }

				exits.put(roomName.substring(10).trim().toUpperCase().replaceAll(" ", "_"), temp);

				// This puts the room we created (Without the exits in the
				// masterMap)
				masterRoomMap.put(roomName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), room);

				// Now we better set the exits.
			}

			for (String key : masterRoomMap.keySet()) {
				Room roomTemp = masterRoomMap.get(key);
				HashMap<String, String> tempExits = exits.get(key);
				for (String s : tempExits.keySet()) {
					// s = direction
					// value is the room.
					String roomName2 = tempExits.get(s.trim());
					Room exitRoom = masterRoomMap.get(roomName2.toUpperCase().replaceAll(" ", "_"));
					roomTemp.setExit(s.trim().charAt(0), exitRoom);
				}

			}

			roomScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the game and initialize its internal map.
	 */
	public Game() {
		try {
			initRooms("data/Rooms.dat");
			currentRoom = masterRoomMap.get("ENTRANCE_HALLWAY");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parser = new Parser();
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		while (Player.getEndBehaviour().equals("alive")) {
			Command command = parser.getCommand();
			processCommand(command);
		}
		if (Player.getEndBehaviour().equals("dead")) {
			System.out.println("\nYou Died! What a pity.\nGAME OVER!");
		} else if (Player.getEndBehaviour().equals("win")) {
			System.out.println("\nCongratulations! You have escaped from the hands of Dr. Rick!");
		}
		System.out.println("Thank you for playing! Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to ESCAPING ROOM 2016!");
		System.out.println("ESCAPING ROOM 2016 is a new & incredible adventure game.");
		boolean newGame = true;
		File save = new File("data/Save.dat");
		if (save.length() > 0) {
			boolean valid = false;
			while (!valid) {
				System.out.print("\nNew Game? This will overwrite your existing data. (yes/no): ");
				String input = keyboard.nextLine();
				if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
					valid = true;
				} else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
					loadGame();
					newGame = false;
					valid = true;
				} else {
					System.out.println("Invalid Option.");
				}
			}
		}
		if (newGame) {
			System.out.println("Type 'help' if you need help or 'help extended' for even more information.");
			System.out.println();
			System.out.println("What is your name?");
			String name = keyboard.nextLine();
			player.addName(name);
			System.out.println(
					"\nYou have been hunted down by a psychotic and mentally ill scientist, Dr. Rick. \nHe is holding a bottle of hydrochloric acid and is ready to soak you in it because you have punched him in the face.");
			System.out.println(
					"You did not like the guy because of his taste in pizza. You enter a house in the hopes that you would lose him but he is chasing you.  \nYou have to move quickly.");
		}
		System.out.println(currentRoom.longDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command. If this command
	 * ends the game, true is returned, otherwise false is returned.
	 */
	private void processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("I don't know what you mean...");
		}

		String commandWord = command.getWord(1);

		if (commandWord.equals("help")) {
			// type "help" to show all commands to the screen
			printHelp(command);
		} else if (commandWord.equals("go") || commandWord.equals("west") || commandWord.equals("east")
				|| commandWord.equals("north") || commandWord.equals("south") || commandWord.equals("up")
				|| commandWord.equals("down") || commandWord.equals("walk") || commandWord.equals("move")) {
			// typing "go" followed by "west", "east", "south", "north" or just
			// type the directions by itself to move around
			goRoom(command);
		} else if (commandWord.equals("quit")) {
			// type "quit" to quit the game
			if (command.hasWord(2)) {
				System.out.println("Quit what?");
			} else {
				Player.setEndBehaviour("quit");
				return; // signal that we want to quit
			}
		} else if (commandWord.equals("save")) {
			// type "save" and it will save the game
			save();
		} else if (commandWord.equals("fly")) {
			// this command can only be used on the helicopter pad, and can be
			// used by typing "fly" followed by "helicopter" or "chopper"
			fly(command);
		} else if (commandWord.equals("throw")) {
			// "throw hydrochloric acid at Dr. Rick" is the only command that
			// does something with throw and kills Dr. Rick
			throwSomething(command);
		} else if (commandWord.equals("drop") || commandWord.equals("release") || commandWord.equals("letgo")) {
			// any of the commands above followed by an object, will get rid of
			// it from the character's inventory and add it to the room's
			// inventory
			dropSomething(command);
		} else if (commandWord.equals("eat")) {
			// if the object you type after "eat" is in your inventory, you can
			// eat it
			eatSomething(command, 1);
		} else if (commandWord.equals("play")) {
			// if you're in the horseracing room or casino, you can type play
			// and then the game (blackjack or horseracing) to play the
			// specified game
			playGame(command);
		} else if (commandWord.equals("grab") || commandWord.equals("lift")
				|| commandWord.equals("pickup") || commandWord.equals("take")) {
			// any of these command words followed by an object in the room will
			// allow you to put it in the character's inventory
			pickUpSomething(command, 1);
		} else if (commandWord.equals("show") || commandWord.equals("view") || commandWord.equals("look") || commandWord.equals("display")) {
			// any of these commands followed by calories, mass, heart,
			// inventory, commands, room inventory to print those to the screen
			printInfo(command);
		} else if (commandWord.equals("search") || commandWord.equals("use")) {
			// type search and then an (x,y,z) coordinate to see if there is a
			// helicopter key around in the room, use can use metal detector if
			// they are in the garden or treasure room
			searchAndUse(command);
		} else if (commandWord.equals("digup") || commandWord.equals("dig")) {
			// use shovel to dig key in garden
			digUp(command);
		} else {
			System.out.println("Command not understood");
		}

	}

	// implementations of user commands:

	/**
	 *dig up helicopter key from garden using a shovel, acts based on the information given in a command
	 *checks if the key is in the location entered based on the location x, y, and z variables in Helicopterkey
	 *Player gets key if they enter the right location in the garden
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */

	private void digUp(Command command) {
		if (!command.hasWord(2)) {
			// if there is no second word, we don't know what to pick up...
			System.out.println("Dig what?");
			return;
		}
		if (!player.isCharacterInventory("shovel")) {
			// you can't dig without a shovel
			System.out.println("You can't dig without a shovel.");
			return;
		}
		String thing = command.getWord(2).trim().toLowerCase();
		if (currentRoom.getRoomName().equalsIgnoreCase("Garden")) {
			if (thing.equalsIgnoreCase("helicopterkey")) {
				try {
					String location = command.getWord(3);
					int x = Integer.parseInt(location.split(",")[0]);
					int y = Integer.parseInt(location.split(",")[1]);
					int z = Integer.parseInt(location.split(",")[2]);
					long distance = Math.round(Helicopterkey.getGardenDistance(x, y, z));

					if (currentRoom.isRoomInventoryItem(thing) && distance <= 5) {

						currentRoom.removeObjectFromRoomInventory(thing);
						currentRoom.addCharacterToRoomInventory(thing);
						System.out.println(
								"Congratulations. You have now dug up a helicopter key, and placed it in your inventory.");
					} else {
						System.out.println("Sorry, you did not dig in a location with a helicopter key.");
					}
				} catch (Exception e) {
					System.out.println("That is not an (x,y,z) coordinate!");
				}
			} else {
				System.out.println("You can't dig up such an item.");
			}
		} else {
			System.out.println("Silly billy, you can only dig in the dirt in the garden.");
		}
	}

	/**
	 *drop something the player has in their inventory, if they have it; acts based on the information given in a command
	 * 
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */

	private void dropSomething(Command command) {
		if (!command.hasWord(2)) {
			// if there is no second word, we don't know what to pick up...
			if (command.getWord(1).equals("letgo")) {
				System.out.println("Let go of what?");
			} else {
				System.out.println(command.getWord(1).substring(0, 1).toUpperCase()
					+ command.getWord(1).substring(1) + " what?");
			}
			return;
		}
		String thing = command.getWord(2);

		if (player.isCharacterInventory(thing)) {
			player.removeCharacterInventory(thing);
			currentRoom.addObjectToRoomInventory(thing);
			System.out.println("Your inventory now contains one less " + thing + ", you placed it in the room.");
		} else {
			System.out.println("Sorry, your inventory doesn't contain this, so you can't drop it. What a pity.");
		}
	}

	/**
	 *throw something the player has in their inventory at dr. rick, if they have it; acts based on the information given in a command
	 * 
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */

	private void throwSomething(Command command) {
		if (!command.hasWord(2)) {
			// if there is no second word, we don't know what to throw...
			System.out.println("Throw what?");
			return;
		}
		String thrown = command.getWord(2);

		if (!command.hasWord(3)) {
			// if there is no second word, we don't know where to throw...
			System.out.println("Throw at what?");
			return;
		}
		String throwAt = command.getWord(3);

		if (thrown.equalsIgnoreCase("hydrochloricacid") && (throwAt.equalsIgnoreCase("drrick") || throwAt.equalsIgnoreCase("dr.rick"))) {
			drrick.killRick();
			player.removeCharacterInventory("hydrochloricacid");
			System.out.println(
					"Congrats, you have killed Dr. Rick with the HCl(aq). \n The house is now safe for you but Dr. Rick's friends are still guarding the entrance.");
		} else {
			System.out.println("Throwing that didn't do anything.");
		}
	}

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void playGame(Command command) {
		if (!command.hasWord(2)) {
			// if there is no second word, we don't know what to play...
			System.out.println("Play what?");
			return;
		}
		String thing = command.getWord(2);

		if (thing.equalsIgnoreCase("horseracing")) {
			if (currentRoom.getRoomName().equalsIgnoreCase("horseracing")
					&& currentRoom.isRoomInventoryItem("Helicopterkey")) {
				boolean winHorseRacing = false;
				winHorseRacing = HorseRacingZork.play(player.toString());
				if (winHorseRacing == true) {
					System.out.println(
							"Congrats, you have earned a key because your horse won. It has been added to your inventory \n You have proven yourself to be an accomplished gambler.");
					player.addCharacterInventory("Helicopterkey");
					currentRoom.removeObjectFromRoomInventory("Helicopterkey");
				} else {
					System.out.println("Sorry, but you sorta suck at gambling.");
				}
			} else if (currentRoom.getRoomName().equalsIgnoreCase("horseracing")
					&& !currentRoom.isRoomInventoryItem("Helicopterkey")) {
				System.out.println(
						"Sorry, but you have already won this horseracing, and there are no more keys in this room.");
			} else {
				System.out.println("There isn't horse racing in this room");
			}
		} else if (thing.equalsIgnoreCase("blackjack")) {
			int wins = 0;
			final int LOSS = 1;
			final int WIN = 2;
			final int WIN_DOUBLE_DOWN = 3;
			final int TARGET_WINS = 5;
			if (currentRoom.getRoomName().equalsIgnoreCase("casino")
					&& currentRoom.isRoomInventoryItem("Helicopterkey")) {
				int winBlackjack = BlackjackZork.play(player.toString());
				System.out.println("\n-----------------------------------------------------------\n");
				if (winBlackjack <= LOSS) {
					wins = 0;
					System.out.println("You Lost!");
				} else if (winBlackjack == WIN) {
					wins++;
					System.out.println("You Won!");
				} else if (winBlackjack == WIN_DOUBLE_DOWN) {
					wins += 2;
					System.out.println("You earned 2 Wins!");
				} else {
					System.out.println("You Tied!");
				}
				if (wins < TARGET_WINS) {
					System.out.println("You need to win " + (TARGET_WINS - wins) + " more times!\n");
				}

				if (wins == TARGET_WINS) {
					System.out.println(
							"Congrats, you have won 5 times and earned a key  It has been added to your inventory \n You must have the luck of the Irish!");
					player.addCharacterInventory("Helicopterkey");
					currentRoom.removeObjectFromRoomInventory("Helicopterkey");
				} else {
					System.out.println("Sorry, but you sorta suck at gambling.");
				}
			} else if (currentRoom.getRoomName().equalsIgnoreCase("casino")
					&& !currentRoom.isRoomInventoryItem("Helicopterkey")) {
				System.out.println("Sorry, but you have already won 5 times, and there are no more keys in this room.");
			} else {
				System.out.println("There isn't blackjack in this room");
			}
		} else {
			System.out.println("Wait, what do you want to play?");
		}

	}

	/**
	 * If user enters search:
	 *search for a key in the room, if garden or treasure room, prints whether they found it or not; acts based on the information given in a command
	 * if the player has the metal detector they know the distance to the key
	 * If user enters use:
	 *use metal detectorsearch for a key in the room, if garden or treasure room, print how far they are from it; acts based on the information given in a command
	 * use command needs metal detector
	 * 
	 * If in garden this method does not add key to player's inventory.
	 * If in treasure room this method adds key to player's inventory.
	 * 
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */

	private void searchAndUse(Command command) {
		if (command.getWord(1).equals("use") && !command.hasWord(2)) {
			System.out.println("Use what?");
			return;
		} else if ((command.getWord(1).equals("search") && !command.hasWord(2)) || (command.getWord(1).equals("use")
				&& command.getWord(2).equals("metaldetector") && !command.hasWord(3))) {
			System.out.println("Search where?");
			return;
		} else if (command.getWord(1).equals("use") && !command.getWord(2).equals("metaldetector")) {
			System.out.println("You cannot use that thing!");
			return;
		} else if (command.getWord(1).equals("use") && command.getWord(2).equals("metaldetector")
				&& !player.isCharacterInventory("Metaldetector")) {
			System.out.println("You do not have a metal detector!");
			return;
		}
		String location = "";
		if (command.getWord(1).equals("search")) {
			location = command.getWord(2);
		} else {
			location = command.getWord(3);
		}
		if (currentRoom.getRoomName().equalsIgnoreCase("Treasure Room")
				|| currentRoom.getRoomName().equalsIgnoreCase("Garden")) {
			if (currentRoom.isRoomInventoryItem("Helicopterkey")) {
				try {
					int x = Integer.parseInt(location.split(",")[0]);
					int y = Integer.parseInt(location.split(",")[1]);
					int z = Integer.parseInt(location.split(",")[2]);
					if (currentRoom.getRoomName().equalsIgnoreCase("Garden")) {
						long distance = Math.round(Helicopterkey.getGardenDistance(x, y, z));
						if (distance <= 5) {
							System.out.println("Congrats, you have found the helicopter key! Now you must dig it up.");

						} else {
							if (player.isCharacterInventory("Metaldetector")) { // Polymorphism at its best
								System.out.println("The key is " + distance + " centimetres away. You know this because you have your metal detector.");
							} else {
								System.out.println("The key isn't here!");
							}
						}
					} else {
						long distance = Math.round(Helicopterkey.getDistance(x, y, z));
						if (distance <= 5) {
							System.out.println(
									"Congrats, you have found the helicopter key! It has been added to your room inventory.");
							player.addCharacterInventory("Helicopterkey");
							currentRoom.removeObjectFromRoomInventory("Helicopterkey");
						} else {
							if (player.isCharacterInventory("Metaldetector")) { // Polymorphism at its best
								System.out.println("The key is " + distance + " centimetres away. You know this because you have your metal detector.");
							} else {
								System.out.println("The key isn't here!");
							}
						}
					}
				} catch (Exception e) {
					System.out.println("That is not an (x,y,z) coordinate!");
				}
			} else {
				System.out.println("Sorry, but you have already found the helicopter key in this room.");
			}
		} else {
			System.out.println("Sorry, but if you cannot search in this room.");
		}

	}

	/**
	 * Prints the readme or the possible commands for the player, depending on what they ask for via their command
	 * 
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */
	private void printHelp(Command command) {
		if (command.hasWord(2) && command.getWord(2).equals("extended")) {
			try {
				Scanner readme = new Scanner(new File("src/com/bayviewglen/zork/README.txt"));
				while(readme.hasNextLine()) {
					System.out.println(readme.nextLine());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Your command words are:");
			parser.showCommands();
		}
	}

	/**
	 * Prints information for the player, prints what is asked via command, such as the inventory of the player
	 * 
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */
	private void printInfo(Command command) {
		if (!command.hasWord(2)) {
			// if there is no second word, we don't know what to eat...
			if (command.getWord(1).equals("look")) {
				System.out.print("This room has: ");
				currentRoom.printRoomInventory();
			} else {
				System.out.println(command.getWord(1).substring(0, 1).toUpperCase()
						+ command.getWord(1).substring(1) + " what?");
			}
			return;
		}
		String thing = command.getWord(2);

		if (thing.equals("calories")) {
			System.out.println("Your calorie amount is " + Player.getCalories());
		} else if (thing.equals("mass")) {
			System.out.println("Your mass is " + Player.getCurrentMass() + "kg");
		} else if (thing.equals("heart")) {
			System.out.println("Your heart attack percent is " + Player.getHeartAttackPercent());
		} else if (thing.equals("inventory")) {
			System.out.print("Your inventory is: ");
			player.printInventory();
		} else if (thing.equals("room") && !command.hasWord(3)) {
			System.out.print("This room has: ");
			currentRoom.printRoomInventory();
		} else if (thing.equals("commands")) {
			CommandWords.showAll();
		} else if (command.hasWord(3)) {
			String thingTwo = command.getWord(3);
			if (thing.equals("room") && thingTwo.equals("inventory")) {
				System.out.print("This room has: ");
				currentRoom.printRoomInventory();
			} else if (thing.equals("room") && (thingTwo.equals("info") || thingTwo.equals("information") || thingTwo.equals("description"))) {
				System.out.println(currentRoom.longDescription());
			} else {
				System.out.println("Sorry, that can't be shown.");
			}
		} else {
			System.out.println("Sorry, that can't be shown.");
		}
	}

	
	/**
	 * eats something from the player's inventory
	 * 
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */
	private void eatSomething(Command command, int wordNum) {
		if (wordNum == 1 && !command.hasWord(2)) {
			// if there is no second word, we don't know what to eat...
			System.out.println("Eat what?");
			return;
		}
		String thing = command.getWord(wordNum + 1);

		if (player.isCharacterInventory(thing)) {
			InventoryItems.toInventoryItem(thing).performEat();
			player.removeCharacterInventory(thing);
			System.out.println("You have now eaten one " + thing + " from your inventory");
		} else {
			System.out.println("Sorry, you can't eat that since you aren't storing it.");
		}

	}

	
	/**
	 * picks something up from the room, adds it to player's inventory and removes it from room's inventory
	 * 
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */
	private void pickUpSomething(Command command, int wordNum) {
		if (wordNum == 1 && !command.hasWord(2)) {
			// if there is no second word, we don't know what to pick up...
			if (command.getWord(1).equals("pickup")) {
				System.out.println("Pick up what?");
			} else {
				System.out.println(command.getWord(1).substring(0, 1).toUpperCase()
						+ command.getWord(1).substring(1) + " what?");
			}
			return;
		}
		String thing = command.getWord(wordNum + 1);

		if (currentRoom.isRoomInventoryItem(thing) && thing.equalsIgnoreCase("helicopterkey")
				&& !currentRoom.getRoomName().equalsIgnoreCase("Fun room")) {
			System.out.println("Sorry, getting the helicopter keys is not that easy.");
		} else if (currentRoom.isRoomInventoryItem(thing)) {
			player.addCharacterInventory(thing);
			currentRoom.removeObjectFromRoomInventory(thing);
			System.out.println("Your inventory now contains one more " + thing);
		} else {
			System.out
					.println("Sorry, the room you are in doesn't contain this, so you can't pick it up. What a pity.");
		}
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 * Kills player under certain circumstances, such as running out of calories, heart attack, or dr.rick altercation
	 * Plays riddle if in riddle room
	 * Asks math question if in school
	 */
	private void goRoom(Command command) {
		if ((command.getWord(1).equals("go") || command.getWord(1).equals("walk") || command.getWord(1).equals("move") )&& !command.hasWord(2)) {
			// if there is no second word, we don't know where to go...
			System.out.println(command.getWord(1).substring(0, 1).toUpperCase()
					+ command.getWord(1).substring(1) + " where?");
			return;
		}

		String direction = "";

		if (command.hasWord(2)){
			direction = command.getWord(2);
		}else{
			direction = command.getWord(1);
		}
			
		// Try to leave current room.
		Room nextRoom = currentRoom.nextRoom(direction);

		if (nextRoom == null) {
			System.out.println("There is no door!");
		} else {

			// make dr rick kill you if you go into the same room as him (behind
			// you) no longer uses Drrick object, just pretend he's there
			tempLastRoom.setRoomName(currentRoom.getRoomName());

			currentRoom = nextRoom;
			

			// if they return to same room as before kill player
			if (currentRoom.getRoomName().equals(lastRoom.getRoomName()) && drrick.isRickAlive()) {
				Player.killPlayer();
				System.out.println(
						"\nSorry, but going back into the room where Dr. Rick is chasing you has killed you \nbecause he sprayed you with HCl(aq)");
				return;
			}

			lastRoom.setRoomName(tempLastRoom.getRoomName());

			//lose calories
			if (currentRoom.getRoomName().toLowerCase().indexOf("elevator") == -1) {
				Player.changeCalories(-50);
			}

			if (currentRoom.getRoomName().equalsIgnoreCase("Outside")) {
				Player.killPlayer();
				System.out.println("Sorry, walking outside you got killed by Dr. Rick throwing HCl(aq) at you.");
				return;
			} else if (currentRoom.getRoomName().equalsIgnoreCase("Dead room")) {
				Player.killPlayer();
				System.out.println("Sorry, you walked into a dungeon and got trapped there for eternity. You died.");
				return;
			} else if (Player.getCalories() < 0) {
				Player.killPlayer();
				System.out.println("Sorry, you ran out of calories and died of starvation.");
				return;
			} else if (Player.getHeartAttackPercent() >= 100) {
				Player.killPlayer();
				System.out.println("Sorry, you had a heart attack and died.");
				return;
			}
			
			System.out.println(currentRoom.longDescription());
			
			// check if it is a school to get key
			if (currentRoom.getRoomName().equalsIgnoreCase("School")
					&& currentRoom.isRoomInventoryItem("Helicopterkey")) {
				String input = keyboard.nextLine().trim().toLowerCase();
				if (input.equalsIgnoreCase("-7") || input.equalsIgnoreCase("negative seven")) {
					System.out.println("Congrats, you have earned another key. It has been added to your inventory");
					player.addCharacterInventory("Helicopterkey");
					currentRoom.removeObjectFromRoomInventory("Helicopterkey");
				} else {
					System.out.println("Sorry, that isn't the answer. You can try again later.");
				}
			} else if (currentRoom.getRoomName().equalsIgnoreCase("School")
					&& !currentRoom.isRoomInventoryItem("Helicopterkey")) {
				System.out.println(
						"Sorry, but you have already answered this question, and there are no more keys in this room.");

			}


			// play riddle with the trolls
			else if (currentRoom.getRoomName().equalsIgnoreCase("riddle room")
					&& currentRoom.isRoomInventoryItem("Helicopterkey")) {
				currentRoom.removeObjectFromRoomInventory("Helicopterkey");
				System.out.println();
				System.out.println("Do you want to ask the troll in front of the west or north door?");
				while (true) {
					String input = keyboard.nextLine().trim();
					if (input.indexOf("west") > -1) {
						// This is in front of the good door with the key
						System.out.println("Ask your question.");
						String answerChecked = checkRiddleAnswer(keyboard.nextLine().trim().toLowerCase());
						if (answerChecked.equals("bad")) {
							System.out.println("The troll in front of the door you chose says \"yes\"");
						} else if (answerChecked.equals("good")) {
							System.out.println("The troll in front of the door you chose says \"no\"");
						} else {
							System.out.println("Sorry, this question won't help you, you lost your chance.");
						}
						break;
					} else if (input.indexOf("north") > -1) {
						// This is in front of the dungeon door
						System.out.println("Ask your question.");
						String answerChecked = checkRiddleAnswer(keyboard.nextLine().trim().toLowerCase());
						if (answerChecked.equals("bad")) {
							System.out.println("The troll in front of the door you chose says \"no\"");
						} else if (answerChecked.equals("good")) {
							System.out.println("The troll in front of the door you chose says \"yes\"");
						} else {
							System.out.println("Sorry, this question won't help you, you lost your chance.");
						}
						break;
					} else {
						System.out.println("Pretty please enter a valid answer.");
					}

				}
			} else if (currentRoom.getRoomName().equalsIgnoreCase("riddle room")) {
				System.out.println("The trolls won't answer your question since you have already used it.");
			}

		}
	}

	/**
	 * checks what the player's answer to the riddle is and returns a string to help the trolls answer the person's question
	 * 
	 *
	 * @param String "fail" if the player's question is not a right answer, "good" if the player is probably in front of the good door, "bad" if player is probably in front of the bad door          
	 * @return nothing
	 */
	private String checkRiddleAnswer(String answer) {
		// If the user is asking a question about "would the other troll tell me
		// this is the good door" then return good
		// If the user is asking a question about "would the other troll tell me
		// this is the bad door" then return bad
		// otherwise return fail
		if (answer.indexOf("other") < 0) {
			return "fail";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("say")
				&& answer.indexOf("say") < answer.indexOf("good")) {
			return "good";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("tell")
				&& answer.indexOf("tell") < answer.indexOf("good")) {
			return "good";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("say")
				&& answer.indexOf("say") < answer.indexOf("right")) {
			return "good";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("tell")
				&& answer.indexOf("tell") < answer.indexOf("right")) {
			return "good";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("say")
				&& answer.indexOf("say") < answer.indexOf("bad")) {
			return "bad";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("tell")
				&& answer.indexOf("tell") < answer.indexOf("bad")) {
			return "bad";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("say")
				&& answer.indexOf("say") < answer.indexOf("wrong")) {
			return "bad";
		} else if (answer.indexOf("other") < answer.indexOf("troll") && answer.indexOf("troll") < answer.indexOf("tell")
				&& answer.indexOf("tell") < answer.indexOf("wrong")) {
			return "bad";
		} else {
			return "fail";
		}
	}

	/**
	 * flies helicopter and checks if player wins
	 * makes sure that the player has enough keys first
	 * 
	 *
	 * @param Command command  the command containing the input words processed by the parser          
	 * @return nothing
	 */
	private void fly(Command command) {
		if (currentRoom.getRoomName().equalsIgnoreCase("helicopter pad")) {
			String thing = command.getWord(2);
			if (thing.equals("helicopter") || thing.equals("chopper")) {
				if (Player.getHelicopterKeys() == TARGET_HELICOPTER_KEYS) {
					System.out.println("You fly off into the sunset with your helicopter, celebrating you victory.");
					Player.setEndBehaviour("win");
				} else {
					System.out.println("You don't have all the keys to start the helicopter!");
				}
			} else {
				System.out.println("You cannot fly that!");
			}
		} else {
			System.out.println("You cannot fly in here!");
		}
		return;
	}

	/**
	 *saves game
	 *
	 * @param nothing         
	 * @return nothing
	 */
	private void save() {
		try {
			File saveFile = new File("data/Save.dat");
			FileOutputStream fileOutput = new FileOutputStream(saveFile);
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			writeObject(objectOutput);
			objectOutput.close();
			fileOutput.close();
			System.out.println("Game Saved");
		} catch (IOException e) {
			e.printStackTrace();
		} // try catch
	}

	/**
	 *starts game with information about objects from saved data file
	 *
	 * @param nothing         
	 * @return nothing
	 */
	private void loadGame() {
		try {
			File saveFile = new File("data/Save.dat");
			FileInputStream fileInput = new FileInputStream(saveFile);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			readObject(objectInput);
			objectInput.close();
			fileInput.close();
			System.out.println("Game Loaded");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.writeObject(currentRoom);
		stream.writeObject(lastRoom);
		stream.writeObject(masterRoomMap);
		stream.writeObject(player);
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		currentRoom = (Room) stream.readObject();
		lastRoom = (Room) stream.readObject();
		masterRoomMap = (HashMap<String, Room>) stream.readObject();
		player = (Player) stream.readObject();
	}
}