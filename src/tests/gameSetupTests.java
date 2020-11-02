package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.Assert;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

class gameSetupTests {
	
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
	 * Tests to make sure the correct number of players are in the
	 * list of Player objects, do not confuse with totalPlayerCards, which is a 
	 * list of Card objects representing players
	 */
	@Test
	public void testPlayers() {
		List<Player> testList = board.getPlayers();
		assertEquals(6, testList.size());
	}

	/*
	 * Tests to make sure the solution has the correct type of card
	 * for the person, the room, and the weapon
	 */
	@Test
	public void testSolution() {
		Solution testSolution = board.getSolution();
		Assert.assertTrue(testSolution.getPerson().getCardType() == CardType.PERSON);
		Assert.assertTrue(testSolution.getRoom().getCardType() == CardType.ROOM);
		Assert.assertTrue(testSolution.getWeapon().getCardType() == CardType.WEAPON);
	}
	
	
	/*
	 * Tests to make sure the correct number of cards are left in the total deck
	 * and the individual decks after creating the solution, ie. one of each type
	 * is missing
	 */
	@Test
	public void testDeck() {
		List<Card> testDeck = board.getDeck();
		Assert.assertEquals(18, testDeck.size());
		Assert.assertEquals(5, board.getPlayerCards().size());
		Assert.assertEquals(8, board.getRoomCards().size());
		Assert.assertEquals(5, board.getWeaponCards().size());
	}
	
	

}
