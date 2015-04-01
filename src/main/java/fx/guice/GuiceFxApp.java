package fx.guice;/**
 * Created by Dawson on 3/30/2015.
 */

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A Guice based sample FXML Application
 */
public class GuiceFxApp extends Application {
	/**
	 * Create the Guice Injector, which is going to be used to supply
	 * the Person model.
	 */
	private final Injector injector = Guice.createInjector(new GuiceModule());

	@Override
	public void start(Stage stage) throws Exception {
		// Create a new Guice-based FXML Loader
		GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);
		// Ask to load the Sample.fxml file, injecting an instance of a SampleController
		Parent root = (Parent) loader.load("Sample.fxml", GuiceFxController.class);

		// Finish constructing the scene
		final Scene scene = new Scene(root, 320, 240);
		// Load up the CSS stylesheet
		scene.getStylesheets().add(getClass().getResource("fxmlapp.css").toString());
		// Show the window
		stage.setScene(scene);
		stage.setTitle("Guiced");
		stage.show();
	}

	// Standard launcher
	public static void main(String[] args) {
		launch(args);
	}
}