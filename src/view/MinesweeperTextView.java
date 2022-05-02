package view;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.MinesweeperController;
import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;
import utilities.Space;

@SuppressWarnings("deprecation")
public class MinesweeperTextView implements Observer {

	MinesweeperController controller;
	
	public void start() { 
		MinesweeperModel model = new MinesweeperModel();
		model.addObserver(this);
		controller = new MinesweeperController(model);
		printField(model);
		System.out.println("Welcome to Minesweeper!");
		inputLoopStep(controller, model);
	}
	
	// coordinates must be entered in format: "x y"
	private void inputLoopStep(MinesweeperController controller, MinesweeperModel model) {
		System.out.println("Type a pair of coordinates in the format 'x y' to begin. Enter 'F' to switch to flag mode.");
		while (!model.isGameOver()) {
			Scanner input = new Scanner(System.in);
			if (input.hasNextLine()) {
				String coords[] = input.nextLine().split(" ");
				if (coords[0].equals("F") || coords[0].equals("f")) {
				 	inputLoopFlag(controller, model);
				} else {
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					try {
						controller.takeStep(x, y);
					} catch (IllegalStepException e) {
						System.out.println(e);
					}
				}
			}
		}
	}

	// coordinates must be entered in format: "x y"
	private void inputLoopFlag(MinesweeperController controller, MinesweeperModel model) {
		System.out.println("Placing flags. Enter the coordinates of the Flag you want to replace/remove in the format 'x y'.\nEnter 'M' to go back to taking steps.");
		while (!model.isGameOver()) {
			Scanner input = new Scanner(System.in);
			if (input.hasNextLine()) {
				String coords[] = input.nextLine().split(" ");
				if (coords[0].equals("M") || coords[0].equals("m")) {
				 	inputLoopStep(controller, model);
				} else {
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					try {
						controller.toggleFlag(x, y);
					} catch (IllegalFlagPlacementException e) {
						System.out.println(e);
					}
				}
			}
		}
	}
	
	private void printField(MinesweeperModel model) {
		Space[][] grid = model.getMinefield();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[grid.length-i-1][j].hasMine() && !grid[grid.length-i-1][j].hasFlag()) {
					System.out.print(". ");
				} else {
					System.out.print(grid[grid.length-i-1][j] + " ");;
				}
			}
			System.out.println();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		MinesweeperModel model = (MinesweeperModel) o;
		printField(model);
		System.out.println("Moves Made: " + model.getTurns());
		System.out.println("Spaces Freed: " + model.getSpacesRevealed() + "/" + (model.getDimensions()[0] * model.getDimensions()[1] - 10));
		System.out.println("Flags: " + model.getFlagsPlaced() + "/" + model.getNumberofMines());
		if (model.isGameOver()) {
			if (model.getPlayerWon()) {
				System.out.println("Congratulations! You won.");
			} else {
				System.out.println("You stepped on a mine. Try again!");
			}
		}
	}
}
