import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

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
	private final char NO_OPERATOR = 'X';
	
	private JTextArea display;
	private JButton buttons[];
	private JButton btnClear, btnEquals, btnDivide, btnMultiply, btnAdd, btnSubtract;
	private double previousNumber;
	
	private char operatorSlot = NO_OPERATOR;
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
		
		BinaryOperator<Double> addDoubles = (a,b) -> a + b;
		
		if (Character.isDigit(triggerChar)) {
			display.append(String.valueOf(triggerChar));
			stage = (stage == STAGE_NO_NUMBER) ? STAGE_FIRST_NUMBER_IN : STAGE_SECOND_NUMBER;
		}
		else if (triggerChar == 'C') {
			display.setText("");
			stage = STAGE_NO_NUMBER;
		}
		else {	// operators
			if (stage == STAGE_NO_NUMBER) return;
			
			Double newNumber = extractNumberFromDisplay(display);
			
			if (stage == STAGE_FIRST_NUMBER_IN && triggerChar != '=') {
				display.append(" " + triggerChar + "\n");
				previousNumber = newNumber;
				stage = STAGE_WAITING_SECOND_NUMBER;
				operatorSlot = triggerChar;
			}
			else if (stage == STAGE_WAITING_SECOND_NUMBER && triggerChar != '=') {
				int offset, lineEndPos = 0;
				try {
					offset = display.getLineOfOffset(display.getCaretPosition());
					lineEndPos = display.getLineEndOffset(offset);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

				operatorSlot = triggerChar;
				display.replaceRange(" " + operatorSlot + "\n", lineEndPos - 3, lineEndPos);	
			}
			else if (stage == STAGE_SECOND_NUMBER) {
				
				if (triggerChar != '=')
					operatorSlot = triggerChar;
				
				try {
					switch (operatorSlot) {
						case '+': previousNumber += newNumber; break;
						case '-': previousNumber -= newNumber; break;
						case '*': previousNumber *= newNumber; break;
						case '/': previousNumber /= newNumber; break;
					}
					
					if (triggerChar != '=') {
						display.append(" =\n" + previousNumber + " " + operatorSlot + "\n");
						stage = STAGE_WAITING_SECOND_NUMBER;
					}
					else {
						display.append(" =\n" + previousNumber);
						stage = STAGE_FIRST_NUMBER_IN;
					}
				}
				catch (Exception ex) {
					display.append("\nNaN");
					stage = STAGE_NO_NUMBER;
					operatorSlot = NO_OPERATOR;
				}
			}
		}
	}
	
	/* HELPER METHODS */
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
	
	private JButton buttonFactory(JButton instanceHolder, String text) {
		instanceHolder = new JButton(text);
		instanceHolder.addActionListener(this);
		return instanceHolder;
	}
	
	
	
	public static void main(String[] args) {
		new Calculator();
	}

}
