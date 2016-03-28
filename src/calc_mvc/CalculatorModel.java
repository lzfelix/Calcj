package calc_mvc;

/**
 * Model interface used on the calculator MVC architecture.
 * @author lzfelix
 */
public interface CalculatorModel {
	
	/** Invoking this function causes the calculator to invert the signal of the current number. */
	public void invertSignal();
	
	/** Resets the calculator's internal state */
	public void resetCalculator();
	
	/** 
	 * Expected outputs are: numbers (from 0 to 9) and the usual operators (+, -, *, /) and the decimal symbol (.).
	 * If the first digit of a number is [.], the calculator represents this number internally as [0.], thus causing the
	 * view to output these two digits.  
	 */
	public void notifyInputChar(char triggerChar);
	
	/** Tries to delete a digit from the number currently being input. If the last digit of this number was a ., it is deleted */
	public void deleteDigit();
}
