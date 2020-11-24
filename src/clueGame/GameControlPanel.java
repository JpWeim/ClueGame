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

	public GameControlPanel(Board board) {
		this.board = board;
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
			
			if(!board.getCell(board.getCurrentPlayer().getRow(), board.getCurrentPlayer().getColumn()).isRoom()) {
				JOptionPane.showMessageDialog(null, "Can only accuse while in a room");
			} else {
				JFrame frame = new JFrame("Make an Accusation");
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(400,400);
				
				JPanel panel = new JPanel();
				
				panel.setLayout(new GridLayout(4,1));
				
				frame.add(panel);
				
				JLabel currentRoom = new JLabel("Current room " + board.getRoom(board.getCell(board.getCurrentPlayer().getRow(), board.getCurrentPlayer().getColumn())).getName());
				
				
				String[] personChoices = new String[board.getPlayers().size()];
				for(int i = 0; i < board.getPlayers().size(); i++) {
					personChoices[i] = board.getPlayers().get(i).getName();
				}
				final JComboBox<String> choosePerson = new JComboBox<String>(personChoices);
				
				String[] weaponChoices = new String[board.getWeapons().size()];
				for(int i = 0; i < board.getWeapons().size(); i++) {
					personChoices[i] = board.getWeapons().get(i);
				}
				final JComboBox<String> chooseWeapon = new JComboBox<String>(personChoices);
				
				panel.add(currentRoom);
				panel.add(choosePerson);
				panel.add(chooseWeapon);

			}
			
						
			
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
			
			

			if (!currentPlayer.getIsHuman()) {
				boolean accuse = currentPlayer.checkCards();
				if (accuse) {
					if(board.checkAccusation(currentPlayer.getFinalPerson(), currentPlayer.getFinalRoom(), currentPlayer.getFinalWeapon())) {
						JTextArea msg = new JTextArea(currentPlayer.getName() + " won! The correct answer was " + currentPlayer.getFinalPerson().getCardName()
								+ " in the " + currentPlayer.getFinalWeapon().getCardName() + ", in the " + currentPlayer.getFinalRoom().getCardName());
						msg.setLineWrap(true);
						
						JOptionPane.showMessageDialog(null, msg);
					}
				}
				
				if (currentPlayer.getFlag()) {
					if(board.checkAccusation(currentPlayer.getFinalPerson(), currentPlayer.getFinalRoom(), currentPlayer.getFinalWeapon())) {
						JTextArea msg = new JTextArea(currentPlayer.getName() + " won! The correct answer was " + currentPlayer.getFinalPerson().getCardName()
								+ " in the " + currentPlayer.getFinalWeapon().getCardName() + ", in the " + currentPlayer.getFinalRoom().getCardName());
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
						currentPlayer.setFinalPerson(suggestion.getPerson());
						currentPlayer.setFinalWeapon(suggestion.getWeapon());
						currentPlayer.setFinalRoom(suggestion.getRoom());
						
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
