import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

public class Calculator extends JFrame implements ActionListener{
	
	private final int AMOUNT_NUMBERS = 10;
	private final int STAGE_NO_NUMBER = 0;
	private final int STAGE_FIRST_NUMBER_IN = 1;
	private final int STAGE_WAITING_SECOND_NUMBER = 2;
	private final int STAGE_SECOND_NUMBER = 3;
	
	private JTextArea display;
	private JButton buttons[];
	private JButton btnClear, btnEquals, btnDivide, btnMultiply, btnAdd, btnSubtract;
	private double previousNumber;
	private int stage = STAGE_NO_NUMBER;
	

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
			buttons[i].addActionListener(this);
			
			if (i == 0) continue;
			pnlKeys.add(buttons[i]);
		}
		pnlKeys.add(buttons[0]);
		
		// clear and equals go at the last row of numbered keys		
		pnlKeys.add(buttonFactory(btnClear, "C"));
		pnlKeys.add(buttonFactory(btnEquals, "="));
		
		// panel with (most of the) operation buttons 
		pnlSideButtons.add(buttonFactory(btnDivide, "/"));
		pnlSideButtons.add(buttonFactory(btnMultiply, "*"));
		pnlSideButtons.add(buttonFactory(btnSubtract, "-"));
		pnlSideButtons.add(buttonFactory(btnAdd, "+"));
		
		add(pnlKeys, BorderLayout.CENTER);
		add(pnlSideButtons, BorderLayout.EAST);		
		
		// adding display
		display = new JTextArea(3, 0);
		display.setEditable(false);
		
		// http://stackoverflow.com/questions/24315757/java-align-jtextarea-to-the-right
		
		JScrollPane jsp = new JScrollPane(display);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.NORTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		char triggerChar = ((JButton)e.getSource()).getText().charAt(0);
		
		if (Character.isDigit(triggerChar)) {
			display.append(String.valueOf(triggerChar));
			stage = (stage == STAGE_NO_NUMBER) ? STAGE_FIRST_NUMBER_IN : STAGE_SECOND_NUMBER;
		}
		else {
			switch (triggerChar) {
				case 'C':
					display.setText("");
					stage = STAGE_NO_NUMBER;
					break;
				
				case '+':
					// hitting operation button without numbers do nothing
					if (stage == STAGE_NO_NUMBER) return;
					
					Double newNumber = extractNumberFromDisplay(display);

					if (stage == STAGE_FIRST_NUMBER_IN) {
						display.append(" +\n");
						previousNumber = newNumber;
						stage = STAGE_WAITING_SECOND_NUMBER;
					}
					else if (stage == STAGE_WAITING_SECOND_NUMBER) {
						int offset, lineEndPos = 0;
						try {
							offset = display.getLineOfOffset(display.getCaretPosition());
							lineEndPos = display.getLineEndOffset(offset);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}

						display.replaceRange(" +\n", lineEndPos - 3, lineEndPos);
					}
					else {	// STAGE_SECOND_NUMBER
						previousNumber += newNumber;
						display.append(" =\n" + previousNumber + " +\n");
						stage = STAGE_WAITING_SECOND_NUMBER;
					}
					
					/*
					if (operationSlot != NO_OPERATION) {
						display.append(" =\n");
						
						try {
							previousNumber = operate(previousNumber, newNumber, operationSlot);
							display.append(Double.toString(previousNumber));
						}
						catch (Exception ex) {
							display.append("NaN");
						}
						
						operationSlot = NO_OPERATION;
					}
					else {
						operationSlot += '+';
					}
					*/
						
					break;
			}
			
		}
	}
	
	private double operate(double a, double b, char operator) {
		double result = 0;
		
		switch (operator) {
			case '+': result = a + b; break;
			case '-': result = a - b; break;
			case '*': result = a * b; break;
			case '/': result = a / b; break;
		}
		
		return result;
	}
	
	private Double extractNumberFromDisplay(JTextArea display) {
		try {
			int offset = display.getLineOfOffset(display.getCaretPosition());
			int indexNumberStart = display.getLineStartOffset(offset);
			int indexNumberEnd = display.getLineEndOffset(offset);
			
			String numberAsString = display.getText(indexNumberStart, indexNumberEnd-indexNumberStart);
			if (numberAsString.length() > 0) 
				return Double.valueOf(numberAsString);
			
		} catch (BadLocationException e1) { 
			// complying with checked exception. It will never happen.
		}
		
		return null;
	}
	
	/* HELPER METHODS */
	private JButton buttonFactory(JButton instanceHolder, String text) {
		instanceHolder = new JButton(text);
		instanceHolder.addActionListener(this);
		return instanceHolder;
	}
	
	
	
	public static void main(String[] args) {
		new Calculator();
	}

}
