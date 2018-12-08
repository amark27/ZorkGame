package com.bayviewglen.zork;
import java.util.*;

//this is the inventory of the player
public class Player extends Characters {
	//private String player;
	//private ArrayList<Objects> playerItems = new ArrayList<Objects>();
	private final double maxMass = 30;
	private static double currentMass = 0;
	private static int calories;
	private static int heartAttackPercent;
	private static int helicopterKeys = 0;
	private static String endBehaviour = "alive";
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Player(){
		super("player");
		endBehaviour = "alive";
	}
	
	
	
	/**
	 *adds the object with the given name to the player's inventory if they can hold it
	 * 
	 * @param String input  the name of the Objects class
	 * @return nothing
	 */
	@Override
	public void addCharacterInventory(String input){
		input = input.toLowerCase().trim();
		if ((getCurrentMass() + InventoryItems.toInventoryItem(input).getMass()) > maxMass){
			System.out.println("I can't pick that up. It's too heavy for me to hold.");
		} else {
			if (input.equals("helicopterkey")) {
				helicopterKeys ++;
			}
			super.addCharacterInventory(input);	
			currentMass += InventoryItems.toInventoryItem(input).getMass();
		}
	}
	
	/**
	 *adds a name to the player
	 * 
	 * @param String name   the new name of the player
	 * @return nothing
	 */
	public void addName(String name){
		super.changeName(name);
	}
	
	/**
	 *changes the player's heart attack risk
	 * 
	 * @param String amount  the amount to add to the heart attack risk
	 * @return nothing
	 */
	public static void changeHeartAttackPercent(int amount){
		heartAttackPercent += amount;
	}
	
	/**
	 *removes an object from the player's inventory and changes their mass
	 * 
	 * @param String thing  the name of the Objects instance to remove from the inventory
	 * @return nothing
	 */
	@Override
	public void removeCharacterInventory(String thing){
		super.removeCharacterInventory(thing.toLowerCase().trim());
		currentMass -= InventoryItems.toInventoryItem(thing).getMass();
	}
	
	/**
	 *changes the player's calories
	 * 
	 * @param String amount  the amount to add to the calories
	 * @return nothing
	 */
	public static void changeCalories(int amount){
		calories += amount;
	}
	
	/**
	 *gets the player's heart attack risk
	 * 
	 * @param nothing
	 * @return double heartAttackPercent
	 */
	public static double getHeartAttackPercent() {
		return heartAttackPercent;
	}
	
	/**
	 *gets the player's mass
	 * 
	 * @param nothing
	 * @return double currentMass
	 */
	public static double getCurrentMass() {
		return currentMass;
	}
	
	/**
	 *gets the player's calories
	 * 
	 * @param nothing
	 * @return double calories
	 */
	public static double getCalories() {
		return calories;
	}
	
	
	/**
	 *kill game to end player
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public static void killPlayer(){
		endBehaviour = "dead";
	}
	
	/**
	 *set the player's current endBehaviour
	 * 
	 * @param String end  the new endbehaviour
	 * @return nothing
	 */
	public static void setEndBehaviour(String end) {
		endBehaviour = end;
	}
	
	/**
	 *get the player's current endBehaviour
	 * 
	 * @param nothing
	 * @return String endBahaviour
	 */
	public static String getEndBehaviour() {
		return endBehaviour;
	}
	
	/**
	 *get the player's helicopter key count
	 * 
	 * @param nothing
	 * @return int helicopterKeys
	 */
	public static int getHelicopterKeys () {
		return helicopterKeys;
	}
	
}
