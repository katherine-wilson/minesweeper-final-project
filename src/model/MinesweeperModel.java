package model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;

import utilities.Space;

@SuppressWarnings("deprecation")
public class MinesweeperModel extends Observable {

	private static int FIELD_WIDTH = 16;
	private static int FIELD_LENGTH = 16;
	private static int NUMBER_OF_MINES = 10; // based on difficulty level (if implemented)

	private Space[][] grid;		// first index is used for the rows (y-values), second index is used for the columns (x-values)

	private HashSet<Point> mineLocations;

	private int safeSquaresRevealed;

	private int turnCounter;

	private boolean gameOver;

	public MinesweeperModel() {
		safeSquaresRevealed = 0;
		turnCounter = 0;
		gameOver = false;
		initField();
		placeMines();
	}

	private void markSpacesAdjacentToMines() {
		for (int i = 0; i < FIELD_LENGTH; i++) {
			for (int j = 0; j < FIELD_WIDTH; j++) {
				if (!mineLocations.contains(new Point(j, i))) { 		// skips spaces containing mines
					if (i != FIELD_LENGTH-1 && grid[i + 1][j].hasMine()) { 									// checks N
						grid[i][j].addAdjacentMine();
					}
					if (i != FIELD_LENGTH-1 && j != FIELD_WIDTH-1 && grid[i + 1][j + 1].hasMine()) {		// checks NE
						grid[i][j].addAdjacentMine();
					}
					if (j != FIELD_WIDTH-1 && grid[i][j + 1].hasMine()) { 									// checks E
						grid[i][j].addAdjacentMine();
					}
					if (i != 0 && j != FIELD_WIDTH-1 && grid[i-1][j+1].hasMine()) {							// checks SE
						grid[i][j].addAdjacentMine();
					}
					if (i != 0 && grid[i-1][j].hasMine()) {													// checks S
						grid[i][j].addAdjacentMine();
					}
					if (i != 0 && j != 0 && grid[i-1][j-1].hasMine()) {										// checks SW
						grid[i][j].addAdjacentMine();
					}
					if (j != 0 && grid[i][j-1].hasMine()) {													// checks W
						grid[i][j].addAdjacentMine();
					}
					if (i != FIELD_LENGTH-1 && j != 0 && grid[i+1][j-1].hasMine()) {						// checks NW
						grid[i][j].addAdjacentMine();
					}
				}
			}
		}
	}
	
	private void revealContiguousZeroes(Point location, HashSet<Point> checkedLocations) {
		if (!checkedLocations.contains(location) && location.x < FIELD_WIDTH && location.x >= 0 && location.y < FIELD_LENGTH && location.y >= 0) {
			if (!grid[location.y][location.x].hasMine() && !grid[location.y][location.x].hasFlag() && !grid[location.y][location.x].isRevealed()) {
				grid[location.y][location.x].reveal();
				safeSquaresRevealed++;
				checkedLocations.add(location);
				if (grid[location.y][location.x].adjacentMines() == 0) {
					revealContiguousZeroes(new Point(location.x, location.y+1), checkedLocations); 		// N
					revealContiguousZeroes(new Point(location.x+1, location.y+1), checkedLocations); 	// NE
					revealContiguousZeroes(new Point(location.x+1, location.y), checkedLocations); 		// E
					revealContiguousZeroes(new Point(location.x+1, location.y-1), checkedLocations); 	// SE
					revealContiguousZeroes(new Point(location.x, location.y-1), checkedLocations); 		// S
					revealContiguousZeroes(new Point(location.x-1, location.y-1), checkedLocations); 	// SW
					revealContiguousZeroes(new Point(location.x-1, location.y), checkedLocations); 		// W
					revealContiguousZeroes(new Point(location.x-1, location.y+1), checkedLocations); 	// NW
				}
			}
		}
	}

	public boolean takeStep(Point location) {
		if (!gameOver) {
			boolean safeStep = grid[location.y][location.x].step();
			if (!safeStep) {
				if (turnCounter != 0) {
					grid[location.y][location.x].reveal();
					gameOver = true;
				} else {
					replaceMine(location);
					markSpacesAdjacentToMines();
				}
			} else {
				if (turnCounter == 0) {
					markSpacesAdjacentToMines();
				}
				revealContiguousZeroes(location, new HashSet<Point>());
				if (safeSquaresRevealed == (FIELD_WIDTH * FIELD_LENGTH - NUMBER_OF_MINES)) {
					gameOver = true;
				}
			}
			turnCounter++;
			setChanged();
			notifyObservers();
			return safeStep;
		}
		return true;
	}

	/**
	 * Initializes blank grid with no mines.
	 */
	private void initField() {
		grid = new Space[FIELD_LENGTH][FIELD_WIDTH];
		for (int i = 0; i < FIELD_LENGTH; i++) {
			for (int j = 0; j < FIELD_WIDTH; j++) {
				grid[i][j] = new Space();
			}
		}
	}

	/**
	 * Adds mines to grid.
	 */
	private void placeMines() {
		mineLocations = new HashSet<Point>();
		Random rdm = new Random();
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			Point mine = new Point(rdm.nextInt(FIELD_WIDTH), rdm.nextInt(FIELD_LENGTH));
			if (mineLocations.contains(mine)) {
				i--;
			} else {
				mineLocations.add(mine);
				grid[mine.y][mine.x].placeMine();
			}
		}
	}

	/**
	 * Replaces mine if player clicks on a mine in their first move
	 */
	public void replaceMine(Point location) {
		Random rdm = new Random();
		mineLocations.remove(location);
		grid[location.y][location.x].removeMine();
		boolean mineAdded = false;
		while (!mineAdded) {
			Point mine = new Point(rdm.nextInt(FIELD_WIDTH), rdm.nextInt(FIELD_LENGTH));
			if (!mineLocations.contains(mine)) {
				mineLocations.add(mine);
				grid[mine.y][mine.x].placeMine();
				mineAdded = true;
			}
		}
	}

	public Space[][] getGrid() {
		return grid;
	}

	public boolean isGameOver() {
		return gameOver;
	}
	
	public int getTurns() {
		return turnCounter;
	}
	
	public int getSquaresRevealed() {
		return safeSquaresRevealed;
	}

	// players cannot step on flags or already revealed spaces (numbered or otherwise)
	public boolean isValidStep(Point location) {
		return !grid[location.y][location.x].hasFlag() && !grid[location.y][location.x].isRevealed();
	}
	
	/**
	 * @return dimensions as array of two elements: [length, width]
	 */
	public int[] getBoardDimensions() {
		return new int[] { FIELD_LENGTH, FIELD_WIDTH };
	}

	public boolean getPlayerWon() {
		return safeSquaresRevealed == (FIELD_WIDTH*FIELD_LENGTH - NUMBER_OF_MINES);
	}
	
}
