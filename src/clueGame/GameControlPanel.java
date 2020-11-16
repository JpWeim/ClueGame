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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	
	/*TODO: 
	 * implement current human player finished y/n
	 * is new player human? y/n
	 * if no:
	 * 	do accusation? for computer
	 * 	make suggestion? for computer
	 * if yes:
	 * 	display targets
	 * 	flag unfinished
	 */
	private class NextListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
				board.nextPlayer();
				int roll = roll();
				Player currentPlayer = board.getCurrentPlayer();
				setTurn(currentPlayer, roll);
				BoardCell currentCell = board.getCell(currentPlayer.getRow(), currentPlayer.getColumn());
				board.calcTargets(currentCell, roll);
				
				if (!currentPlayer.getIsHuman()) {
					
					
					//computer chooses to move
					BoardCell target = currentPlayer.selectTargets();
					currentPlayer.setRow(target.getRow());
					currentPlayer.setColumn(target.getCol());
					
					board.repaint();
				}
				else {
					Set<BoardCell> targets = board.getTargets();
					for (BoardCell x : targets) {
						x.setTarget(true);
					}
					board.repaint();
					
					//for cleanup after the player has chosen a move
					/*for (BoardCell y : targets) {
						y.setTarget(false);
					}
					board.repaint();
					*/
				}
				
				playerInfo.revalidate();
		}
		
	}
	public int roll() {
		Random r = new Random();
		int roll = r.nextInt(6) + 1;
		return roll;
	}
}
