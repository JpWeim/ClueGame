package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{

	private JTextField player = new JTextField();
	private JTextField rollResult = new JTextField();
	private JTextField playerGuess = new JTextField();
	private JTextField playerGuessResult = new JTextField();


	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		JPanel playerInfo = new JPanel();
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
		
		JButton accuseButton = new JButton("Make Accusation");
		JButton nextTurn = new JButton("NEXT!");
		
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
}
