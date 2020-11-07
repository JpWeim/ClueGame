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
	
	public GameControlPanel() {
		
	}

	  public static void main(String[] args) {
          GameControlPanel panel = new GameControlPanel();  // create the panel
          JFrame frame = new JFrame();  // create the frame
          frame.setContentPane(panel); // put the panel in the frame
          frame.setSize(750, 180);  // size the frame
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
          frame.setVisible(true); // make it visible

          
       // test filling in the data
          JPanel playerInfo = panel.setTurn(new ComputerPlayer( "Col. Mustard", Color.YELLOW, 0, 0), 5);
          
          JPanel guess = panel.setGuess( "I have no guess!");
          JPanel guessResult = panel.setGuessResult( "So you have nothing?");
          JPanel guessInfo = new JPanel();
          guessInfo.setLayout(new GridLayout(0,2));
          guessInfo.add(guess);
          guessInfo.add(guessResult);
          
          
          JPanel GameControlPanel = new JPanel();
          GameControlPanel.setLayout(new GridLayout(2,0));
          GameControlPanel.add(playerInfo, BorderLayout.NORTH);
          GameControlPanel.add(guessInfo, BorderLayout.SOUTH);
          panel.add(GameControlPanel);
   }
  private JPanel setTurn(ComputerPlayer computerPlayer, int i) {
	  	JPanel playerInfo = new JPanel();
	  	playerInfo.setLayout(new GridLayout(1,4));
	  	
		JPanel turn = new JPanel();
		JLabel label = new JLabel("Who's turn?");
		JTextField player = new JTextField(computerPlayer.getName());
		player.setEditable(false);
		player.setBackground(computerPlayer.getColor());
		turn.add(label);
		turn.add(player);
		
		JPanel roll = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		JTextField rollResult = new JTextField(Integer.toString(i));
		rollResult.setEditable(false);
		roll.add(rollLabel);
		roll.add(rollResult);

		JButton accuseButton = new JButton("Make Accuestion");
		JButton nextTurn = new JButton("NEXT!");

		playerInfo.add(turn);
		playerInfo.add(roll);
		playerInfo.add(accuseButton);
		playerInfo.add(nextTurn);
		
		return playerInfo;
	}

  private JPanel setGuess(String string) {
	  JPanel guess = new JPanel();
	  guess.setLayout(new GridLayout(1,0));
	  guess.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
	  JTextField playerGuess = new JTextField(string);
	  playerGuess.setEditable(false);
	  guess.add(playerGuess);
	  
	  return guess;
	}
	private JPanel setGuessResult(String string) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(1,0));
		result.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		JTextField playerGuessResult = new JTextField(string);
		playerGuessResult.setEditable(false);
		result.add(playerGuessResult);
		  
		return result;
	}

	

	
	  
}
