/*
 * @author John Walter
 * @author Hunter Sitki
 */

package clueGame; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;



public class Board extends JPanel implements MouseListener{
	private BoardCell[][] grid;
	ArrayList<String[]> roomRows;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private List<Player> players = new ArrayList<>();
	private List<String> weapons = new ArrayList<>();
	private List<String> rooms = new ArrayList<>();
	private List<Card> deck = new ArrayList<>();
	private List<Card> shuffledDeck;
	private List<Card> suggestibleWeapons = new ArrayList<>();
	private List<Card> suggestiblePlayers = new ArrayList<>();
	private List<Card> suggestibleRooms = new ArrayList<>();
	private List<Card> roomsInPlay = new ArrayList<>();
	private List<Card> weaponsInPlay = new ArrayList<>();
	private List<Card> playersInPlay = new ArrayList<>();
	private List<BoardCell> allCells = new ArrayList<>();
	private static int numColumns, numRows, totalCells;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private Map<String, Card> roomNameToCardMap;
	private Solution solution;
	private static final int TOT_PLAYERS = 6;
	public static final Color PURPLE = new Color(102,0,153);
	public static final Color LIGHT_ORANGE = new Color (255,153,0);
	private int currentPlayer = 0;
	private int cellWidth;
	private int cellHeight;
	private boolean playerDone;
	private String currentSuggestion;
	private String suggestionResult;

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
		roomsInPlay.clear();
		playersInPlay.clear();
		weaponsInPlay.clear();
		roomRows = new ArrayList<>();
		loadConfigFiles();

		if (!playersInPlay.isEmpty()) { //to allow for 306 tests to run
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
			System.err.println("Error: file not found");
			e1.printStackTrace();
		} catch (BadConfigFormatException e1) {
			System.err.println("Error: bad config format");
			e1.printStackTrace();
		}
		try {
			loadLayoutConfig();
		} catch (FileNotFoundException e1) {
			System.err.println("Error: file not found");
			e1.printStackTrace();
		} catch (BadConfigFormatException e1) {
			System.err.println("Error: bad config format");
			e1.printStackTrace();
		}

	}


	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
		roomNameToCardMap = new HashMap<>();
		File file = new File(setupConfigFile);
		try {
			Scanner sc = new Scanner(file);
			scanSetupFile(sc);
		} catch (FileNotFoundException e) {
			System.err.println("Error: file not found");
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
					createRoomAndCard(line);
				}
				else if (line.startsWith("Player")) {
					createCompAndHumanCard(line);
				}
				else if (line.startsWith("Weapon")) {
					createWeaponCard(line);
				}
				else {
					throw new BadConfigFormatException("Error, invalid layout");
				}
			}    
		}

	}

	private void createRoomAndCard(String line) {
		String[] roomInfo = line.split(", ");
		Room room = new Room();
		room.setName(roomInfo[1]);
		Character letter = roomInfo[2].charAt(0);
		roomMap.put(letter, room); 

		if (letter != 'W' && letter != 'X') {
			Card card = new Card(roomInfo[1],CardType.ROOM);
			roomsInPlay.add(card);
			rooms.add(card.getCardName());
			deck.add(card);
			suggestibleRooms.add(card);
			roomNameToCardMap.put(room.getName(), card);
		}
	}

	private void createWeaponCard(String line) {
		String[] weapon = line.split(", ");
		weapons.add(weapon[1]);

		Card card = new Card(weapon[1],CardType.WEAPON);
		weaponsInPlay.add(card);
		deck.add(card);
		suggestibleWeapons.add(card);
	}

	private void createCompAndHumanCard(String line) {
		String[] playerInfo = line.split(", ");
		if (playerInfo[3].equalsIgnoreCase("Computer")) {
			players.add(new ComputerPlayer(playerInfo[1], convertColor(playerInfo[2]), Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5])));

			Card card = new Card(playerInfo[1],CardType.PERSON);
			playersInPlay.add(card);
			deck.add(card);
			suggestiblePlayers.add(card);
		}
		else if (playerInfo[3].equalsIgnoreCase("Human")) {	//No difference right now between human and computer
			players.add(new HumanPlayer(playerInfo[1], convertColor(playerInfo[2]), Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5])));

			Card card = new Card(playerInfo[1],CardType.PERSON);
			playersInPlay.add(card);
			deck.add(card);
			suggestiblePlayers.add(card);
		}
	}

	public Color convertColor(String strColor) {
		Color c = Color.BLACK;
		switch(strColor) {
		case "Yellow":
			c = LIGHT_ORANGE;
			break;
		case "Red":
			c = Color.RED;
			break;
		case "White":
			c = Color.WHITE;
			break;
		case "Green":
			c = Color.GREEN;
			break;
		case "Blue":
			c = Color.BLUE;
			break;
		case "Purple":
			c = PURPLE;
			break;
		}
		return c;
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
		totalCells = numRows * numColumns;
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

					addDoorToAdjList(centerCell, k, l, adjCell);
				}
			}
		}
	}

	private void addDoorToAdjList(BoardCell centerCell, int k, int l, BoardCell adjCell) {
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
						cell.setRoomName(roomMap.get(cellIcon.charAt(0)).getName());
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
				allCells.add(grid[i][j]);
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
		visited = new HashSet<>();
		targets = new HashSet<>();
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
		else if (adjCell.isRoomCenter()) {
			targets.add(adjCell);
		}
		else if (adjCell.getOccupied()) {
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
		if (getCurrentPlayer().getPreviousCell() != null) {
			if (getCurrentPlayer().getPreviousCell() != getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn())) {
				targets.add(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn()));
				getCurrentPlayer().setPreviousCell(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn()));
			}
		}
	}

	/*
	 * *************************************************************************************************************************************************
	 * Card methods
	 * *************************************************************************************************************************************************
	 */
	public void deal() {
		pickSolutionCards();

		shuffledDeck = new ArrayList<>(deck);
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
		int playerCard = rand.nextInt(playersInPlay.size());
		int roomCard = rand.nextInt(roomsInPlay.size());
		int weaponCard = rand.nextInt(weaponsInPlay.size());

		solution = new Solution(playersInPlay.get(playerCard), roomsInPlay.get(roomCard), weaponsInPlay.get(weaponCard));

		deck.remove(playersInPlay.get(playerCard));
		playersInPlay.remove(playerCard);

		deck.remove(roomsInPlay.get(roomCard));
		roomsInPlay.remove(roomCard);

		deck.remove(weaponsInPlay.get(weaponCard));
		weaponsInPlay.remove(weaponCard);
	}

	/*
	 * *************************************************************************************************************************************************
	 * Player methods
	 * *************************************************************************************************************************************************
	 */
	public boolean checkAccusation(String string, String string2, String string3) {
		if (solution.getPerson().getCardName() == string && solution.getRoom().getCardName() == string2 && solution.getWeapon().getCardName() == string3) {
			return true;
		}
		return false;
	}

	public Card handleSuggestion(Player suggestor, Card person, Card room, Card weapon) {
		String playerInQuestion = person.getCardName();
		
		
		for (Player x : players) {
			if (x.getName() == playerInQuestion) {
				x.setRow(suggestor.getRow());
				x.setColumn(suggestor.getColumn());
				repaint();
			}
		}
		
		currentSuggestion = (person.getCardName() + " in " + room.getCardName() + ", with the " + weapon.getCardName());
		
		int start = players.indexOf(suggestor);

		for (int i = start +1; i < players.size() + start; i++) {
			Player currPlayer = players.get(i%players.size());
			if (currPlayer.disproveSuggestion(person, room, weapon) == null) {
				suggestionResult = "No disproves";
			}
			else {
				if (suggestor.getIsHuman()) {
					
					
					for(Player p : players) {
						if (p.getName() == person.getCardName()) {
							p.setRow(suggestor.getRow());
							p.setColumn(suggestor.getColumn());
						}
					}
					
					
					
					JTextArea msg = new JTextArea(currPlayer.disproveSuggestion(person, room, weapon).getCardName() + " from " + currPlayer.getName());
					msg.setLineWrap(true);
					
					JOptionPane.showMessageDialog(null, msg);
				}
				else {
					suggestionResult = "Suggestion disproved by " + currPlayer.getName();
				}
				
				return currPlayer.disproveSuggestion(person, room, weapon);
			}
		}
		return null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int frameWidth = super.getWidth();
		int frameHeight = super.getHeight();
		cellWidth = frameWidth/numColumns;
		cellHeight = frameHeight/numRows;

		//Will keep the boardcells square
		if(cellWidth < cellHeight) {
			cellHeight = cellWidth;
		} else if (cellHeight < cellWidth) {
			cellWidth = cellHeight;
		}


		// Draw the Board squares
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				grid[i][j].draw(cellWidth, cellHeight, g);
			}
		}

		// Draw the Players
		for(int i = 0; i < players.size(); i++) {
			
			int j = 2;
			for(Player p : players) {
				if(p.getRow() == players.get(i).getRow() && p.getColumn() == players.get(i).getColumn() && !p.shifted() && players.get(i) != p) {
					p.drawShifted(cellWidth, cellHeight, j, g);
					p.notShifted();
					j++;
				} else {
					players.get(i).draw(cellWidth, cellHeight, g);
				}
				
			
			}
		}
		
	}

	/*
	 * Mouse event listeners
	 */

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {

		if(getCurrentPlayer().getIsHuman()) {

			
			for (BoardCell x : allCells) {
				if (x.isTarget() && x.containsClick(e.getX(), e.getY())) {
					getCurrentPlayer().setIsFinished(false);
					
					getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn()).setOccupied(false);			
					getCurrentPlayer().setRow(x.getRow());
					getCurrentPlayer().setColumn(x.getCol());
					getCurrentPlayer().setPreviousCell(getCell(getCurrentPlayer().getRow(),getCurrentPlayer().getColumn()));
					getCell(x.getRow(), x.getCol()).setOccupied(true);
					removeTargets();
					getCurrentPlayer().setIsFinished(true);

					if (x.isRoom()) {
						
						JFrame suggestionFrame = new JFrame("Make a suggestion");
						suggestionFrame.setVisible(true);
						suggestionFrame.setSize(400,200);
						suggestionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						JPanel panel = new JPanel();
						panel.setLayout(new GridLayout(0,2));
						suggestionFrame.add(panel);
						
						JLabel roomLabel = new JLabel("Current room:" );
						panel.add(roomLabel);
						JLabel currentRoom = new JLabel(getRoom(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn())).getName());
						currentRoom.setBorder(BorderFactory.createLineBorder(Color.black));
						panel.add(currentRoom);
						
						JLabel personLabel = new JLabel("Person:" );
						panel.add(personLabel);
						String[] personChoices = new String[getPlayers().size()];
						for(int i = 0; i < getPlayers().size(); i++) {
							personChoices[i] = getPlayers().get(i).getName();
						}
						JComboBox<String> choosePerson = new JComboBox<String>(personChoices);
						choosePerson.setBorder(BorderFactory.createLineBorder(Color.black));
						panel.add(choosePerson);
						
						JLabel weaponLabel = new JLabel("Weapon:" );
						panel.add(weaponLabel);
						String[] weaponChoices = new String[getWeapons().size()];
						for(int i = 0; i < getWeapons().size(); i++) {
							weaponChoices[i] = getWeapons().get(i);
						}
						JComboBox<String> chooseWeapon = new JComboBox<String>(weaponChoices);
						chooseWeapon.setBorder(BorderFactory.createLineBorder(Color.black));
						panel.add(chooseWeapon);
						
						JButton suggest = new JButton ("Suggest");
						JButton cancel = new JButton ("Cancel");
						
						panel.add(suggest);
						panel.add(cancel);
						
						SuggestListener suggestButton = new SuggestListener(choosePerson, getRoom(getCell(getCurrentPlayer().getRow(), getCurrentPlayer().getColumn())).getName(), chooseWeapon, suggestionFrame);
						suggest.addActionListener(suggestButton);
						CancelListener cancelButton = new CancelListener(suggestionFrame);
						cancel.addActionListener(cancelButton);
						
					}
					
					
					playerDone = true;
					break;
				} else if (!x.isTarget() && x.containsClick(e.getX(), e.getY())){
					JOptionPane.showMessageDialog(null, "Please select a highlighted target");
					
				}
			}
		

			repaint();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {}

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
	
	public List<String> getWeapons() {
		return weapons;
	}

	public List<String> getRooms() {
		return rooms;
	}
	public List<Card> getDeck() {
		return deck;
	}
	public List<Card> getShuffledDeck() {
		return shuffledDeck;
	}
	public List<Card> getPlayerCards(){
		return playersInPlay;
	}
	public List<Card> getWeaponCards(){

		return weaponsInPlay;
	}
	public List<Card> getRoomCards(){
		return roomsInPlay;
	}
	public List<Card> getSuggestiblePlayerCards(){
		return suggestiblePlayers;
	}
	public List<Card> getSuggestibleWeaponCards(){
		return suggestibleWeapons;
	}
	public List<Card> getSuggestibleRoomCards(){
		return suggestibleRooms;
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

	public Card getCardFromRoom(String n) {
		return roomNameToCardMap.get(n);
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
	public Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}
	public void nextPlayer() {
		currentPlayer += 1;
		currentPlayer = currentPlayer % 6;
	}
	public int getCellWidth() {
		return cellWidth;
	}
	public int getCellHeight() {
		return cellHeight;
	}
	public boolean getPlayerDone() {
		return playerDone;
	}
	public void setPlayerDone(boolean done) {
		playerDone = done;
	}

	public void removeTargets() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				grid[i][j].setTarget(false);
			}
		}
	}

	public void flagTargets() {
		if(targets.size() > 0) {
			for(BoardCell x : targets) {
				for(int i = 0; i < numRows; i++) {
					for(int j = 0; j < numColumns; j++) {
						if(x.getRow() == grid[i][j].getRow() && x.getCol() == grid[i][j].getCol()) {
							grid[i][j].setTarget(true);
						}
					}
				}

			}
		}
		
	}
	
	public String getCurrentSuggestion() {
		return currentSuggestion;
	}
	public String getSuggestionResult() {
		return suggestionResult;
	}
	private class CancelListener implements ActionListener{
		JFrame frame;
		
		public CancelListener(JFrame frame) {
			this.frame = frame;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
	
	private class SuggestListener implements ActionListener{
		JComboBox person;
		String room;
		JComboBox weapon;
		JFrame frame;
		Card sPerson;
		Card sRoom;
		Card sWeapon;

		
		public SuggestListener(JComboBox person, String room, JComboBox weapon, JFrame frame) {
			this.person = person;
			this.room = room;
			this.weapon = weapon;
			this.frame = frame;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Card x : suggestiblePlayers) {
				if ((String) person.getSelectedItem() == x.getCardName()) {
					sPerson = x;
				}
			}
			for (Card x : suggestibleRooms) {
				if (room == x.getCardName()) {
					sRoom = x;
				}
			}
			for (Card x : suggestibleWeapons) {
				if ((String) weapon.getSelectedItem() == x.getCardName()) {
					sWeapon = x;
				}
			}
				
			
			
			Card returnCard = handleSuggestion(getCurrentPlayer(), sPerson, sRoom, sWeapon);
			if (returnCard != null) {
				getCurrentPlayer().updateSeenCards(returnCard);
			}
			else {
				JTextArea msg = new JTextArea("No disproves");
				msg.setLineWrap(true);
				
				JOptionPane.showMessageDialog(null, msg);
			}
			
			ClueGame.updateCardPanel();
			
			frame.dispose();
		}
	}
	
	public void clearSuggestionsForTesting() {
		suggestiblePlayers.clear();
		suggestibleWeapons.clear();
		suggestibleRooms.clear();
	}
	
	public void addSuggestiblePlayer(Card c){
		suggestiblePlayers.add(c);
	}
	public void addSuggestibleRoom(Card c){
		suggestibleRooms.add(c);
	}
	public void addSuggestibleWeapon(Card c){
		suggestibleWeapons.add(c);
	}
}


