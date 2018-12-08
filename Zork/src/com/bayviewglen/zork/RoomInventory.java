package com.bayviewglen.zork;

import java.util.*;

//An inventory of the objects and characters that a room stores
public class RoomInventory implements java.io.Serializable {
	//lists for the characters and objects in a room
	private ArrayList<Characters> characters = new ArrayList<Characters>();
	private ArrayList<Objects> roomItems = new ArrayList<Objects>(); // was
																		// Object
	/**
	 *a constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public RoomInventory() {
	}

	
	 /**
   	 *adds a object to the room's inventory
   	 * 
   	 * @param String input  the name of the object to add
   	 * @return nothing
   	 */
	public void addObject(String input) {
		roomItems.add(InventoryItems.toInventoryItem(input));
	}

	/**
   	 * checks if an object of a given name is a room inventory item
   	 * 
   	 * @param String item  the name of the object to check
   	 * @return boolean  true if object is in room, false otherwise
   	 */
	public boolean isRoomInventoryItem(String item) {
		if (InventoryItems.isInventoryItem(item)) {
			for (int i = 0; i < roomItems.size(); i++) {
				if (roomItems.get(i).toString().equalsIgnoreCase(item.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	 /**
   	 *adds a character to the room's inventory, creating an instance of the character
   	 * 
   	 * @param String name  the name of the character to add
   	 * @return nothing
   	 */
	public void addCharacters(String name) {
		Characters thing = null;
		name.toLowerCase();
		// make first letter capitalized
		String first = name.substring(0, 1);
		name = name.substring(1);
		name = "com.bayviewglen.zork." + first.toUpperCase() + name;
		try {
			Class<?> classy = Class.forName(name);
			thing = (Characters) classy.newInstance();
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		characters.add(thing);
	}

	/**
   	 * removes object from room
   	 * 
   	 * @param String item  the name of the object to add
   	 * @return nothing
   	 */
	public void removeRoomInventoryItem(String item) {
		for (int i = 0; i < roomItems.size(); i++) {
			if (roomItems.get(i).toString().equalsIgnoreCase(item.trim())) {
				roomItems.remove(i);
				return;
			}
		}
	}

	/**
   	 * print's the room's inventory for the user, excluding keys
   	 * 
   	 * @param nothing
   	 * @return nothing
   	 */
	public void printInventory() {
		for (Objects x : roomItems) {
			if (!x.toString().equalsIgnoreCase("helicopterkey")) {
				System.out.print(x.toString() + " ");
			}
		}
		System.out.println();
	}

}
