package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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
	protected boolean shifted = false;
	protected List<Card> cards = new ArrayList<Card>();
	protected List<Card> possibleDisproveCards = new ArrayList<Card>();
	public List<Card> seenCards = new ArrayList<Card>();
	protected BoardCell previousCell = null;
	protected boolean flag = false;
	
	public Player(String name, Color color, int startRow, int startCol) {
		this.name = name;
		this.color = color;
		this.startRow = startRow;
		this.row = startRow;
		this.startCol = startCol;
		this.col = startCol;
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
	
	public void draw(int width, int height, Graphics g) {
		int x = col*width;
		int y = row*height;
		
		int a = width/6;
		int b = height/6;
		
		
		g.setColor(Color.BLACK);
		g.fillOval(x, y, width, height);
		g.setColor(color);
		g.fillOval(x + (a/2), y + (b/2), width - a, height - b);
	}
	
	public void drawShifted(int width, int height, int offsetFactor, Graphics g) {
		
		int offset = (offsetFactor * width) / 2;
		int x, y;
		
		if(offsetFactor > 4 && offsetFactor % 2 == 0) {
			offsetFactor -= 2;
			x = col*width - offset;
			y = row*height - offset;
		} else if (offsetFactor > 4 && offsetFactor % 2 != 0) {
			offsetFactor -= 2;
			x = col*width - offset;
			y = row*height + offset;
		} else if(offsetFactor % 2 == 0) {
			x = col*width + offset;
			y = row*height - offset;
		} else {
			x = col*width + offset;
			y = row*height + offset;
		}
		
		
		int a = width/6;
		int b = height/6;
		
		g.setColor(Color.BLACK);
		g.fillOval(x, y, width, height);
		g.setColor(color);
		g.fillOval(x + (a/2), y + (b/2), width - a, height - b);
		
		shifted = true;
		
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
	
	public void updateSeenCards(Card card) {
		seenCards.add(card);
	}
	
	public List<Card> getSeenCards() {
		return seenCards;
	}
	
	public boolean shifted() {
		return shifted;
	}
	
	public void notShifted() {
		shifted = false;
	}
	
	public void clearSeenCards() {
		seenCards.clear();
	}
	public abstract boolean getIsHuman();

	public abstract Solution createSuggestion();

	public abstract BoardCell selectTargets();

	public void addSeenCard(Card card) {
		seenCards.add(card);
		
	}

	protected abstract boolean checkCards();

	protected abstract String getFinalPerson();

	protected abstract String getFinalRoom();

	protected abstract String getFinalWeapon();

	public BoardCell getPreviousCell() {
		return previousCell;
	}
	public void setPreviousCell(BoardCell cell) {
		previousCell = cell;
	}

	protected abstract void setFinalPerson(String aPerson);

	protected abstract void setFinalWeapon(String aWeapon);

	protected abstract void setFinalRoom(String aRoom);

	public boolean getFlag() {
		return flag;
	}
	public void setFlag() {
		flag = true;
	}

	protected abstract boolean getIsFinished();
	protected abstract void setIsFinished(boolean b);
	
}
