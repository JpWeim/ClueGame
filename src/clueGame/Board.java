/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoard;
import experiment.TestBoardCell;

public class Board {
	private BoardCell[][] grid;
	ArrayList<String[]> roomRows;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private static int numColumns;
	private static int numRows;
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
	 * Calls loadSetupConfig and loadLayoutConfig with try/catch
	 */
	public void initialize() {
		roomRows = new ArrayList<String[]>();
		try {
			loadSetupConfig();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadConfigFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//Not sure what this is for...
	public void loadConfigFiles() {

	}


	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		roomMap = new HashMap<Character, Room>();
		File file = new File(setupConfigFile);
		try {
			Scanner sc = new Scanner(file);
			scanSetupIntoMap(sc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Scan in setup file. Scans through file, ignoring comment lines
	 * noted by "//". Splits the non-comment lines into arrays of strings,
	 * creates a new empty Room. Gets the last letter of the line and casts it
	 * into a char. This is the key, and the room is the value; these are put into
	 * the map
	 */
	private void scanSetupIntoMap(Scanner sc) throws BadConfigFormatException {
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(!line.startsWith("/")) {
				if (line.startsWith("Room") || line.startsWith("Space")) {
					String[] roomInfo = line.split(", ");
					Room room = new Room();
					room.setName(roomInfo[1]);
					Character letter = roomInfo[2].charAt(0);
					roomMap.put(letter, room);
				}
				else {
					throw new BadConfigFormatException("Error, invalid layout");
				}
			}    
		}
	}

	/*
	 * Scans through the file, converts every line into an array using
	 * split acting on ", ", and adds the array to an array list.
	 * Then takes the first row and measures how long it is. Then compares
	 * every other row in terms of length. If one row is not the same length,
	 * throws a BadConfigFormatException.
	 * Then checks to make sure the icons are all included in the map and 
	 * therefore, part of the Setup file.
	 * Finally, creates the grid, and fills it with cells.
	 */
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		File file = new File(layoutConfigFile);

		Scanner sc = new Scanner(file);

		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] split = line.split(",");
			roomRows.add(split);
		}

		int rowLength = roomRows.get(0).length;
		rowLengthCheck(rowLength);


		for (String[] x : roomRows) {
			iconCheck(x);
		}

		numColumns = rowLength;
		numRows = roomRows.size();

		grid = new BoardCell[numRows][numColumns];
		cellSetup();

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				centerRoomAdj(i, j);
				doorAdj(i, j);

				//TODO Refactor this, comment refactored code
				if (getCell(i,j).getInitial() == 'W' && !(getCell(i,j).isDoorway())) {
					if(i-1 >= 0) {
						if (getCell(i-1, j).getInitial() != 'X' && getCell(i-1,j).getInitial() == 'W') {
							getCell(i,j).addAdj(getCell(i-1, j));
						}
					}
					if(i+1 < Board.numRows) {
						if (getCell(i+1, j).getInitial() != 'X' && getCell(i+1,j).getInitial() == 'W') {
							getCell(i,j).addAdj(getCell(i+1, j));
						}
					}
					if(j-1 >= 0) {
						if (getCell(i, j-1).getInitial() != 'X' && getCell(i,j-1).getInitial() == 'W') {
							getCell(i,j).addAdj(getCell(i, j-1));
						}
					}
					if(j+1 < Board.numColumns) {
						if (getCell(i, j+1).getInitial() != 'X' && getCell(i,j+1).getInitial() == 'W') {
							getCell(i,j).addAdj(getCell(i, j+1));
						}
					}

				}
			}
		}
	}

	private void doorAdj(int i, int j) {
		if (getCell(i,j).isDoorway()) {
			if (getCell(i,j).getDoorDirection() == DoorDirection.UP) {
				getCell(i,j).addAdj(roomMap.get(getCell(i-1,j).getInitial()).getCenterCell());

				if(getCell(i+1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i+1,j));	
				}

				if(getCell(i,j+1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j+1));	
				}

				if(getCell(i,j-1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j-1));	
				}
			}

			if (getCell(i,j).getDoorDirection() == DoorDirection.DOWN) {
				getCell(i,j).addAdj(roomMap.get(getCell(i+1,j).getInitial()).getCenterCell());

				if(getCell(i-1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i-1,j));	
				}

				if(getCell(i,j+1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j+1));	
				}

				if(getCell(i,j-1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j-1));	
				}
			}

			if (getCell(i,j).getDoorDirection() == DoorDirection.LEFT) {
				getCell(i,j).addAdj(roomMap.get(getCell(i,j-1).getInitial()).getCenterCell());

				if(getCell(i,j+1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j+1));	
				}

				if(getCell(i+1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i+1,j));	
				}

				if(getCell(i-1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i-1,j));	
				}
			}

			if (getCell(i,j).getDoorDirection() == DoorDirection.RIGHT) {
				getCell(i,j).addAdj(roomMap.get(getCell(i,j+1).getInitial()).getCenterCell());

				if(getCell(i+1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i+1,j));	
				}

				if(getCell(i-1,j).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i-1,j));	
				}

				if(getCell(i,j-1).getInitial() =='W'){
					getCell(i,j).addAdj(getCell(i,j-1));	
				}
			}
		}
	}

	private void centerRoomAdj(int i, int j) {
		if (getCell(i,j).isRoomCenter()) {
			for(int k = 0; k < numRows; k++) {
				for(int l = 0; l < numColumns; l++) {

					if(getCell(k,l).getInitial() == getCell(i,j).getInitial() && getCell(k,l).hasSecretPassage()) {
						getCell(i,j).addAdj(roomMap.get(getCell(k,l).getSecretPassage()).getCenterCell());
					}

					if (getCell(k,l).isDoorway()) {
						if (getCell(k,l).getDoorDirection() == DoorDirection.UP) {
							if(getCell(k-1,l).getInitial() == getCell(i,j).getInitial()) {
								getCell(i,j).addAdj(getCell(k,l));
							}
						}

						if (getCell(k,l).getDoorDirection() == DoorDirection.DOWN) {
							if(getCell(k+1,l).getInitial() == getCell(i,j).getInitial()) {
								getCell(i,j).addAdj(getCell(k,l));
							}
						}

						if (getCell(k,l).getDoorDirection() == DoorDirection.RIGHT) {
							if(getCell(k,l+1).getInitial() == getCell(i,j).getInitial()) {
								getCell(i,j).addAdj(getCell(k,l));
							}
						}

						if (getCell(k,l).getDoorDirection() == DoorDirection.LEFT) {
							if(getCell(k,l-1).getInitial() == getCell(i,j).getInitial()) {
								getCell(i,j).addAdj(getCell(k,l));
							}
						}
					}
				}
			}
		}
	}

	/*
	 * Two for loops to iterate through the 2d board. Sets the cell's initial, checks to see
	 * if the cell is a door, a center cell, a label cell, or a secret passageway, and assigns
	 * it as such. Finally puts the cell into the board.  
	 */
	private void cellSetup() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				BoardCell cell = new BoardCell(i, j);

				String cellIcon = roomRows.get(i)[j];
				cell.setInitial(cellIcon.charAt(0));
				if (cellIcon.length() == 2) {
					if (cellIcon.contains("<")) {
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.LEFT);
					}
					else if (cellIcon.contains(">")) {
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.RIGHT);
					}
					else if (cellIcon.contains("v")) {
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.DOWN);
					}
					else if (cellIcon.contains("^")) {
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.UP);
					}
					else if (cellIcon.contains("*")) {
						cell.setRoomCenter(true);
						roomMap.get(cellIcon.charAt(0)).setCenterCell(cell); //Unsure if this is the correct code, works
						//for tests, but may have to revisit
					}
					else if (cellIcon.contains("#")) {
						cell.setRoomLabel(true);
						roomMap.get(cellIcon.charAt(0)).setLabelCell(cell); //see above
					}
					else {
						cell.setSecretPassage(cellIcon.charAt(1));
						cell.setHasSecretPassage(true);
					}
				}
				if (!(cellIcon.contains("W")) && !(cellIcon.contains("X"))) {
					cell.setIsRoom(true);
				}
				

				grid[i][j] = cell;
			}
		}
	}

	//Checks to make sure icons are valid and in the map
	private void iconCheck(String[] x) throws BadConfigFormatException {
		for (int i = 0; i < x.length; i++) {
			char roomLetter = x[i].charAt(0);
			if (!(roomMap.containsKey(roomLetter))){
				throw new BadConfigFormatException("Error, invalid room");
			}
		}
	}

	//Makes sure every row is the same length
	private void rowLengthCheck(int rowLength) throws BadConfigFormatException {
		for (String[] x : roomRows) {
			if (rowLength != x.length) {
				throw new BadConfigFormatException("Error, invalid layout");
			}
		}
	}

	/*
	 * Sets variables to file names, needed to add "data/"
	 * for the program to find the correct path 
	 */
	public void setConfigFiles(String board, String setup) {
		board = "data/" + board;
		setup = "data/" + setup;
		layoutConfigFile = board;
		setupConfigFile = setup;
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
		return roomMap.get(c);
	}

	public Room getRoom(BoardCell cell) {
		char c = cell.getInitial();
		return roomMap.get(c);
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return (getCell(i,j).adjList);
	}


	/*
	 * Sets up for recursive findAllTargets
	 * Instantiates the sets and adds the starting cell
	 * to visited so it does not loop back, then calls
	 * findAllTargets
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
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
	private void findAllTargets(BoardCell startCell, int pathLength) {
		for(BoardCell adjCell : startCell.getAdjList()) {
			if(visited.contains(adjCell)) {
			}
			else if (adjCell.isRoomCenter() == true) {
	
				targets.add(adjCell);
			}
			else if (adjCell.getOccupied() == true) {
			}
			else {
			visited.add(adjCell);
			if(pathLength == 1) {
				if (adjCell.isDoorway()) {
					for (BoardCell x : adjCell.adjList) {
						if (x.isRoomCenter()) {
							targets.add(x);
						}
					}
				}
				targets.add(adjCell);

			} 
			else {

				findAllTargets(adjCell, pathLength-1);
			}
			visited.remove(adjCell);
			}
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}


}
