package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

class BoardAdjTargetTest {

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
	/*
	 * Ensures when in a room, a player can only move to door
	 * or secret passage if it applies
	 * Marked light red
	 */
	public void testAdjacenciesRooms()
	{
		//Testing Periodic Table, one door, secret path to Green
		Set<BoardCell> testList = board.getAdjList(3, 19);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(22, 3)));
		assertTrue(testList.contains(board.getCell(6, 17)));
		
		//Testing Mines Market, three doors
		testList = board.getAdjList(3, 11);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(5, 8)));
		assertTrue(testList.contains(board.getCell(7, 11)));
		assertTrue(testList.contains(board.getCell(7, 12)));

		//Testing Brown, four doors
		testList = board.getAdjList(20, 12);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(19, 7)));
		assertTrue(testList.contains(board.getCell(16, 9)));
		assertTrue(testList.contains(board.getCell(6, 14)));
		assertTrue(testList.contains(board.getCell(6, 16)));
	}
	
	/*
	 * Tests to make sure players can move into a room
	 * when next to a door, as well as the surrounding tiles
	 * Marked light red
	 */
	@Test
	public void testAdjacencyDoor()
	{
		//Testing door entering brown from right side
		Set<BoardCell> testList = board.getAdjList(19, 16);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(20, 12)));
		assertTrue(testList.contains(board.getCell(20, 16)));
		assertTrue(testList.contains(board.getCell(18, 16)));
		
		//Testing door entering CTLM from top side
		testList = board.getAdjList(8, 17);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(8, 16)));
		assertTrue(testList.contains(board.getCell(8, 18)));
		assertTrue(testList.contains(board.getCell(7, 17)));
		assertTrue(testList.contains(board.getCell(11, 20)));

		//Testing door entering Hill from bottom side
		testList = board.getAdjList(4, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(4, 5)));
		assertTrue(testList.contains(board.getCell(4, 7)));
		assertTrue(testList.contains(board.getCell(5, 6)));
	}
	
	/*
	 * Testing different walkways, from one possible move to four
	 * Marked dark red
	 */
	@Test
	public void testAdjacencyWalkways()
	{
		//Starting location upper right, one possible move
		Set<BoardCell> testList = board.getAdjList(7, 24);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(7, 23)));
		
		//Between Trads and Elm, two possible moves
		testList = board.getAdjList(11, 5);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(11, 4)));
		assertTrue(testList.contains(board.getCell(11, 6)));

		//Between Elm and Green, three possible moves
		testList = board.getAdjList(17, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 1)));
		assertTrue(testList.contains(board.getCell(17, 3)));
		assertTrue(testList.contains(board.getCell(18, 2)));
		
		//Open space surrounded by walkways, four possible moves
		testList = board.getAdjList(7, 15);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(7, 14)));
		assertTrue(testList.contains(board.getCell(7, 16)));
		assertTrue(testList.contains(board.getCell(6, 15)));
		assertTrue(testList.contains(board.getCell(8, 15)));

	}
	
	/*
	 * Tests the situation where the player starts their
	 * turn in periodic
	 */
	@Test
	public void testTargetsFromPeriodic() {
		// test a roll of 1
		//Should end up with the door and the secret passage
		board.calcTargets(board.getCell(3, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(22, 3)));	
		
		// test a roll of 2
		//Should end up with 3 walkways and the secret passage
		board.calcTargets(board.getCell(3, 19), 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(22, 3)));		
		assertTrue(targets.contains(board.getCell(7, 19)));
		assertTrue(targets.contains(board.getCell(6, 16)));
		assertTrue(targets.contains(board.getCell(6, 18)));	
		
		// test a roll of 4
		//Should end up with 10 total, secret pathway, enter CTLM
		//and the rest walkways
		board.calcTargets(board.getCell(20, 19), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(22, 3)));
		assertTrue(targets.contains(board.getCell(11, 20)));
		assertTrue(targets.contains(board.getCell(6, 20)));
		assertTrue(targets.contains(board.getCell(8, 16)));
		assertTrue(targets.contains(board.getCell(11, 20)));
		assertTrue(targets.contains(board.getCell(4, 216)));
	}
	
	/*
	 * Tests where the target starts at a door
	 * Start at mines market left door
	 */
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		//Able to enter Market, rest are walkways
		board.calcTargets(board.getCell(5, 8), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(4, 8)));	
		assertTrue(targets.contains(board.getCell(6, 8)));	
		assertTrue(targets.contains(board.getCell(5, 7)));	

		// test a roll of 3
		//Able to enter Hill and Market, the rest are walkways
		board.calcTargets(board.getCell(8, 17), 3);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(10, 8)));
		assertTrue(targets.contains(board.getCell(7, 9)));
		assertTrue(targets.contains(board.getCell(4, 7)));
		
		// test a roll of 5
		//Able to enter Market, Hill, and Trads, rest are walkways
		board.calcTargets(board.getCell(8, 17), 5);
		targets= board.getTargets();
		assertEquals(18, targets.size());
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(8, 2)));
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(5, 3)));
		assertTrue(targets.contains(board.getCell(1, 7)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(16, 16), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(16, 15)));
		assertTrue(targets.contains(board.getCell(15, 16)));
		assertTrue(targets.contains(board.getCell(16, 17)));
		assertTrue(targets.contains(board.getCell(17, 16)));
		
		// test a roll of 3
		//Able to enter Alderson and Brown
		board.calcTargets(board.getCell(16, 16), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(16, 15)));
		assertTrue(targets.contains(board.getCell(15, 16)));
		assertTrue(targets.contains(board.getCell(16, 17)));
		assertTrue(targets.contains(board.getCell(17, 16)));
		assertTrue(targets.contains(board.getCell(20, 12)));
		assertTrue(targets.contains(board.getCell(21, 19)));


		
		// test a roll of 4
		//Able to enter Alderson and Brown
		board.calcTargets(board.getCell(16, 16), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(20, 12)));
		assertTrue(targets.contains(board.getCell(21, 19)));
		assertTrue(targets.contains(board.getCell(16, 20)));
		assertTrue(targets.contains(board.getCell(20, 16)));
		assertTrue(targets.contains(board.getCell(15, 13)));
		assertTrue(targets.contains(board.getCell(14, 14)));
	}
	
	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 3, 3 sides of player are blocked
		board.getCell(15, 10).setOccupied(true);
		board.getCell(16, 11).setOccupied(true);
		board.calcTargets(board.getCell(15, 11), 3);
		board.getCell(15, 10).setOccupied(false);
		board.getCell(16, 11).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(16, 13)));	
		assertTrue(targets.contains(board.getCell(15, 14)));	

	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(22, 3).setOccupied(true);
		board.getCell(23, 7).setOccupied(true);
		board.calcTargets(board.getCell(23, 6), 1);
		board.getCell(22, 3).setOccupied(false);
		board.getCell(23, 7).setOccupied(false);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(22, 3)));	
		assertTrue(targets.contains(board.getCell(22, 7)));	
			
		
		// check leaving a room with a blocked doorway
		board.getCell(5, 8).setOccupied(true);
		board.getCell(7, 11).setOccupied(true);
		board.calcTargets(board.getCell(3, 1), 4);
		board.getCell(5, 8).setOccupied(false);
		board.getCell(7, 11).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(7, 15)));
		assertTrue(targets.contains(board.getCell(6, 14)));	
		assertTrue(targets.contains(board.getCell(8, 14)));

	}
		
		
	
}
