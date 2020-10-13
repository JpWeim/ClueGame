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

public class Board {
	private BoardCell[][] grid;
	ArrayList<String[]> roomRows = new ArrayList<String[]>();
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


	public void loadConfigFiles() {
		
	}
	
	/*
	 * Scan in setup file. Scans through file, ignoring comment lines
	 * noted by "//". Splits the non-comment lines into arrays of strings,
	 * creates a new empty Room for testing, and adds the letter held at the end 
	 * of the array casted to a char and the Room object into the map
	 */
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		roomMap = new HashMap<Character, Room>();
		File file = new File(setupConfigFile);
		try {
			Scanner sc = new Scanner(file);
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Scans through the file, converts every line into an array using
	 * split acting on ",", and adds the array to an array list.
	 * Then takes the first row and measures how long it is. Then compares
	 * every other row in terms of length. If one row is not the same length,
	 * throws a BadConfigFormatException.
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
		for (String[] x : roomRows) {
			if (rowLength != x.length) {
				throw new BadConfigFormatException("Error, invalid layout");
			}
		}
		
		
		for (String[] x : roomRows) {
			for (int i = 0; i < x.length; i++) {
				char roomLetter = x[i].charAt(0);
				if (!(roomMap.containsKey(roomLetter))){
					throw new BadConfigFormatException("Error, invalid room");
				}
			}
		}
		
		grid = new BoardCell[numRows][numColumns];
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
						roomMap.get(cellIcon.charAt(0)).setCenterCell(cell);
					}
					
					else if (cellIcon.contains("#")) {
						cell.setRoomLabel(true);
						roomMap.get(cellIcon.charAt(0)).setLabelCell(cell);
					}
				
					else {
						cell.setSecretPassage(cellIcon.charAt(1));
					}
				}
				
				grid[i][j] = cell;
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
		System.out.println(c);
		return roomMap.get(c);
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		//Temp code to allow tests to run
		targets = new HashSet<BoardCell>();
		return targets;
	}

	public void calcTargets(BoardCell cell, int i) {
		
	}

	public Set<BoardCell> getTargets() {
		//Temp code to allow tests to run
		targets = new HashSet<BoardCell>();
		return targets;
	}


}
