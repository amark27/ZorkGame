package com.bayviewglen.zork;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Class Room - a room in an adventure game.
 *
 * Author:  Michael Kolling
 * Version: 1.1
 * Date:    August 2000
 * 
 * This class is part of Zork. Zork is a simple, text based adventure game.
 *
 * "Room" represents one location in the scenery of the game.  It is 
 * connected to at most four other rooms via exits.  The exits are labelled
 * north, east, south, west.  For each direction, the room stores a reference
 * to the neighbouring room, or null if there is no exit in that direction.
 */
public class Room implements java.io.Serializable {
	private String roomName;
    private String description;
    private HashMap<String, Room> exits;   // stores exits of this room.
    private RoomInventory roomInventory = new RoomInventory();
    /**
     * a constructor
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "a kitchen" or "an open court yard".
     * @param String description
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
    }

    /**
	 *a constructor
	 * 
	 * @param nothing
	 * @return nothing
	 */
    public Room() {
		// default constructor.
    	roomName = "DEFAULT ROOM";
    	description = "DEFAULT DESCRIPTION";
    	exits = new HashMap<String, Room>();
	}

    /**
	 *adds an exit to the rooms exits hashmap
	 * 
	 * @param char direction   the direction of the exit
	 * @param Room r  the room at the exit 
	 * @return nothing
	 * @throws Exception
	 */
    public void setExit(char direction, Room r) throws Exception{
    	String dir= "";
    	switch (direction){
    	case 'E': dir = "east";break;
    	case 'W': dir = "west";break;
    	case 'S': dir = "south";break;
    	case 'N': dir = "north";break;
    	case 'U': dir = "up";break;
    	case 'D': dir = "down";break;
    	default: throw new Exception("Invalid Direction");
    	
    	}
    	
    	exits.put(dir, r);
    }
    
	/**
     * Define the exits of this room.  Every direction either leads to
     * another room or is null (no exit there).
     */
    public void setExits(Room north, Room east, Room south, Room west, Room up, Room down) 
    {
        if(north != null)
            exits.put("north", north);
        if(east != null)
            exits.put("east", east);
        if(south != null)
            exits.put("south", south);
        if(west != null)
            exits.put("west", west);
        if(up != null)
            exits.put("up", up);
        if(up != null)
            exits.put("down", down);
        
    }

    /**
     * Return the description of the room (the one that was defined in the
     * constructor).
     */
    public String shortDescription()
    {
        return "\nRoom: " + roomName +"\n\n" + description;
    }

    /**
     * Return a long description of this room, on the form:
     *     You are in the kitchen.
     *     Exits: north west
     */
    public String longDescription()
    {
    	
        return "\nRoom: " + roomName +"\n\n" + description + "\n" + exitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west ".
     */
    private String exitString()
    {
        String returnString = "Exits:";
		Set keys = exits.keySet();
        for(Iterator iter = keys.iterator(); iter.hasNext(); )
            returnString += " " + iter.next();
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room nextRoom(String direction) 
    {
        return (Room)exits.get(direction);
    }

    /**
   	 *gets room name
   	 * 
   	 * @param nothing 
   	 * @return String roomName
   	 */
	public String getRoomName() {
		return roomName;
	}

	 /**
   	 *sets room description
   	 * 
   	 * @param String roomName the name of the room 
   	 * @return nothing
   	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	 /**
   	 *gets room description
   	 * 
   	 * @param nothing 
   	 * @return String roomDescription
   	 */
	public String getDescription() {
		return description;
	}

	 /**
   	 *sets room description
   	 * 
   	 * @param String description  the description
   	 * @return nothing
   	 */
	public void setDescription(String description) {
		this.description = description;
	}
	

	 /**
   	 *adds a object to the room's inventory
   	 * 
   	 * @param String object  the name of the object to add
   	 * @return nothing
   	 */
	public void addObjectToRoomInventory(String object){
		roomInventory.addObject(object);
	}
	
	 /**
   	 *adds a character to the room's inventory
   	 * 
   	 * @param String character  the name of the character to add
   	 * @return nothing
   	 */
	public void addCharacterToRoomInventory(String character){
		roomInventory.addCharacters(character);
	}
	
	/**
   	 * removes object from room
   	 * 
   	 * @param String object  the name of the object to add
   	 * @return nothing
   	 */
	public void removeObjectFromRoomInventory(String object){
		roomInventory.removeRoomInventoryItem(object);
	}
	
	/**
   	 * checks if an object of a given name is a room inventory item
   	 * 
   	 * @param String object  the name of the object to check
   	 * @return boolean  true if object is in room, false otherwise
   	 */
	public boolean isRoomInventoryItem(String object){
		return roomInventory.isRoomInventoryItem(object);
	}
	
	/**
   	 * print's the room's inventory for the user, excluding keys
   	 * 
   	 * @param nothing
   	 * @return nothing
   	 */
	public void printRoomInventory(){
		roomInventory.printInventory();
	}


}
