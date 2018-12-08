package com.bayviewglen.zork;


//a doughnut class, extends Food
public class Doughnut extends Food{
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Doughnut (){
		super("doughnut", new EatUnhealthy(), 0.1);
	}

}
