package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	private static Board board;
	private JTextField player = new JTextField();
	private JTextField rollResult = new JTextField();
	private JTextField playerGuess = new JTextField();
	private JTextField playerGuessResult = new JTextField();


	public GameControlPanel() {

	}

	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		panel.setLayout(new GridLayout(2,1));
		JFrame frame = new JFrame();  // create the frame
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible


		//TODO rewrite so don't return panels for guess stuff
		/*
		 * Right now, the methods below return panels. The player info panels
		 * are combined into one larger panel in its own method, and the guess info
		 * panels are combined here. Then, both those panels are added to
		 * the panel already in the frame.
		 */
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		List<Card> players = board.getPlayerCards();
		List<Card> rooms = board.getRoomCards();
		List<Card> weapons = board.getWeaponCards();
		
		Player currPlayer = new ComputerPlayer( "Col. Mustard", Color.YELLOW, 0, 0);
		currPlayer.updateHand(players.get(0));
		currPlayer.addSeenCard(players.get(2));
		currPlayer.addSeenCard(players.get(4));
		
		currPlayer.updateHand(rooms.get(0));
		currPlayer.addSeenCard(rooms.get(2));
		currPlayer.addSeenCard(rooms.get(4));
		
		currPlayer.updateHand(weapons.get(0));
		currPlayer.addSeenCard(weapons.get(2));
		currPlayer.addSeenCard(weapons.get(4));
		
		panel.setTurn(currPlayer, 5);
		JPanel guess = panel.setGuess( "I have no guess!");
		JPanel guessResult = panel.setGuessResult( "So you have nothing?");

		JPanel guessInfo = new JPanel();
		guessInfo.setLayout(new GridLayout(0,2));
		guessInfo.add(guess);
		guessInfo.add(guessResult);

		JPanel knownCards = new JPanel();
		knownCards.setLayout(new GridLayout(3,1));
		JPanel knownPeople = panel.setPeople(currPlayer);
		JPanel knownRooms = panel.setRooms(currPlayer);
		JPanel knownWeapons = panel.setWeapons(currPlayer);
		knownCards.add(knownPeople);
		knownCards.add(knownRooms);
		knownCards.add(knownWeapons);


		panel.setLayout(new GridLayout(2,1));
		panel.add(knownCards, BorderLayout.NORTH);
		panel.add(guessInfo, BorderLayout.SOUTH);


	}
	/*
	 * Creates a 1x4 panel and adds panels containing current player label and name,
	 * roll label and result, and two buttons to make an accusation and next turn
	 */
	private void setTurn(Player currPlayer, int i) {
		JPanel playerInfo = new JPanel();
		playerInfo.setLayout(new GridLayout(1,4));

		JPanel turn = new JPanel();
		JLabel label = new JLabel("Who's turn?");
		player.setText(currPlayer.getName());;
		player.setEditable(false);
		player.setBackground(currPlayer.getColor());
		turn.add(label);
		turn.add(player);

		JPanel roll = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		rollResult.setText(Integer.toString(i));
		rollResult.setEditable(false);
		roll.add(rollLabel);
		roll.add(rollResult);

		JButton accuseButton = new JButton("Make Accusation");
		JButton nextTurn = new JButton("NEXT!");

		playerInfo.add(turn);
		playerInfo.add(roll);
		playerInfo.add(accuseButton);
		playerInfo.add(nextTurn);

		add(playerInfo, BorderLayout.NORTH);
	}

	private JPanel setGuess(String string) {
		JPanel guess = new JPanel();
		guess.setLayout(new GridLayout(1,0));
		guess.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		playerGuess.setText(string);
		playerGuess.setEditable(false);
		guess.add(playerGuess);

		return guess;
	}
	private JPanel setGuessResult(String string) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(1,0));
		result.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		playerGuessResult.setText(string);
		playerGuessResult.setEditable(false);
		result.add(playerGuessResult);

		return result;
	}

	private JPanel setPeople(Player currPlayer) {
		JPanel people = new JPanel();
		people.setLayout(new GridLayout(2,1));
		people.setBorder(new TitledBorder(new EtchedBorder(), "People"));

		CardType cardType = CardType.PERSON;

		JPanel inHand = getHandPanel(currPlayer, cardType);
		JPanel seen = getSeenPanel(currPlayer, cardType);
		
		people.add(inHand);
		people.add(seen);
		
		return people;
	}
	

	private JPanel setRooms(Player currPlayer) {
		JPanel rooms = new JPanel();
		rooms.setLayout(new GridLayout(2,1));
		rooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		
		CardType cardType = CardType.ROOM;

		JPanel inHand = getHandPanel(currPlayer, cardType);
		JPanel seen = getSeenPanel(currPlayer, cardType);
		
		rooms.add(inHand);
		rooms.add(seen);
		
		return rooms;
	}

	private JPanel setWeapons(Player currPlayer) {
		JPanel weapons = new JPanel();
		weapons.setLayout(new GridLayout(2,1));
		weapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));

		CardType cardType = CardType.WEAPON;

		JPanel inHand = getHandPanel(currPlayer, cardType);
		JPanel seen = getSeenPanel(currPlayer, cardType);
		
		weapons.add(inHand);
		weapons.add(seen);
		
		return weapons;
	}

	private JPanel getHandPanel(Player currPlayer, CardType type) {
		JPanel inHand = new JPanel();
		inHand.setLayout(new GridLayout());
		inHand.setBorder(new TitledBorder(new EtchedBorder(), "In hand: "));
		
		for (Card x : currPlayer.getHand()) {
			if(x.getCardType() == type) {
				JTextField card = new JTextField();
				card.setText(x.getCardName());
				card.setEditable(false);
				card.setBackground(currPlayer.getColor());
				inHand.add(card);
			}
		}
		return inHand;
	}
	
	private JPanel getSeenPanel(Player currPlayer, CardType type) {
		JPanel seen = new JPanel();
		seen.setLayout(new GridLayout());
		seen.setBorder(new TitledBorder(new EtchedBorder(), "Seen: "));

		for (Card x : currPlayer.getSeenCards()) {
			if(x.getCardType() == type && !currPlayer.getHand().contains(x)) {
				JTextField card = new JTextField();
				card.setText(x.getCardName());
				card.setEditable(false);
				seen.add(card);
			}
		}
		return seen;
	}
	

}
