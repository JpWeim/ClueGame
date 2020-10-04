package experiment;

 import java.util.*;

public class TestBoard {
	TestBoardCell[][] board = new TestBoardCell[4][4];
	Map<TestBoardCell, Set<TestBoardCell>> adjList = new HashMap<TestBoardCell, Set<TestBoardCell>>();
	public final static int ROW_SIZE = 4;
	public final static int COL_SIZE = 4;
	
	public TestBoard() {
		TestBoardCell[][] board = new TestBoardCell[ROW_SIZE][COL_SIZE];
		this.board = board;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				TestBoardCell cell = new TestBoardCell(i,j);
				board[i][j] = cell;
			}
		}
	}
	
	private void setAdjList() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				
			}
		}
	}
	
	public boolean validAdj(TestBoardCell cell) {
		if(cell.getRow() - 1 < 0) {
			return false;
		} else if (cell.getRow() + 1 >= ROW_SIZE) {
			return false;
		} else if (cell.getCol() - 1 < 0) {
			return false;
		} else if (cell.getCol() + 1 >= COL_SIZE) {
			return false;
		} else {
			return true;
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
