import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Calculator extends JFrame{
	
	private JTextArea display;
	private JButton buttons[];
	private JButton btnClear, btnEquals, btnDivide, btnMultiply, btnAdd, btnSubtract;
	
	private int AMOUNT_NUMBERS = 10;

	public Calculator() {
		super("Calculator");
		
		setLayout(new BorderLayout());
		makeLayout();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void makeLayout() {
		buttons = new JButton[AMOUNT_NUMBERS];
		JPanel pnlKeys = new JPanel(new GridLayout(0, 3));
		JPanel pnlSideButtons = new JPanel(new GridLayout(0, 1));
		
		// adding numbered buttons, 0 is the last one 
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(Integer.toString(i));
			
			if (i == 0) continue;
			pnlKeys.add(buttons[i]);
		}
		pnlKeys.add(buttons[0]);
		
		// clear and equals go at the last row of numbered keys		
		pnlKeys.add(createButton(btnClear, "C"));
		pnlKeys.add(createButton(btnEquals, "="));
		
		// panel with (most of the) operation buttons 
		pnlSideButtons.add(createButton(btnDivide, "/"));
		pnlSideButtons.add(createButton(btnMultiply, "*"));
		pnlSideButtons.add(createButton(btnSubtract, "-"));
		pnlSideButtons.add(createButton(btnAdd, "+"));
		
		add(pnlKeys, BorderLayout.CENTER);
		add(pnlSideButtons, BorderLayout.EAST);
		
		// adding display
		display = new JTextArea(3, 0);
		add(display, BorderLayout.NORTH);
	}
	
	private JButton createButton(JButton instanceHolder, String text) {
		instanceHolder = new JButton(text);
		return instanceHolder;
	}
	
	public static void main(String[] args) {
		new Calculator();
	}
}
