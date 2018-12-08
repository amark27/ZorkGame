package com.bayviewglen.zork;


import java.util.ArrayList;

//This class is the superclass of all player and character objects in the game
public class Characters implements java.io.Serializable {
	private String name;
	private ArrayList<Objects> characterItems = new ArrayList<Objects>();

	
	/**
	 *constructs Characters object
	 * 
	 * @param String name  The name of the character          
	 * @return nothing
	 */
	public Characters(String name) {
		this.name = name;
	}

	
	/**
	 *return the character's name
	 * 
	 * @param nothing          
	 * @return String name  The name of the character
	 */
	public String toString() {
		return name;
	}


	/**
	 *change the character's name to the name given via the parameter
	 * 
	 * @param String name  The new name of the character          
	 * @return nothing
	 */
	public void changeName(String name) {
		this.name = name;
	}

	/**
	 *adds an object of an Objects class with the name given to the character's inventory
	 * 
	 * @param String thing  The name of the object        
	 * @return nothing
	 */
	public void addCharacterInventory(String thing) {
		if (InventoryItems.isInventoryItem(thing)) {
			characterItems.add(InventoryItems.toInventoryItem(thing));
		} else {
			System.out.println("The item is not present.");
		}
	}

	/**
	 *remove an object of an Objects class with the name given from the character's inventory
	 * 
	 * @param String thing  The name of the object       
	 * @return nothing
	 */
	public void removeCharacterInventory(String thing) {
		for (int i = 0; i < characterItems.size(); i++) {
			if (characterItems.get(i).toString().equalsIgnoreCase(thing.trim())) {
				characterItems.remove(i);
				return;
			}
		}
	}

	/**
	 *check whether an object of an Objects class with the name given is in the character's inventory
	 * 
	 * @param String thing  The name of the object         
	 * @return boolean  true if the thing is in the inventory, false otherwise
	 */
	public boolean isCharacterInventory(String thing) {
		for (Objects item : characterItems) {
			if (item.toString().equalsIgnoreCase(thing.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 *cycle through the character's inventory and print the name of every item within
	 * 
	 * @param nothing       
	 * @return nothing
	 */
	public void printInventory() {
		for (Objects x : characterItems) {
			System.out.print(x.toString() + " ");
		}
		System.out.println();
	}
}
