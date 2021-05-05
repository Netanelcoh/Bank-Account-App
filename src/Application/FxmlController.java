package Application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//load fxml file and set controller to fxml file
//every controller that need to load a new fxml should contains instance of this class
public class FxmlController {

	public void LoadAnotherFxml(Stage primaryStage, String path, FindController con) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		loader.setController(con.getController());
		try {
			Pane pane = loader.load();
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
