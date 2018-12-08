package com.bayviewglen.zork;

//the lying troll
public class Lyingtroll extends Characters{
	
	public static String[] keywords = {}; // fill in later
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Lyingtroll(){
		super("Lying Troll");
	}
	
	/**
	 *never used
	 */
	public boolean correct(String response){
		for (String word: keywords) {
			if (word.equalsIgnoreCase(response)) {
				return true;
			}
		}
		return false;
	}
}
