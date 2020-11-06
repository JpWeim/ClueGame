package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

class ComputerAITest {
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
		
		//@Test
		public void computerSuggestionTest() {
			
		}
		
		/*
		 * The first test creates a computer player just to the south of Hill and has a move of 3.
		 * This allows the computer to enter the room, so it does.
		 * The second test is the same as the first, but the player has Hill in its hand, so it does
		 * not move into Hill, and instead chooses a different spot.
		 * The third test puts a new player in the middle of a walkway with a move of 2. It cannot 
		 * reach any rooms, so it chooses a new cell randomly.
		 */
		@Test
		public void computerTargetTest() {
			board.calcTargets(board.getCell(4, 5), 3);
			Player comp = new ComputerPlayer("Mr. Green", Color.GREEN, 4,5);
			BoardCell target = board.getCell(1, 3);
			BoardCell test = comp.selectTargets();
			Assert.assertEquals(target, test);
			
			List<Card> rooms = board.getSuggestibleRoomCards();
			comp.updateHand(rooms.get(7));
			target = board.getCell(1, 3);
			test = comp.selectTargets();
			Assert.assertNotEquals(target, test);
			
			board.calcTargets(board.getCell(11, 7), 2);
			Player comp2 = new ComputerPlayer("Mrs. White", Color.WHITE, 11,7);
			BoardCell target1 = board.getCell(9, 7);
			BoardCell target2 = board.getCell(12, 8);
			BoardCell target3 = board.getCell(13, 7);
			BoardCell target4 = board.getCell(12, 6);
			int t1 = 0;
			int t2 = 0;
			int t3 = 0;
			int t4 = 0;
			
			for (int i = 0; i < 100; i++) {
				BoardCell walkway = comp2.selectTargets();
				if (walkway.equals(target1)) {
					t1++;
				}
				if (walkway.equals(target2)) {
					t2++;
				}
				if (walkway.equals(target3)) {
					t3++;
				}
				if (walkway.equals(target4)) {
					t4++;
				}
			}
			if (t1 < 5) {
				fail("Not random");
			}
			if (t2 < 5) {
				fail("Not random");
			}
			if (t3 < 5) {
				fail("Not random");
			}
			if (t4 < 5) {
				fail("Not random");
			}

		}
}
