package calc_mvc;

public interface CalculatorModel {
	
	public void invertSignal();
	
	public void resetCalculator();
	
	public void notifyInputChar(char triggerChar);
	
	public void deleteDigit();
}
