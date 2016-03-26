import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A "Controller-Model" class to decouple the calculator's logic from its view.
 * @author lzfelix
 */
public class Controller implements ActionListener{
	
	private Model model;
	
	public void addModel(Model model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		char triggerChar = ((JButton)e.getSource()).getText().charAt(0);
		
		if (triggerChar == 'C')
			model.resetCalculator();
		else
			model.notifyInputChar(triggerChar);
	}
}
