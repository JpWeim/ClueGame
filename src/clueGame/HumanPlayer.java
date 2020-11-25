package clueGame;

import java.awt.Color;
import java.util.function.BooleanSupplier;

public class HumanPlayer extends Player{
	private boolean isHuman = true;
	private boolean isFinished;
	private String finalPerson;
	private String finalRoom;
	private String finalWeapon;

	public HumanPlayer(String name, Color color, int startRow, int startCol) {
		super(name, color, startRow, startCol);
	}
	
	public boolean getIsFinished() {
		return isFinished;
	}
	
	public void setIsFinished(boolean f) {
		isFinished = f;
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

	public void setFinalPerson(String card) {
		finalPerson = card;
	}
	public String getFinalPerson() {
		return finalPerson;
	}

	public void setFinalWeapon(String card) {
		finalWeapon = card;
	}
	public String getFinalWeapon() {
		return finalWeapon;
	}

	public void setFinalRoom(String card) {
		finalRoom = card;
	}
	public String getFinalRoom() {
		return finalRoom;
	}


}
