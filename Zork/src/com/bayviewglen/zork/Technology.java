package com.bayviewglen.zork;

//technology, superclass of all technology objects, is an Objects
public class Technology extends Objects{

	/**
	 *constructor
	 * 
	 * @param String name  name of object
	 * @param double mass  mass of object
	 * @return nothing
	 */
	public Technology(String name, double mass){
		super(name, new EatDie(), mass);
	}
}
