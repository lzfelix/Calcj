package calc_mvc;

public interface CalculatorObserver {
	public void appendLine(String s);
	public void appendString(String s);
	public void addOperator(char c);
	public void deleteChar();
	public void clearDisplay();
	public void deleteOperator();
	public void invertSignal(boolean isNegative);
}
