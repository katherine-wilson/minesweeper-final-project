package controller;

import java.awt.Point;

import model.MinesweeperModel;
import utilities.Space;

public class MinesweeperController {
	
	private MinesweeperModel model;
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
	}
	
	public boolean takeStep(int x, int y) throws IllegalArgumentException {
		if (!model.isValidStep(new Point(x, y))) {
			throw new IllegalArgumentException();		// TODO: create custom exception class to replace this
		} else {
			return model.takeStep(new Point(x, y));
		}
	}
	
	public boolean isGameOver() {
		return model.isGameOver();
	}
	public boolean playerWon() {
		return model.getPlayerWon();
	}
	
	public Space[][] getGrid() {
		return model.getGrid();
	}

}
