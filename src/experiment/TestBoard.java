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
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//calcTargets and assign to targets
	}
	
	public Set<TestBoardCell> getTargets(){
		//calls calcTargets
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
