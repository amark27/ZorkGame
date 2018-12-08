package com.bayviewglen.zork;

public class EatHealthy implements EatBehaviour, java.io.Serializable{

	/**
	 *adds calories and subtracts form heart attack risk if the player eats something healthy
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public void eat(){
		Player.changeCalories(100);
		Player.changeHeartAttackPercent(-10);

	}
	
}
