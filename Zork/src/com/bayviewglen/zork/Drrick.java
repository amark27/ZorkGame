package com.bayviewglen.zork;

//the Drrick class, extends Characters

public class Drrick extends Characters{
	//states whether Drrick is alive
	private boolean isAlive;
	

	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Drrick (){
		
		
		super("drrick");
		
		isAlive = true;
	}
	/**
	 *checks whether the doctor is alive
	 * 
	 * @param nothing
	 * @return boolean isAlive true is character is alive, false otherwise
	 */
	public boolean isRickAlive(){
		return isAlive;
	}
	
	/**
	 *sets isAlive to false
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public void killRick(){
		isAlive = false;
	}
	
}
