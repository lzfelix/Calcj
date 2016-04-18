package calculator;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

import calc_mvc.CalculatorController;
import calc_mvc.CalculatorObserver;

/**
 * The View part from a pseudo-MVC-based calculator . 
 * @author lzfelix
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame implements CalculatorObserver {
	private JTextArea display;	
	private CalculatorController controller;

	public static final char CHAR_EQUALS = (char)KeyEvent.VK_ENTER;
	public static final char CHAR_DELETE = (char)KeyEvent.VK_BACK_SPACE;
	public static final char CHAR_CLEAR = (char)KeyEvent.VK_ESCAPE;
	public static final char CHAR_SIGNAL = 'i';
	
	public Calculator(CalculatorController controller) {
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
		
		// Removes KeyMap from the TextArea, so the keyboard events are captured regardless of the focused element
		display.setKeymap(null);
		
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
		
		setKeyboardBindings(pnlButtons);
		
		// adding the buttons panel to the layout.
		add(pnlButtons, BorderLayout.SOUTH);
	}
	
	/**
	 * Installs shortcuts to all calculators buttons. This is done via KeyBindings, so the shortcuts can be
	 * used regardless of the focus inside the frame. 
	 * @param containerPanel The panel containing the buttons.
	 */
	private void setKeyboardBindings(JPanel containerPanel) {
		for (int i = 0; i < 10; i++) 
			installKeyBinding(containerPanel, "PressBtn" + i, KeyStroke.getKeyStroke(Integer.toString(i).charAt(0)), this.controller);
		
		installKeyBinding(containerPanel, "Delete", KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), this.controller);
		installKeyBinding(containerPanel, "EnterAsEquals", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), this.controller);
		installKeyBinding(containerPanel, "Equals", KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), this.controller);
		installKeyBinding(containerPanel, "ClearAll", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), this.controller);
		installKeyBinding(containerPanel, "InvertSignal", KeyStroke.getKeyStroke('i'), this.controller);
		installKeyBinding(containerPanel, "InvertSignal2", KeyStroke.getKeyStroke('I'), this.controller);
		installKeyBinding(containerPanel, "Dot", KeyStroke.getKeyStroke('.'), this.controller);

		installKeyBinding(containerPanel, "OperandAdd", KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.SHIFT_MASK), this.controller);
		installKeyBinding(containerPanel, "OperandMinus", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), this.controller);
		installKeyBinding(containerPanel, "OperandDivide", KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), this.controller);
		installKeyBinding(containerPanel, "OperandMultiply", KeyStroke.getKeyStroke(KeyEvent.VK_ASTERISK, InputEvent.SHIFT_MASK), this.controller);
		
		// sets the class (which extends JFrame) as focusable, so inputs such as Backspace are also captured by the KeyBinding
		setFocusable(true);
		requestFocus();
	}

	/**
	 * Maps a keybinding to a specific action.
	 * @param targetComponent The frame which the InputMap will be used.
	 * @param actionName The name of the action, used internally only. You must ensure that two distinct actions
	 * have distinct names.
	 * @param keyStroke The keystroke that is going to trigger the desired action.
	 * @param actionPerformed An object that performs such action/s.
	 */
	private void installKeyBinding(JComponent targetComponent, String actionName, KeyStroke keyStroke, Action actionPerformed) {
		targetComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
		targetComponent.getActionMap().put(actionName, actionPerformed);
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
