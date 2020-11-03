/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame; 

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Board {
	private BoardCell[][] grid;
	ArrayList<String[]> roomRows;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private List<Player> players = new ArrayList<Player>();//TODO initialize these in code rather than at start
	private List<String> weapons = new ArrayList<String>();
	private List<Card> deck = new ArrayList<Card>();
	private List<Card> shuffledDeck;
	private List<Card> totalRooms = new ArrayList<Card>();
	private List<Card> totalWeapons = new ArrayList<Card>();
	private List<Card> totalPlayers = new ArrayList<Card>();
	private static int numColumns;
	private static int numRows;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Solution solution;
	private static final int TOT_PLAYERS = 6;

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
 * *************************************************************************************************************************************************
 * Initialize methods
 * *************************************************************************************************************************************************
 */

	/*
	 * Calls loadSetupConfig and loadLayoutConfig with try/catch
	 */
	public void initialize() {
		//clear to allow tests to work because we are using one instance of the board
		deck.clear();
		players.clear();
		totalRooms.clear();
		totalPlayers.clear();
		totalWeapons.clear();
		roomRows = new ArrayList<String[]>();
		loadConfigFiles();

		if (!totalPlayers.isEmpty()) {
			deal();
		}
	}
	
	/*
	 * Calls loadSetupConfig and loadLayoutConfig
	 */
	public void loadConfigFiles() {
		try {
			loadSetupConfig();
		} catch (FileNotFoundException e1) {
			// TODO Custom exception
			e1.printStackTrace();
		} catch (BadConfigFormatException e1) {
			// TODO Custom exception
			e1.printStackTrace();
		}
		try {
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			// TODO Custom exception
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			// TODO Custom exception
			e.printStackTrace();
		}
		
	}


	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<Character, Room>();
		File file = new File(setupConfigFile);
		try {
			Scanner sc = new Scanner(file);
			scanSetupFile(sc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	/*
	 * Scan in setup file. Scans through file, ignoring comment lines
	 * noted by "//". Splits the non-comment lines into arrays of strings,
	 * creates a new empty Room. Gets the last letter of the line and casts it
	 * into a char. This is the key, and the room is the value; these are put into
	 * the map
	 * If the line is a player, adds a new computer or human player depending to the 
	 * set of players. A player has a name, color, and starting coords
	 * If the line is a weapon, adds it to a set
	 * 
	 * After each line is added to its respective sets/maps, creates a card of said type
	 * and puts it in the deck
	 * 
	 */
	private void scanSetupFile(Scanner sc) throws BadConfigFormatException {
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(!line.startsWith("/")) {
				if (line.startsWith("Room") || line.startsWith("Space")) {
					String[] roomInfo = line.split(", ");
					Room room = new Room();
					room.setName(roomInfo[1]);
					Character letter = roomInfo[2].charAt(0);
					roomMap.put(letter, room);
					
					if (letter != 'W' && letter != 'X') {
						Card card = new Card(roomInfo[1],CardType.ROOM);
						totalRooms.add(card);
						deck.add(card);
						
					}
				}
				else if (line.startsWith("Player")) {
					String[] playerInfo = line.split(", ");
					if (playerInfo[3].equalsIgnoreCase("Computer")) {
						players.add(new ComputerPlayer(playerInfo[1], Color.getColor(playerInfo[2]), Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5])));
						
						Card card = new Card(playerInfo[1],CardType.PERSON);
						totalPlayers.add(card);
						deck.add(card);
						
					}
					else if (playerInfo[3].equalsIgnoreCase("Human")) {	//No difference right now between human and computer
						players.add(new HumanPlayer(playerInfo[1], Color.getColor(playerInfo[2]), Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5])));
						
						Card card = new Card(playerInfo[1],CardType.PERSON);
						totalPlayers.add(card);
						deck.add(card);
						
					}
				}
				else if (line.startsWith("Weapon")) {
					String[] weapon = line.split(", ");
					weapons.add(weapon[1]);
					
					Card card = new Card(weapon[1],CardType.WEAPON);
					totalWeapons.add(card);
					deck.add(card);
					
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
				walkwayAjd(i, j);
			}
		}
	}

	
/*
 * *************************************************************************************************************************************************
 * Adjacent methods
 * *************************************************************************************************************************************************
 */
	/*
	 * Loops through board adding secret passage (if applicable) and doorways to 
	 * the center room adj list
	 */
	private void centerRoomAdj(int i, int j) {
		BoardCell centerCell = getCell(i,j);
		
		if (centerCell.isRoomCenter()) {
			for(int k = 0; k < numRows; k++) {
				for(int l = 0; l < numColumns; l++) {
					BoardCell adjCell = getCell(k,l);

					if(adjCell.getInitial() == centerCell.getInitial() && getCell(k,l).hasSecretPassage()) {
						centerCell.addAdj(roomMap.get(adjCell.getSecretPassage()).getCenterCell());
					}

					if (adjCell.isDoorway()) {
						if (adjCell.getDoorDirection() == DoorDirection.UP) {
							if(getCell(k-1,l).getInitial() == centerCell.getInitial()) {
								centerCell.addAdj(adjCell);
							}
						}

						if (adjCell.getDoorDirection() == DoorDirection.DOWN) {
							if(getCell(k+1,l).getInitial() == centerCell.getInitial()) {
								centerCell.addAdj(adjCell);
							}
						}

						if (adjCell.getDoorDirection() == DoorDirection.RIGHT) {
							if(getCell(k,l+1).getInitial() == centerCell.getInitial()) {
								centerCell.addAdj(adjCell);
							}
						}

						if (adjCell.getDoorDirection() == DoorDirection.LEFT) {
							if(getCell(k,l-1).getInitial() == centerCell.getInitial()) {
								centerCell.addAdj(adjCell);
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * Adds cells around doorway
	 * Makes sure the cell being added is also a walkway or doorway
	 */
	
	private void doorAdj(int i, int j) {
		BoardCell doorCell = getCell(i,j);
		
		if (doorCell.isDoorway()) {
			if (doorCell.getDoorDirection() == DoorDirection.UP) {
				doorCell.addAdj(roomMap.get(getCell(i-1,j).getInitial()).getCenterCell());

				if(getCell(i+1,j).isWalkway() || getCell(i+1,j).isDoorway()){
					doorCell.addAdj(getCell(i+1,j));	
				}

				if(getCell(i,j+1).isWalkway() || getCell(i,j+1).isDoorway()){
					doorCell.addAdj(getCell(i,j+1));	
				}

				if(getCell(i,j-1).isWalkway() || getCell(i,j-1).isDoorway()){
					doorCell.addAdj(getCell(i,j-1));	
				}
			}

			if (doorCell.getDoorDirection() == DoorDirection.DOWN) {
				doorCell.addAdj(roomMap.get(getCell(i+1,j).getInitial()).getCenterCell());

				if(getCell(i-1,j).isWalkway() || getCell(i-1,j).isDoorway()){
					doorCell.addAdj(getCell(i-1,j));	
				}

				if(getCell(i,j+1).isWalkway() || getCell(i,j+1).isDoorway()){
					doorCell.addAdj(getCell(i,j+1));	
				}

				if(getCell(i,j-1).isWalkway() || getCell(i,j-1).isDoorway()){
					doorCell.addAdj(getCell(i,j-1));	
				}
			}

			if (doorCell.getDoorDirection() == DoorDirection.LEFT) {
				doorCell.addAdj(roomMap.get(getCell(i,j-1).getInitial()).getCenterCell());

				if(getCell(i,j+1).isWalkway() || getCell(i,j+1).isDoorway()){
					doorCell.addAdj(getCell(i,j+1));	
				}

				if(getCell(i+1,j).isWalkway() || getCell(i+1,j).isDoorway()){
					doorCell.addAdj(getCell(i+1,j));	
				}

				if(getCell(i-1,j).isWalkway() || getCell(i-1,j).isDoorway()){
					doorCell.addAdj(getCell(i-1,j));	
				}
			}

			if (doorCell.getDoorDirection() == DoorDirection.RIGHT) {
				doorCell.addAdj(roomMap.get(getCell(i,j+1).getInitial()).getCenterCell());

				if(getCell(i+1,j).isWalkway() || getCell(i+1,j).isDoorway()){
					doorCell.addAdj(getCell(i+1,j));	
				}

				if(getCell(i-1,j).isWalkway() || getCell(i-1,j).isDoorway()){
					doorCell.addAdj(getCell(i-1,j));	
				}

				if(getCell(i,j-1).isWalkway() || getCell(i,j-1).isDoorway()){
					doorCell.addAdj(getCell(i,j-1));	
				}
			}
		}
	}
	
	/*
	 * Added cells around a walkway cell to its adjacency list, making sure
	 * they are walkways
	 */
	private void walkwayAjd(int i, int j) {
		BoardCell walkwayCell = getCell(i,j);
		
		if (walkwayCell.isWalkway()) {
			if(i-1 >= 0) {
				if (getCell(i-1, j).getInitial() != 'X' && getCell(i-1,j).getInitial() == 'W') {
					walkwayCell.addAdj(getCell(i-1, j));
				}
			}
			if(i+1 < Board.numRows) {
				if (getCell(i+1, j).getInitial() != 'X' && getCell(i+1,j).getInitial() == 'W') {
					walkwayCell.addAdj(getCell(i+1, j));
				}
			}
			if(j-1 >= 0) {
				if (getCell(i, j-1).getInitial() != 'X' && getCell(i,j-1).getInitial() == 'W') {
					walkwayCell.addAdj(getCell(i, j-1));
				}
			}
			if(j+1 < Board.numColumns) {
				if (getCell(i, j+1).getInitial() != 'X' && getCell(i,j+1).getInitial() == 'W') {
					walkwayCell.addAdj(getCell(i, j+1));
				}
			}

		}
	}

	
/*
 * *************************************************************************************************************************************************
 * Cell methods
 * *************************************************************************************************************************************************
 */
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
					
					switch(cellIcon.charAt(1)) {
					case '<':
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.LEFT);
						break;
					case '>':
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.RIGHT);
						break;
					case 'v':
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.DOWN);
						break;
					case '^':
						cell.setDoorway(true);
						cell.setDoorDirection(DoorDirection.UP);
						break;
					case '*':
						cell.setRoomCenter(true);
						roomMap.get(cellIcon.charAt(0)).setCenterCell(cell);
						break;
					case '#':
						cell.setRoomLabel(true);
						roomMap.get(cellIcon.charAt(0)).setLabelCell(cell);
						break;
					default:
						cell.setSecretPassage(cellIcon.charAt(1));
						cell.setHasSecretPassage(true);
					}
					
				}
				if (!(cellIcon.contains("W")) && !(cellIcon.contains("X"))) {
					cell.setIsRoom(true);
				}
				if (cellIcon.contains("W") && cellIcon.length() == 1) {
					cell.setIsWalkway(true);
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
 * *************************************************************************************************************************************************
 * Target methods
 * *************************************************************************************************************************************************
 */
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
	* */
	private void findAllTargets(BoardCell startCell, int pathLength) {
		for(BoardCell adjCell : startCell.getAdjList()) {
			addCorrectCells(pathLength, adjCell);
		}
	}

	/*
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
	private void addCorrectCells(int pathLength, BoardCell adjCell) {
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
			targets.add(adjCell);
		} 
		else {
			findAllTargets(adjCell, pathLength-1);
		}
		visited.remove(adjCell);
		}
	}
	
/*
 * *************************************************************************************************************************************************
 * Card methods
 * *************************************************************************************************************************************************
 */
	public void deal() {
		pickSolutionCards();
		
		shuffledDeck = new ArrayList<Card>(deck);
		Collections.shuffle(shuffledDeck);
		
		int totalCards = shuffledDeck.size();
		for (int i = 0; i < totalCards; i++) {
			Player currPlayer = players.get(i%TOT_PLAYERS);
			Card nextCard = shuffledDeck.get(0);
			currPlayer.updateHand(nextCard);
			shuffledDeck.remove(nextCard);
		}

		
	}

	/*
	 * Generates 3 random numbers and picks three random cards out of the player cards, room cards, and weapon cards respectively
	 * Puts those three cards into the solution class and removes them from the total deck and their lists
	 */
	private void pickSolutionCards() {
		Random rand = new Random();
		int playerCard = rand.nextInt(totalPlayers.size());
		int roomCard = rand.nextInt(totalRooms.size());
		int weaponCard = rand.nextInt(totalWeapons.size());

		solution = new Solution(totalPlayers.get(playerCard), totalRooms.get(roomCard), totalWeapons.get(weaponCard));
		
		deck.remove(totalPlayers.get(playerCard));
		totalPlayers.remove(playerCard);

		deck.remove(totalRooms.get(roomCard));
		totalRooms.remove(roomCard);

		deck.remove(totalWeapons.get(weaponCard));
		totalWeapons.remove(weaponCard);
	}

	public boolean checkAccusation(Card person, Card room, Card weapon) {
		if (solution.getPerson().equals(person) && solution.getRoom().equals(room) && solution.getWeapon().equals(weapon)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void handleSuggestion(Card person, Card room, Card weapon) {
		
	}
	
/*
 * *************************************************************************************************************************************************
 * Getters and setters 
 * *************************************************************************************************************************************************
 */
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public List<Player> getPlayers(){
		return players;
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	public List<Card> getShuffledDeck() {
		return shuffledDeck;
	}
	public List<Card> getPlayerCards(){
		return totalPlayers;
	}
	public List<Card> getWeaponCards(){

		return totalWeapons;
	}
	public List<Card> getRoomCards(){
		return totalRooms;
	}

	public Solution getSolution() {
		return solution;
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
	
}
