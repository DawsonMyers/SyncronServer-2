package fx.test;

import ca.syncron.network.tcp.client.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Dawson on 3/29/2015.
 */
public class TestMain extends Application {
	ClientController client;
	//	String fxml = "/fx/fxml/sample.fxml";
	String fxml = "sync-stage.fxml";

	//	String fxml = "sample.fxml";
	@Override
	public void start(Stage primaryStage) throws Exception {
		client = ClientController.begin(null);
		Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource(fxml));
		primaryStage.setTitle("Syncron");
		primaryStage.setScene(new Scene(root, 740, 480));

		primaryStage.getScene().getStylesheets().add("/fx/styles/DarkTheme.css");


		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}



