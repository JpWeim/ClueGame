/*
 * @author John Walter
 * @author Hunter Sitki
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	Set<TestBoardCell> targets = new HashSet<TestBoardCell>();

	public TestBoard() {
		
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//calcTargets and assign to targets
	}
	
	public Set<TestBoardCell> getTargets(){
		//calls calcTargets
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col) {
		TestBoardCell cell = new TestBoardCell(row, col);
		return cell;
	}
}
