package utilities;

/**
 * This class stores gameplay data to fit the needs of the Minesweeper game's mine field.
 * Each <code>Space</code> object represents a space in the mine field and its relevant data.
 * This object keeps track of whether or not the space has a mine, whether or not it has been
 * revealed by the player, whether or not it has a flag, and the number of mines that are
 * adjacent to it (if it is not a mine itself).
 * 
 * @author Katherine Wilson
 */
public class Space {
	// ------------------------------------------------------[  FIELDS  ]------------------------------------------------------	
	/**
	 * <code>true</code> if this <code>Space</code> contains a mine.
	 * <br><code>false</code> if it's safe.
	 */
	private boolean hasMine;
	
	/**
	 * <code>true</code> if this <code>Space</code> has been revealed
	 * by the player.
	 * <br><code>false</code> if it has not.
	 */
	private boolean revealed;
	
	/**
	 * <code>true</code> if this <code>Space</code> contains a flag.
	 * <br><code>false</code> if it does not.
	 */
	private boolean hasFlag;
	
	/**
	 * An integer representing the number of mines that are adjacent
	 * to the <code>Space</code>. This calculation is based off of
	 * both cardinal and intermediate directions.
	 */
	private int adjacentMines;
	
	
	// --------------------------------------------------[  PUBLIC METHODS  ]--------------------------------------------------
	/**
	 * Creates a <code>Space</code> object and initializes it
	 * to a default state. By default, a <code>Space</code> 
	 * object contains no mines, contains no flags, has not
	 * been revealed by the player, and has zero adjacent mines.
	 */
	public Space() {
		revealed = false;
		hasMine = false;
		hasFlag = false;
		adjacentMines = 0;
	}
	
	/**
	 * Places a mine in this <code>Space</code>. Sets the {@link #hasMine} flag
	 * to <code>true</code>. Does not check if this <code>Space</code> already
	 * contains a mine.
	 */
	public void placeMine() {
		hasMine = true;
	}
	
	/**
	 * Removes a mine from this <code>Space</code>. Sets the {@link #hasMine} flag
	 * to <code>false</code>. Does not check if this <code>Space</code> contains a mine.
	 */
	public void removeMine() {
		hasMine = false;
	}
	
	/**
	 * Increments the value that keeps track of the number of mines that are 
	 * adjacent to this <code>Space</code>.
	 * 
	 * @see {@link #adjacentMines}
	 */
	public void addAdjacentMine() {
		adjacentMines++;
	}
	
	/**
	 * Places a flag in this <code>Space</code>. Sets {@link #hasFlag} to <code>true</code>.
	 * Does not check if this <code>Space</code> already contains a flag.
	 */
	public void placeFlag() {
		hasFlag = true;
	}
	
	/**
	 * Removes a flag from this <code>Space</code>. Sets {@link #hasFlag} to <code>false</code>.
	 * Does not check if this <code>Space</code> doesn't contain a flag.
	 */
	public void removeFlag() {
		hasFlag = false;
	}
	
	/**
	 * Marks this <code>Space</code> as revealed. This should be called when the player steps on
	 * this space or it is revealed by proxy. Sets {@link #hasFlag} to <code>true</code>. Does
	 * not check if this <code>Space</code> has already been revealed.
	 */
	public void reveal() {
		revealed = true;
	}
	
	/**
	 * Returns whether or not this <code>Space</code> has been flagged by the player.
	 * 
	 * @return <code>true</code>  if this <code>Space</code> contains a flag.
	 * 	   <br><code>false</code> if this <code>Space</code> does not contain a flag.
	 */
	public boolean hasFlag() {
		return hasFlag;
	}
	
	/**
	 * Returns whether or not this <code>Space</code> contains a mine or not.
	 * 
	 * @return <code>true</code>  if this <code>Space</code> contains a mine.
	 * 	   <br><code>false</code> if this <code>Space</code> does not contain a mine.
	 */
	public boolean hasMine() {
		return hasMine;
	}
	
	/**
	 * Returns whether or not this <code>Space</code> has been revealed by the
	 * player yet or not. This occurs when the space is "confirmed safe" either
	 * by the player directly stepping on this place, or this place being found
	 * through contiguous means. 
	 * 
	 * @return <code>true</code>  if this <code>Space</code> has been revealed.
	 * 	   <br><code>false</code> if this <code>Space</code> has not been revealed yet.
	 */
	public boolean isRevealed() {
		return revealed;
	}
	
	/**
	 * Returns the number of mines that are adjacent to this <code>Space</code>.
	 * This refers to the number of mines that can be found within each of the 8
	 * <code>Space</code>s that surround the <code>Space</code>.
	 * 
	 * @return the number of mines surrounding this <code>Space</code>.
	 * 
	 * @see {@link #adjacentMines}
	 */
	public int adjacentMines() {
		return adjacentMines;
	}
	
	public String toString() {			// XXX: may become deprecated
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
