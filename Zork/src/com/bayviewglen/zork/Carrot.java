package com.bayviewglen.zork;

//this is a carrot object, which extends food
public class Carrot extends Food{
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Carrot(){
		super("carrot", new EatHealthy(), 0.1);
	}
}
