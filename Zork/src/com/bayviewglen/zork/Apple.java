package com.bayviewglen.zork;

public class Apple extends Food {

	//This is an apple opbject, which extends food
	public Apple (){
		/**
		 *constructor
		 * 
		 * @param nothing
		 * @return nothing
		 */
		super("apple", new EatHealthy(), 0.1);
	}
}
