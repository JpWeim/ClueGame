package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

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
	public void test() {
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assert.assertTrue(testList.contains(board.getCell(1, 0)));
		
	}

