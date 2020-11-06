package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	private boolean isHuman = false; 

	public ComputerPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

	@Override
	public boolean getIsHuman() {
		return isHuman;
	}
	
	public Solution createSuggestion() {
		Board board = Board.getInstance();
		if (board.getCell(row, col).isRoom()) {
			List<Card> notSeenPlayers = board.getSuggestiblePlayerCards();
			List<Card> notSeenWeapons = board.getSuggestibleWeaponCards();
			List<Card> rooms = board.getSuggestibleRoomCards();
			
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
			
			Card roomInQuestion = null;
			
			for (Card x : rooms) {
				if (x.getCardName() == roomName) {
					roomInQuestion = x;
				}
			}
			
			Solution guess = new Solution (playerInQuestion, roomInQuestion, weaponInQuestion);
			return guess;
		}
		else {
			return null;
		}
	}
	
	public BoardCell selectTargets() {
		Board board = Board.getInstance();
		Set<BoardCell> targets = board.getTargets();
		List<BoardCell> possibleRooms = new ArrayList<BoardCell>();
		for (BoardCell x : targets) {
			if (x.isRoomCenter()) {
				Room currentRoom = board.getRoom(x);
				boolean seen = false;
				for (Card y : seenCards) {
					if (currentRoom.getName().equals(y.getCardName())) {
						seen = true;
					}
				}
				if (seen == false) {
					possibleRooms.add(x);
				}
			}
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
					return x;
				}
				i++;
			}
		}
		return null;
	}

}
