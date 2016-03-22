import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * A "Controller-Model" class to decouple the calculator's logic from its view.
 * @author lzfelix
 */
public class Controller implements ActionListener{

	private JTextArea display;
	
	private enum States {STAGE_NO_NUMBER, STAGE_FIRST_NUMBER_IN, STAGE_WAITING_SECOND_NUMBER, STAGE_SECOND_NUMBER};
	private final char NO_OPERATOR = 'X';
	
	private States calculatorState = States.STAGE_NO_NUMBER;
	private double previousNumber;
	private char operatorSlot = NO_OPERATOR;
	
	/**
	 * Stores internally a reference to the calculator display in order to update it.
	 * @param display a JTextArea used by the calculator as display.
	 */
	public void registerDisplay(JTextArea display) {
		this.display = display;
	}
	
	@Override
	/**
	 * Keeps track of the calculator internal status and handles input signals. This method
	 * works as both Model and Controller.
	 */
	public void actionPerformed(ActionEvent e) {
		char triggerChar = ((JButton)e.getSource()).getText().charAt(0);
		
		// if this is a number, add it to the display. Update the calculator stage accordingly 
		if (Character.isDigit(triggerChar)) {
			display.append(String.valueOf(triggerChar));
			calculatorState = (calculatorState == States.STAGE_NO_NUMBER) ? States.STAGE_FIRST_NUMBER_IN : States.STAGE_SECOND_NUMBER;
		}
		else if (triggerChar == 'C') {
			// if clear is pressed, reset the calculator stage and clear its log
			
			previousNumber = 0;
			display.setText("");
			calculatorState = States.STAGE_NO_NUMBER;
		}
		else {	// operators
			
			// if the user is pressing a operator key before inputing the first number, simply ignore (invalid transition)
			if (calculatorState == States.STAGE_NO_NUMBER) return;
			
			Double newNumber = extractNumberFromDisplay();
			
			if (calculatorState == States.STAGE_FIRST_NUMBER_IN && triggerChar != '=') {
				/* the '=' key will only work if the calculator is on the SECOND_NUMBER state (number, operator and number were input)
				 * if just entered the first number, then this is the operation to be used in the future. Now expects either
				 * another number or another operator (will overwrite the current saved operation)
				 */
				
				display.append(" " + triggerChar + "\n");
				previousNumber = newNumber;
				calculatorState = States.STAGE_WAITING_SECOND_NUMBER;
				operatorSlot = triggerChar;
			}
			else if (calculatorState == States.STAGE_WAITING_SECOND_NUMBER && triggerChar != '=') {
				/* An operation was selected, but another one was chosen, rewrite the previous one both in memory
				 * and on the display
				 */
				
				int offset, lineEndPos = 0;
				try {
					offset = display.getLineOfOffset(display.getCaretPosition());
					lineEndPos = display.getLineEndOffset(offset);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

				operatorSlot = triggerChar;
				display.replaceRange(" " + operatorSlot + "\n", lineEndPos - 3, lineEndPos);	
			}
			else if (calculatorState == States.STAGE_SECOND_NUMBER) {
				/* Perform the operation either if the '=' was pressed after entering (number, operator, number) or
				 * if another operator key was pressed after entering the previous triple of data. In this case, the
				 * current operator key is the one that takes place
				 */
				
				if (triggerChar != '=')
					operatorSlot = triggerChar;
				
				try {
					switch (operatorSlot) {
						case '+': previousNumber += newNumber; break;
						case '-': previousNumber -= newNumber; break;
						case '*': previousNumber *= newNumber; break;
						case '/': previousNumber /= newNumber; break;
					}
					
					if (triggerChar != '=') {
						display.append(" =\n" + previousNumber + " " + operatorSlot + "\n");
						calculatorState = States.STAGE_WAITING_SECOND_NUMBER;
					}
					else {
						display.append(" =\n" + previousNumber);
						calculatorState = States.STAGE_FIRST_NUMBER_IN;
					}
				}
				catch (Exception ex) {
					display.append("\nNaN");
					calculatorState = States.STAGE_NO_NUMBER;
					operatorSlot = NO_OPERATOR;
				}
			}
		}
	}
	
	/**
	 * Retrieves the last number input by the user on the display. If the number is invalid, returns <code>null</code>.
	 * @return a double containing the last number typed by the user or <code>null</code> if it is invalid (an operation
	 * symbol was appended to the display, or no number at all was entered).
	 */
	private Double extractNumberFromDisplay() {
		try {
			int offset = display.getLineOfOffset(display.getCaretPosition());
			int indexNumberStart = display.getLineStartOffset(offset);
			int indexNumberEnd = display.getLineEndOffset(offset);
			
			String numberAsString = display.getText(indexNumberStart, indexNumberEnd-indexNumberStart);
			if (numberAsString.length() > 0) 
				return Double.valueOf(numberAsString);
			
		} catch (BadLocationException e1) { 
			// complying with checked exception. It will never happen.
		}
		
		return null;
	}
}
