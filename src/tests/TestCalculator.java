package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestCalculator {

	static TapeMachine logger;
	final double EPSILON = 1e-7;
	static char DEL_SYMBOL;
	static char DEL_OP;
	
	@BeforeClass
	public static void setUp() {
		logger = new TapeMachine();
		DEL_SYMBOL = logger.SIG_DELETE;
		DEL_OP = logger.SIG_DEL_OP;
	}
	
	@Test
	public void simpleSingleDigitAddition() {
		logger.performOperations("2+2=");
		assertEquals("2+2=4.0", logger.getTapeText());
	}
	
	@Test
	public void simpleSingleDigitSubtraction() {
		logger.performOperations("10-3=");
		assertEquals("10-3=7.0", logger.getTapeText());
	}
	
	@Test
	public void simpleSingleDigitNegativeSubtraction() {
		logger.performOperations("10-30=");
		assertEquals("10-30=-20.0", logger.getTapeText());
	}
	
	@Test
	public void simpleSingleDigitMultiplication() {
		logger.performOperations("10*3=");
		assertEquals("10*3=30.0", logger.getTapeText());
	}

	@Test
	public void simpleSingleDigitDivision() {
		logger.performOperations("10/2=");
		assertEquals("10/2=5.0", logger.getTapeText());
	}
	
	@Test
	public void simpleDecimalDivision() {
		logger.performOperations("10/4=");
		assertEquals("10/4=2.5", logger.getTapeText());
	}
	
	@Test
	public void periodicDivision() {
		logger.performOperations("10/3=");
		String logData = logger.getTapeText();
		
		int numberPos = logData.indexOf('.') - 1;	// to get 0.xxxxx
		double value = Double.valueOf(logData.substring(numberPos));
		
		assertEquals(value, 10.0/3, EPSILON);
	}
	
	@Test
	public void divisionByZero() {
		logger.performOperations("10/0=");
		assertEquals("10/0=Infinity",logger.getTapeText());
	}
	
	@Test
	public void zeroDividedByZero() {
		logger.performOperations("0/0=");
		assertEquals("0/0=NaN",logger.getTapeText());
	}
	
	public void finishingOperationWithEquals() { }
	
	@Test
	public void operatingNaNMustResultInNaN() {
		logger.performOperations("0/0=*2+2-2/0=");
		assertEquals("0/0=NaN*2=NaN+2=NaN-2=NaN/0=NaN",logger.getTapeText());
	}
	
	@Test
	public void chainedIntegerOperations() {
		logger.performOperations("12+7-1/2*4-3=");
		assertEquals("12+7=19.0-1=18.0/2=9.0*4=36.0-3=33.0",logger.getTapeText());
	}
	
	@Test
	public void chainedDecimalOperations() { 
		logger.performOperations("2/4*7-2.3+9.3-0.2/1.0=");
		assertEquals("2/4=0.5*7=3.5-2.2=1.2+9.3=10.5-0.2=10.3/1.0=10.3", logger.getTapeText());
	}
	
	@Test
	public void deletingDigits() { 
		logger.performOperations("23<2</444<<<2=");
		assertEquals("23" + DEL_SYMBOL + "2" + DEL_SYMBOL + "/444" + DEL_SYMBOL + DEL_SYMBOL + DEL_SYMBOL + 
				"2=1.0", logger.getTapeText());
	}
	
	@Test
	public void deleteDigitsButNotOperator() { 
		logger.performOperations("23.3+<<<1.3<=");
		assertEquals("23.3+1.3"  + DEL_SYMBOL + "=24.3" , logger.getTapeText());
	}
	
	@Test
	public void deletingDigitsAndDotButNotOperators() {
		logger.performOperations("23<2.<</5.96<<<=");
		assertEquals("23" + DEL_SYMBOL + "2." + DEL_SYMBOL + DEL_SYMBOL + "/5.96" + DEL_SYMBOL + DEL_SYMBOL + DEL_SYMBOL + 
				"=0.4", logger.getTapeText());
	}
	
	@Test
	public void overwritingOperator() { 
		logger.performOperations("2.4+-*/2.4+-1.7*0=");
		assertEquals("2.4+" + DEL_OP + "-" + DEL_OP + "*" + DEL_OP + "/2.4=1.0+" + DEL_OP + "-1.7=-0.7*0=0.0", logger.getTapeText());
	}
	
	public void invertingDigitOnSimpleOperation() { }
	
	public void invertingResultSig() { }
	
	public void invertingSignalOfSecondNumberAfterDeletingNumberDotAndReaddingThem() { }
	
	public void sendingInvalidInstruction() { }
	
	public void performOperationClearCalcAndPerformSimpleOperation() { }
	
	public void performComplexOperationClearCalcAndDoAgain() { }
	
	@Test
	public void chainOfSimpleSums() {
		logger.performOperations("2+2+6+8+5=");
		assertEquals("2+2=4.0+6=10.0+8=18.0+5=23.0", logger.getTapeText());
	}
}
