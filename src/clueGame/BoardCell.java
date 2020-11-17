/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	Set<BoardCell> adjList = new HashSet<BoardCell>();
	private boolean isRoom, isOccupied, isDoorway, isWalkway;
	private int row, col, x, y, width, height;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean hasPassage;
	private String roomName;
	private boolean target = false;
	
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
	
	public boolean containsClick(int mouseX, int mouseY) {
		
		int col1 = mouseX/width;
		int row1 = mouseY/height -1;
		
		if (col1 == col && row1 == row) {
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public String toString() {
		return"(" + getRow() + ", " + getCol() + ")";
	}

	public void draw(int width, int height, Graphics g) {
		
		this.width = width;
		this.height = height;
		x = col*width;
		y = row*height;
		
		if (hasSecretPassage()) {
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, height);
			if (target == true) {
				g.setColor(Color.GREEN);
			}
			else{
				g.setColor(BROWN);
			}
			g.fillRect(x+width/6, y+height/6, width-width/3, height-height/3);
		} else if (isRoom()) {
			if (target == true) {
				g.setColor(Color.GREEN);
			}
			else{
				g.setColor(Color.LIGHT_GRAY);
			}
			g.fillRect(x, y, width, height);
			
			if (isLabel()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("TimesRoman", Font.BOLD, 15));
				g.drawString(roomName, x, y);
			}
		}
		else if (isWalkway()) {
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			g.fillRect(x, y, width, height);
			
			if (target == true) {
				g.setColor(Color.GREEN);
			}
			else{
				g.setColor(Color.YELLOW);
			}
			g.drawRect(x+2, y+2, width-2, height-2);
			g.fillRect(x+2, y+2, width-2, height-2);
		} else if (isDoorway()) {
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			g.fillRect(x, y, width, height);
			
			if (target == true) {
				g.setColor(Color.GREEN);
			}
			else{
				g.setColor(Color.YELLOW);
			}
			g.drawRect(x+2, y+2, width-2, height-2);
			g.fillRect(x+2, y+2, width-2, height-2);
			
			g.setColor(BROWN);
			switch (doorDirection) {
			case LEFT:
				g.drawLine(x+1, y, x+1, y+height);
				g.drawLine(x+2, y, x+2, y+height);
				g.drawLine(x+3, y, x+3, y+height);
				break;
			case RIGHT:
				g.drawLine(x+width-1, y, x+width-1, y+height);
				g.drawLine(x+width-2, y, x+width-2, y+height);
				g.drawLine(x+width-3, y, x+width-3, y+height);
				break;
			case UP:
				g.drawLine(x, y+1, x+width, y+1);
				g.drawLine(x, y+2, x+width, y+2);
				g.drawLine(x, y+3, x+width, y+3);
				break;
			case DOWN:
				g.drawLine(x, y-1 + height, x+width, y-1+ height);
				g.drawLine(x, y-2 + height, x+width, y-2+ height);
				g.drawLine(x, y-3 + height, x+width, y-3+ height);
				break;
			}
		}
		else {
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, height);
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
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public void setTarget(boolean t) {
		target = t;
	}
	
	public boolean isTarget() {
		return target;
	}
}


