package clueGame;

public class Card {
	private String cardName;
	private CardType type;
	
	public Card(String cardName, CardType type) {
		this.cardName = cardName;
		this.type = type;
	}
	//public boolean equals(Card target) {
	//	if (target.getCardName().equalsIgnoreCase(cardName) && target.getCardType() == type) {
	//		return true;
	//	}
	//	else {
	//		return false;
	//	}
	//}
	
	
	/* 
	 * ------------------------------------------------------------------------------------------------------------------------------------------
	 * Getters and setters
	 *  ------------------------------------------------------------------------------------------------------------------------------------------
	*/
	public String getCardName() {
		return cardName;
	}
	public CardType getCardType() {
		return type;
	}
}
