/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	Set<BoardCell> adjList = new HashSet<BoardCell>();
	private boolean isRoom, isOccupied;
	private int row, col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean isDoorway;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	

	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/*
	 * Adding and return function for adjacent cells
	 */
	public void addAdj(BoardCell cell) {
		adjList.add(cell);
	}
	public Set<BoardCell> getAdjList(){
		return adjList;
	}
	
	/*
	 * Getter and setter for cell that is a room
	 */
	public void setIsRoom(boolean room) {
		this.isRoom = room;
	}
	public boolean isRoom() {
		return isRoom;
	}

	/*
	 * Getter and setter for occupied cell
	 */
	public void setOccupied(boolean occ) {
		this.isOccupied = occ;
	}
	public boolean getOccupied() {
		return isOccupied;
	}

	/*
	 * Getter and setter for room initial
	 */
	public void setInitial (char initial) {
		this.initial = initial;
	}
	public char getInitial() {
		return initial;
	}
	
	/*
	 * Getter and setter for door direction
	 */
	public void setDoorDirection(DoorDirection direction) {
		doorDirection = direction;
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	/*
	 * Getter and setter for door
	 */
	public void setDoorway(boolean door) {
		isDoorway = door;
	}
	public boolean isDoorway() {
		return isDoorway;
	}

	/*
	 * Getter and setter for room label
	 */
	public void setRoomLabel(boolean label) {
		roomLabel = label;
	}
	public boolean isLabel() {
		return roomLabel;
	}

	/*
	 * Getter and setter for room center
	 */
	public void setRoomCenter(boolean center) {
		roomCenter = center;
	}
	public boolean isRoomCenter() {
		return roomCenter;
	}

	/*
	 * Getter and setter for occupied cell
	 */
	public void setSecretPassage(char secret) {
		secretPassage = secret;
	}
	public char getSecretPassage() {
		return secretPassage;
	}
}


