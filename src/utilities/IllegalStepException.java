package utilities;

/**
 * Simple exception class used in the validation of steps
 * made by the player in the Minesweeper game. An
 * "IllegalStepException" is thrown when the player attempts
 * to step somewhere that is prohibited by the game rules
 * (Ex. they try to step somewhere that has already been
 * revealed or has a flag).
 *
 * @author Katherine Wilson
 */
public class IllegalStepException extends Exception {
	// --------------------------------------------------[  PUBLIC METHODS  ]--------------------------------------------------
	/**
	 * Constructs an instance of an <code>IllegalStepException</code>.
	 * This exception's <code>message</code> is set to the string passed in as 
	 * a parameter for this constructor.
	 * 
	 * @param message Brief description of what caused the exception.
	 */
	public IllegalStepException(String message) {
		super(message);
	}
		
	/**
	 * Determines what is shown when an instance of this class is printed. 
	 * For this exception, the words "INVALID PLACEMENT: " followed by a 
	 * brief description of the error (<code>message</code>) are returned.
	 * 
	 * @return <code>String</code> representation of this error, which
	 * briefly describes its cause.
	 */
	public String toString() {
		return "INVALID STEP: " + getMessage();
	 }
}