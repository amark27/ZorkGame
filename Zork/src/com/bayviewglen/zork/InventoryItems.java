package com.bayviewglen.zork;

//helps with the analysis of inventory objects, checks whether objects exist and converts strings to objects with that name
public class InventoryItems implements java.io.Serializable {

	//an array of the names of all teh objects
	private static String[] objectList = {"pizza", "computer", "metaldetector", "hydrochloricacid", "shovel", "helicopterkey", "apple", "doughnut", "carrot"};

	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public InventoryItems() {
	}

	
	/**
	 *returns whether the thing named in the string exists as an object
	 * 
	 * @param String item  the name of item
	 * @return boolean  true if such an object exists, false otherwise
	 */
	public static boolean isInventoryItem(String item) {
		for (int i = 0; i < objectList.length; i++) {
			if (item.trim().equalsIgnoreCase(objectList[i])) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 *will return a new class of the type named by the string
	 * 
	 * @param String item  the name of item
	 * @return Objects  an instance of the class with the given name
	 */
	public static Objects toInventoryItem(String item) {
		Objects thing = null;
		item = item.toLowerCase().trim();
		//make first letter capitalized
		String first = item.substring(0, 1);
		item = item.substring(1);
		item = "com.bayviewglen.zork." + first.toUpperCase() + item;
		try {
			Class<?> classy = Class.forName(item);
			thing = (Objects) classy.newInstance();
		} catch (ClassNotFoundException e){
		} catch (InstantiationException e){
		} catch (IllegalAccessException e){
		}
		return thing;
	}
}
