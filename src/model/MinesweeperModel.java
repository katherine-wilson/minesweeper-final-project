package model;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;

import utilities.Space;

/**
 * This class stores and maintains the game state for a Minesweeper game.
 * It keeps track of the locations of mines, flags, and safe spaces in the
 * minefield as well which parts of the minefield have been "revealed" by 
 * the player. It also stores various gameplay data such as the number of
 * mines in the minefield, the dimensions of the minefield, the number of
 * flags that have been placed, whether or not the game is over, and other
 * information.
 * <br><br>
 * This class is designed to communicate with
 * <code><a href="../controller/MinesweeperController.html"> MinesweeperController</a></code>
 * and be observed by a Minesweeper view.
 * 
 * @see <code><a href="../controller/MinesweeperController.html"> MinesweeperController</a></code>
 * @see <code><a href="../view/MinesweeperView.html"> MinesweeperView</a></code>
 * @see <code><a href="../utilities/Space.html">Space</a></code>
 * @see Observable
 * 
 * @author Katherine Wilson
 * @author Giang Huong Pham
 */
@SuppressWarnings("deprecation")
public class MinesweeperModel extends Observable {
	// ------------------------------------------------------[  FIELDS  ]------------------------------------------------------	
	/**
	 * Width of mine field.
	 */
	private static int FIELD_WIDTH;		// XXX: changed with difficulty level (if implemented)
	/**
	 * Length of the mine field.
	 */
	private static int FIELD_LENGTH;	// XXX: changed with difficulty level
	/**
	 * Number of mines in the mine field.
	 */
	private static int NUMBER_OF_MINES = 25; // XXX: changed with difficulty level 
	
	/**
	 * Number of flags that have been placed in the field so far by the player.
	 * This value should not exceed the number of mines in the field.
	 */
	private int flagsPlaced;

	/**
	 * Two-dimensional grid of <code>Space</code> objects that represent each possible
	 * step in the minefield. The first index represents the y-value of a <code>Space</code>.
	 * The second index represents the x-value of <code>Space</code>. Note that rows are
	 * from bottom to top (Ex. the first row represents the bottom of the minefield, the second
	 * represents the one on top of that one, etc.).
	 */
	private Space[][] minefield;

	/**
	 * <code>HashSet</code> that stores the locations of each mine on the minefield using
	 * <code>Point</code> objects.
	 */
	private HashSet<Point> mineLocations;

	/**
	 * Number of safe <code>Space</code> objects that the player has been revealed by the
	 * player so far. This number increases with each safe step the player makes. This will
	 * reveal the <code>Space</code> they stepped on as well as any "contiguous zeroes".
	 */
	private int safeSpacesRevealed;

	/**
	 * Number of steps made by the player so far. This is used to determine if a mine should
	 * be replaced, should a player step on a mine in their first turn.
	 */
	private int stepsTaken;

	/**
	 * <code>true</code> if the game has been won or lost by the player.
	 * <br><code>false</code> if the game is still in progress. The player
	 * can win by revealing all safe steps in the minefield. The player
	 * loses by stepping on a mine.
	 */
	private boolean gameOver;
	
	/**
	 * <code>time elapsed</code> in seconds since the start of the game. 
	 */
	private int timeInSeconds;

	// --------------------------------------------------[  PUBLIC METHODS  ]--------------------------------------------------
	/**
	 * Creates a <code>MinesweeperModel</code> object by
	 * loading data from the input file.
	 * 
	 * @param filename name of the file which saves data from the old game.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public MinesweeperModel(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
		MinesweeperModel model = (MinesweeperModel) input.readObject();
		if (model != null) {
			this.safeSpacesRevealed = model.safeSpacesRevealed;
			this.stepsTaken = model.stepsTaken;
			this.flagsPlaced = model.flagsPlaced;
			this.gameOver = model.gameOver;
			this.timeInSeconds = model.timeInSeconds;
		}else {
			this.defaultSetting();
		}
	}
	
	/**
	 * Creates a <code>MinesweeperModel</code> object and initializes it
	 * to a default state by calling helper function to set all fields
	 * to the beginning value.
	 */
	public MinesweeperModel() {
		this.defaultSetting();
	}
	
	/**
	 * Initialize the MinesweeperModel to a default state. 
	 * By default, the <code>MinesweeperModel</code>
	 * has no revealed safe squares, no turns made, no flags placed, and
	 * has not been won or lost yet. This method will also initialize the
	 * minefield and place mines within it.
	 */
	private void defaultSetting() {
		this.FIELD_WIDTH = 16;
		this.FIELD_LENGTH = 16;
		safeSpacesRevealed = 0;
		stepsTaken = 0;
		flagsPlaced = 0;
		gameOver = false;
		timeInSeconds = 0;
		initField();
		placeMines();
	}

	/**
	 * Updates the minefield with the player's new step. This method
	 * will update the number of turns made by the player and add their
	 * step to appropriate <code>Space</code> in the {@link #minefield} field.
	 * If the player stepped on a mine, the game will end. If the player's
	 * step was safe, any "contiguous zeroes" will be revealed. If this is
	 * the player's first step and they happen to step on a mine, then that
	 * mine will be moved and the minefield will be updated accordingly. This
	 * function marks this model as changed and notifies observers of the
	 * changes.
	 * 
	 * @param location <code>Point</code> object that stores the x and y-coordinates
	 * 				   of the player's step.
	 * 
	 * @return <code>true</code>  if the step was safe.
	 * 	   <br><code>false</code> if the player stepped on a mine.
	 */
	public boolean takeStep(Point location) { 
		if (!gameOver) {		// only updates game state if the game is still in progress
			if (stepsTaken == 0) {
				replaceMine(location);
				markSpacesAdjacentToMines();
			}
			
			boolean safeStep = !minefield[location.y][location.x].hasMine();
			if (!safeStep) {										// if player stepped on a mine...
				if (stepsTaken != 0) {								
					minefield[location.y][location.x].reveal();		// marks mine as revealed
					gameOver = true;							// ends game
				}
			} else {												// if the player's step was safe...
				revealContiguousZeroes(location, new HashSet<Point>());
				if (safeSpacesRevealed == (FIELD_WIDTH * FIELD_LENGTH - NUMBER_OF_MINES)) {
					System.out.println("Player won.");
					gameOver = true; // ends game if this step reveals the remaining safe spaces on the board
				}	
			}
			stepsTaken++;
			this.setChanged();
			this.notifyObservers(false);
			return safeStep;
		}
		return true;		// ignores input
	}
	
	/**
	 * Adds or removes a flag from the minefield to the <code>Space</code>
	 * chosen by the player. If the <code>Space</code> already has a flag,
	 * then that flag will be removed. If the <code>Space</code> doesn't
	 * have a flag, then a flag will be placed there. This method does not
	 * check if the flag's placement is valid. This method will also mark
	 * this model as changed and notify any observers of the changes.
	 * 
	 * @param location <code>Point</code> object that stores the location
	 * 		  chosen by the player to add/remove a flag
	 */
	public void toggleFlag(Point location) {
		if (minefield[location.y][location.x].hasFlag()) {
			minefield[location.y][location.x].removeFlag();
			flagsPlaced--;
		} else {
			minefield[location.y][location.x].placeFlag();
			flagsPlaced++;
		}
		setChanged();
		notifyObservers(false);
	}

	/**
	 * Returns the 2-dimensional grid of <code>Space</code> objects used
	 * to store data about each location in the minefield.
	 * 
	 * @return 2-dimensional grid of <code>Space</code> objects that represent
	 * 		   the minefield.
	 */
	public Space[][] getMinefield() {
		return minefield;
	}

	/**
	 * Returns the status of the game. This should be used to determine if the
	 * game is in progress or not.
	 * 
	 * @return <code>true</code>  if the game has ended (the player won or lost)
	 *     <br><code>false</code> if the game is still going (the player hasn't won or lost yet)
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	/**
	 * Returns the number of steps taken by the player so far.
	 * 
	 * @return an integer representing the number of steps taken by the player so far.
	 */
	public int getTurns() {
		return stepsTaken;
	}
	
	/**
	 * Returns the number of spaces revealed by the player so far.
	 * Spaces are revealed by stepping on them or through contiguous
	 * means.
	 * 
	 * @return an integer representing the number of safe locations revealed
	 * by the player so far.
	 */
	public int getSpacesRevealed() {
		return safeSpacesRevealed;
	}
	
	/**
	 * Returns the number of flags that are currently in the minefield.
	 * 
	 * @return an integer representing the number of flags in the minefield.
	 */
	public int getFlagsPlaced() {
		return flagsPlaced;
	}
	
	/**
	 * Returns the number of mines that are in the minefield.
	 * 
	 * @return an integer representing the number of mines in the minefield.
	 */
	public int getNumberofMines() {
		return NUMBER_OF_MINES;
	}

	/**
	 * Returns the dimensions of the minefield.
	 * 
	 * @return the dimensions of the minefield as an array of two elements: [length, width].
	 */
	public int[] getDimensions() {
		return new int[] { FIELD_LENGTH, FIELD_WIDTH };
	}
	
	/**
	 * gets the current time in seconds
	 * @return the current time in seconds
	 */
	public int getTime() {
		return this.timeInSeconds;
	}
	
	/**
	 * Updates the current time in seconds. 
	 * @param time
	 */
	public void setTime(int time) {
		if(!this.gameOver) {
			this.timeInSeconds = time;
		}
		this.setChanged();
		this.notifyObservers(!this.isGameOver());
	}

	/**
	 * Returns whether or not the player has won.
	 * 
	 * @return <code>true</code>  if the player has revealed all of the safe <code>Space</code> objects in the minefield.
	 * 	   <br><code>false</code> if the player has not revealed them all yet.
	 */
	public boolean getPlayerWon() {
		return safeSpacesRevealed == (FIELD_WIDTH*FIELD_LENGTH - NUMBER_OF_MINES);
	}
	
	/**
	 * This method is meant to be called when the user try to close the window. 
	 */
	public void gameExit() {
		if (!this.isGameOver()) {
			setChanged();
			this.notifyObservers(true);
		}
	}
	
	// --------------------------------------------------[  PRIVATE METHODS  ]--------------------------------------------------
	/**
	 * Iterates over each <code>Space</code> object in the minefield and calculates
	 * the number of mines that are adjacent to it. A mine is considered "adjacent"
	 * if it occurs in any of the 8 spaces surrounding a given space. This function
	 * skips over spaces that contain mines. The resulting counts are stored in the
	 * respective <code>Space</code> objects.
	 */
	private void markSpacesAdjacentToMines() {
		for (int i = 0; i < FIELD_LENGTH; i++) {
			for (int j = 0; j < FIELD_WIDTH; j++) {
				if (!mineLocations.contains(new Point(j, i))) { 		// skips spaces containing mines
					if (i != FIELD_LENGTH-1 && minefield[i + 1][j].hasMine()) { 									// checks N
						minefield[i][j].addAdjacentMine();
					}
					if (i != FIELD_LENGTH-1 && j != FIELD_WIDTH-1 && minefield[i + 1][j + 1].hasMine()) {			// checks NE
						minefield[i][j].addAdjacentMine();
					}
					if (j != FIELD_WIDTH-1 && minefield[i][j + 1].hasMine()) { 										// checks E
						minefield[i][j].addAdjacentMine();
					}
					if (i != 0 && j != FIELD_WIDTH-1 && minefield[i-1][j+1].hasMine()) {							// checks SE
						minefield[i][j].addAdjacentMine();
					}
					if (i != 0 && minefield[i-1][j].hasMine()) {													// checks S
						minefield[i][j].addAdjacentMine();
					}
					if (i != 0 && j != 0 && minefield[i-1][j-1].hasMine()) {										// checks SW
						minefield[i][j].addAdjacentMine();
					}
					if (j != 0 && minefield[i][j-1].hasMine()) {													// checks W
						minefield[i][j].addAdjacentMine();
					}
					if (i != FIELD_LENGTH-1 && j != 0 && minefield[i+1][j-1].hasMine()) {							// checks NW
						minefield[i][j].addAdjacentMine();
					}
				}
			}
		}
	}
	
	/**
	 * Recursively marks contiguous zeroes as "revealed" in the {@link #minefield} grid.
	 * Recursion stops when the edge of the minefield is reached, a mine is found, a flag
	 * is found, or an already-revealed location was reached.
	 * 
	 * @param location location in the minefield that the next spaces to be revealed are
	 * 		  derived from.
	 * 
	 * @param checkedLocations <code>HashSet</code> containing locations that have already
	 * 		  been checked so far by this method. 
	 */
	private void revealContiguousZeroes(Point location, HashSet<Point> checkedLocations) {
		if (!checkedLocations.contains(location) && location.x < FIELD_WIDTH && location.x >= 0 && location.y < FIELD_LENGTH && location.y >= 0) {
			if (!minefield[location.y][location.x].hasMine() && !minefield[location.y][location.x].hasFlag() && !minefield[location.y][location.x].isRevealed()) {
				minefield[location.y][location.x].reveal();
				safeSpacesRevealed++;
				checkedLocations.add(location);
				if (minefield[location.y][location.x].adjacentMines() == 0) {
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

	/**
	 * Initializes a blank minefield with no mines. <code>Space</code> objects
	 * with default states are initialized for each location in the minefield.
	 */
	private void initField() {
		minefield = new Space[FIELD_LENGTH][FIELD_WIDTH];
		for (int i = 0; i < FIELD_LENGTH; i++) {
			for (int j = 0; j < FIELD_WIDTH; j++) {
				minefield[i][j] = new Space();
			}
		}
	}

	/**
	 * Adds mines randomly to minefield. This is based on the {@link #NUMBER_OF_MINES}
	 * field. If a mine happens to be placed where another mine already is, then that
	 * mine will be moved to a new location. The locations of the placed mines will
	 * be added to the {@link #mineLocations} set.
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
				minefield[mine.y][mine.x].placeMine();
			}
		}
	}
	
	/**
	 * Replaces a mine if the player steps on it on their first turn. The mine is
	 * removed from the {@link #mineLocations} set and its location in the 
	 * {@link #minefield} grid. If the field happens to be placed in the location
	 * of another mine, it will be replaced until it occupies an empty location that
	 * is not adjacent to step.
	 */
	private void replaceMine(Point location) {
		HashSet<Point> adjacentLocations = new HashSet<Point>();
		adjacentLocations.add(new Point(location.x, location.y));	
		adjacentLocations.add(new Point(location.x+1, location.y));			// E
		adjacentLocations.add(new Point(location.x, location.y+1));			// N
		adjacentLocations.add(new Point(location.x+1, location.y+1));		// NE
		adjacentLocations.add(new Point(location.x-1, location.y));			// W
		adjacentLocations.add(new Point(location.x, location.y-1));			// S
		adjacentLocations.add(new Point(location.x-1, location.y-1));		// SW
		adjacentLocations.add(new Point(location.x+1, location.y-1));		// SE
		adjacentLocations.add(new Point(location.x-1, location.y+1));		// NW
		Random rdm = new Random();
		
		for (Point p : adjacentLocations) {
			if (p.y >= 0 && p.y < FIELD_LENGTH && p.x >= 0 && p.y <= FIELD_WIDTH) {
				if (minefield[p.y][p.x].hasMine()) {
					mineLocations.remove(p);
					minefield[p.y][p.x].removeMine();
					boolean mineAdded = false;
					while (!mineAdded) {
						Point mine = new Point(rdm.nextInt(FIELD_WIDTH), rdm.nextInt(FIELD_LENGTH));
						if (!mineLocations.contains(mine) && !adjacentLocations.contains(mine)) {
							mineLocations.add(mine);
							minefield[mine.y][mine.x].placeMine();
							mineAdded = true;
						}
					}
				}
			}
		}
	}
	
}
