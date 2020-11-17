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
		
		int roll = gcp.roll();
		gcp.setTurn(board.getCurrentPlayer(), roll); //for testing
		gcp.setGuess("");
		gcp.setGuessResult("");
		
		add(gcp, BorderLayout.SOUTH);
		
		BoardCell currentCell = board.getCell(board.getCurrentPlayer().getRow(), board.getCurrentPlayer().getColumn());
		board.calcTargets(currentCell, roll);
		board.flagTargets();
		
		board.repaint();
		
		CardPanel cardPanel = new CardPanel(board.getPlayers().get(0));
		add(cardPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}
}
