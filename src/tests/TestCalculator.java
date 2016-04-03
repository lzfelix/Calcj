package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestCalculator {

	static TapeMachine logger;
	final double EPSILON = 1e-7; 
	
	@BeforeClass
	public static void setUp() {
		logger = new TapeMachine();
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
	
	public void chainedDecimalOperations() { }
	
	public void deletingDigits() { }
	
	public void deletingDigitsButNotOperator() { }
	
	public void deletingDigitsAndDotButNotOperators() { }
	
	public void overwritingOperator() { }
	
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
