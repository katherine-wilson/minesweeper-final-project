/**
 * This class serve as the main class for the Minesweeper game. 
 * The program will launch the GUI view or text view based on the
 * commandline arguments. Default mode without any argument is the gui view.
 * 
 * The gui view also offer save/load feature that saves the game is the user quit
 * before the game ends, and loads the most recent game everytime the game is launched. 
 * 
 * @author Giang Huong Pham
 * @author Katherine Wilson
 * @author Eleanor Simon
 * @author Tanmay  Agrawal 
 */
package view;

import javafx.application.Application;

public class MinesweeperLauncher {
	
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].toLowerCase().equals("-text")) {
				MinesweeperTextView textView = new MinesweeperTextView();
				textView.start();
			} else if (args[0].toLowerCase().equals("-gui")) {
				Application.launch(MinesweeperGUIView.class,args );
			} else {
				System.out.println("Invalid command. Please enter -text or -gui "
								+ "or leave blank for gui mode.");
			}
		} else {
			Application.launch(MinesweeperGUIView.class,args );
		}
		
	}
	
}
