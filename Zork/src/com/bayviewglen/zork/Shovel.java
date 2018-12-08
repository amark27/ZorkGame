package com.bayviewglen.zork;

// a shovel is an object 
public class Shovel extends Objects{

	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Shovel (){
		super("shovel", new EatDie(), 10);
	}
	
}
