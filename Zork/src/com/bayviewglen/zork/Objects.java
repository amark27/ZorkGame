package com.bayviewglen.zork;


//this class is the parent class of all storable inventory items
public class Objects implements java.io.Serializable {

	private String name;
	private double mass;
	//now all the interfaces
	private EatBehaviour eatBehaviour;
	
	/**
	 *constructor
	 * 
	 * @param String name  the name of the thing
	 * @param eatBehaviour EatBehaviour  instance of teh eatbehavriour class used for this object  
	 * @param double mass   the mass of the object
	 * @return nothing
	 */
	public Objects(String name, EatBehaviour eatBehaviour, double mass) {
		this.name = name;
		this.eatBehaviour = eatBehaviour;
		this.mass= mass;
	}
	

	/**
	 *returns name of thing
	 * 
	 * @param nothing  
	 * @return String  the name of the thing
	 */
	public String toString (){
		return name;
	}
	
	/**
	 *calls eatBehaviour
	 * 
	 * @param nothing  
	 * @return nothing
	 */
	public void performEat (){
		eatBehaviour.eat();
	}
	
	/**
	 *creturns mass of the object
	 * 
	 * @param nothing  
	 * @return double mass
	 */
	public double getMass(){
	return mass;	
	}
	
	
}
