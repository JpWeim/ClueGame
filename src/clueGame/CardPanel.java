package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardPanel extends JPanel{
	private static Board board;
	private static Player currPlayer;
	private JPanel seenPanel;
	private JPanel handPanel;
	private JPanel cardSection;
	private JPanel knownCards = new JPanel();

	
	public CardPanel(Player currPlayer) {
		this.currPlayer = currPlayer;
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(150,0));
		knownCards.setLayout(new BoxLayout(knownCards, BoxLayout.Y_AXIS));
		knownCards.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		JPanel knownPeople = new JPanel();
		JPanel knownRooms = new JPanel();
		JPanel knownWeapons = new JPanel();

		knownPeople = setCardSection(currPlayer, CardType.PERSON);
		knownRooms = setCardSection(currPlayer, CardType.ROOM);
		knownWeapons = setCardSection(currPlayer, CardType.WEAPON);

		knownCards.add(knownPeople);
		knownCards.add(knownRooms);
		knownCards.add(knownWeapons);



		add(knownCards);

	}
	
	public void update() {
		repaint();
	}

	/*
	 * Makes a card type section within known cards and populates with cards of that type
	 * in the player's hand and seen cards.
	 */
	public JPanel setCardSection(Player currPlayer, CardType type) {
		JPanel cardSection = new JPanel();
		cardSection.setLayout(new GridLayout(2,1));

		String section = "";
		switch(type) {
		case PERSON:
			section = "People";
			break;
		case ROOM:
			section = "Rooms";
			break;
		case WEAPON:
			section = "Weapons";
			break;
		}
		cardSection.setBorder(new TitledBorder(new EtchedBorder(), section));

		getHandPanel(currPlayer, type);
		getSeenPanel(currPlayer, type);

		cardSection.add(handPanel);
		cardSection.add(seenPanel);

		return cardSection;
	}

	/*
	 * Adds cards in player's hand to "In Hand:" section of their known cards
	 */
	private void getHandPanel(Player currPlayer, CardType type) {
		JPanel inHand = new JPanel();
		inHand.setLayout(new BoxLayout(inHand, BoxLayout.Y_AXIS));
		inHand.setBorder(new TitledBorder("In Hand:"));

		boolean foundCard = false;
		for (Card x : currPlayer.getHand()) {
			if(x.getCardType() == type) {
				JTextField card = new JTextField();
				card.setText(x.getCardName());
				card.setEditable(false);
				card.setBackground(currPlayer.getColor());
				inHand.add(card);
				foundCard = true;
			}
		}


		if(!foundCard) {
			JTextField noCard = new JTextField();
			noCard.setText("None");
			noCard.setEditable(false);
			inHand.add(noCard);
		}

		handPanel = inHand;
	}


	/*
	 * Adds cards in player's seenCards to "Seen:" section of their known cards
	 */
	private void getSeenPanel(Player currPlayer, CardType type) {
		JPanel seen = new JPanel();
		seen.setLayout(new BoxLayout(seen, BoxLayout.Y_AXIS));
		seen.setBorder(new TitledBorder(new EtchedBorder(), "Seen:"));

		
		boolean foundCard = false;
		for (Card x : currPlayer.getSeenCards()) {
			if(x.getCardType() == type && !currPlayer.getHand().contains(x)) {
				JTextField card = new JTextField();
				card.setText(x.getCardName());
				card.setEditable(false);
				seen.add(card);
				foundCard = true;
			}
		}

		if(!foundCard) {
			JTextField noCard = new JTextField();
			noCard.setText("None");
			noCard.setEditable(false);
			seen.add(noCard);
		}

		seenPanel = seen;
	}

	public void setCurrPlayer(Player p) {
		currPlayer = p;
	}
	
	public JPanel getCardPanel() {
		return knownCards;
	}
}