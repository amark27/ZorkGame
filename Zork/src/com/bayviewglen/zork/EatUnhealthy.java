package com.bayviewglen.zork;

public class EatUnhealthy implements EatBehaviour, java.io.Serializable{

	/**
	 *adds calories and increases heart attack risk if the player eats something unhealthy
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public void eat(){
		Player.changeCalories(100);
		Player.changeHeartAttackPercent(20);
	}
}
