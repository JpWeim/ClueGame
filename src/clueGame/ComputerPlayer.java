package clueGame;

import java.awt.Color;

public class ComputerPlayer extends Player{
	private boolean isHuman = false; 

	public ComputerPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

	@Override
	public boolean getIsHuman() {
		return isHuman;
	}
	
	

}
