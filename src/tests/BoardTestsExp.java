package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

class BoardTestsExp {
	TestBoard board;

	@BeforeEach // 
	public void setUp() {
		board = new TestBoard();
	}
	
	@Test
	public void testAdjacencyTopLeft() {
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
	}
	@Test
	public void testAdjacencyBotRight() {
		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertEquals(2, testList.size());
	}
	@Test
	public void testAdjacencyLeftEdge() {
		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(2, testList.size());
	}
	@Test
	public void testAdjacencyRightEdge() {
		TestBoardCell cell = board.getCell(3,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 0)));
		Assert.assertTrue(testList.contains(board.getCell(4, 0)));
		Assert.assertTrue(testList.contains(board.getCell(3, 1)));
		Assert.assertEquals(2, testList.size());
	}
	
	
	@Test
	public void testEmptyBoard() {
		TestBoard board = new TestBoard();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				TestBoardCell cell = board.getCell(i, j);
				Assert.assertTrue(cell.getOccupied());
			}
		}
	}
	
	@Test
	public void testOccupied() {
		TestBoard board = new TestBoard();
		board.getCell(0, 1).setOccupied(true);
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				TestBoardCell cell = board.getCell(i, j);
				Assert.assertTrue(cell.getOccupied());
			}
		}
	}
	@Test
	public void testRoom() {
		TestBoard board = new TestBoard();
		board.getCell(0, 1).setRoom(true);
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				TestBoardCell cell = board.getCell(i, j);
				Assert.assertTrue(cell.isRoom());
			}
		}
	}
	@Test
	public void testMove() {
		TestBoard board = new TestBoard();
		Assert.assertEquals(2, board.getTargets().size());

	}

}