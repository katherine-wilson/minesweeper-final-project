/**
 * This class serves as the main class for the Minesweeper game. 
 * The program will launch the GUI view or text view based on the
 * command-line arguments. If this game is launched with no arguments,
 * then the GUI view will be loaded with 25 mines and a 16x16 minefield.
 * 
 * The GUI view also offers the ability to save/load the state of a Minesweeper game if 
 * the user quits before the end of the game. Every time the application is launched, the
 * most recent save is loaded.
 * 
 * @see <code><a href="../view/MinesweeperView.html"> MinesweeperView</a></code>
 * 
 * @author Giang Huong Pham
 * @author Katherine Wilson
 * @author Eleanor Simon
 * @author Tanmay Agrawal 
 */
package view;

import java.io.File;

import javafx.application.Application;

public class MinesweeperLauncher {
	
	public static void main(String[] args) {
		if (args.length == 3) {
			try {
				if (argIsValid(args)) {
					Application.launch(MinesweeperGUIView.class, args);
				}
				else {
					System.out.println("Please enter valid positive number for the length and width of "
							+ "the mine field and the number of mines. The number of mines should be less "
							+ "than (length-1)(width-1)");
				}
			} catch (Exception e) {
				System.out.println("Please enter valid positive number for the length and width of "
						+ "the mine field and the number of mines. The number of mines should be less "
						+ "than (length-1)(width-1)");
			}
		} else {
			String[] arg = {"16", "16", "25"};
			Application.launch(MinesweeperGUIView.class, arg);
		}
	}
	
	private static boolean argIsValid(String[] args) {
		try {
			int length = Integer.parseInt(args[0]);
			int width = Integer.parseInt(args[1]);
			int count = Integer.parseInt(args[2]);
			if (length <= 0 || length > 30 || width <= 0 || width > 30 || count <= 0) {
				return false;
			} 
			return count < (length-1)*(width-1);
		} catch (Exception e) {
			throw e;
		}
	} 
	
}
