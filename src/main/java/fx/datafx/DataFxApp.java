package fx.datafx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;


/**
 * Created by Dawson on 3/31/2015.
 */
public class DataFxApp extends Application {
	static              String           nameId = DataFxApp.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Flow(DataFxController.class).startInStage(primaryStage);
	}

	public DataFxApp() {}
}
