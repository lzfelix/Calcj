package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * I developed most tests using integeres, instead of doubles because of the hassle on
 * extracting the double from the display and comparing it properly.
 */
public class TestCalculator {

	static TapeMachine logger;
	final double EPSILON = 1e-7;
	static char DEL_SYMBOL;
	static char DEL_OP;
	static char SIG_INV;
	static String SIG_INV_NEG;
	static String SIG_INV_POS;
	static char SIG_CLEAR;
	
	@BeforeClass
	public static void setUp() {
		logger = new TapeMachine();
		DEL_SYMBOL = logger.SIG_DELETE;
		DEL_OP = logger.SIG_DEL_OP;
		SIG_CLEAR = logger.SIG_CLEAR;
		
		char SIG_INV = logger.SIG_INV;
		SIG_INV_NEG = "[" + SIG_INV + "-]";
		SIG_INV_POS = "[" + SIG_INV + "+]";
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
	
	@Test
	public void finishingOperationWithEquals() {
		logger.performOperations("2+3*5/5=");
		assertEquals("2+3=5.0*5=25.0/5=5.0",logger.getTapeText());
	}
	
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
	
	/*
	 * I Have to find a way to compare the resulting strings that contain doubles with rounding 
	 * imprecisions. Eg: 1.200000000000000002
	@Test
	public void chainedDecimalOperations() { 
		logger.performOperations("2/4*7-2.3+9.3-0.2/1.0=");
		assertEquals("2/4=0.5*7=3.5-2.2=1.2+9.3=10.5-0.2=10.3/1.0=10.3", logger.getTapeText());
	}
	*/
	
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
	
	@Test
	public void invertingDigitOnSimpleOperation() { 
		logger.performOperations("2I+7=");
		assertEquals("2" + SIG_INV_NEG + "+7=5.0", logger.getTapeText());
	}
	
	@Test
	public void invertingResultSig() { 
		logger.performOperations("2+7=I");
		assertEquals("2+7=9.0"+ SIG_INV_NEG, logger.getTapeText());
	}
	
	@Test
	public void invertingSecondNumberBeforeEquals() { 
		logger.performOperations("2+7I=");
		assertEquals("2+7"+ SIG_INV_NEG + "=-5.0", logger.getTapeText());
	}
	
	@Test
	public void invertingSecondNumberBeforeAnotherOperation() { 
		logger.performOperations("2+7I-5=");
		assertEquals("2+7"+ SIG_INV_NEG + "=-5.0-5=-10.0", logger.getTapeText());
	}
	
	@Test
	public void invertingSignalOfSecondNumberAfterDeletingNumberDotAndReaddingThem() {
		logger.performOperations("2+72.<<I5.2=");
		assertEquals("2+72." + DEL_SYMBOL + DEL_SYMBOL + SIG_INV_NEG + "5.2=-73.2", logger.getTapeText());
	}
	
	@Test
	public void invertingThreeNumbersOneTwice() {
		logger.performOperations("3I+5II=");
		assertEquals("3" + SIG_INV_NEG + "+5" + SIG_INV_NEG + SIG_INV_POS + "=2.0", logger.getTapeText());
	}
	
	@Test
	public void sendingInvalidInstruction() { 
		logger.performOperations("++-*///=");
		assertEquals("", logger.getTapeText());
	}
	
	@Test
	public void performOperationClearCalcAndPerformSimpleOperation() { 
		logger.performOperations("2*7/2-3=C5/2=");
		assertEquals("2*7=14.0/2=7.0-3=4.0" + SIG_CLEAR + "5/2=2.5", logger.getTapeText());
	}
	
	@Test
	public void performComplexOperationClearCalcAndDoAgain() { 
		String complexOperation = "2+2.5<</4*<<9/II18=I";
		String expectedOutput = "2+2.5" + DEL_SYMBOL  + DEL_SYMBOL + "=4.0/4=1.0*9=9.0/" + 
									SIG_INV_NEG + SIG_INV_POS + "18=0.5" + SIG_INV_NEG;
		
		logger.performOperations(complexOperation + "C" + complexOperation);
		assertEquals(expectedOutput + SIG_CLEAR + expectedOutput, logger.getTapeText());
	}
	
	@Test
	public void chainOfSimpleSums() {
		logger.performOperations("2+2+6+8+5=");
		assertEquals("2+2=4.0+6=10.0+8=18.0+5=23.0", logger.getTapeText());
	}
	
	@Test
	public void clearOnMiddleOfOperation() {
		logger.performOperations("2+C3/3=");
		assertEquals("2+" + SIG_CLEAR + "3/3=1.0", logger.getTapeText());
	}
}
