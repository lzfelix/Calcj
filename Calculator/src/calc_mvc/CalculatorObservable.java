package calc_mvc;
import java.util.LinkedList;
import java.util.List;

public class CalculatorObservable {
	private List<CalculatorObserver> observers;
	
	public CalculatorObservable() {
		observers = new LinkedList<>();
	}
	
	public void subscribe(CalculatorObserver cobs) {
		if (!observers.contains(cobs))
			observers.add(cobs);
	}
	
	public boolean unsubscribe(CalculatorObserver cobs) {
		return observers.remove(cobs);
	}
	
	public void notifyAppendLine(String s) {
		for (CalculatorObserver cobs : observers)
			cobs.appendLine(s);
	}
	
	public void notifyDeleteChar() {
		for (CalculatorObserver cobs : observers)
			cobs.deleteChar();
	}
	
	public void notifyAppendString(String s) {
		for (CalculatorObserver cobs : observers)
			cobs.appendString(s);
	}
	
	public void notifyClearDisplay() {
		for (CalculatorObserver cobs : observers)
			cobs.clearDisplay();
	}
	
	public void notifyDeleteOperator() {
		for (CalculatorObserver cobs : observers)
			cobs.deleteOperator();
	}
	
	public void notifyAddOperator(char c) {
		for (CalculatorObserver cobs : observers)
			cobs.addOperator(c);
	}
	
	public void notifyInvertSignal(boolean isNegative) {
		for (CalculatorObserver cobs : observers)
			cobs.invertSignal(isNegative);
	}
}
