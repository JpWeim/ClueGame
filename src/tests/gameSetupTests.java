package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Assert;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.Player;

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

	@Test
	public void testPlayers() {
		Set<Player> testList = board.getPlayers();
		assertEquals(6, testList.size());
	}
	
	@Test
	public void testDeck() {
		Set<Card> testDeck = board.getDeck();
		Assert.assertEquals(21, testDeck.size());
		Assert.assertEquals(6, board.getPlayerCards().size());
		Assert.assertEquals(9, board.getRoomCards().size());
		Assert.assertEquals(6, board.getWeaponCards().size());
	}

}
