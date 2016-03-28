package calc_mvc;

/**
 * This interface must be implemented by the Calculator's view.
 * @author lzfelix
 */
public interface CalculatorObserver {
	
	/** Invoked when the model needs to append a String (which may contain a newline symbol) into the displaying interface. */ 
	public void appendString(String s);
	
	/** Invoked when observer has to add a symbol that illustrates the operation to be performed on the display. */
	public void addOperator(char c);
	
	/** Invoked to notify the observer to delete the last appended char from its display. */
	public void deleteChar();
	
	/** Invoked to notify the observer to clear completely its display and anything related to all operations performed so far. */
	public void clearDisplay();

	/** Invoked to delete the last added operator from the observer's display. */
	public void deleteOperator();
	
	/**
	 * Invoked to notify the observer to invert (show or remove a negative symbol, for example) the signal of the number
	 * being informed or resulted from the previous operation. 
	 * @param isNegative <code>true</code> if the number has just became negative, <code>false</code> otherwise.
	 */
	public void invertSignal(boolean isNegative);
}
