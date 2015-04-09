package fx.test;

import ca.syncron.network.tcp.client.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/29/2015.
 */
public class TestMain extends Application {
	ClientController client;
	//	String fxml = "/fx/fxml/sample.fxml";
	String fxml = "sync-stage.fxml";
//	String fxml = "sync-stage.fxml";

	//	String fxml = "sample.fxml";
	@Override
	public void start(Stage primaryStage) throws Exception {
		client = ClientController.begin(null);
		Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource(fxml));
		primaryStage.setTitle("Syncron");
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//		Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

//		primaryStage.setScene(scene);
		primaryStage.setScene(new Scene(root, 900, 600));

		primaryStage.getScene().getStylesheets().add("/fx/styles/DarkTheme.css");
		primaryStage.setOnCloseRequest((e) -> {
			out.println("SHUTTING DOWN");
			Platform.exit();
			System.exit(0);
		});

		primaryStage.setMaximized(true);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}



