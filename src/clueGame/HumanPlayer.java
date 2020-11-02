package clueGame;

import java.awt.Color;
import java.util.function.BooleanSupplier;

public class HumanPlayer extends Player{
	private boolean isHuman = true;

	public HumanPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

	@Override
	public boolean getIsHuman() {
		return isHuman;
	}
	

}
