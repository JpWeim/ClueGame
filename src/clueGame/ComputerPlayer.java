package clueGame;

import java.awt.Color;
import java.util.List;
import java.util.Random;

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
	
	public void selectMove() {
		
	}

}
