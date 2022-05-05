package utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import controller.MinesweeperController;
import model.MinesweeperModel;

/**
 * JUnit Testing class that includes test cases for the testable components of the 
 * Minesweeper controller, model, utility classes.<br><br> 
 * 
 * >95% Branch, Path, and Statement Coverage for:<ul>
 * 		<li><code>MinesweeperModel</code></li>
 * 		<li><code>MinesweeperController</code></li>
 * 		<li><code>IllegalStepException</code></li>
 * 		<li><code>IllegalFlagPlacementException</code></li>
 * 		<li><code>Space</code></li>
 * </ul>
 * <br>
 * Utilizes the following Exception classes:<ul>
 * 		<li>IllegalFlagPlacementException.java</li>
 * 		<li>IllegalStepException.java</li></ul>
 * 
 * @author Katherine Wilson
 * @author Giang Huong Pham
 */
public class MinesweeperTests {
	//------------------------------------------------------- MINESWEEPER CONTROLLER TESTS -------------------------------------------------------	
	@Test
	void step_test_first_step() {				// first step should always be safe
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			assertEquals(0, model.getSpacesRevealed());
			assertEquals(true, controller.takeStep(0, 0));
			assertEquals(true, model.getMinefield()[0][0].isRevealed());
			assertNotEquals(0, model.getSpacesRevealed());
		} catch (IllegalStepException e) {
			fail();
		}
	}
	
	@Test
	void step_test_step_on_same_space() {		// attempts to step on space that's already been stepped on (should throw exception)
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			assertEquals(true, controller.takeStep(1, 1));
		} catch (IllegalStepException e) {
			
			fail();
		}
		
		assertThrows(IllegalStepException.class, () -> controller.takeStep(1, 1));
	}
	
	@Test
	void step_on_flag() {						// attempts to step on flag
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			controller.toggleFlag(3, 3);
		} catch (IllegalFlagPlacementException e) {
			fail();
		}
		
		assertThrows(IllegalStepException.class, () -> controller.takeStep(3, 3));
	}
	
	@Test
	void place_flag() {							// valid placement of flag
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			assertEquals(0, model.getFlagsPlaced());
			controller.toggleFlag(3, 5);
			assertEquals(true, model.getMinefield()[5][3].hasFlag());
			assertEquals(1, model.getFlagsPlaced());
		} catch (IllegalFlagPlacementException e) {
			fail();
		}
	}
	
	@Test
	void place_and_remove_flag() {				// valid placement and removal of flag
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			controller.toggleFlag(6, 1);
			assertEquals(true, model.getMinefield()[1][6].hasFlag());
			assertEquals(1, model.getFlagsPlaced());
			controller.toggleFlag(6, 1);
			assertEquals(false, model.getMinefield()[1][6].hasFlag());
			assertEquals(0, model.getFlagsPlaced());
		} catch (IllegalFlagPlacementException e) {
			fail();
		}
	}
	
	@Test
	void place_flag_on_another_flag() {			// invalid placement of flag (on a revealed space)
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			controller.takeStep(5, 5);
			assertEquals(true, model.getMinefield()[5][5].isRevealed());
		} catch (IllegalStepException e) {
			fail();
		}
		
		assertThrows(IllegalFlagPlacementException.class, () -> controller.toggleFlag(5, 5));
	}
	
	@Test
	void place_maximum_flags() {				// ensures that the maximum number of flags can be placed without issue
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		int row = 0; int col = 0;
		for (int i = 0; i < model.getNumberofMines(); i++) {
			if (col == model.getDimensions()[1]) {
				col = 0;
				row++;
			}
			try {
				controller.toggleFlag(col, row);
				assertEquals(true, model.getMinefield()[row][col].hasFlag());
				assertEquals(i+1, model.getFlagsPlaced());
			} catch (IllegalFlagPlacementException e) {
				fail();
			}
			col++;
		}
		assertEquals(model.getNumberofMines(), model.getFlagsPlaced());
	}
	
	@Test
	void place_too_many_flags() {				// ensures exception is thrown when player attempts to put more flags than available
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		int row = 0; int col = 0;
		for (int i = 0; i < model.getNumberofMines(); i++) {
			if (col == model.getDimensions()[1]) {
				col = 0;
				row++;
			}
			try {
				controller.toggleFlag(col, row);
				assertEquals(true, model.getMinefield()[row][col].hasFlag());
				assertEquals(i+1, model.getFlagsPlaced());
			} catch (IllegalFlagPlacementException e) {
				fail();
			}
			col++;
		}
		assertEquals(model.getNumberofMines(), model.getFlagsPlaced());
		assertThrows(IllegalFlagPlacementException.class, () -> controller.toggleFlag(model.getDimensions()[0]-1, model.getDimensions()[1]-1));
	}
	
	@Test
	void remove_flag_at_maximum() {				// ensures player can still remove flags when maximum number is on the minefield
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		int row = 0; int col = 0;
		for (int i = 0; i < model.getNumberofMines(); i++) {
			if (col == model.getDimensions()[1]) {
				col = 0;
				row++;
			}
			try {
				controller.toggleFlag(col, row);
				assertEquals(true, model.getMinefield()[row][col].hasFlag());
				assertEquals(i+1, model.getFlagsPlaced());
			} catch (IllegalFlagPlacementException e) {
				fail();
			}
			col++;
		}
		assertEquals(model.getNumberofMines(), model.getFlagsPlaced());
		try {
			assertEquals(true, model.getMinefield()[0][0].hasFlag());
			controller.toggleFlag(0, 0);
			assertEquals(model.getNumberofMines()-1, model.getFlagsPlaced());
		} catch (IllegalFlagPlacementException e) {
			fail();
		}
	}
	
	@Test
	void step_on_mine() {						// tests game behavior when a mine is stepped on
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		int row = 0; int col = 0;
		boolean steppedOnMine = false;
		while (!steppedOnMine) {
			try {
				if (col == model.getDimensions()[1]) {
					col = 0;
					row++;
				}
				col++;
				steppedOnMine = !controller.takeStep(col, row);
			} catch (IllegalStepException | IndexOutOfBoundsException e) { }
		}
		assertEquals(true, model.isGameOver());
		assertEquals(false, model.getPlayerWon());
	}
	
	
	//------------------------------------------------------- MINESWEEPER MODEL TESTS -------------------------------------------------------	
	/*
	@Test
	void load_save() {							// checks that data is loaded properly from a save
		MinesweeperModel model;
		try {
			model = new MinesweeperModel("./src/utilities/saved_game.dat");
			MinesweeperController controller = new MinesweeperController(model);
			assertEquals(30, model.getTime());
			assertEquals(2, model.getFlagsPlaced());
			assertEquals(3, model.getTurns());
		} catch (FileNotFoundException e) {
			fail();
		} catch (ClassNotFoundException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}
	*/
	
	@Test
	void winner() {								// ensures that whether the game was won or lost is not falsely determined at the start of a game
		MinesweeperModel model = new MinesweeperModel();
		assertEquals(false, model.getPlayerWon());
	}
	
	@Test
	void exit() {								// tests the model's exit method to make sure it runs when the game is in progress
		MinesweeperModel model = new MinesweeperModel();
		assertEquals(false, model.getPlayerWon());
		assertEquals(false, model.isGameOver());
		model.gameExit();
	}
	
	
	//------------------------------------------------------- OTHER TESTS -------------------------------------------------------	
	@Test
	void time_functions() {						// checks that elapsed time is stored and updated properly with controller/model methods
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		assertEquals(0, controller.getTime());
		assertEquals(0, model.getTime());
		
		controller.setTime(5);
		assertEquals(5, controller.getTime());
		assertEquals(5, model.getTime());
	}
	
	@Test
	void space_strings() {						// checks that the right strings are returned by Space's toString() method
		Space[] spaces = new Space[6];
		for (int i = 0; i < 6; i++) {
			spaces[i] = new Space();
		}
		spaces[0].placeMine(); spaces[0].reveal();
		spaces[1].placeFlag();
		spaces[2].placeMine();
		spaces[3].addAdjacentMine(); spaces[3].addAdjacentMine(); spaces[3].reveal();
		spaces[4].reveal();
		
		assertEquals("*", spaces[0].toString());
		assertEquals("F", spaces[1].toString());
		assertEquals("M", spaces[2].toString());
		assertEquals("2", spaces[3].toString());
		assertEquals(" ", spaces[4].toString());
		assertEquals(".", spaces[5].toString());
	}
	
	@Test
	void space_booleans() {
		Space[] spaces = new Space[6];
		for (int i = 0; i < 6; i++) {
			spaces[i] = new Space();
		}
		spaces[0].placeMine(); spaces[0].reveal();
		spaces[1].placeFlag();
		spaces[2].placeMine();
		spaces[3].addAdjacentMine(); spaces[3].addAdjacentMine(); spaces[3].reveal();
		spaces[4].reveal();
		
		assertEquals(true, spaces[0].hasMine());
		assertEquals(true, spaces[0].isRevealed());
		assertEquals(false, spaces[0].hasFlag());
		
		assertEquals(true, spaces[1].hasFlag());
		assertEquals(false, spaces[1].hasMine());
		assertEquals(false, spaces[1].isRevealed());
		
		assertEquals(true, spaces[2].hasMine());
		assertEquals(false, spaces[2].isRevealed());
		assertEquals(0, spaces[2].adjacentMines());
		assertEquals(false, spaces[2].hasFlag());
		
		assertEquals(false, spaces[3].hasMine());
		assertEquals(false, spaces[3].hasFlag());
		assertEquals(2, spaces[3].adjacentMines());
		assertEquals(true, spaces[3].isRevealed());
		
		assertEquals(true, spaces[4].isRevealed());
		assertEquals(false, spaces[4].hasFlag());
		assertEquals(false, spaces[4].hasMine());
		assertEquals(0, spaces[4].adjacentMines());
		
		assertEquals(false, spaces[5].isRevealed());
		assertEquals(false, spaces[5].hasFlag());
		assertEquals(false, spaces[5].hasMine());
		assertEquals(0, spaces[5].adjacentMines());
		
		spaces[0].removeMine();
		spaces[1].removeFlag();
		assertEquals(false, spaces[0].hasMine());
		assertEquals(false, spaces[1].hasFlag());
		
	}
	
	@Test
	void exception_strings() {					// checks that messages are added to custom exceptions properly
		IllegalFlagPlacementException fpe = new IllegalFlagPlacementException("Flag");
		assertEquals("INVALID FLAG PLACEMENT: Flag", fpe.toString());
		
		IllegalStepException se = new IllegalStepException("Step");
		assertEquals("INVALID STEP: Step", se.toString());
	}
}
