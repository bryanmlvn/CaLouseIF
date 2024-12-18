package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.RegisterView;
//TES BUAT CHANGES
public class Main extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		new RegisterView(arg0);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
