package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	/*
	 * Gets the 3 correct cards and tests them with out checkAccusation method which should return true
	 * Then gets an incorrect player, room, or answer card to make sure it returns false when a card
	 * is incorrect. Finally, tests 3 incorrect cards which should also return false
	 */
	@Test
	public void testAccusation() {
		Solution theAnswer = board.getSolution();
		String person = theAnswer.getPerson().getCardName();
		String room = theAnswer.getRoom().getCardName();
		String weapon = theAnswer.getWeapon().getCardName();
		Assert.assertTrue(board.checkAccusation(person, room, weapon));
		
		List<Card> players = board.getPlayerCards();
		person = players.get(0).getCardName();
		Assert.assertFalse(board.checkAccusation(person, room, weapon));
		
		person = theAnswer.getPerson().getCardName();
		List<Card> rooms = board.getRoomCards();
		room = rooms.get(0).getCardName();
		Assert.assertFalse(board.checkAccusation(person, room, weapon));
		
		person = theAnswer.getPerson().getCardName();
		room = theAnswer.getRoom().getCardName();
		List<Card> weapons = board.getWeaponCards();
		weapon = weapons.get(0).getCardName();
		Assert.assertFalse(board.checkAccusation(person, room, weapon));
		
		person = players.get(3).getCardName();
		room = rooms.get(5).getCardName();
		weapon = weapons.get(2).getCardName();
		Assert.assertFalse(board.checkAccusation(person, room, weapon));
	}
	
	/*
	 * Makes a new player with a hand of cards of each type. Each card is 
	 * suggested and then tested to see that it is or is not disproven by a
	 * player having the card corresponding to the suggestion
	 */
	@Test
	public void testDisprove() {
		Player human = new HumanPlayer("Miss Scarlett", Color.RED, 5, 2);
		List<Card> players = board.getPlayerCards();
		List<Card> rooms = board.getRoomCards();
		List<Card> weapons = board.getWeaponCards();
		
		human.updateHand(players.get(0));
		human.updateHand(rooms.get(0));
		human.updateHand(weapons.get(0));
		
		Assert.assertEquals(players.get(0), human.disproveSuggestion(players.get(0), rooms.get(1), weapons.get(1)));
		Assert.assertNull(human.disproveSuggestion(players.get(1),rooms.get(1), weapons.get(1)));
		
		Assert.assertEquals(rooms.get(0), human.disproveSuggestion(players.get(1), rooms.get(0), weapons.get(1)));
		Assert.assertNull(human.disproveSuggestion(players.get(1), rooms.get(1), weapons.get(1)));
		
		Assert.assertEquals(weapons.get(0), human.disproveSuggestion(players.get(1), rooms.get(1), weapons.get(0)));
		Assert.assertNull(human.disproveSuggestion(players.get(1), rooms.get(1), weapons.get(1)));
		
		
		int pCard = 0;
		int rCard = 0;
		int wCard = 0;
		for (int i = 0; i < 100; i++) {
			Card newCard = human.disproveSuggestion(players.get(0), rooms.get(0), weapons.get(0));
			if (newCard.getCardType() == CardType.PERSON){
				pCard++;
			}
			if (newCard.getCardType() == CardType.ROOM){
				rCard++;
			}
			if (newCard.getCardType() == CardType.WEAPON){
				wCard++;
			}
		}
		if (pCard < 1) {
			fail("Not random");
		}
		if (rCard < 1) {
			fail("Not random");
		}
		if (wCard < 1) {
			fail("Not random");
		}
	}
	
	@Test
	public void testSuggestion() {
		
		List<Player> playerObjects = board.getPlayers();
		
		Player human = playerObjects.get(0);
		Player comp1 = playerObjects.get(1);
		Player comp2 = playerObjects.get(2);
		Player comp3 = playerObjects.get(3);
		
		human.clearHand();
		comp1.clearHand();
		comp2.clearHand();
		comp3.clearHand();
		playerObjects.get(4).clearHand();
		playerObjects.get(5).clearHand();
		
		List<Card> players = board.getPlayerCards();
		List<Card> rooms = board.getRoomCards();
		List<Card> weapons = board.getWeaponCards();
		
		human.updateHand(players.get(0));
		human.updateHand(rooms.get(0));
		human.updateHand(weapons.get(0));
		
		comp1.updateHand(players.get(1));
		comp1.updateHand(rooms.get(1));
		comp1.updateHand(weapons.get(1));
		
		comp2.updateHand(players.get(2));
		comp2.updateHand(rooms.get(2));
		comp2.updateHand(weapons.get(2));
		
		comp3.updateHand(players.get(3));
		comp3.updateHand(rooms.get(3));
		comp3.updateHand(weapons.get(3));

		Assert.assertNull(board.handleSuggestion(human, players.get(4), rooms.get(4), weapons.get(4)));
		Assert.assertNull(board.handleSuggestion(human, players.get(0), rooms.get(0), weapons.get(0)));
		Assert.assertEquals(weapons.get(1), board.handleSuggestion(human, players.get(0), rooms.get(0), weapons.get(1)));
		Assert.assertEquals(rooms.get(3), board.handleSuggestion(human, players.get(0), rooms.get(3), weapons.get(0)));
		Assert.assertEquals(players.get(2), board.handleSuggestion(comp1, players.get(2), rooms.get(1), weapons.get(1)));
		Assert.assertEquals(weapons.get(2), board.handleSuggestion(comp1, players.get(1), rooms.get(3), weapons.get(2)));
	}
	
	
}



