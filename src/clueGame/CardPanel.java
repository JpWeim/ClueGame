package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardPanel extends JPanel{
	private static Board board;

	public CardPanel() {

	}

	public static void main(String[] args) {
		CardPanel panel = new CardPanel();  // create the panel
		panel.setLayout(new GridLayout());
		JFrame frame = new JFrame();  // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(180, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		/*
		 * Creates a board in order to get all types of cards easily for testing
		 */
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		List<Card> players = board.getPlayerCards();
		List<Card> rooms = board.getRoomCards();
		List<Card> weapons = board.getWeaponCards();

		/*
		 * Sets up player and their cards for testing
		 */
		Player currPlayer = new HumanPlayer("Miss Scarlett", Color.RED, 0, 0);
		currPlayer.updateHand(players.get(0));
		currPlayer.addSeenCard(players.get(2));
		currPlayer.addSeenCard(players.get(4));
		currPlayer.updateHand(rooms.get(0));
		currPlayer.addSeenCard(weapons.get(2));
		currPlayer.addSeenCard(weapons.get(4));
		currPlayer.addSeenCard(weapons.get(1));


		JPanel knownCards = new JPanel();
		knownCards.setLayout(new GridLayout(3,1));
		knownCards.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		JPanel knownPeople = panel.setCardSection(currPlayer, CardType.PERSON);
		JPanel knownRooms = panel.setCardSection(currPlayer, CardType.ROOM);
		JPanel knownWeapons = panel.setCardSection(currPlayer, CardType.WEAPON);
		knownCards.add(knownPeople);
		knownCards.add(knownRooms);
		knownCards.add(knownWeapons);


		panel.setLayout(new GridLayout(2,1));
		panel.add(knownCards, BorderLayout.EAST);
	}

	/*
	 * Makes a card type section within known cards and populates with cards of that type
	 * in the player's hand and seen cards.
	 */
	private JPanel setCardSection(Player currPlayer, CardType type) {
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

		JPanel inHand = getHandPanel(currPlayer, type);
		JPanel seen = getSeenPanel(currPlayer, type);

		cardSection.add(inHand);
		cardSection.add(seen);

		return cardSection;
	}

	/*
	 * Adds cards in player's hand to "In Hand:" section of their known cards
	 */
	private JPanel getHandPanel(Player currPlayer, CardType type) {
		JPanel inHand = new JPanel();
		inHand.setLayout(new GridLayout());
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
		return inHand;
	}


	/*
	 * Adds cards in player's seenCards to "Seen:" section of their known cards
	 */
	private JPanel getSeenPanel(Player currPlayer, CardType type) {
		JPanel seen = new JPanel();
		seen.setLayout(new GridLayout());
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

		return seen;
	}
}