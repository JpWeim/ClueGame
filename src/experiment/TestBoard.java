package experiment;

import java.util.ArrayList;
 import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	TestBoardCell[][] board = new TestBoardCell[4][4];

	public TestBoard() {
		TestBoardCell[][] board = new TestBoardCell[4][4];
		this.board = board;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				TestBoardCell cell = new TestBoardCell(i,j);
				board[i][j] = cell;
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
	}
	
	public Set<TestBoardCell> getTargets(){
		Set<TestBoardCell> temp = new HashSet<TestBoardCell>();
		return temp;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
}
