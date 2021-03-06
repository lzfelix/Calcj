package calculator;
import calc_mvc.CalculatorModel;
import calc_mvc.CalculatorObservable;

public class Model extends CalculatorObservable implements CalculatorModel{
	
	private enum States {WAITING_NUMBER, INPUTING_1ST_NUMBER, INSERTING_OPERATOR, INSERTING_2ND_NUMBER, FINISHED_OPERATION};
	private final char NO_OPERATOR = 'X';
	
	private States calculatorState;
	
	private StringBuilder numberBuffer;
	private double previousNumber;
	private boolean numberHasDot;
	private boolean isNumberNegative;
	
	private char operatorSlot = NO_OPERATOR;
	
	char operators[] = {'+', '-', '*', '/'};
	
	public Model() {
		numberBuffer = new StringBuilder();
		resetCalculator();
	}

	public void invertSignal() {
		isNumberNegative = !isNumberNegative;
		notifyInvertSignal(isNumberNegative);
	}
	
	public void resetCalculator() {
		previousNumber = 0;
		calculatorState = States.WAITING_NUMBER;
		
		// clear the numbers' buffer
		numberBuffer.setLength(0);
		numberHasDot = false;
		isNumberNegative = false;
		
		// clear interface
		notifyClearDisplay();
	}
	
	/**
	 * Checks if the incoming char is valid to form a number. If so, add it to the number buffer and set the
	 * number flag <code>numberHasDot</code> properly.
	 * @param c The char trying to be added to the number.
	 * @return <code>true</code> if <code>c</code> was added successfully, <code>false</code> otherwise. 
	 */
	private boolean tryToAppendToNumber(char c) {
		boolean toReturn = false;
		
		if (c == '.' && !numberHasDot) {
			if (numberBuffer.length() == 0) {
				numberBuffer.append("0");
				notifyAppendString("0");
			}
			
			numberHasDot = true;
			toReturn = true;
		}
		else
			toReturn = Character.isDigit(c);	
		
		if (toReturn) {
			numberBuffer.append(c);
			notifyAppendString(String.valueOf(c));
		}
		
		return toReturn;
	}
	
	/**
	 * Gets the number stored on the buffer, clearing it and its flags, making the buffer ready to start receiving
	 * new digits to form the next number.
	 * @return The stored number.
	 */
	private double getNumberFromBuffer() {
		double number = Double.valueOf(numberBuffer.toString());
		numberBuffer.setLength(0);
		numberHasDot = false;
		
		if (isNumberNegative)
			number *= -1;

		isNumberNegative = false;
		return number;
	}
	
	private boolean validateOperator(char c) {
		return (c == '+' || c == '-' || c == '*' || c == '/');
	}
	
	public void deleteDigit() {
		int len = numberBuffer.length();
		
		if (len > 0) {
			if (numberHasDot && numberBuffer.charAt(len - 1) == '.')
				numberHasDot = false;
			
			numberBuffer.setLength(len - 1);
			notifyDeleteChar();
		}
	}
	
	public void notifyInputChar(char triggerChar) {
		
		switch (calculatorState) {
			case WAITING_NUMBER:
				// if it is a valid number (digit, inverse signal, valid dot), appends it to the buffer.
				
				if (tryToAppendToNumber(triggerChar)) 
					calculatorState = States.INPUTING_1ST_NUMBER;
				
				break;
				
			case INPUTING_1ST_NUMBER:
				// if it is a valid number, still appends it to the buffer. If it is a valid operator, stores it and gets
				// the number on the buffer.
				
				if (!tryToAppendToNumber(triggerChar) && validateOperator(triggerChar)) {
					previousNumber = getNumberFromBuffer();
					operatorSlot = triggerChar;
					calculatorState = States.INSERTING_OPERATOR;
					
					notifyAddOperator(triggerChar);
				}
				
				break;
				
			case INSERTING_OPERATOR:
				// if it is a valid operator, replaces the stored one. If it's a number, change state.
				
				if (validateOperator(triggerChar)) {
					operatorSlot = triggerChar;
					
					notifyDeleteOperator();
					notifyAddOperator(triggerChar);
				}
				else 
					if (tryToAppendToNumber(triggerChar)) 
						calculatorState = States.INSERTING_2ND_NUMBER; 
				
				break;
				
			case INSERTING_2ND_NUMBER:
				// if it is a valid number, append it to the second number, if it is an equals sign,
				// perform the operation using the previous number, the current one and the operator on the slot,
				// if it is another operator, performs the previous operation as if a = was pressed and already add
				// this new operator to the slot, so the user can input just the second number.
				
				if (!tryToAppendToNumber(triggerChar)) {
					if (triggerChar == '=' || validateOperator(triggerChar)) {
						double secondNumber = getNumberFromBuffer();
						
						notifyAddOperator('=');
						
						try {
							switch (operatorSlot) {
								case '+': previousNumber += secondNumber; break;
								case '-': previousNumber -= secondNumber; break;
								case '*': previousNumber *= secondNumber; break;
								case '/': previousNumber /= secondNumber; break;
							}
							
							// treats negative zero (-0.0) case, which happens on Double
							if (previousNumber == 0.0)
								previousNumber = 0.0;
							
							notifyAppendString(String.valueOf(previousNumber));
						}
						catch (Exception e) {
							notifyAppendString(String.valueOf("NaN"));
						}
						
						// if the operation is taking place because a key different from = was pressed, right after
						// showing the result of the operation, add this operator's symbol and prepare to either have
						// it rewritten or to start receiving the second number.
						if (triggerChar != '=') {
							notifyAddOperator(triggerChar);
							operatorSlot = triggerChar;
							calculatorState = States.INSERTING_OPERATOR;
						}
						else {
							if (Math.signum(previousNumber) < 0)
								isNumberNegative = true;
							
							// if the operation was completed because = was pressed, then different scenarios are possible.
							// Move to a new state.
							calculatorState = States.FINISHED_OPERATION;
						}
					}
				}
				
				break;
				
			case FINISHED_OPERATION:
				// This state is reached only if an operation was performed after the user pressing the = sign,
				// in this case, the user can either input a new operator and use the result of the previous operation
				// as the first number for the next one OR she can press a number, causing the calculator to reset
				// its status.
				
				if (tryToAppendToNumber(triggerChar)) {
					getNumberFromBuffer();	// forces buffer cleaning.
					tryToAppendToNumber(triggerChar);
					calculatorState = States.INPUTING_1ST_NUMBER;
					
					notifyClearDisplay();
					notifyAppendString(String.valueOf(triggerChar));
				}
				else if (validateOperator(triggerChar)) {
					operatorSlot = triggerChar;
					calculatorState = States.INSERTING_OPERATOR;
					
					notifyAddOperator(triggerChar);
				}
				
				break;
		}
	}
}
