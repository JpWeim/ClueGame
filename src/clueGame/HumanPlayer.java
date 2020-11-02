package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player{
	private boolean isHuman = true;

	public HumanPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

}
