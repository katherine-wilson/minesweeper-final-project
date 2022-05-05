package view;

import javafx.application.Application;

/**
 * This class serves as the main class for the Minesweeper game. 
 * The program will launch the GUI view or text view based on the
 * command-line arguments. If this game is launched with no arguments,
 * then the GUI view will be loaded with 25 mines and a 16x16 minefield.
 * Otherwise, this class expects to receive three positive integers that
 * represent the length, width, and number of mines in the minefield, in 
 * that order. The minimum length and width of the minefield is 4 spaces
 * long. The number of mines that can be placed must be less than half
 * the number of spaces in the field.
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
public class MinesweeperLauncher {
	
	/**
	 * Launches the Minesweeper game. The dimensions of the minefield and number of mines
	 * will depend on the command-line arguments given.
	 * 
	 * @param args argument given by the player from the command line. These arguments should
	 * be three non-negative, non-zero integers that represent the length, width, and number
	 * of mines in the minefield. The arguments must be provided in that order.
	 */
	public static void main(String[] args) {
		if (args.length == 3) {
			try {
				if (argIsValid(args)) {
					Application.launch(MinesweeperGUIView.class, args);
				}
				else {
					System.out.println("Please enter valid positive number for the length and width of "
							+ "the mine field and the number of mines. The number of mines should be less "
							+ "than (length*width)/2");
				}
			} catch (Exception e) {
				System.out.println("Please enter valid positive number for the length and width of "
						+ "the mine field and the number of mines. The number of mines should be less "
						+ "than (length*width)/2");
			}
		} else if (args.length == 0) {
			String[] arg = {"16", "16", "25"};
			Application.launch(MinesweeperGUIView.class, arg);
		} else {
			System.out.println("Please leave the arguments blank for a default setting"
					+ " or enter valid positive number for the length and width of "
					+ "the mine field and the number of mines. The number of mines should be less "
					+ "than (length*width)/2");
		}
	}
	
	/**
	 * Validations the command-line arguments provided by the player to customize
	 * the minefield.
	 * 
	 * @param args command-line arguments provided by the player to customize the minefield.
	 * 			   This array should contain three strings: <ol>
	 * 					<li>Length of the minefield</li>
	 * 					<li>Width of the minefield</li>
	 * 					<li>Number of mines in the minefield</li>
	 * 				</ol>
	 * 
	 * @return <code>true</code> if the given arguments are valid integers that fall within
	 * the limitations of the game and <code>false</code> if not. These limitations are as
	 * follows:<ul>
	 * 				<li>Length/width of the minefield must be greater than three</li>
	 * 				<li>length/width of the minefield may not exceed 30</li>
	 * 				<li>The number of mines must be greater than zero</li>
	 * 				<li>The number of mines must be less than half the spaces in the minefield</li>
	 * </ul>
	 */
	private static boolean argIsValid(String[] args) {
		try {
			int length = Integer.parseInt(args[0]);
			int width = Integer.parseInt(args[1]);
			int count = Integer.parseInt(args[2]);
			// 4 is the minimum because the mine cannot occur in a non-adjacent space if you click in the center of a 3x3 grid
			if (length <= 3 || length > 30 || width <= 3 || width > 30 || count <= 0) {
				return false;
			} 
			return count < (length*width) / 2.0;
		} catch (Exception e) {
			throw e;
		}
	} 
	
}
