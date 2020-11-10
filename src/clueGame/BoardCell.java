/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Stroke;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	Set<BoardCell> adjList = new HashSet<BoardCell>();
	private boolean isRoom, isOccupied, isDoorway, isWalkway;
	private int row, col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean hasPassage;
	private String roomName;
	
	public static final Color BROWN = new Color(102,51,0);

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
	
	public void draw(int width, int height, Graphics o) {
		int y = row*height;
		int x = col*width;
		
		
		if (hasSecretPassage()) {
			o.setColor(Color.BLACK);
			o.drawRect(x, y, width, height);
			o.setColor(Color.GREEN);
			o.fillRect(x, y, width, height);
		} else if (isRoom()) {
			o.setColor(Color.LIGHT_GRAY);
			o.fillRect(x, y, width, height);
			
			if (isLabel()) {
				o.setColor(Color.BLACK);
				o.setFont(new Font("TimesRoman", Font.BOLD, 15));
				o.drawString(roomName, x, y);
			}
		}
		else if (isWalkway()) {
			o.setColor(Color.BLACK);
			o.drawRect(x, y, width, height);
			o.fillRect(x, y, width, height);
			
			o.setColor(Color.YELLOW);
			o.drawRect(x+2, y+2, width-2, height-2);
			o.fillRect(x+2, y+2, width-2, height-2);
		} else if (isDoorway()) {
			o.setColor(Color.BLACK);
			o.drawRect(x, y, width, height);
			o.fillRect(x, y, width, height);
			
			o.setColor(Color.YELLOW);
			o.drawRect(x+2, y+2, width-2, height-2);
			o.fillRect(x+2, y+2, width-2, height-2);
			
			o.setColor(BROWN);
			switch (doorDirection) {
			case LEFT:
				o.drawLine(x+1, y, x+1, y+height);
				o.drawLine(x+2, y, x+2, y+height);
				o.drawLine(x+3, y, x+3, y+height);
				break;
			case RIGHT:
				o.drawLine(x+width-1, y, x+width-1, y+height);
				o.drawLine(x+width-2, y, x+width-2, y+height);
				o.drawLine(x+width-3, y, x+width-3, y+height);
				break;
			case UP:
				o.drawLine(x, y+1, x+width, y+1);
				o.drawLine(x, y+2, x+width, y+2);
				o.drawLine(x, y+3, x+width, y+3);
				break;
			case DOWN:
				o.drawLine(x, y-1 + height, x+width, y-1+ height);
				o.drawLine(x, y-2 + height, x+width, y-2+ height);
				o.drawLine(x, y-3 + height, x+width, y-3+ height);
				break;
			}
		}
		else {
			o.setColor(Color.BLACK);
			o.drawRect(x, y, width, height);
			o.setColor(Color.BLACK);
			o.fillRect(x, y, width, height);
		}

		
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
	
	public void setIsWalkway(boolean walkway) {
		this.isWalkway = walkway;
	}
	public boolean isWalkway() {
		return isWalkway;
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
	 * Getter and setter for secret passage cell
	 */
	public void setSecretPassage(char secret) {
		secretPassage = secret;
	}
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setHasSecretPassage(boolean passage) {
		hasPassage = passage;
	}
	public boolean hasSecretPassage() {
		return hasPassage;
	}
	
	public void setRoomName(String name) {
		roomName = name;
	};
}


