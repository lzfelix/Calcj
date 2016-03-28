package calculator;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

import calc_mvc.CalculatorObserver;

/**
 * The View part from a pseudo-MVC-based calculator . 
 * @author lzfelix
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame implements CalculatorObserver {
	private JTextArea display;	
	private Controller controller;

	public Calculator(Controller controller) {
		super("Calculator");
		
		setLayout(new BorderLayout());
		
		this.controller = controller;
		makeLayout();
		
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Creates the GUI elements
	 */
	private void makeLayout() {
		
		display = new JTextArea(8, 25);
		display.setEditable(false);
		JScrollPane jsp = new JScrollPane(display);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(jsp, BorderLayout.NORTH);
		
		JPanel pnlButtons = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// adding meta keys and division
		pnlButtons.add(buttonFactory("C"), gbc);
		pnlButtons.add(buttonFactory("<"), gbc);
		pnlButtons.add(buttonFactory("±"), gbc);
		pnlButtons.add(buttonFactory("/"), gbc);
		
		// numbers
		gbc.gridy = 1;
		gbc.gridx = GridBagConstraints.RELATIVE;
		
		for (int i = 1; i < 10; i++) {
			pnlButtons.add(buttonFactory(Integer.toString(i)), gbc);
			
			if (i % 3 == 0)
				gbc.gridy++;
		}
		
		// zero (occupies 2 cells), dot and equals
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlButtons.add(buttonFactory("0"), gbc);
		
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		pnlButtons.add(buttonFactory("."), gbc);
		pnlButtons.add(buttonFactory("="), gbc);
		
		gbc.weightx = 0;
		gbc.gridy = 1;
		
		// operations' buttons
		for (String operand : new String[]{"*", "-", "+"}) {
			pnlButtons.add(buttonFactory(operand), gbc);
			gbc.gridy++;
		}
		
		// adding the buttons panel to the layout.
		add(pnlButtons, BorderLayout.SOUTH);
	}
	
	/**
	 * Factory method that instantiates a <code>JButton</code> and
	 * subscribes the internal controller to listen to its events. 
	 * @param text The text displayed by the button to be created.
	 * @return The same reference to <code>instanceHolder</code>, just for convenience.
	 */
	private JButton buttonFactory(String text) {
		JButton instanceHolder = new JButton(text);
		instanceHolder.addActionListener(controller);
		return instanceHolder;
	}

	// View pattern methods

	@Override
	public void appendLine(String s) {
		display.append("\n" + s);
	}

	@Override
	public void appendString(String s) {
		display.append(String.valueOf(s));
	}

	@Override
	public void deleteChar() {
		deleteChars(1);
	}

	@Override
	public void clearDisplay() {
		display.setText("");
	}

	@Override
	public void deleteOperator() {
		deleteChars(3);
	}
	
	private void deleteChars(int amount) {
		int offset, lineEndPos = 0;
		try {
			offset = display.getLineOfOffset(display.getCaretPosition());
			lineEndPos = display.getLineEndOffset(offset);
			
			display.replaceRange("", lineEndPos - amount, lineEndPos);
		} catch (BadLocationException e) {
			e.printStackTrace();	// it will never happen
		}
	}
	
	@Override
	public void addOperator(char c) {
		display.append(" " + String.valueOf(c) + "\n");
	}

	public void invertSignal(boolean isNegative) {
		int lineBeginPos, offset;
		
		try {
			offset = display.getLineOfOffset(display.getCaretPosition());
			lineBeginPos = display.getLineStartOffset(offset);
		
			if (isNegative) {
				display.setCaretPosition(lineBeginPos);
				display.insert("-", display.getCaretPosition());
			}
			else
				display.replaceRange("", lineBeginPos, lineBeginPos + 1);
			
			display.setCaretPosition(display.getDocument().getLength());
		}
		catch (BadLocationException e) {
			e.printStackTrace(); // it will never happen
		}
	}
	
}
