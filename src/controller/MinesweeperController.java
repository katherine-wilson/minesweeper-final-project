package controller;

import java.awt.Point;

import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;

/**
 * This class is designed to mediate any necessary interactions between the model 
 * and the view that cannot otherwise be handled by the <code>Observer</code>/<code>Observable</code>
 * design pattern for the Minesweeper game. This controller's primary interactions with the model 
 * consist of interpreting the player's input provided by the view (taking steps and placing
 * flags) and retrieving any necessary gameplay state information stored in the model. This
 * includes information such as whether or not the game is over or if the player won or lost.
 * 
 * To construct this class, an instance of <code><a href="../model/MinesweeperModel.html">
 * MinesweeperModel</a></code> is needed.
 * 
 * @see <code><a href="../view/MinesweeperView.html"> MinesweeperView</a></code>
 * @see <code><a href="../model/MinesweeperModel.html"> MinesweeperModel</a></code>
 * @see <code><a href="../utilities/Space.html">Space</a></code>
 *
 * @author Katherine Wilson
 */
public class MinesweeperController {
	// ------------------------------------------------------[  FIELDS  ]------------------------------------------------------
	/**
	 * Minesweeper model that this controller interacts with.
	 * This model will receive input from the view that is
	 * passed to this controller and provide gameplay data
	 * for functions that return it to the view, such as
	 * {@link #getMinefield()}.
	 */
	private MinesweeperModel model;
	
	
	// --------------------------------------------------[  PUBLIC METHODS  ]--------------------------------------------------
	/**
	 * Creates a <code>MinesweeperController</code> object.
	 * A reference to the <code><a href="../model/MinesweeperModel.html"> MinesweeperModel</a></code>
	 * parameter is stored by this object and will act as the specific instance that this controller
	 * interacts with.
	 * 
	 * @param board model instance associated with this controller instance.
	 */
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
	}
	
	/**
	 * Uses the x and y coordinates given based on the player's input to
	 * add a step to the model. The values are used to instantiate
	 * a <code>Point</code> object, which is then passed to the
	 * model, which will update the game's state accordingly.
	 * 
	 * @param x The x-coordinate of the player's new step in the minefield.
	 * 
	 * @param y The y-coordinate of the player's new step in the minefield.
	 * 
	 * @return <code>true</code>  if a safe step was made.
	 * 	   <br><code>false</code> if the player stepped on a mine.
	 *  
	 * @throws IllegalStepException when an invalid location is given
	 * for a step. This occurs when: <ul>
	 * <li>The player attempts to step on a flag</li>
	 * <li>The player attempts to step on an already revealed space</li>
	 * </ul>
	 */
	public boolean takeStep(int x, int y) throws IllegalStepException {
		if (model.getMinefield()[y][x].hasFlag()) {
			throw new IllegalStepException("Cannot step on a flag.");
		} else if (model.getMinefield()[y][x].isRevealed()) {
			throw new IllegalStepException("Cannot step on a revealed space.");
		} else {
			return model.takeStep(new Point(x, y));
		}
	}
	
	/**
	 * Uses the x and y coordinates given based on the user's input to
	 * add a flag to the model's minefield. The values are used to instantiate
	 * a <code>Point</code> object, which is passed to the model to update
	 * the game's state accordingly. This function will also remove flags.
	 * 
	 * @param x The x-coordinate of the player's new flag in the minefield.
	 * 
	 * @param y The y-coordinate of the player's new flag in the minefield.
	 * 
	 * @throws IllegalFlagPlacementException when an invalid location is given
	 * for flag placement. This occurs when: <ul>
	 * <li>The player attempts to place a flag on a revealed safe space</li>
	 * <li>The player attempts to place a flag when they're out of flags</li>
	 * </ul>
	 */
	public void toggleFlag(int x, int y) throws IllegalFlagPlacementException {
		if (model.getMinefield()[y][x].isRevealed()) {
			throw new IllegalFlagPlacementException("Cannot place a flag on a revealed space.");
		} else if (model.getFlagsPlaced() == model.getNumberofMines() && !model.getMinefield()[y][x].hasFlag()) {
			throw new IllegalFlagPlacementException("Out of flags!");
		} else {
			model.toggleFlag(new Point(x, y));
		}
	}
	
	/**
	 * Returns the time that has elapsed since the start of the game.
	 * 
	 * @return the time (in seconds) that have elapsed since the player's
	 * first move.
	 */
	public int getTime() {
		return model.getTime();
	}
	
	/**
	 * Manually sets the elapsed time stored in the model.
	 * 
	 * @param time new elapsed time (in seconds).
	 */
	public void setTime(int time) {
		model.setTime(time);
	}
	
	/**
	 * Signals to the model that a new game save should be created.
	 */
	public void saveGameState() {
		model.saveGameData();
	}
}
