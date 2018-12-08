package com.bayviewglen.zork;

public class Food extends Objects{

	/**
	 *constructor
	 * 
	 * @param String name  name of object
	 * @param EatBehaviour eatingBehaviour  the eatbehaviour for this particular object
	 * @param double mass  the mass of the object
	 * 
	 * @return nothing
	 */
	public Food(String name, EatBehaviour eatingBehaviour, double mass){
		super(name, eatingBehaviour, mass);
	}
	
}
