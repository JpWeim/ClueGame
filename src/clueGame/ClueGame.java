package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame{

	private static Board board;
	
	public ClueGame() {
		setSize(800,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		
		add(board, BorderLayout.CENTER);
		
		GameControlPanel gcp = new GameControlPanel();
		
		gcp.setTurn(new ComputerPlayer( "Col. Mustard", Color.YELLOW, 0, 0), 5); //for testing
		gcp.setGuess("I have no guess");
		gcp.setGuessResult("So you have nothing?");
		add(gcp, BorderLayout.SOUTH);
		
		CardPanel cardPanel = new CardPanel(board.getPlayers().get(0));
		add(cardPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}
}
