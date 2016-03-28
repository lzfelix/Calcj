package calculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import calc_mvc.CalculatorModel;

/**
 * A "Controller-Model" class to decouple the calculator's logic from its view.
 * @author lzfelix
 */
public class Controller implements ActionListener{
	
	private CalculatorModel model;
	
	public void addModel(CalculatorModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		char triggerChar = ((JButton)e.getSource()).getText().charAt(0);
		
		switch (triggerChar) {
			case 'C': model.resetCalculator(); break;
			case '<': model.deleteDigit(); break;
			case '±': model.invertSignal(); break;
			default:
				model.notifyInputChar(triggerChar);
		}
	}
}
