/*
 * @author John Walter
 * @author Hunter Sitki
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	Set<TestBoardCell> adjList = new HashSet<TestBoardCell>();
	private boolean isRoom, isOccupied;
	private int row, col;

	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void addAdj(TestBoardCell cell) {
		adjList.add(cell);
	}

	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	public void setIsRoom(boolean room) {
		this.isRoom = room;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setOccupied(boolean occ) {
		this.isOccupied = occ;
	}
	public boolean getOccupied() {
		return isOccupied;
	}
}


