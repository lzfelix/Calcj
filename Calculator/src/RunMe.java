public class RunMe {
	
	public static void main(String args[]) {
		Model model = new Model();
		Controller controller = new Controller();
		
		controller.addModel(model);
		
		Calculator calc = new Calculator(controller);
		model.subscribe(calc);
	}
}
