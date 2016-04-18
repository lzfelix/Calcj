package calculator;
public class RunMe {
	
	public static void main(String args[]) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Model model = new Model();
				Controller controller = new Controller();
				
				controller.addModel(model);
				
				Calculator calc = new Calculator(controller);
				model.subscribe(calc);	
			}
		});
	}
}
