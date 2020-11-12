package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private Player currPlayer;
	private JPanel seenPanel;
	private JPanel handPanel;
	private JPanel cardSection;


	public CardPanel(Player currPlayer) {

		this.currPlayer = currPlayer;
		setLayout(new GridLayout(1,1));
		JPanel knownCards = new JPanel();
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

	/**
	public static void main(String[] args) {

		CardPanel panel = new CardPanel();  // create the panel
		panel.setLayout(new GridLayout());
		JFrame frame = new JFrame();  // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(500, 800);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		/*
	 * Creates a board in order to get all types of cards easily for testing
	 *//*
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		List<Card> players = board.getPlayerCards();
		List<Card> rooms = board.getRoomCards();
		List<Card> weapons = board.getWeaponCards();

		/*
	  * Sets up player and their cards for testing
	  *//*
		Player currPlayer = new HumanPlayer("Miss Scarlett", Color.RED, 0, 0);
		currPlayer.updateHand(players.get(0));
		currPlayer.addSeenCard(players.get(2));
		currPlayer.addSeenCard(players.get(4));
		currPlayer.updateHand(rooms.get(0));
		currPlayer.addSeenCard(weapons.get(2));
		currPlayer.addSeenCard(weapons.get(4));
		currPlayer.addSeenCard(weapons.get(1));


		JPanel knownCards = new JPanel();
		knownCards.setLayout(new BoxLayout(knownCards, BoxLayout.Y_AXIS));
		knownCards.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		JPanel knownPeople = panel.setCardSection(currPlayer, CardType.PERSON);
		JPanel knownRooms = panel.setCardSection(currPlayer, CardType.ROOM);
		JPanel knownWeapons = panel.setCardSection(currPlayer, CardType.WEAPON);
		knownCards.add(knownPeople);
		knownCards.add(knownRooms);
		knownCards.add(knownWeapons);


		panel.setLayout(new GridLayout(2,1));
		panel.add(knownCards, BorderLayout.EAST);
	}*/

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

		//this.cardSection = cardSection;
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
		//return inHand;
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
		//return seen;
	}

	public void setCurrPlayer(Player p) {
		currPlayer = p;
	}
}