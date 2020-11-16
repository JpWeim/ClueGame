package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame{

	private static Board board;
	
	public ClueGame() {
		setSize(900,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JOptionPane.showMessageDialog(null, "You are Miss Scarlett. Can you find the solution before"
				+ " the computer players?");
		
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		addMouseListener(board);
		add(board, BorderLayout.CENTER);
		
		GameControlPanel gcp = new GameControlPanel(board);
		
		gcp.setTurn(board.getCurrentPlayer(), gcp.roll()); //for testing
		gcp.setGuess("");
		gcp.setGuessResult("");
		add(gcp, BorderLayout.SOUTH);
		
		CardPanel cardPanel = new CardPanel(board.getPlayers().get(0));
		add(cardPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}
}
