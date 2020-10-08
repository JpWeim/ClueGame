/*
 * @author John Walter
 * @author Hunter Sitki
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;

	/*
	 * Creates new board and fills it with cells
	 * Then loops again through the cells and 
	 * fills a set with the adjacent cells to the
	 * cell at said index
	 */
	public TestBoard() {
		grid = new TestBoardCell[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				TestBoardCell cell = new TestBoardCell(i, j);
				grid[i][j] = cell;
			}
		}
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if(i-1 >= 0) {
					getCell(i,j).addAdj(getCell(i-1, j));
				}
				if(i+1 < TestBoard.ROWS) {
					getCell(i,j).addAdj(getCell(i+1, j));
				}
				if(j-1 >= 0) {
					getCell(i,j).addAdj(getCell(i, j-1));
				}
				if(j+1 < TestBoard.COLS) {
					getCell(i,j).addAdj(getCell(i, j+1));
				}
			}
		}

	}
	/*
	 * Sets up for recursive findAllTargets
	 * Instantiates the sets and adds the starting cell
	 * to visited so it does not loop back, then calls
	 * findAllTargets
	 */
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visited = new HashSet<TestBoardCell>();
		targets = new HashSet<TestBoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	/*
	 * Iterates through every adjacent cell to the current cell
	 * If the adjacent cell has already been visited, it is skipped
	 * If the adjacent cell is occupied by someone else, it is skipped
	 * If the adjacent cell is a room, it is added to targets and the 
	 * method is not called again, therefore "taking up the rest of 
	 * the movement.
	 * If none of the above occur, checks to see if the pathLength is 1 and 
	 * if so, adds the cell to the targets, else, recursively calls itself
	 * with the pathLength -1. 
	 * At the end, removed the visited cell for cleanup.
	 */
	private void findAllTargets(TestBoardCell startCell, int pathLength) {
		for(TestBoardCell adjCell : startCell.getAdjList()) {
			if(visited.contains(adjCell)) {
			}
			else if (adjCell.isRoom() == true) {
				targets.add(adjCell);
			}
			else if (adjCell.getOccupied() == true) {
			}
			else {
			visited.add(adjCell);
			if(pathLength == 1) {
				targets.add(adjCell);
			} else {
				findAllTargets(adjCell, pathLength-1);
			}
			visited.remove(adjCell);
			}
		}
	}
	
	
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
