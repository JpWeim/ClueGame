package clueGame;

import java.awt.Color;
import java.util.function.BooleanSupplier;

public class HumanPlayer extends Player{
	private boolean isHuman = true;
	private boolean isFinished;

	public HumanPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}

	@Override
	public boolean getIsHuman() {
		return isHuman;
	}

	@Override
	public Solution createSuggestion() {
		return null;
	}

	@Override
	public BoardCell selectTargets() {
		return null;
	}

	@Override
	protected boolean checkCards() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Card getFinalPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Card getFinalRoom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Card getFinalWeapon() {
		// TODO Auto-generated method stub
		return null;
	}


}
