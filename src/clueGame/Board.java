/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private static int numColumns = 24;
	private static int numRows = 25;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	
	
	private static Board theInstance = new Board();
	//constructor is private to ensure only one can be created
	private Board() {
		super();
	}
	
	//this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	
	/*
	 * Creates new board and fills it with cells
	 */
	public void initialize() {
		grid = new BoardCell[numRows][numColumns];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				BoardCell cell = new BoardCell(i, j);
				grid[i][j] = cell;
			}
		}

	}

	/*
	 * Stubs for JUnit tests
	 */
	public void loadConfigFiles() {
		
	}
	
	public void loadSetupConfig() {
		
	}
	public void loadLayoutConfig() {
		
	}
	
	public void setConfigFiles(String string, String string2) {

	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public Room getRoom(char c) {
		Room room = new Room();
		return room;
	}

	public Room getRoom(BoardCell cell) {
		Room room = new Room();
		return room;
	}


}
