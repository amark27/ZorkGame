package com.bayviewglen.zork;

public class Truthfultroll extends Characters {

	public static String[] keywords = {}; // fill in later
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */

	public Truthfultroll(){
		super("Truthful Troll");
	}
	
	//not used
	public boolean correct(String response){
		for (String word: keywords) {
			if (word.equalsIgnoreCase(response)) {
				return true;
			}
		}
		return false;
	}
}
