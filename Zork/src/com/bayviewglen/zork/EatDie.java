package com.bayviewglen.zork;


public class EatDie implements EatBehaviour, java.io.Serializable {

	/**
	 *kills the player if they eat an inedible object
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public void eat(){
		Player.killPlayer();
	}
}
