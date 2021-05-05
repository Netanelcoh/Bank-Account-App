package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//Entry point of application. loading login page
public class Login extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Load Login page as Entry Point
		FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
		loaderLogin.setController(new LoginController());
		Pane loginPane = loaderLogin.load();
        Scene sceneLogin = new Scene(loginPane);
		
		primaryStage.setScene(sceneLogin);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
