package utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import controller.MinesweeperController;
import model.MinesweeperModel;

/**
 * JUnit Testing class that includes test cases for the testable components of the 
 * Minesweeper controller, model, utility classes.<br><br> 
 * 
 * XXX:% Branch, Path, and Statement Coverage for:<ul>
 * 		<li>MinesweeperController.java</li>
 * </ul>
 * <br>
 * Utilizes the following Exception classes:<ul>
 * 		<li>IllegalFlagPlacementException.java</li>
 * 		<li>IllegalStepException.java</li></ul>
 * 
 * @author Katherine Wilson
 */
public class MinesweeperTests {
	//------------------------------------------------------- MINESWEEPER CONTROLLER TESTS -------------------------------------------------------	
	
	@Test
	void step_test_first_step() {				// first step should always be safe
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			assertEquals(true, controller.takeStep(0, 0));
			assertEquals(true, model.getMinefield()[0][0].isRevealed());
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
			controller.placeFlag(3, 3);
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
			controller.placeFlag(3, 5);
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
			controller.placeFlag(6, 1);
			assertEquals(true, model.getMinefield()[1][6].hasFlag());
			assertEquals(1, model.getFlagsPlaced());
			controller.placeFlag(6, 1);
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
		
		assertThrows(IllegalFlagPlacementException.class, () -> controller.placeFlag(5, 5));
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
				controller.placeFlag(col, row);
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
				controller.placeFlag(col, row);
				assertEquals(true, model.getMinefield()[row][col].hasFlag());
				assertEquals(i+1, model.getFlagsPlaced());
			} catch (IllegalFlagPlacementException e) {
				fail();
			}
			col++;
		}
		assertEquals(model.getNumberofMines(), model.getFlagsPlaced());
		assertThrows(IllegalFlagPlacementException.class, () -> controller.placeFlag(model.getDimensions()[0]-1, model.getDimensions()[1]-1));
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
				controller.placeFlag(col, row);
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
			controller.placeFlag(0, 0);
			assertEquals(model.getNumberofMines()-1, model.getFlagsPlaced());
		} catch (IllegalFlagPlacementException e) {
			fail();
		}
	}
	

}
