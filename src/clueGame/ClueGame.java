package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame{

	private static Board board;
	private ClueGame game;
	private static JFrame frame;
	
	public ClueGame() {
		frame = new JFrame("Cluedo");
		frame.setVisible(true);
		frame.setSize(900,900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JOptionPane.showMessageDialog(null, "You are Miss Scarlett. Can you find the solution before"
				+ " the computer players?");
		
		board = Board.getInstance();
			
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		frame.addMouseListener(board);
		frame.add(board, BorderLayout.CENTER);
		
		CardPanel cardPanel = new CardPanel(board.getPlayers().get(0));
		
		GameControlPanel gcp = new GameControlPanel(board, cardPanel);
		
		int roll = gcp.roll();
		gcp.setTurn(board.getCurrentPlayer(), roll); //for testing
		gcp.setGuess("");
		gcp.setGuessResult("");
		frame.add(gcp, BorderLayout.SOUTH);
				
		BoardCell currentCell = board.getCell(board.getCurrentPlayer().getRow(), board.getCurrentPlayer().getColumn());
		board.calcTargets(currentCell, roll);
		board.flagTargets();
		
		board.repaint();
		
		
		frame.add(cardPanel, BorderLayout.EAST);
		
	}
	
	public static void updateCardPanel() {
		CardPanel cardPanel = new CardPanel(board.getPlayers().get(0));
		frame.add(cardPanel, BorderLayout.EAST);
		cardPanel.repaint();
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
	}
	
	//TODO implement player suggestion and accusation
	//allow for player to have no moves, right now breaks it
	//add ending screen/pop-up when game is over
	
}
