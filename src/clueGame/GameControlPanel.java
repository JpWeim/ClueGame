package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	private Board board;
	private JTextField player = new JTextField();
	private JTextField rollResult = new JTextField();
	private JTextField playerGuess = new JTextField();
	private JTextField playerGuessResult = new JTextField();
	private JButton accuseButton = new JButton("Make Accusation");
	private JButton nextTurn= new JButton("NEXT!");
	private JPanel playerInfo = new JPanel();
	private boolean humanFinished = false;
	private CardPanel cPanel;

	public GameControlPanel(Board board, CardPanel cPanel) {
		this.board = board;
		this.cPanel = cPanel;
		setLayout(new GridLayout(2,0));

		playerInfo.setLayout(new GridLayout(1,4));
		JPanel turn = new JPanel();
		JLabel label = new JLabel("Who's turn?");
		turn.add(label);
		turn.add(player);

		JPanel roll = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		rollResult.setEditable(false);
		rollResult.setEditable(false);
		roll.add(rollLabel);
		roll.add(rollResult);


		NextListener next = new NextListener();
		nextTurn.addActionListener(next);
		AccusationListener accuse = new AccusationListener();
		accuseButton.addActionListener(accuse);

		playerInfo.add(turn);
		playerInfo.add(roll);
		playerInfo.add(accuseButton);
		playerInfo.add(nextTurn);


		add(playerInfo, BorderLayout.NORTH);

		JPanel guess = new JPanel();
		guess.setLayout(new GridLayout(1,0));
		guess.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		guess.add(playerGuess);
		playerGuess.setEditable(false);

		JPanel result = new JPanel();
		result.setLayout(new GridLayout(1,0));
		result.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		result.add(playerGuessResult);
		playerGuessResult.setEditable(false);

		JPanel guessInfo = new JPanel();
		guessInfo.setLayout(new GridLayout(0,2));
		guessInfo.add(guess);
		guessInfo.add(result);

		add(guessInfo, BorderLayout.SOUTH);


	}

	/*
	 * Creates a 1x4 panel and adds panels containing current player label and name,
	 * roll label and result, and two buttons to make an accusation and next turn
	 */

	public void setTurn(Player currPlayer, int i) {

		player.setText(currPlayer.getName());;
		player.setEditable(false);
		player.setBackground(currPlayer.getColor());

		rollResult.setText(Integer.toString(i));
	}
	public void setGuess(String string) {
		playerGuess.setText(string);

	}
	public void setGuessResult(String string) {
		playerGuessResult.setText(string);
		playerGuessResult.setEditable(false);

	}



	private class AccusationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if(board.getCurrentPlayer() == board.getPlayers().get(0) && !board.getCurrentPlayer().getIsFinished()) {

				JFrame frame = new JFrame("Make an Accusation");
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(400,200);

				JPanel panel = new JPanel();

				panel.setLayout(new GridLayout(4,2));

				frame.add(panel);

				JLabel roomLabel = new JLabel("Room");
				String[] roomChoices = new String[board.getRooms().size()];
				for(int i = 0; i < board.getRooms().size(); i++) {
					roomChoices[i] = board.getRooms().get(i);
				}
				final JComboBox<String> chooseRoom = new JComboBox<String>(roomChoices);

				JLabel personLabel = new JLabel("Person");
				String[] personChoices = new String[board.getPlayers().size()];
				for(int i = 0; i < board.getPlayers().size(); i++) {
					personChoices[i] = board.getPlayers().get(i).getName();
				}
				final JComboBox<String> choosePerson = new JComboBox<String>(personChoices);

				JLabel weaponLabel = new JLabel("Weapon");
				String[] weaponChoices = new String[board.getWeapons().size()];
				for(int i = 0; i < board.getWeapons().size(); i++) {
					weaponChoices[i] = board.getWeapons().get(i);
				}
				final JComboBox<String> chooseWeapon = new JComboBox<String>(weaponChoices);

				panel.add(roomLabel);
				panel.add(chooseRoom);
				panel.add(personLabel);
				panel.add(choosePerson);
				panel.add(weaponLabel);
				panel.add(chooseWeapon);

				JButton accuse = new JButton ("Accuse");
				JButton cancel = new JButton ("Cancel");

				panel.add(accuse);
				panel.add(cancel);

				AccuseListener accuseButton = new AccuseListener(choosePerson, chooseRoom, chooseWeapon, frame);
				accuse.addActionListener(accuseButton);
				CancelListener cancelButton = new CancelListener(frame);
				cancel.addActionListener(cancelButton);

			} else {

				JOptionPane.showMessageDialog(null, "Can only accuse before you move on your turn");
			}

		}


	}

	private class AccuseListener implements ActionListener {
		JComboBox chooseRoom, choosePerson, chooseWeapon;
		String aRoom, aPerson, aWeapon;
		JFrame frame;

		public AccuseListener(JComboBox person, JComboBox room, JComboBox weapon, JFrame frame) {
			this.choosePerson = person;
			this.chooseRoom = room;
			this.chooseWeapon = weapon;
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if(board.getCurrentPlayer() == board.getPlayers().get(0) && !board.getCurrentPlayer().getIsFinished()) {

				aPerson = (String) choosePerson.getSelectedItem();
				aRoom = (String) chooseRoom.getSelectedItem();
				aWeapon = (String) chooseWeapon.getSelectedItem();


				Player currentPlayer = board.getCurrentPlayer();
				currentPlayer.setFinalPerson(aPerson);
				currentPlayer.setFinalRoom(aRoom);
				currentPlayer.setFinalWeapon(aWeapon);
				board.getCurrentPlayer().setFlag();

				frame.dispose();
				
				if(board.getPlayers().get(0).getFlag() ) {
					if(board.checkAccusation(board.getPlayers().get(0).getFinalPerson(), board.getPlayers().get(0).getFinalRoom(), board.getPlayers().get(0).getFinalWeapon())) {
						JTextArea msg = new JTextArea(board.getPlayers().get(0).getName() + " won! The correct answer was " + board.getPlayers().get(0).getFinalPerson()
								+ " with the " + board.getPlayers().get(0).getFinalWeapon() + ", in " + board.getPlayers().get(0).getFinalRoom());
						msg.setLineWrap(true);

						JOptionPane.showMessageDialog(null, msg);
						System.exit(0);
					} else if (!board.checkAccusation(board.getPlayers().get(0).getFinalPerson(), board.getPlayers().get(0).getFinalRoom(), board.getPlayers().get(0).getFinalWeapon())){
						JOptionPane.showMessageDialog(null, "You got the accusation wrong! The correct answer was " + board.getPlayers().get(0).getFinalPerson() + " with the " + board.getPlayers().get(0).getFinalWeapon() + ", in " + board.getPlayers().get(0).getFinalRoom());
						System.exit(0);
					}
				}
				
			}
			else {
				JOptionPane.showMessageDialog(null, "You can only accuse at the beginning of your turn.");
			}
		}



	}

	private class CancelListener implements ActionListener{
		JFrame frame;

		public CancelListener(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}

	private class NextListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean done = board.getPlayerDone();
			setGuess("");
			setGuessResult("");

			if (!done) {
				JOptionPane.showMessageDialog(null, "Turn not done");
			}
			else {
				board.nextPlayer();
				int roll = roll();
				Player currentPlayer = board.getCurrentPlayer();
				setTurn(currentPlayer, roll);
				BoardCell currentCell = board.getCell(currentPlayer.getRow(), currentPlayer.getColumn());
				board.calcTargets(currentCell, roll);
				board.getCurrentPlayer().setIsFinished(false);


				if (!currentPlayer.getIsHuman()) {
					boolean accuse = currentPlayer.checkCards();
					if (accuse) {
						if(board.checkAccusation(currentPlayer.getFinalPerson(), currentPlayer.getFinalRoom(), currentPlayer.getFinalWeapon())) {
							JTextArea msg = new JTextArea(currentPlayer.getName() + " won! The correct answer was " + currentPlayer.getFinalPerson()
									+ " with the " + currentPlayer.getFinalWeapon() + ", in " + currentPlayer.getFinalRoom());
							msg.setLineWrap(true);

							JOptionPane.showMessageDialog(null, msg);
						}
					}

					//computer chooses to move
					BoardCell target = currentPlayer.selectTargets();
					if (target == null) {

					}
					else {
						board.getCell(currentPlayer.getRow(), currentPlayer.getColumn()).setOccupied(false);
						currentPlayer.setRow(target.getRow());
						currentPlayer.setColumn(target.getCol());
						board.getCell(target.getRow(), target.getCol()).setOccupied(true);

						if(target.isRoom()) {
							Solution suggestion = currentPlayer.createSuggestion();

							Card disprove = board.handleSuggestion(currentPlayer, suggestion.getPerson(), suggestion.getRoom(), suggestion.getWeapon());
							if (disprove == null) {
								currentPlayer.setFinalPerson(suggestion.getPerson().getCardName());
								currentPlayer.setFinalWeapon(suggestion.getWeapon().getCardName());
								currentPlayer.setFinalRoom(suggestion.getRoom().getCardName());

								currentPlayer.setFlag();
							}
							else {
								currentPlayer.updateSeenCards(disprove);
							}
							setGuess(board.getCurrentSuggestion());
							setGuessResult(board.getSuggestionResult());
						}
					}
					board.repaint();
				} else {
					board.flagTargets();
					board.setPlayerDone(false);
					board.repaint();
					cPanel.revalidate();
				}
				if (board.getTargets().isEmpty()) {
					board.setPlayerDone(true);
				}

				playerInfo.revalidate();
			}
		}

	}


	public int roll() {
		Random r = new Random();
		int roll = r.nextInt(6) + 1;
		return roll;
	}
}
