import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * The View part from a pseudo-MVC-based calculator . 
 * @author lzfelix
 */
public class Calculator extends JFrame {
	
	private final int AMOUNT_NUMBERS = 10;
	
	private JTextArea display;
	private JButton buttons[];
	private JButton btnClear, btnEquals, btnDivide, btnMultiply, btnAdd, btnSubtract;
	
	private Controller controller;

	public Calculator() {
		super("Calculator");
		
		setLayout(new BorderLayout());
		
		controller = new Controller();
		makeLayout();
		controller.registerDisplay(this.display);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Creates the GUI elements
	 */
	private void makeLayout() {
		buttons = new JButton[AMOUNT_NUMBERS];
		JPanel pnlKeys = new JPanel(new GridLayout(0, 3));
		JPanel pnlSideButtons = new JPanel(new GridLayout(0, 1));
		
		// adding numbered buttons, 0 is the last one 
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(Integer.toString(i));
			buttons[i].addActionListener(controller);
			
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
	
	/**
	 * Factory method that instantiates a <code>JButton</code> on <code>instanceHolder</code> and
	 * subscribes the internal controller to listen to its events. 
	 * @param instanceHolder A non-initialized <code>JButton</code> pointer.
	 * @param text The text displayed by the button to be created.
	 * @return The same reference to <code>instanceHolder</code>, just for convenience.
	 */
	private JButton buttonFactory(JButton instanceHolder, String text) {
		instanceHolder = new JButton(text);
		instanceHolder.addActionListener(controller);
		return instanceHolder;
	}

}
