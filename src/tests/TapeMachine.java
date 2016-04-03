package tests;

import calc_mvc.CalculatorObserver;
import calculator.Model;

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
	private Model calcModel;
	
	public TapeMachine() {
		tape = new StringBuilder();
		calcModel = new Model();
		calcModel.subscribe(this);
	}
	
	/**
	 * Sends all the characters from this String to the calculator as actions.
	 * Both [±] and [I] chars can be used to invert the signal of the digits. 
	 * @param operations The sequence of operations to be sent to the calculator.
	 */
	public void performOperations(String operations) {
		for (char c : operations.toCharArray()) {
			
			switch (c) {
				case 'C': calcModel.notifyClearDisplay(); break;
				case '<': calcModel.deleteDigit(); break;
				case 'I':
				case '±': calcModel.invertSignal(); break;
				default:
					calcModel.notifyInputChar(c);		
			}
		}
	}
	
	/**
	 * Returns the logged signals as a String and clears the internal log.
	 * @return all the signals received by the view.
	 */
	public String getTapeText() {
		String toReturn = tape.toString();
		
		// reset calculator and tape for the next operations.
		calcModel.resetCalculator();
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
