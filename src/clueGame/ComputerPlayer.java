package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	private boolean isHuman = false;
	String finalPerson;
	String finalRoom;
	String finalWeapon;
	

	public ComputerPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

	@Override
	public boolean getIsHuman() {
		return isHuman;
	}
	
	/*
	 * First checks to see if the current cell is a room, and if it is, gets all possible cards
	 * Goes through the cards in seenCards list and removes from correct list
	 * After that, chooses a random player and weapon card that it has not seen,
	 * creates a new solution using the chosen player, weapon, and current room cards and returns
	 */
	public Solution createSuggestion() {
		Board board = Board.getInstance();
		if (board.getCell(row, col).isRoom()) {
			List<Card> notSeenPlayers = new ArrayList<>(board.getSuggestiblePlayerCards());
			List<Card> notSeenWeapons = new ArrayList<>(board.getSuggestibleWeaponCards());
			//List<Card> rooms = board.getRoomCards();
			
			for (Card x : seenCards) {
				if (notSeenPlayers.contains(x)) {
					notSeenPlayers.remove(x);
				}
				if (notSeenWeapons.contains(x)) {
					notSeenWeapons.remove(x);
				}
			}
			
			Random r = new Random();
			Card playerInQuestion = notSeenPlayers.get(r.nextInt(notSeenPlayers.size()));
			Card weaponInQuestion = notSeenWeapons.get(r.nextInt(notSeenWeapons.size()));
			Room currentRoom = board.getRoom(board.getCell(row, col));
			String roomName = currentRoom.getName();
			Card roomInQuestion = board.getCardFromRoom(roomName);
			
			Solution guess = new Solution (playerInQuestion, roomInQuestion, weaponInQuestion);
			return guess;
		}
		else {
			return null;
		}
	}
	
	/*
	 * Gets the target set from board and iterates through it
	 * If the cell is a room center, it adds the respective card
	 * to a list if it has not seen it yet
	 * If there are possible rooms to enter that it has not seen,
	 * it will randomly choose from one of those rooms,
	 * else, it will pick a random board cell from the targets
	 */
	public BoardCell selectTargets() {
		Board board = Board.getInstance();
		Set<BoardCell> targets = board.getTargets();
		List<BoardCell> possibleRooms = new ArrayList<BoardCell>();
		if (targets.isEmpty()) {
			return null;
		}
		for (BoardCell x : targets) {
			checkSeenRoom(board, possibleRooms, x);
		}
		if (!possibleRooms.isEmpty()) {
			Random rand = new Random();
			return possibleRooms.get(rand.nextInt(possibleRooms.size()));
		}
		else {
			Random rand = new Random();
			int item = rand.nextInt(targets.size());
			int i = 0;
			for (BoardCell x : targets) {
				if (i == item) {
					setPreviousCell(board.getCell(x.getRow(), x.getCol()));
					return x;
				}
				i++;
			}
		}
		return null;
	}

	private void checkSeenRoom(Board board, List<BoardCell> possibleRooms, BoardCell x) {
		if (x.isRoomCenter()) {
			Room currentRoom = board.getRoom(x);
			boolean seen = false;
			for (Card y : seenCards) {
				if (currentRoom.getName() == (y.getCardName())) {
					seen = true;
				}
			}
			if (seen == false) {
				possibleRooms.add(x);
			}
		}
	}
	
	public boolean checkCards() {
		if (seenCards.size() == 18) {
			Board board = Board.getInstance();
			List<Card> notSeenPlayers = board.getSuggestiblePlayerCards();
			List<Card> notSeenWeapons = board.getSuggestibleWeaponCards();
			List<Card> notSeenRooms = board.getRoomCards();
			
			for (Card x : seenCards) {
				if (notSeenPlayers.contains(x)) {
					notSeenPlayers.remove(x);
				}
				if (notSeenWeapons.contains(x)) {
					notSeenWeapons.remove(x);
				}
				if (notSeenRooms.contains(x)) {
					notSeenRooms.remove(x);
				}
			}
			finalPerson = notSeenPlayers.get(0).getCardName();
			finalWeapon = notSeenWeapons.get(0).getCardName();
			finalRoom = notSeenRooms.get(0).getCardName();
			return true;
		}
		else {
			return false;
		}
	}

	public void setFinalPerson(String card) {
		finalPerson = card;
	}
	public String getFinalPerson() {
		return finalPerson;
	}

	public void setFinalWeapon(String card) {
		finalWeapon = card;
	}
	public String getFinalWeapon() {
		return finalWeapon;
	}

	public void setFinalRoom(String card) {
		finalRoom = card;
	}
	public String getFinalRoom() {
		return finalRoom;
	}

	@Override
	protected boolean getIsFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setIsFinished(boolean b) {
		// TODO Auto-generated method stub
		
	}


}
