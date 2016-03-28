package tests;

import calc_mvc.CalculatorObserver;

/** 
 * This class wraps the CalculatorObserver, logging all the notifications usually sent to the view
 * into a String. Signals to delete a digit, clear the display, delete the last input operator and to
 * invert a number are represented, respectively by the following constants: <code>SIG_DELETE</code>, 
 * <code>SIG_CLEAR</code>, <code>SIG_DEL_OP</code> and <code>SIG_INV</code>.
 * @author lzfelix
 *
 */
public class TapeMachine implements CalculatorObserver{

	public final char SIG_DELETE = 'D';
	public final char SIG_CLEAR = 'C'; 
	public final char SIG_DEL_OP = 'O';
	public final char SIG_INV = 'S';
	
	private StringBuilder tape;
	
	public TapeMachine() {
		tape = new StringBuilder();
	}
	
	/**
	 * Returns the logged signals as a String and clears the internal log.
	 * @return all the signals received by the view.
	 */
	public String getTapeText() {
		String toReturn = tape.toString();
		tape.setLength(0);
		return toReturn;
	}
	
	// Observer methods. Logs all the events into the tape
	
	@Override
	public void appendString(String s) {
		tape.append(s);
	}

	@Override
	public void addOperator(char c) {
		tape.append(c);
	}

	@Override
	public void deleteChar() {
		tape.append("[D]");
	}

	@Override
	public void clearDisplay() {
		tape.append("[C]");
	}

	@Override
	public void deleteOperator() {
		tape.append("[O]");
	}

	@Override
	public void invertSignal(boolean isNegative) {
		char signal = (isNegative) ? '-' : '+';
		tape.append("[S" + signal + "]");
	}

}
