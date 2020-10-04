package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	
	int row, col;
	boolean occupied = false;
	boolean isRoom = false;
	
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	

	public int getRow() {
		return row;
	}



	public void setRow(int row) {
		this.row = row;
	}



	public int getCol() {
		return col;
	}



	public void setCol(int col) {
		this.col = col;
	}



	public Set<TestBoardCell> getAdjList(){
		Set<TestBoardCell> temp = new HashSet<TestBoardCell>();
		return temp;
	}
	
	public void setRoom(boolean room) {
		this.isRoom = room;
	}
	
	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean getOccupied() {
		return occupied;
	}
}
