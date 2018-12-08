package com.bayviewglen.zork;


//class for the helicopter key objects, extends Objects
public class Helicopterkey extends Objects{
	
	//The following three variables are the coordinates of the key in the garden
	private static final int gardenX = (int) (Math.random() * 1000);
	private static final int gardenY = (int) (Math.random() * 1000);
	private static final int gardenZ = (int) (Math.random() * 1000);
	//The following three variables are the coordinates of the key in the treasure room
	private static final int x = (int) (Math.random() * 1000);
	private static final int y = (int) (Math.random() * 1000);
	private static final int z = (int) (Math.random() * 1000);
	
	/**
	 *constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
	public Helicopterkey(){
		super("helicopterkey", new EatDie(), 1);
	}
	
	/**
	 *gets the distance from some inputed location to the key in the treasure room, using pythagorean theorem in 3D space
	 * 
	 * @param int searchX  the x coordinate to check
	 * @param int searchY  the y coordinate to check
	 * @param int searchZ  the z coordinate to check
	 * 
	 * @return double  the distance to the key
	 */
	public static double getDistance (int searchX, int searchY, int searchZ) {
		return Math.sqrt(Math.pow(x-searchX, 2) + Math.pow(y-searchY, 2) + Math.pow(z-searchZ, 2));
	}
	
	/**
	 *gets the distance from some inputed location to the key in the garden, using pythagorean theorem in 3D space
	 * 
	 * @param int searchX  the x coordinate to check
	 * @param int searchY  the y coordinate to check
	 * @param int searchZ  the z coordinate to check
	 * 
	 * @return double  the distance to the key
	 */
	public static double getGardenDistance (int searchX, int searchY, int searchZ) {
		return Math.sqrt(Math.pow(gardenX-searchX, 2) + Math.pow(gardenY-searchY, 2) + Math.pow(gardenZ-searchZ, 2));
	}
}
