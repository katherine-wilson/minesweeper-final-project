package view;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.MinesweeperController;
import model.MinesweeperModel;
import utilities.Space;

@SuppressWarnings("deprecation")
public class MinesweeperTextView implements Observer {

	MinesweeperController controller;
	
	public void start() { 
		MinesweeperModel model = new MinesweeperModel();
		model.addObserver(this);
		controller = new MinesweeperController(model);
		printField(model);
		inputLoop(controller);
	}
	
	// coordinates must be entered in format: "x y"
	private void inputLoop(MinesweeperController controller) {
		System.out.println("Welcome to Minesweeper! Type a pair of coordinates in the format 'x y' to begin.");
		while (!controller.isGameOver()) {
			Scanner input = new Scanner(System.in);
			if (input.hasNextLine()) {
				String coords[] = input.nextLine().split(" ");
				int x = Integer.parseInt(coords[0]);
				int y = Integer.parseInt(coords[1]);
				try {
					controller.takeStep(x, y);
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid step! Try again. You cannot step on flags or revealed spaces.");
				}
			}
		}
		if (controller.playerWon()) {
			System.out.println("Congratulations! You won.");
		} else {
			System.out.println("You lost. Try again!");
		}
	}

	
	private void printField(MinesweeperModel model) {
		Space[][] grid = model.getGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				System.out.print(grid[grid.length-i-1][j] + " ");;
			}
			System.out.println();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		MinesweeperModel model = (MinesweeperModel) o;
		printField(model);
		System.out.println("Moves Made: " + model.getTurns());
		System.out.println("Spaces Freed: " + model.getSquaresRevealed() + "/" + (model.getBoardDimensions()[0] * model.getBoardDimensions()[1] - 10));
	}
}
