package utilities;

/**
 * @author Katherine Wilson
 */
public class Space {
	// ------------------------------------------------------[  FIELDS  ]------------------------------------------------------	
	private boolean hasMine;
	
	private boolean revealed;
	
	private boolean hasFlag;
	
	private int adjacentMines;
	
	
	// --------------------------------------------------[  PUBLIC METHODS  ]--------------------------------------------------
	/**
	 * Constructor 
	 */
	public Space() {
		revealed = false;
		hasMine = false;
		hasFlag = false;
		adjacentMines = 0;
	}
	
	public void placeMine() {
		hasMine = true;
	}
	
	public void removeMine() {
		hasMine = false;
	}
	
	public void addAdjacentMine() {
		adjacentMines++;
	}
	
	public void placeFlag() {
		hasFlag = true;
	}
	
	public void reveal() {
		revealed = true;
	}
	
	/**
	 * @return true if the step is safe
	 */
	public boolean step() {
		return !hasMine;
	}
	
	public boolean hasFlag() {
		return hasFlag;
	}
	
	public boolean hasMine() {
		return hasMine;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public int adjacentMines() {
		return adjacentMines;
	}
	
	public String toString() {
		if (hasMine && revealed) {
			return "*";
		} else if (hasFlag) {
			return "F";
		} else if (hasMine) {
			return "M"; 
		} else if (adjacentMines > 0 && revealed) {
			return Integer.toString(adjacentMines);
		} else if (revealed) {
			return " ";
		} else {
			return ".";
		}
	}
}
