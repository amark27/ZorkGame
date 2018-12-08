package com.bayviewglen.zork;

//This is obviously a pizza, get it out of your head that it's not.
public class Pizza extends Food {
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Pizza (){
	super("pizza", new EatNothingHappens(), 0.2);
	}
	
}
