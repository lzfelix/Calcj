package calc_mvc;
import java.util.LinkedList;
import java.util.List;

/**
 * This class must be inherited by the Calculator's Model.
 * @author lzfelix
 */
public abstract class CalculatorObservable {
	private List<CalculatorObserver> observers;
	
	public CalculatorObservable() {
		observers = new LinkedList<>();
	}
	
	/** Allows adding a new observer to this model. If it already exists, nothing is done. */
	public void subscribe(CalculatorObserver cobs) {
		if (!observers.contains(cobs))
			observers.add(cobs);
	}
	
	/** 
	 * Removes a given observer from the subscription list, if it exists.
	 * @param cobs The observer to be deattached.
	 * @return <code>true</code> if the observer was present on the list, <code>false</code> otherwise.
	 */
	public boolean unsubscribe(CalculatorObserver cobs) {
		return observers.remove(cobs);
	}
	
	/** Notifies all the observers to remove the last appended character from their displaying interface. */
	public void notifyDeleteChar() {
		for (CalculatorObserver cobs : observers)
			cobs.deleteChar();
	}
	
	/** 
	 * Notifies all the observers to append a String into their displaying interface. This method may ask the
	 * observer to append a newline.
	 */
	public void notifyAppendString(String s) {
		for (CalculatorObserver cobs : observers)
			cobs.appendString(s);
	}
	
	/** Causes all the observers to clear their displaying interface. */
	public void notifyClearDisplay() {
		for (CalculatorObserver cobs : observers)
			cobs.clearDisplay();
	}
	
	/** Notifies all the observers to delete the last added operator from its display. */
	public void notifyDeleteOperator() {
		for (CalculatorObserver cobs : observers)
			cobs.deleteOperator();
	}
	
	/** Notifies all the observers to add an operator into its display. */
	public void notifyAddOperator(char c) {
		for (CalculatorObserver cobs : observers)
			cobs.addOperator(c);
	}
	
	/** 
	 * Notifies all the observers to add or remove a symbol that shows that the number being input (or the result
	 * of the prevoius operation) is either positive or negative.
	 * @param isNegative <code>true</code> if the number has just became negative, <code>false</code> otherwise.
	 */
	public void notifyInvertSignal(boolean isNegative) {
		for (CalculatorObserver cobs : observers)
			cobs.invertSignal(isNegative);
	}
}
