package calculator;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import calc_mvc.CalculatorController;
import calc_mvc.CalculatorModel;

/**
 * The calculator's controller, as the class name implies :)
 * @author lzfelix
 */
@SuppressWarnings("serial")
public class Controller extends CalculatorController{
	
	private CalculatorModel model;
	
	public void addModel(CalculatorModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	
		char triggerChar;
		
		if (e.getSource() instanceof JButton)		
			triggerChar = ((JButton)e.getSource()).getText().charAt(0);
		else  {
			System.out.println(">"+e.getActionCommand()+">");
			triggerChar = e.getActionCommand().charAt(0);
			
			switch (triggerChar) {
				case Calculator.CHAR_CLEAR: triggerChar = 'C'; break;
				case Calculator.CHAR_EQUALS: triggerChar = '='; break;
				case Calculator.CHAR_DELETE: triggerChar = '<'; break;
				case Calculator.CHAR_SIGNAL: triggerChar = '±'; break;
			}
		}
		
		switch (triggerChar) {
			case 'C': model.resetCalculator(); break;
			case '<': model.deleteDigit(); break;
			case '±': model.invertSignal(); break;
			default:
				model.notifyInputChar(triggerChar);
		}
	}
	
	
}
