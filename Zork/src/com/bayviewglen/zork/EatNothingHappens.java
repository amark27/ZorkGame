package com.bayviewglen.zork;

public class EatNothingHappens implements EatBehaviour, java.io.Serializable{

	/**
	 *adds calories if the player eats something that is neither healthy nor unhealthy
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public void eat(){
		Player.changeCalories(100);
	}
}
