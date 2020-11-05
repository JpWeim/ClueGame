package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;


public abstract class Player {
	private String name;
	private Color color;
	protected int startRow, startCol;
	protected int row, col;
	protected boolean isHuman;
	protected List<Card> cards = new ArrayList<Card>();
	protected List<Card> possibleDisproveCards = new ArrayList<Card>();
	protected List<Card> seenCards = new ArrayList<Card>();
	
	public Player(String name, Color color, int startRow, int startCol) {
		this.name = name;
		this.color = color;
		this.startRow = startRow;
		this.startCol = startCol;
	}
	
	public void updateHand(Card card) {
		cards.add(card);
		seenCards.add(card);
	}
	
	public Card disproveSuggestion(Card player, Card room, Card weapon) {
		possibleDisproveCards.clear();
		if (cards.contains(player) || cards.contains(room) || cards.contains(weapon)) {
			if(cards.contains(player)) {
				possibleDisproveCards.add(player);
			} 
			if(cards.contains(room)) {
				possibleDisproveCards.add(room);
			} 
			if(cards.contains(weapon)) {
				possibleDisproveCards.add(weapon);
			} 
			
			Random r = new Random();
			return possibleDisproveCards.get(r.nextInt(possibleDisproveCards.size()));
		}
		else {
			return null;
		}
	}
	
	public void clearHand() {
		cards.clear();
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
		return col;
	}

	public void setColumn(int column) {
		this.col = column;
	}

	public int getHandSize() {
		return cards.size();
	}
	
	public Card getCard(int index) {
		return cards.get(index);
	}

	public List<Card> getHand() {
		return cards;
	}
	public abstract boolean getIsHuman();

	
	
}
