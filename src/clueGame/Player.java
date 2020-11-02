package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;


public abstract class Player {
	private String name;
	private Color color;
	protected int startRow, startCol;
	protected int row, column;
	protected boolean isHuman;
	protected List<Card> cards = new ArrayList<Card>();
	
	public Player(String name, Color color, int startRow, int startCol) {
		this.name = name;
		this.color = color;
		this.startRow = startRow;
		this.startCol = startCol;
	}
	
	public void updateHand(Card card) {
		cards.add(card);
	}
	
	
	/* 
	 * ------------------------------------------------------------------------------------------------------------------------------------------
	 * Getters and setters
	 *  ------------------------------------------------------------------------------------------------------------------------------------------
	*/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getHandSize() {
		return cards.size();
	}

	public abstract boolean getIsHuman();

	
	
}
