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
		
	}

	  public static void main(String[] args) {
          GameControlPanel panel = new GameControlPanel();  // create the panel
          panel.setLayout(new GridLayout(2,0));
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
          panel.setTurn(new ComputerPlayer( "Col. Mustard", Color.YELLOW, 0, 0), 5);
          totalGuess(panel);
    
   }

	private static void totalGuess(GameControlPanel panel, String guess, String guessResult) {
		JPanel guess = panel.setGuess(guess);
		JPanel guessResult = panel.setGuessResult(guessResult);
      
      	JPanel guessInfo = new JPanel();
      	guessInfo.setLayout(new GridLayout(0,2));
      	guessInfo.add(guess);
      	guessInfo.add(guessResult);
      
      	panel.setLayout(new GridLayout(2,0));
      	panel.add(guessInfo, BorderLayout.SOUTH);
}
	  /*
	   * Creates a 1x4 panel and adds panels containing current player label and name,
	   * roll label and result, and two buttons to make an accusation and next turn
	   */
  private void setTurn(ComputerPlayer computerPlayer, int i) {
	  	JPanel playerInfo = new JPanel();
	  	playerInfo.setLayout(new GridLayout(1,4));
	  	
		JPanel turn = new JPanel();
		JLabel label = new JLabel("Who's turn?");
		player.setText(computerPlayer.getName());;
		player.setEditable(false);
		player.setBackground(computerPlayer.getColor());
		turn.add(label);
		turn.add(player);
		
		JPanel roll = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		rollResult.setText(Integer.toString(i));
		rollResult.setEditable(false);
		roll.add(rollLabel);
		roll.add(rollResult);

		JButton accuseButton = new JButton("Make Accuestion");
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

	

	
	  
}
