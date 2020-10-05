/*
 * @author John Walter
 * @author Hunter Sitki
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	Set<TestBoardCell> adjList = new HashSet<TestBoardCell>();
	boolean isRoom = false;
	boolean isOcc = false;
	
	public TestBoardCell(int row, int col) {
		//creates adjList here
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
		this.isOcc = occ;
	}
	public boolean getOccupied() {
		return isOcc;
	}
}

	
